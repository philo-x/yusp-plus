/**
  @created by tuxw on 2020-01-01
  @updated by
  @description 数据监控
*/
import Vue from 'vue';
import router from '@/router'
import { logger, xyRequest, extend, args2Arr, clone, dateFormat } from '@/utils';
import { getTFP } from './tfp';
import { monitorLogUpload } from '@/api/common/monitor'
import { systemPid, delay } from '@/config'
class Monitor {
  ROUTER_TYPE_BEFORE = 'ROUTER-BEFORE';
  ROUTER_TYPE_READY = 'ROUTER-READY';
  ROUTER_TYPE_MOUNT = 'ROUTER-MOUNT';
  ROUTER_TYPE_UNMOUNT = 'ROUTER-UNMOUNT';
  AJAX_TYPE_BEFORE = 'AJAX-BEFORE';
  AJAX_TYPE_AFTER = 'AJAX-AFTER';
  AJAX_TYPE_EXCEPTION = 'AJAX-EXCEPTION';
  GUID_PREFIX = 'yufpm'; // 生成随机号前缀
  _BROWSER_FINGERPRINT = ''; // 浏览器指纹
  _CLIENT_IP = ''; // 客户端IP
  _ROUTER_TRACEID = ''; // 路由跟踪ID
  _ROUTER_BEGINTIME = Date.now(); // 路由开始时间
  _RESOURCE_INDEX = 0; // 资源列表数组长度指标
  _POST_LOGS_LENGTH = 20; // 上传日志长度
  _POST_LOGS_PATCHID = ''; // 日志批传key值
  _POST_LOGS_TEMP = {}; // 日志批传日志数组
  _logs = []; // 所有的日志
  TEMP_LOGS = {}; // 路由和接口请求的临时日志
  currentRouteData = null; // 当前的路由数据
  interavalId = null;
  delayed = 120000;
  maxLength = 20;
  firstFetchStart = 0;
  updateUrl = '/api/monitor/upload';
  COMMON_DATA = {
    // 系统全局ID，在index.html中配置死值
    pid: systemPid,
    // 文件/api加载状态
    code: 200,
    ip: '', // 客户端IP地址
    // 数据类型
    dataType: 'otherOpen',
    ispv: false, // 标注是否一个独立的pv数据
    // 耗时
    time: '',
    // 当前请求地址
    url: '',
    isError: false,
    errorJsUrl: '',
    // // 请求状态
    // status: '',
    '@timestamp': '', // 时间戳
    // 登录用户ID
    userId: '',
    // 异常信息
    // exceptionInfo: '',
    // 错误类型
    errorType: '',
    // 错误信息
    errorInfo: '',
    // 所在页面位置，路由页面路径
    // pagelocation: '',
    // 错误信息所在行数
    errorRow: '',
    // 错误信息所在列数
    errorCol: '',
    // 详细错误信息
    errorDetail: ''
  }
  constructor(delay = 1200000, maxLength = 20) {
    this.delayed = delay;
    this.maxLength = maxLength;
    this._ROUTER_TRACEID = this.getTraceId();
    this.init();
    this.addInterceptor();
  }

  /**
   * 初始化对象
   * @param {Object} setting 初始化配置信息
   */
  init (setting) {
    const _this = this;
    this.COMMON_DATA = extend(this.COMMON_DATA, this.getStaticData());
    this.getIp();
    // 解析window.performance.timing 性能数据并形成对应日志
    this.outPutData();
    this.analysisResourceData();
    clearInterval(this.interavalId);
    this.interavalId = setInterval(function () {
      _this.patchUploadLogs();
    }, this.delayed);
  }

  /**
   * 添加拦截器
   * 1、添加service过滤器
   * 2、添加router过滤器
   * 3、添加window.onerror错误拦截器
   * 4、添加errorHandler错误拦截器
   */
  addInterceptor (...args) {
    // 拦截错误日志打印
    const _this = this;
    const _oldError = logger.error;
    logger.error = () => {
      _oldError.apply(logger, args);
      const args = args2Arr(args);
      args.forEach((arg) => {
        if (Object.prototype.toString.call(arg) === '[object Error]') {
          _this.pushLogs(_this.errorAnalysis(arg, {}));
        }
      });
    };
    /**
     * window.onerror绑定window的错误事件
     * 无法捕获的错误：ajax 404，图片资源404
     * 图片资源404需用window.addEventListner才可监听到
     * 可捕获错误：脚本错误
     * @param {String} message  错误信息
     * @param {String} source   出错的文件
     * @param {Long}  lineno   出错代码的行号
     * @param {Long}  colno  出错代码的列号
     * @param {Object} error    错误的详细信息，Anything
     */
    window.onerror = function (message, source, lineno, colno, error) {
      let errorDetail = error;
      errorDetail = typeof errorDetail === 'object' ? JSON.stringify(errorDetail) : errorDetail;
      let data = {};
      data = _this.errorAnalysis(error, {});
      data['isError'] = true;
      data['errorMsg'] = message;
      data['errorRow'] = lineno; // 发生错误的行号
      data['errorCol'] = colno; // 发生错误的列号
      data['url'] = source; // 发生错误的脚本URL
      data['time'] = 0;
      data['errorDetail'] = errorDetail;
      _this.pushLogs(data);
    };
    // 添加路由监控
    router.beforeEach((to, from, next) => {
      _this._ROUTER_TRACEID = _this.getTraceId();
      _this._ROUTER_BEGINTIME = Date.now();
      next();
      _this.load({type: _this.ROUTER_TYPE_BEFORE, data: clone({}, to), code: to.name});
    });
    router.afterEach((to, from) => {
      _this.load({type: _this.ROUTER_TYPE_READY, data: clone({}, to), code: to.name});
    });
    Vue.mixin({
      mounted: function() {
        _this.load({type: _this.ROUTER_TYPE_MOUNT, data: {time: Date.now()}});
      }
    });
    // 添加接口请求监控
    xyRequest.addFilter({
      requestSucces: _this.requestSuccessFunc.bind(_this),
      responseSuccess: _this.responseSuccessFunc.bind(_this),
      responseFail: _this.responseFailFunc.bind(_this)
    })
  }

  // Vue框架异常捕获 (因为error-log.js中有定义Vue.config.errorHandler, 此处直接定义Vue.config.errorHandler会被覆盖)
  /**
   * 指定组件的渲染和观察期间未捕获错误的处理函数
   * @param {object} error 错误信息。error.message 错误信息，error.stack: 代码执行栈
   * @param {*} vm 抛出异常的Vue实例
   * @param {*} info `info` 是 Vue 特定的错误信息，比如错误所在的生命周期钩子，只在 2.2.0+ 可用
   */
  vueErrorHandler(error, vm, info) {
    let data = {};
    data = this.errorAnalysis(error, {});
    data.info = error.info;
    data.errorMsg = error.message;
    data.errorDetail = error.stack;
    this.pushLogs(data);
  }

  /**
   * 解析错误信息
   * @param {Object} error 错误对象
   * @param {Object} data 接受信息对象
   */
  errorAnalysis (error, data) {
    const stacks = error.stack.split(/(\s){2,}/);
    for (let i = 0, len = stacks.length; i < len; i++) {
      const s = stacks[i];
      if (s.length > 0) {
        if (i === 0) {
          data.errorName = s;
          // 获取错误类型
          const errorTypes = s.split(':');
          data.errorType = errorTypes[0];
        }
        if (i > 1) {
          const file = s.split(/\((.+?)\)/g);
          if (file.length > 2) {
            const fs = file[1].split(/\?/);
            data.errorJsUrl = fs[0];
            if (fs.length > 1) {
              const fss = fs[1].split(/:/);
              data.errorRow = fss.length > 1 ? fss[1] : '';
              data.errorCol = fss.length > 2 ? fss[2] : '';
            }
          }
          break;
        }
      }
    }
    data['isError'] = true;
    data['beginTime'] = this._ROUTER_BEGINTIME || Date.now();
    data['time'] = this._ROUTER_BEGINTIME;
    data['urlType'] = 'html';
    data['code'] = 200;
    return data;
  }

  // 请求前置成功处理函数
  requestSuccessFunc (config) {
    config.monitorCode = 'AJAX_' + Date.now() + ((Math.random() * 1000).toFixed(0) + '');
    config.time = Date.now();
    config.message = 'success';
    this.load({type: this.AJAX_TYPE_BEFORE, data: config});
    return config;
  }

  // 请求成功处理函数
  responseSuccessFunc (res) {
    const config = res.config || {};
    config.time = Date.now();
    config.message = 'success';
    this.load({type: this.AJAX_TYPE_AFTER, data: config});
    return res;
  }

  // 请求失败处理函数
  responseFailFunc (error) {
    error.time = Date.now();
    error.message = 'fail';
    this.load({type: this.AJAX_TYPE_EXCEPTION, data: error});
    return Promise.reject(error);
  }

  /**
  * 加载事件，路由 before/路由 ready/路由 unMount
  * /request before/request after/requestexception
  * /页面内mounted后调用了此方法
  * @param {Object} setting
  */
  load (setting) {
    const type = setting.type + '';
    // 路由加载信息
    if (type.indexOf('ROUTER-') > -1) {
      this.addRouterLog(setting);
    } else if (type.indexOf('AJAX-') > -1) {
      this.addAPILog(setting);
    } else {
      logger.info('数据监控丢失数据', type);
    }
  }

  // 合并数据
  mergeData (log) {
    if (!this._BROWSER_FINGERPRINT) {
      this._BROWSER_FINGERPRINT = getTFP();
    }
    if (!this._CLIENT_IP) {
      this.getIp();
    }
    this.COMMON_DATA.traceId = this._ROUTER_TRACEID;
    this.COMMON_DATA.beginTime = this._ROUTER_BEGINTIME;
    this.COMMON_DATA.userId = localStorage.getItem('userId');
    this.COMMON_DATA.browerMark = this._BROWSER_FINGERPRINT;
    this.COMMON_DATA.ip = this._CLIENT_IP;
    this.COMMON_DATA['@timestamp'] = this.getFormatDate();
    return extend({}, this.COMMON_DATA, this.getRouterInfo(), log);
  }

  /**
   * 添加路由日志
   * @param {object} setting 添加信息
   */
  addRouterLog (setting) {
    const type = setting.type + '';
    const cite = setting.data || {};
    if (!this.TEMP_LOGS[type]) {
      this.TEMP_LOGS[type] = {};
    }
    this.TEMP_LOGS[type][this._ROUTER_TRACEID] = {
      time: Date.now(),
      data: cite,
      code: setting.code
    };
    if (type === this.ROUTER_TYPE_READY) {
      const startStatus = this.TEMP_LOGS[this.ROUTER_TYPE_BEFORE];
      const endStatus = this.TEMP_LOGS[this.ROUTER_TYPE_READY];
      for (const key in endStatus) {
        if (key && startStatus[key]) {
          this.resetCurrentRouteData();
          this.currentRouteData.readyTime = endStatus[key].time - startStatus[key].time;
          this.currentRouteData.routerEndTime = endStatus[key].time;
          this.currentRouteData.url = cite.fullPath;
          delete this.TEMP_LOGS[this.ROUTER_TYPE_BEFORE][key];
          delete this.TEMP_LOGS[type][key];
        }
      }
    } else if (type === this.ROUTER_TYPE_MOUNT && this.currentRouteData) {
      this.currentRouteData.time = cite.time - this.currentRouteData.beginTime; 
      this.currentRouteData.mountedTime = cite.time - this.currentRouteData.routerEndTime;
      delete this.currentRouteData.routerEndTime;
      // 加载本次路由的静态资源信息
      this.analysisResourceData();
      this.pushLogs(this.currentRouteData);
      this.currentRouteData = null;
    }
  }
  resetCurrentRouteData () {
    this.currentRouteData = {};
    this.currentRouteData.time = 0; // cite.time 组件调用mounted方法的时间
    this.currentRouteData.readyTime = 0; // beforeEach 到 afterEach的时间
    this.currentRouteData.routerEndTime = 0; // router调用afterEach的时间
    this.currentRouteData.mountedTime = 0; // 从 routerEndTime 到 组件调用mounted方法的时间
    this.currentRouteData.url = '';
    this.currentRouteData.urlType = 'html';
    this.currentRouteData.ispv = true;
    this.currentRouteData.code = 200;
    this.currentRouteData.beginTime = this._ROUTER_BEGINTIME;
  }

  /**
   * 添加api请求日志
   * @param {object} setting api请求信息
   */
  addAPILog (setting) {
    const type = setting.type + '';
    if (!this.TEMP_LOGS[type]) {
      this.TEMP_LOGS[type] = {};
    }
    if (type.search('AJAX-') !== -1) { // ajax请求数据
      const event = setting.data;
      const o = {
        msg: event.message, // 请求结果成功或失败
        data: (event.url && event.url.indexOf(this.updateUrl) > -1) ? '' : JSON.stringify(event.data),
        time: Date.now(),
        code: event.xhr && event.xhr.status,
        url: event.url
      };

      // 唯一标识
      this.TEMP_LOGS[type][event.monitorCode] = o;
      if (type.search('BEFORE') === -1) {
        // 做计算并存储数据
        const start = this.TEMP_LOGS[this.AJAX_TYPE_BEFORE];
        const end = this.TEMP_LOGS[type];
        for (const k in end) {
          if (k) {
            // 生成一条监控数据 --start
            const ajaxData = {};
            ajaxData.url = end[k].url;
            ajaxData.time = (start && start[k]) ? (end[k].time - start[k].time) : 0;
            // 请求状态
            ajaxData.code = parseInt(end[k].code) || (end[k].msg === 'success' ? 200 : end[k].msg);
            if (type === this.AJAX_TYPE_EXCEPTION) {
              ajaxData.isError = true;
            }
            ajaxData.urlType = 'api';
            ajaxData.beginTime = (start && start[k] && start[k].time) || end[k].time;
            // 生成一条监控数据 --end
            this.pushLogs(ajaxData);
            // 计算后移除该项，避免下次做重复计算
            delete this.TEMP_LOGS[type][k];
            this.TEMP_LOGS[this.AJAX_TYPE_BEFORE] && delete this.TEMP_LOGS[this.AJAX_TYPE_BEFORE][k];
          }
        }
      }
    }
  }

  /**
  * 添加数据到数组中
  * @param {object} log 日志数据
  */
  pushLogs (log) {
    this._logs.push(this.mergeData(log));
    this.checkLogs();
  }

  // 校验日志数组长度
  checkLogs () {
    const len = this._logs.length;
    if (len >= this.maxLength && !this._POST_LOGS_PATCHID) {
      this.patchUploadLogs();
    }
  }

  // 处理批量日志上传
  patchUploadLogs () {
    this._POST_LOGS_PATCHID = this.getTraceId();
    this._POST_LOGS_TEMP[this._POST_LOGS_PATCHID] = this._logs.splice(0, this._logs.length);
    this.updateLog(this._POST_LOGS_PATCHID);
  }

  /**
  * window.performance.timing 性能数据
  */
  outPutData () {
    const data = this.analysisTiming(window.performance.timing, true);
    this.firstFetchStart = window.performance.timing.fetchStart || 0;
    data.dataType = 'firstOpen';
    data.url = window.location.href;
    data.urlType = 'html';
    this.pushLogs(data);
  }

  // 分析资源数据
  analysisResourceData () {
    const allResource = window.performance.getEntriesByType('resource');
    const list = allResource.slice(this._RESOURCE_INDEX);
    this._RESOURCE_INDEX = allResource.length;
    list.forEach((item) => {
      this.pushLogs(this.analysisTiming(item));
    });
  }

  /**
  * 初始化相关数据分析
  * @param {Object} timing 开始事件
  */
  analysisTiming (timing, isFirst) {
    const timeData = {
      url: timing.name,
      urlType: timing.initiatorType,
      domainLookupStart: timing.domainLookupStart + this.firstFetchStart, // 域名查询开始时的时间戳
      domainLookupEnd: timing.domainLookupEnd + this.firstFetchStart, // 域名查询结束时的时间戳
      dnsTime: timing.domainLookupEnd - timing.domainLookupStart,
      connectEnd: timing.connectEnd + this.firstFetchStart, // 浏览器与服务器之间的连接建立时的时间戳
      connectStart: timing.connectStart + this.firstFetchStart, // HTTP请求开始向服务器发送时的时间戳
      tcpTime: timing.connectEnd - timing.connectStart,
      responseEnd: timing.responseEnd + this.firstFetchStart, // 浏览器从服务器收到（或从本地缓存读取）最后一个字节时的时间戳。
      responseStart: timing.responseStart + this.firstFetchStart, // 浏览器从服务器收到（或从本地缓存读取）第一个字节时的时间戳
      contentTime: timing.responseEnd - timing.responseStart,
      domLoading: timing.domLoading + this.firstFetchStart,
      domInteractive: timing.domInteractive + this.firstFetchStart,
      domContentLoadedEventEnd: timing.domContentLoadedEventEnd + this.firstFetchStart, // 当前网页所有需要执行的脚本执行完成时的时间戳
      domContentLoadedEventStart: timing.navigationStart + this.firstFetchStart, // 当前浏览器窗口的前一个网页关闭，发生unload事件时的时间戳。如果没有前一个网页，则等于fetchStart属性。
      domTime: timing.domContentLoadedEventEnd - timing.navigationStart, // domready时间
      whiteScreenTime: timing.responseStart - timing.navigationStart, // 白屏时间
      loadPage: timing.loadEventEnd - timing.navigationStart, // 页面加载完成的时间
      loadEventEnd: timing.loadEventEnd + this.firstFetchStart, // 当前网页load事件的回调函数运行结束时的Unix毫秒时间戳。如果该事件还没有发生，返回0
      loadEventStart: timing.loadEventStart + this.firstFetchStart,
      resourceTime: timing.loadEventEnd - timing.loadEventStart, // 执行 onload 回调函数的时间
      requestStart: timing.requestStart + this.firstFetchStart,
      fetchStart: timing.fetchStart + this.firstFetchStart,
      beginTime: Date.now(),
      '@timestamp': this.getFormatDate()
    };
    if (isFirst) {
      timeData['FirstPaintTime'] = timing.responseEnd - timing.fetchStart;
    } else {
      timeData['time'] = timing.responseEnd - timing.fetchStart;
    }
    return this.cleaningData(timeData);
  }  

  // 清洗数据,将数据中的NaN清空
  cleaningData (data) {
    for (const k in data) {
      if (k) {
        if (Number.isNaN(data[k]) || Object.prototype.toString.call(data[k]) === '[object Undefined]') {
          data[k] = '';
        }
      }
    }
    return data;
  }

  // 数据满条件后调用接口上传数据
  updateLog (patchid) {
    const _this = this;
    const _tmplogs = this._POST_LOGS_TEMP[patchid].slice(0, this._POST_LOGS_LENGTH);
    if (_tmplogs.length > 0) {
      const param = _tmplogs;
      monitorLogUpload(param).then(res => {
        const {code, message} = res;
        if (code === 0) {
          // 删除已经上传的数据
          this._POST_LOGS_TEMP[patchid].splice(0, this._POST_LOGS_LENGTH);
          // 还有数据就继续上传，否则删除这个key值
          if (this._POST_LOGS_TEMP[patchid].length > 0) {
            _this.updateLog(patchid);
          } else {
            delete this._POST_LOGS_TEMP[patchid];
            if (Object.keys(this._POST_LOGS_TEMP).length > 0) {
              _this.updateLog(Object.keys(this._POST_LOGS_TEMP)[0]);
            } else {
              // 没有待上传数据了就清空
              this._POST_LOGS_PATCHID = '';
            }
          }
        }
      })
    }
  }
  getFormatDate () {
    return dateFormat(new Date());
  }

  // 获取静态数据
  getStaticData () {
    const navigator = window.navigator;
    const os = this.getOsInfo();
    const browers = this.getBrowerInfo();
    return {
      // 分辨率
      resolution: window.screen.width + '*' + window.screen.height,
      // 客户端ip
      ip: '',
      // 浏览器信息
      navigatorInfo: navigator.userAgent,
      // 操作系统信息
      system: os.name + os.version,
      // 浏览器名称
      browser: browers.type,
      // 浏览器版本
      browerVersion: browers.version,
      // 应用名称
      // title: document.title,
      // 访问域名
      hostname: window.location.hostname,
      // 通信协议
      protocol: window.location.protocol.replace(':', '')
      // 当前请求地址
      // address: window.location.href
    };
  }

  // 获取本机ip地址
  getIp () {
    const _this = this;
    const userAgent = window.navigator.userAgent;
    const isIE = userAgent.indexOf('compatible') > -1 && userAgent.indexOf('MSIE') > -1; // 判断是否IE<11浏览器
    const isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf('rv:11.0') > -1;
    if (!isIE && !isIE11) {
      // 在firefox44浏览器、opera63.0中可以得出结果
      // 在Google76上默认获取的是mdns，需要 将浏览器的 chrome://flags/#enable-webrtc-hide-local-ips-with-mdns flag设置为Disable
      // NOTE: window.RTCPeerConnection is 'not a constructor' in FF22/23
      const RTCPeerConnection = /* window.RTCPeerConnection || */ window.webkitRTCPeerConnection || window.mozRTCPeerConnection;
      if (RTCPeerConnection) {
        (function () {
          const rtc = new RTCPeerConnection({
            iceServers: []
            // update by taoting1 at 20190821 14:21
            // iceServers: [ {urls: 'stun:stun.services.mozilla.com'} ]
          });
          // if (1 || window.mozRTCPeerConnection) { // FF [and now Chrome!] needs a channel/stream to proceed
          rtc.createDataChannel('', {
            reliable: false
          });
          // };

          rtc.onicecandidate = function (evt) {
            // convert the candidate to SDP so we can run it through our general parser
            // see https://twitter.com/lancestout/status/525796175425720320 for details
            if (evt.candidate) {
              grepSDP('a=' + evt.candidate.candidate);
            }
          };
          rtc.createOffer(function (offerDesc) {
            grepSDP(offerDesc.sdp);
            rtc.setLocalDescription(offerDesc);
          }, function (e) {
            console.warn('offer failed', e);
          });

          const addrs = Object.create(null);
          addrs['0.0.0.0'] = false;

          function updateDisplay (newAddr) {
            const displayAddrs = Object.keys(addrs).filter(function (k) {
              return addrs[k];
            });
            if (newAddr in addrs) {
              return;
            } else {
              addrs[newAddr] = true;
            }
            _this._CLIENT_IP = displayAddrs.join(' or perhaps ') || 'n/a';
          }

          function grepSDP (sdp) {
            const hosts = [];
            let parts, addr, type;
            sdp.split('\r\n').forEach(function (line) { // c.f. http://tools.ietf.org/html/rfc4566#page-39
              if (~line.indexOf('a=candidate')) { // http://tools.ietf.org/html/rfc4566#section-5.13
                parts = line.split(' '); // http://tools.ietf.org/html/rfc5245#section-15.1
                addr = parts[4];
                type = parts[7];
                if (type === 'host') {
                  updateDisplay(addr);
                }
              } else if (~line.indexOf('c=')) { // http://tools.ietf.org/html/rfc4566#section-5.7
                parts = line.split(' ');
                addr = parts[2];
                updateDisplay(addr);
              }
            });
          }
        }());
      } else {
        logger.warn('<code>ifconfig | grep inet | grep -v inet6 | cut -d\' \' -f2 | tail -n1</code>');
      }
    }
  }

  // 获取操作系统信息
  getOsInfo () {
    const userAgent = navigator.userAgent.toLowerCase();
    let name = 'Unknown';
    let version = 'Unknown';
    if (userAgent.indexOf('win') > -1) {
      name = 'Windows';
      if (userAgent.indexOf('windows nt 5.0') > -1) {
        version = '2000';
      } else if (
        userAgent.indexOf('windows nt 5.1') > -1 ||
        userAgent.indexOf('windows nt 5.2') > -1
      ) {
        version = 'XP';
      } else if (userAgent.indexOf('windows nt 6.0') > -1) {
        version = 'Vista';
      } else if (
        userAgent.indexOf('windows nt 6.1') > -1 ||
        userAgent.indexOf('windows 7') > -1
      ) {
        version = '7';
      } else if (
        userAgent.indexOf('windows nt 6.2') > -1 ||
        userAgent.indexOf('windows 8') > -1
      ) {
        version = '8';
      } else if (userAgent.indexOf('windows nt 6.3') > -1) {
        version = '8.1';
      } else if (
        userAgent.indexOf('windows nt 6.2') > -1 ||
        userAgent.indexOf('windows nt 10.0') > -1
      ) {
        version = '10';
      } else {
        version = 'Unknown';
      }
    } else if (userAgent.indexOf('iphone') > -1) {
      name = 'Iphone';
    } else if (userAgent.indexOf('mac') > -1) {
      name = 'Mac';
    } else if (
      userAgent.indexOf('x11') > -1 ||
      userAgent.indexOf('unix') > -1 ||
      userAgent.indexOf('sunname') > -1 ||
      userAgent.indexOf('bsd') > -1
    ) {
      name = 'Unix';
    } else if (userAgent.indexOf('linux') > -1) {
      if (userAgent.indexOf('android') > -1) {
        name = 'Android';
      } else {
        name = 'Linux';
      }
    } else {
      name = 'Unknown';
    }
    return {
      name: name,
      version: version
    };
  }

  // 获取浏览器信息
  getBrowerInfo () {
    const Browser = (function() {
      const document = window.document,
        navigator = window.navigator,
        agent = navigator.userAgent.toLowerCase(),
        // IE8+支持.返回浏览器渲染当前文档所用的模式
        // IE6,IE7:undefined.IE8:8(兼容模式返回7).IE9:9(兼容模式返回7||8)
        // IE10:10(兼容模式7||8||9)
        IEMode = document.documentMode,
        // chorme
        chrome = window.chrome || false,
        System = {
          // user-agent
          agent: agent,
          // 是否为IE
          isIE: /trident/.test(agent),
          // Gecko内核
          isGecko:
            agent.indexOf('gecko') > 0 && agent.indexOf('like gecko') < 0,
          // webkit内核
          isWebkit: agent.indexOf('webkit') > 0,
          // 是否为标准模式
          isStrict: document.compatMode === 'CSS1Compat',
          // 是否支持subtitle
          supportSubTitle: function() {
            return 'track' in document.createElement('track');
          },
          // 是否支持scoped
          supportScope: function() {
            return 'scoped' in document.createElement('style');
          },

          // 获取IE的版本号
          ieVersion: function() {
            const rMsie = /(msie\s|trident.*rv:)([\w.]+)/;
            const ma = window.navigator.userAgent.toLowerCase();
            const match = rMsie.exec(ma);
            try {
              return match[2];
            } catch (e) {
              return IEMode;
            }
          },
          // Opera版本号
          operaVersion: function() {
            try {
              if (window.opera) {
                return agent.match(/opera.([\d.]+)/)[1];
              } else if (agent.indexOf('opr') > 0) {
                return agent.match(/opr\/([\d.]+)/)[1];
              }
            } catch (e) {
              return 0;
            }
          }
        };

      try {
        // 浏览器类型(IE、Opera、Chrome、Safari、Firefox)
        if (System.isIE) {
          System.type = 'IE';
        } else if (window.opera || agent.indexOf('opr') > 0) {
          System.type = 'Opera';
        } else if (agent.indexOf('chrome') > 0) {
          System.type = 'chrome';
        } else if (window.openDatabase) {
          System.type = 'Safari';
        } else if (agent.indexOf('firefox') > 0) {
          System.type = 'Firefox';
        } else {
          System.type = 'unknow';
        }
        // 版本号
        switch (System.type.toLowerCase()) {
          case 'ie':
            System.version = System.ieVersion();
            break;
          case 'opera':
            System.version = System.operaVersion();
            break;
          case 'chrome':
            System.version = agent.match(/chrome\/([\d.]+)/)[1];
            break;
          case 'safari':
            System.version = agent.match(/version\/([\d.]+)/)[1];
            break;
          case 'firefox':
            System.version = agent.match(/firefox\/([\d.]+)/)[1];
            break;
          default:
            break;
        }
        // 浏览器外壳
        System.shell = function() {
          if (agent.indexOf('edge') > 0) {
            System.version = agent.match(/edge\/([\d.]+)/)[1] || System.version;
            return 'edge浏览器';
          }
          // 遨游浏览器
          if (agent.indexOf('maxthon') > 0) {
            System.version =
              agent.match(/maxthon\/([\d.]+)/)[1] || System.version;
            return '傲游浏览器';
          }
          // QQ浏览器
          if (agent.indexOf('qqbrowser') > 0) {
            System.version =
              agent.match(/qqbrowser\/([\d.]+)/)[1] || System.version;
            return 'QQ浏览器';
          }
          // 搜狗浏览器
          if (agent.indexOf('se 2.x') > 0) {
            return '搜狗浏览器';
          }
          // Chrome:也可以使用window.chrome && window.chrome.webstore判断
          if (chrome && System.type !== 'Opera') {
            const external = window.external,
              clientInfo = window.clientInformation,
              // 客户端语言:zh-cn,zh.360下面会返回undefined
              clientLanguage = clientInfo.languages;

            // 猎豹浏览器:或者agent.indexOf('lbbrowser')>0
            if (external && 'LiebaoGetVersion' in external) {
              return '猎豹浏览器';
            }
            // 百度浏览器
            if (agent.indexOf('bidubrowser') > 0) {
              System.version =
                agent.match(/bidubrowser\/([\d.]+)/)[1] ||
                agent.match(/chrome\/([\d.]+)/)[1];
              return '百度浏览器';
            }
            // 360极速浏览器和360安全浏览器
            if (
              System.supportSubTitle() &&
              typeof clientLanguage === 'undefined'
            ) {
              // object.key()返回一个数组.包含可枚举属性和方法名称
              const storeKeyLen = Object.keys(chrome.webstore).length,
                v8Locale = 'v8Locale' in window;
              return storeKeyLen > 1 ? '360极速浏览器' : '360安全浏览器';
            }
            return 'Chrome';
          }
          return System.type;
        };
        // 浏览器名称(如果是壳浏览器,则返回壳名称)
        System.name = System.shell();
        // 对版本号进行过滤过处理
        // System.version = System.versionFilter(System.version);
      } catch (e) {
        // console.log(e.message);
      }
      return {
        client: System
      };
    })();
    if (Browser.client.name === undefined || Browser.client.name === '') {
      Browser.client.name = 'Unknown';
      Browser.client.version = 'Unknown';
    } else if (Browser.client.version === undefined) {
      Browser.client.version = 'Unknown';
    }
    console.log('Browser', Browser)
    return Browser.client;
  }

  // 获取traceid,每次路由加载生成唯一
  getTraceId () {
    return this.GUID_PREFIX + this.guid();
  }
  guid () {
    return 'xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      const r = Math.random() * 16 | 0, v = c === 'x' ? r : r & 0x3 | 0x8;
      return v.toString(16);
    });
  }

  // 获取当前菜单信息
  getRouterInfo () {
    return {
      title: document.title,
      url: window.location.href
    };
  }
}
export default new Monitor(delay);
/**
 * 为兼容组件内部默认同步方式，增加同步请求
 * @create by kongqf 2021.02.04
 */
/* eslint-disable */
import { extend, logger, args2Arr, type } from 'uadp-utils';
import { showMessage } from '@/utils/util'
import { X_AUTHORIZATION } from '@/config/constant/app.data.common'
import { getToken } from '@/utils/oauth'
import { getLanguage } from '@/utils/i18n'
import { getI18nMessage } from "@/locale"
import store from '@/store'

/**
 * 构建延期对象状态
 */
const DEFERRED_STATUS = {
  0: 'Running',
  1: 'Done',
  2: 'Fail',
  Running: 0,
  Done: 1,
  Fail: 2
}
/**
 * 构建延期/延缓对象
 */
class Deferred {
  constructor(args) {
    /**
     * 成功函数
     */
    this.doneFns = [];
    /**
     * 失败函数
     */
    this.failFns = [];
    /**
     * always函数
     */
    this.alwaysFns = [];
    /*
     * 运行状态
     */
    this.status = DEFERRED_STATUS.Running;
  }

  /**
   * fire
   */
  fire() {
    if (this.status == DEFERRED_STATUS.Running) {
      return;
    }
    if (this.alwaysFns) {
      while (this.alwaysFns.length > 0) {
        var fn = this.alwaysFns.shift();
        fn.apply(this, this.args);
      }
    }
    if (this.status == DEFERRED_STATUS.Done) {
      while (this.doneFns.length > 0) {
        var fn = this.doneFns.shift();
        fn.apply(this, this.args);
      }
    } else if (this.status == DEFERRED_STATUS.Fail) {
      while (this.failFns.length > 0) {
        var fn = this.failFns.shift();
        fn.apply(this, this.args);
      }
    }
  }

  /**
   * 注册成功函数
   * @param fn
   */
  done(fn) {
    this.doneFns.push(fn);
    this.fire();
    return this;
  }

  /**
   * 注册失败函数
   * @param fn
   */
  fail(fn) {
    this.failFns.push(fn);
    this.fire();
    return this;
  }

  /**
   * 注册always函数
   * @param fn
   */
  always(fn) {
    this.alwaysFns.push(fn);
    this.fire();
    return this;
  }

  /**
   * 成功
   * @param params
   */
  resolve() {
    this.args = args2Arr(arguments);
    this.status = DEFERRED_STATUS.Done;
    this.fire();
    return this;
  }
  /**
   * 拒绝
   * @param params
   */
  reject() {
    this.args = args2Arr(arguments);
    this.status = DEFERRED_STATUS.Fail;
    this.fire();
    return this;
  }
}
/**
  * 默认配置
  * @type {{type: string, async: boolean, contentType: string, dataType: string, cache: boolean, timeout: number, processData: boolean}}
  */
const defaultSetting = {
  // 请求类型
  type: 'GET',
  // 是否异步
  async: true,
  // 内容类型
  contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
  // 数据类型
  dataType: 'string',
  // 是否缓存
  cache: false,
  // 超时时间
  timeout: 90000,
  // 是否处理数据
  processData: true,
  // 是否对特殊字符编码
  encode: false
}
class Ajax {
  /**
   * 构建AJAX对象
   * @constructor
   */
  constructor(args) {
    var _this = this;
    var xhr = this.createXHR();
    if (!xhr) {
      core.logger.error('core.ajax: Your browser not support AJAX XMLHttpRequest.');
      return;
    }
    var setting = {};
    extend(true, setting, defaultSetting);
    extend(true, setting, args);
    args = setting;
    // 异步请求、超时设置
    _this.asyncTimeout(args, xhr);
    // 指定ready state change事件句柄对应的函数
    _this.onreadystatechange(args, xhr);
    var method = args.type.toUpperCase();
    var reqData = _this.processData(args, method);
    // 初始化HTTP请求参数
    args.url += method == 'GET' && reqData ? (args.url.indexOf('?') === -1 ? '?' : '&') + reqData : '';
    // fixed: 处理配置responseType支持二进制流请求
    if (args.responseType) {
      xhr.responseType = args.responseType;
    }
    xhr.open(args.type, args.encode ? args.url : encodeURI(args.url), args.async);
    // fixed: firefox 45 窗口上下文的同步模式中，已不再支持使用的xmlhttprequest 的withcredentials属性
    if (args.withCredentials !== false) {
      xhr.withCredentials = args.withCredentials === undefined ? true : args.withCredentials;
    }

    if (type(args.beforeSend) == 'function') {
      // 克隆数据
      var cloneArgs = {};
      extend(true, cloneArgs, args);
      args.beforeSend.call(_this, xhr, cloneArgs);
    }
    // 设置缓存策略
    if (!args.cache) {
      xhr.setRequestHeader('Cache-Control', 'no-cache');
    }
    // 设置请求头
    if (typeof args.headers === 'object') {
      for (var key in args.headers) {
        xhr.setRequestHeader(key, args.headers[key]);
      }
    }
    try {
      // 发送数据
      xhr.send(method != 'GET' && reqData ? reqData : null);
    } catch (ex) {
      if (args.error) {
        args.error(xhr, ex.message);
      }
      // 完成处理
      if (args.complete) {
        args.complete(xhr, ex.message);
      }
    }
  }
  /**
   * 加载处理请求数据
   * @param {*} args
   * @param {*} method
   */
  processData (args, method) {
    var reqData = null;
    try {
      if (args.data && args.processData && typeof args.data !== 'string') {
        reqData = this.encode(args.data, method == 'GET' ? 'application/x-www-form-urlencoded' : args.headers['Content-Type'], args.encode);
      } else {
        reqData = args.data;
      }
    } catch (ex) {
      core.logger.error('core.ajax: Request params format error；' + ex.Message, ex);
    }
    return reqData;
  }
   /**
   * 处理响应数据
   * @param {*} args
   * @param {*} xhr
   */
  onreadystatechange (args, xhr) {
    var _this = this;
    xhr.onreadystatechange = function () {
      try {
        if (xhr.readyState < 4) {
          if (args.loading) {
            args.loading(xhr.readyState);
          }
          return;
        }
        if (args.success && (xhr.status == 0 || (xhr.status >= 200 && xhr.status < 300))) {
          var rspData = xhr.response == undefined ? xhr.responseText : xhr.response;
          if (xhr.status === 0 && !rspData) {
            // 失败回调
            args.error && args.error(xhr, '网络请求异常');
            return;
          } else {
            try {
              rspData = _this.decode(rspData, args.dataType); // 解码数据
            } catch (ex) {
              // 失败回调
              args.error && args.error(xhr, ex.message);
              return;
            }
          }
          // 成功回调
          args.success(rspData, xhr.status, xhr);
        } else if (args.error) {
          args.error(xhr, xhr.status);
        }
        // 完成处理
        if (args.complete) {
          args.complete(xhr, xhr.status);
        }
      } catch (e) {
        throw e;
      }
    };
  }
  /**
   * 处理异步请求超时
   * @param {*} args
   * @param {*} xhr
   */
  asyncTimeout (args, xhr) {
    if (!args.async) {
      return;
    }
    // 防止IE导致的问题
    try {
      xhr.timeout = args.timeout;
      xhr.ontimeout = function () {
        // 超时处理
        if (args.error) {
          args.error(xhr, 'timeout');
        }
        // 完成处理
        if (args.complete) {
          args.complete(xhr, 'timeout');
        }
        xhr = null;
        args = null;
      };
    } catch (ex) {
      core.logger.warn('core.ajax: ie timeout, ' + ex.Message, ex);
    }
  }
  /**
   * 创建xml http request对象
   * @returns xhr
   */
  createXHR () {
    var xhr;
    try {
      xhr = new XMLHttpRequest();
    } catch (e) {
      var IEXHRVers = ['Msxml3.XMLHTTP', 'Msxml2.XMLHTTP', 'Microsoft.XMLHTTP'];
      for (var i = 0, len = IEXHRVers.length; i < len; i++) {
        try {
          xhr = new window.ActiveXObject(IEXHRVers[i]);
          break; // 按IE版本创建xml http request对象
        } catch (e) {
          continue;
        }
      }
    }
    return xhr;
  }
  /**
   * 编码数据
   * @param {*} data 待编码数据
   * @param {*} contentType 编码类型
   */
  encode (data, contentType, encode) {
    if (!data || typeof data === 'string' || !contentType) {
      return data;
    }
    contentType = contentType.toLowerCase();
    var res = '';
    var ct = 'application/x-www-form-urlencoded';
    var isObj = type(data) == 'object';
    if (contentType.indexOf(ct) > -1) {
      // 对象转换为form提交格式
      if (isObj) {
        for (var key in data) {
          var value = data[key];
          // 获取value数据类型
          var valType = type(value);
          if (valType == 'array') {
            for (var i = 0; i < value.length; i++) {
              var item = key + '=' + (!encode ? value : encodeURIComponent(value[i]));
              if (res.length > 0) {
                res += '&';
              }
              res += item;
            }
          } else {
            var item = key + '=' + (!encode ? value : encodeURIComponent(value));
            if (res.length > 0) {
              res += '&';
            }
            res += item;
          }
        }
      } else {
        res = data;
      }
    } else if (contentType.indexOf('application/json') > -1 || contentType.indexOf('text') == 0) {
      if (isObj) {
        res = JSON.stringify(data);
      }
    } else {
      if (isObj) {
        res = JSON.stringify(data);
      } else {
        res = data;
      }
    }
    return res;
  }

  /**
   * 解码数据
   * @param {*} data
   * @param {*} dataType
   */
  decode (data, dataType) {
    if (dataType == 'json') {
      if (type(data) == 'string' && data != '') {
        try {
          data = JSON.parse(data);
        } catch (e) {
          logger.error('core.ajax: Response text format error ', e);
          throw e;
        }
      }
    }
    return data;
  }
}

const DEFAUT_OPTION = {
  fullscreen: true, // 遮罩是否全屏
  lock: true, // 在使用全屏遮罩时，控制是否锁定屏幕的滚动，true: 锁定，false: 不锁定
  body: false // 遮罩是否插入至 DOM 中的 body 中，true: 插入，false: 不插入
};
let hasHintMsg = false;
class Service{
  constructor(args) {
    this.options = {
      method: 'POST', // 默认POST，支持4种访问类型 GET/POST/PUT/DELETE
      async: false, // 异步请求
      data: {}, // 请求数据
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        'Accept-Language': getLanguage() === 'zh_CN' ? 'zh-CN' : 'en-US'
        // 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
      }, // http请求头
      dataType: 'json', // 默认返回数据类型
      timeout: 90000, // 默认超时时间
      cache: false, // 是否缓存
      needToken: true, // 是否传认证Token值去后台
      callback: false, // 回调方法
      loadingUi: {
        // 是否展示加载loading
        show: false,
        // 局部loading 作用的/覆盖的 DOM 对象，
        // 如果不配置，则显示全局loading
        target: null,
        // loading的自定义option
        option: null
      }
    };
    this._filters = []; // 过滤器集合
    this.basePath = ''; // 应用名
    this.tokenId = X_AUTHORIZATION; // TOKEN 名
    this.tokenVal = ''; // TOKEN 值
    this.successCallback = null; // 成功回调
    this.errorCallback = null; // 失败回调
    const _this = this;
    this.returnObj = null; // 返回数据对象
    // 返回模拟Promise对象
    this.returnPromise = {
      then(callback) {
        if(callback) {
          callback(_this.returnObj)
        }
        return _this.returnPromise
      },
      catch(callback) {
        if(callback && _this.returnObj.code != 0) {
          callback(_this.returnObj)
        }
        return _this.returnPromise
      }
    }
  }
  /**
   * 发送请求
   * @param options
   */
  request (options) {
    var _this = this;
    // var option;
    var _options = extend({}, this.options, options);
    // 添加逻辑处理url 中+号特殊字符
    if (_options.url) {
      _options.url = _options.url.replace(/\+/g, '%2B');
    }
    var deferred = new Deferred();
    var event = {
      data: _options.data,
      requestUrl: _options.url
    };
    // before过滤
    if (_this._doFilter(0, event) === false) {
      event.code = event.code ? event.code : 2;
      deferred.reject(event.code, event.message, event.data);
      if (_options.callback) {
        _options.callback(event.code, event.message, event.data);
      }
      return deferred;
    }
    // todo 修改同步时参数的转变
    // _options.data = !options.async ? JSON.stringify(event.data) : event.data;
    _options.data = event.data;
    _options.headers = extend({}, event.data.headers || {}, _options.headers);
    if (_options.needToken) {
      var token = getToken();
      if (!token) { // 若token不存在，表示会话过期，终止请求
        if (hasHintMsg) {
          return deferred;
        }
        hasHintMsg = true;
        logger.warn('Service: Session expiration request termination');
        showMessage(getI18nMessage('component.session_001'), 'error');
        store.dispatch('oauth/resetToken').then(() => {
          window.location.reload() // 为了重新实例化vue-router对象 避免bug
        })
        return deferred;
      }
      hasHintMsg = false;
      _options.headers[this.tokenId] = 'Bearer ' + token.access_token;
    }
    _options.type = options.method;
    _options.async = options.async;
    _options.success = function (data, status, xhr) {
      // 定义过滤事件
      var successEvent = { code: 0, message: 'success', data: data};
      successEvent = _this.deepTraverseMsg(successEvent);
      // after过滤器
      // 过滤器中断调用处理
      if (_this._doFilter(1, successEvent, event) === false) {
        var code = successEvent.code ? successEvent.code : 1;
        // 通知调用失败
        deferred.reject(successEvent.code, successEvent.message, successEvent.data);
      } else {
        // 通知调用成功
        deferred.resolve(successEvent.code, successEvent.message, successEvent.data);
      }
      // _this.successCallback && _this.successCallback(code, successEvent.message, successEvent.data)
      _this.returnObj = {code: `${successEvent.code}`, message: successEvent.message, data: successEvent.data}
    };
    _options.error = function (xhr, status) {
      var msg = xhr.responseText;
      msg = !msg ? status : msg;
      // 定义过滤事件
      var errorEvent = { code: 1, message: msg, xhr: xhr, requestUrl: _options.url}; // todo
      // 处理消息翻译
      errorEvent = _this.deepTraverseMsg(errorEvent);
      // exception过滤
      if (_this._doFilter(2, errorEvent, event) === false) {
        // 通知调用失败
        deferred.reject(errorEvent.code, errorEvent.message, errorEvent.response); // todo
        return;
      }
      errorEvent.code = errorEvent.code ? errorEvent.code : 1;
      // 通知调用失败
      deferred.reject(errorEvent.code, errorEvent.message, errorEvent.data);
      // 判断是否存在回调函数
      // _this.errorCallback && _this.errorCallback(code, successEvent.message, successEvent.data)
      _this.returnObj = {code: `${successEvent.code}`, message: successEvent.message, data: successEvent.data}
    };
    new Ajax(_options);
    return this.returnPromise
  };
  /**
   * @desc 二层翻译 返回层中的message数据
   * @param {Object} res：response数据
   */
  deepTraverseMsg = function (res) {
    if ((res.message || res.msg) && res.code) {
      res[res.message ? 'message' : 'msg'] = this.handleMsg(res) || res.message || res.msg || '';
    }
    if (res.data && res.data.code) {
      this.deepTraverseMsg(res.data);
    }
    return res;
  }
  /**
   * @desc 处理消息语言翻译并返回信息
   * @param {Object} data 返回消息内容中data
   *
   */
  handleMsg (data) {
    var code = data.code,
      message = data.message || data.msg,
      i = 0,
      countMsg = 0,
      transalteMsg = '',
      res = {};
    // code是否存在多个
    if (code && String(code).indexOf('|') != -1) {
      code = code.split('|');
      message = message.split('|');
      for (var len = code.length; i < len; ++i) {
        var curCode = String(code[i]).replace(/(^\s*)|(\s*$)/g, '');
        res = getI18nMessage('message.' + curCode, data.i18nData, countMsg) || message[i] || '';
        transalteMsg += res.temp + (i === (len - 1) ? '' : ' | ');
        countMsg = res.count;
      }
    } else {
      transalteMsg = getI18nMessage('message.' + code, data.i18nData) || message;
    }
    return transalteMsg;
  };

  /**
   * 添加服务请求过滤器
   * @param {*} obj 过滤器对象
   */
  addFilter (obj, unshift) {
    if (typeof obj != 'object') {
      logger.error('filter args must been json object');
      return;
    }
    if (!obj.name) {
      logger.error('filter args must have name attribute');
      return;
    }
    if (!obj.before || typeof obj.before !== 'function') {
      logger.error('filter args must have before function');
      return;
    }
    if (!obj.after || typeof obj.after !== 'function') {
      logger.error('filter args must have after function');
      return;
    }
    if (!obj.exception || typeof obj.exception !== 'function') {
      logger.error('filter args must have exception function');
      return;
    }
    unshift ? this._filters.unshift(obj) : this._filters.push(obj);
  };

  /**
   * 移除服务请求过滤器
   * @param {*} obj 过滤器对象
   */
  removeFilter (obj) {
    var name = typeof obj == 'string' ? obj : obj.name;
    var i = 0;
    for (; i < this._filters.length; i++) {
      if (name == this._filters[i].name) {
        break;
      }
    }
    if (i < this._filters.length) {
      this._filters.splice(i, 1);
    }
  };

  /**
   * 执行过滤
   * @param {*} type 过滤类型
   * @param {*} event 过滤事件参数
   */
  _doFilter (type, event, oldEvent) {
    var fname = 'exception';
    fname = type == 0 ? 'before' : fname;
    fname = type == 1 ? 'after' : fname;
    for (var i = 0; i < this._filters.length; i++) {
      if (!this._filters[i][fname]) {
        continue;
      }
      var res = this._filters[i][fname](event, oldEvent);
      if (res === false) {
        return false;
      }
    }
  }
}

export default new Service()

  

 

  

  

  
/**
 * 该文件为兼容组件库，补齐的部分方法，后续调整后不再使用
 * @create by kongqf 2019.08.06
 */
/* eslint-disable */
import router from '@/router'
import store from '@/store'

import { sessionStore, lookup, request, clone, extend, type, dateFormat } from '@/utils'
import { getAccessToken, refreshToken, getContrs, logout } from '@/utils/oauth'
import { getUrl, exportExcelByTable } from '@/utils/util'
import { validator } from "@/utils/validate"
import { getLanguage } from "@/utils/i18n";
import { iconsOld } from "@/config/constant/app.data.icon";
import { VIEW_SIZE, MENU_STOREOG_KEY } from '@/config/constant/app.data.common'
const globalEventBus = new Vue();
export default window.yufp = {
  lookup: lookup,
  clone: clone,
  refreshToken: refreshToken,
  validator: { validator },
  sessionStorage: {
    put: sessionStore.set,
    get: sessionStore.get
  },
  logger: {
    warn: function(msg) {
      console.error(msg);
    }
  },
  util: {
    icons: function () {
      const icons = [];
      iconsOld.forEach(icon => {
        icons.push(icon.id)
      })
      return icons;
    },
    /** 根据数组和对应属性返回满足el-tree的树形数据,
     *id: 对应id,
     *pid: 对应pid,
     *label: 对应展示字段,
     *root: 如果值为空或不存在则计算
     */
    genTree: function (data, attr) {
      var root = {};
      if (data == null || data.length == 0) {
        return [attr.root];
      }
      if (attr.root == null || attr.root == undefined || attr.root == '') {
        root.id = this.getTreeRoot(data, attr)[attr.pid];
      } else if (typeof attr.root == 'object') {
        var root = attr.root;
        root.id = root[attr.id] === undefined ? root.id : root[attr.id];
        root.pid = root[attr.pid] === undefined ? root.pid : root[attr.pid];
        root.label = root[attr.label] === undefined ? root.label : root[attr.label];
      } else {
        for (var i in data) {
          if (data[i][attr.id] == attr.root) {
            root.id = data[i][attr.pid];
            break;
          }
        }
        root.id = root.id == undefined ? attr.root : root.id;
      }
  
      var genTreeData = function (data, attr) {
        var ckey = {},
          pkey = {};
  
        for (var i = 0; i < data.length; i++) {
          var row = data[i];
          row.id = row[attr.id];
          row.pid = row[attr.pid];
          row.label = row[attr.label];
          row.children = [];
  
          ckey[row.id] = row;
          if (pkey[row.pid]) {
            pkey[row.pid].push(row);
          } else {
            pkey[row.pid] = [row];
          }
  
          var c = pkey[row.id];
          if (c) {
            row.children = c.concat();
          }
  
          var p = ckey[row.pid];
          if (p) {
            p.children.push(row);
          }
        }
        return pkey;
      };
  
      if (root.label) {
        root.children = genTreeData(data, attr)[root.id];
        return [root];
      }
      return genTreeData(data, attr)[root.id];
    },
    /**
     * 获取对应数据的根节点
     * @param data
     * @param attr
     */
    getTreeRoot:function (data, attributes) {
      var _root = {};
      if (data != null && data.length != 0) {
        _root = data[0];
        for (var k = 1; k < data.length; k++) {
          var i = 1;
          for (; i < data.length; i++) {
            if (data[i][attributes.id] == _root[attributes.pid]) {
              _root = data[i];
              break;
            }
          }
          if (i == data.length - 1) {
            break;
          }
        }
        return _root;
      }
    },
    /**
     * 为url添加token信息
     * @param url
     * @returns {string}
     */
    addTokenInfo: function (url) {
      var token = 'access_token=';
      var _url = '';
      if (url == null || url == '') {
        return _url;
      }
  
      if (!url.indexOf(token) > -1) {
        _url = url + (url.indexOf('?') > -1 ? '&' : '?') + token + getAccessToken();
      }
      return _url;
    },
    dateFormat
  },
  session: {
    checkCtrl: function (ctrlCode, menuId, isView) {
      var ctrls = getContrs();
      menuId = menuId || '';
      // if (!menuId) {
      //   // TODO 视图菜单时，获取控制点ID逻辑暂且未定
      //   menuId = isView ? '' : yufp.frame.tab().url;
      // }
      if (!ctrlCode || !ctrls || !menuId) {
        return false;
      }
      if (ctrls && menuId && ctrls[menuId] && ctrls[menuId][ctrlCode]) {
        return false;
      }
      return true;
    },
    settings: {
      logoutUrl: backend.uaaService + '/api/logout', // 注销URL
    },
    /**
     * 移除会话信息
     * @param already
     * undefined|false 主动触发退出，如：yufp.session.logout();
     * true 会话已过期，如：yufp.session.logout(true);
     */
    async logout(already) {
      await logout(already)
    }
    // session其他拓展属性在src/store/modules/oauth.js -> getSessionInfo
  },
  custom: {
    viewSize: function() {
      return sessionStore.get(VIEW_SIZE);
    },
    size: function() {
      return sessionStore.get(VIEW_SIZE);
    }
  },
  frame: {
    size: function() {
      return sessionStore.get(VIEW_SIZE);
    },
    addTab(options) {
      const menuList = sessionStore.get(MENU_STOREOG_KEY)
      const menuFunId = [];
      let path = options.id || "";
      let meta = options.data || {};
      for(var i = 0;i < menuList.length;i++) {
        menuFunId.push(menuList[i].funcId);
      } 
      if(menuFunId.indexOf(options.id) > -1 ){
        path = menuList[menuFunId.indexOf(options.id)].menuId
      }
      if(options.title) {
        meta.title = options.title
      }
      if(options.data) {
        meta.params = yufp.clone(options.data)
      }
      
      let addParams = {
        name : path,
        title : options.title,
        data: meta
      }
      if(options.key){
        addParams.key = options.key;
      }
      router.addTab(addParams)
    },
    // 新增路由 兼容1.0写法
    addRoute(route, title, meta, path) {
      router.addRoute(route, title, meta, path);
    },
    removeTab(path) {
      store.dispatch("tagsView/delVisitedView", { path });
      const visitedViews = store.state.tagsView.visitedViews;
      const lastView = visitedViews[visitedViews.length - 1];
      router.push({
        path: lastView.fullPath,
        params: lastView.params
      })
    },
  },
  service: {
    request: param => {
      if ((param.method||'get').toLowerCase() === 'get') {
        param['params'] = param.data;
      }
      request(param).then(res => {
        param.callback(res && res.code, '', res)
      })
    },
    /**
   * @desc 二层翻译 返回层中的message数据
   * @param {Object} res：response数据
   */
    deepTraverseMsg: function (res) {
      var _this = this;
      if ((res.message || res.msg) && res.code) {
        res[res.message ? 'message' : 'msg'] = _this.handleMsg(res) || res.message || res.msg || '';
      }
      if (res.data && res.data.code) {
        _this.deepTraverseMsg(res.data);
      }
      return res;
    },
    /**
   * @desc 处理消息语言翻译并返回信息
   * @param {Object} data 返回消息内容中data
   */
    handleMsg: function (data) {
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
          res = yufp.language.getLocaleText('message.' + curCode, data.i18nData, countMsg) || message[i] || '';
          transalteMsg += res.temp + (i === (len - 1) ? '' : ' | ');
          countMsg = res.count;
        }
      } else {
        transalteMsg = yufp.language.getLocaleText('message.' + code, data.i18nData) || message;
      }
      return transalteMsg;
    },
    getUrl: function(param) {
      return getUrl(param)
    }
  },
  language: {
    language: getLanguage(),
    /**
     * 获取对应的翻译文本
     * @param {String} key 待转换的key
     * @param {Object} ops 格式化字符串
     */
    getLocaleText: function (key, ops, count) {
      return this.resetTemplate(this.getValueByPath(window['lang' + this.language.substr(0, 1).toLocaleUpperCase() + this.language.substring(1)], key), ops, count);
    },
    /**
     * 格式化模板
     * @param {string} template 模板
     * @param {Object} ops 模板中对应的key值对
     */
    resetTemplate:function(template, ops, count) {
      var num = count || 0;
      var temp = ops && template ? template.replace(/{\w*}/g, function (node, key, index) {
        return ops[num++] || '';
      }) : template;
      return count == undefined ? temp : {
        count: num,
        temp: temp
      };
    },
    /**
     * 根据属性在对象中取值
     * @param {Object} object 取值对象
     * @param {String} prop 属性
     */
    getValueByPath: function (object, prop) {
      prop = prop || '';
      var paths = prop.split('.');
      var current = object;
      var result = null;
      for (var i = 0, j = paths.length; i < j; i++) {
        var path = paths[i];
        if (!current) {
          break;
        }
        if (i === j - 1) {
          result = current[path];
          break;
        }
        current = current[path];
      }
      return result;
    }
  },
  exportExcelByTable: function(options) {
    exportExcelByTable(options)
  },
  extend: function() {
    return extend(...arguments)
  },
  dateFormat: function(time, format) {
    return dateFormat(time, format);
  },
  isDate: function (date) {
    if (date === null || date === undefined) {
      return false;
    }
    if (isNaN(new Date(date.replace(/-/g, '/')).getTime())) {
      return false;
    }
    return true;
  },
  router: {
    to(route, data, divId) {
      router.push({
        path: route,
        params: data
      })
    },
    replace(route, data = {}, divId) {
      data._routeType = 'replace'
      router.replace({
        name: route,
        params: data
      })
    },
    getRoute(rid) {
      return router.match(rid)
    },
    addRoute(route, title, meta, path) {
      router.addRoute(route, title, meta, path)
    },
    removeTab(path) {
      store.dispatch("tagsView/delVisitedView", { path });
      const visitedViews = store.state.tagsView.visitedViews;
      const lastView = visitedViews[visitedViews.length - 1];
      router.push({
        path: lastView.fullPath,
        params: lastView.params
      })
    },
  },
  type: function (obj) {
    return type(obj);
  },
  globalEventBus // 全局事件总线
}

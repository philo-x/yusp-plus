/**
 * vue事件总线
 * 使用：
 *  Vue Api:        $on   $once   $emit   $off
 *  geventbus Api:  $gon  $gonce  $gemit  $goff
 * @created by qfkong 2019-09-24
 * @description vue事件总线
 * 参考：https://github.com/banxi1988/vue-geventbus
 */

/* eslint consistent-this:0 */
/* eslint prefer-rest-params:0 */
import Vue from "vue";
import { logger } from '@/utils';

export const eventBus = new Vue();
const pluginName = "YUFPEventBus";
function makeEventKey(event) {
  return `global:${event}`;
}
let fnIdSeq = 0;
function makeFnId(vid) {
  fnIdSeq++;
  return vid + "-" + fnIdSeq;
}
const fnIdkey = Symbol("fnId");
export default class VueEventBus {
  /**
   * 安装插件
   */
  static install(vueClass) {
    if (this.installed && this.vueClass === vueClass) {
      return;
    }
    logger.info(pluginName, " installed");
    this.installed = true;
    this.vueClass = vueClass;
    this.applyMixin(vueClass);
    this.addGlobalEventApi(vueClass);
  }

  static addGlobalEventApi(vueClass) {
    const clazz = this;
    vueClass.prototype.$gon = function (event, fn) {
      const _this = this;
      const vid = _this._uid;
      if (vid === undefined) {
        logger.error(pluginName, "no vid");
        return _this;
      }
      let events = event;
      if (!Array.isArray(event)) {
        events = [event];
      }
      const fnId = makeFnId(vid);
      fn[fnIdkey] = fnId;
      clazz.listenerVueMap.set(fnId, _this);
      for (const eventName of events) {
        const eventKey = makeEventKey(eventName);
        const listeners = clazz.keyEventListenersMap.get(eventKey) || [];
        listeners.push(fn);
        clazz.keyEventListenersMap.set(eventKey, listeners);
      }
      const vidFns = clazz.vidListenersMap.get(vid) || [];
      vidFns.push(fn);
      clazz.vidListenersMap.set(vid, vidFns);
      return _this;
    };
    vueClass.prototype.$gonce = function (event, fn) {
      const _this = this;
      function on() {
        _this.$goff(event, on);
        fn.apply(_this, arguments);
      }
      on.fn = fn;
      _this.$gon(event, on);
      return _this;
    };
    /**
     * clear all global event listener
     */
    vueClass.prototype.$goffAll = function () {
      clazz.removeListenersByVue(this);
      return this;
    };
    vueClass.prototype.$goff = function (event, fn) {
      const _this = this;
      if (!event) {
        _this.$goffAll();
        return _this;
      }
      if (Array.isArray(event)) {
        for (const eventName of event) {
          this.$goff(eventName, fn); // 递归
        }
        return _this;
      }
      const vid = _this._uid;
      if (vid === undefined) {
        logger.error(pluginName, "no vid");
        return _this;
      }
      const eventKey = makeEventKey(event);
      const listeners = clazz.keyEventListenersMap.get(eventKey);
      if (!listeners || listeners.length < 1) {
        return _this;
      }
      if (!fn) {
        clazz.keyEventListenersMap.delete(eventKey);
        return _this;
      } else {
        const otherListeners = [];
        for (const listener of listeners) {
          const cb = listener;
          // $goff 会把原始事件 Function 放在中间 Function 的 fn 属性中
          if (cb !== fn && cb.fn !== fn) {
            otherListeners.push(listener);
          }
        }
        clazz.keyEventListenersMap.set(eventKey, otherListeners);
        return _this;
      }
    };
    vueClass.prototype.$gemit = function (event, ...args) {
      const _this = this;
      const eventKey = makeEventKey(event);
      const listeners = clazz.keyEventListenersMap.get(eventKey);
      if (listeners && listeners.length > 0) {
        for (const listener of listeners) {
          const fnId = listener[fnIdkey];
          if (!fnId) {
            logger.warn(pluginName, "not fnId in listener", listener);
            continue;
          }
          const origVue = clazz.listenerVueMap.get(fnId);
          if (origVue) {
            listener.apply(origVue, args);
          } else {
            logger.warn(pluginName, "listener's original vue instance is gone:", listener);
          }
        }
      } else {
        logger.error(pluginName, "No global event listener for event:" + event);
      }
      return _this;
    };
  }

  /**
   * 移除 vue 实例设置的监听器
   * @param vue Vue 实例
   */
  static removeListenersByVue(vue) {
    const vid = vue._uid;
    const vueListeners = this.vidListenersMap.get(vid) || [];
    for (const listener of vueListeners) {
      const fnId = listener[fnIdkey];
      if (fnId) {
        this.listenerVueMap.delete(fnId);
      } else {
        logger.warn("No fnI in listener ", listener, " for vue ", vid);
      }
    }
    this.keyEventListenersMap.forEach(listeners => {
      let i = listeners.length;
      while (i--) {
        const listener = listeners[i];
        if (vueListeners.includes(listener)) {
          listeners.splice(i, 1);
        }
      }
    });
  }

  static applyMixin(vueClass) {
    const clazz = this;
    vueClass.mixin({
      beforeCreate() { },
      destroyed() {
        const vue = this;
        clazz.removeListenersByVue(vue);
      }
    });
  }
}
VueEventBus.installed = false;
VueEventBus.vueClass = null;
/**
 * 全局事件名与监听函数的映射
 */
VueEventBus.keyEventListenersMap = new Map();
/**
 * 监听函数与注册时所在 Vue 实例的映射
 * （为了保证监听函数调用时其绑定的 this 是其 监听事件时所在的 Vue 实例）
 */
VueEventBus.listenerVueMap = new Map();
VueEventBus.vidListenersMap = new Map();

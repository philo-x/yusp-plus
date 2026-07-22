/**
* 表单字段格式化
* created by kongqf  2019/02/23
*/
import { type } from '@/utils'
import { getOrgName, getUserName, moneyFormatter } from './util'
import { getLanguage } from '@/utils/i18n'
import { logInfo, checkCtrl } from '@/utils/util'
import router from '@/router'
const devEnv = process.env.NODE_ENV === 'development' // 是否开发环境 toto 
const formFormaterStr = {
  D1: 'yyyy-MM-dd',
  D2: 'yyyy/MM/dd',
  D3: 'yyyy年MM月dd',
  D4: 'yyyy年MM月dd日 HH:mm:ss',
  D5: 'yyyyMMdd',
  D6: 'yyyy/MM/dd HH:mm:ss',
  D7: 'yyyyMMddHHmmss',
  D8: 'yyyy/MM',
  D9: 'yyyyMM'
};
const _isObject = function (o) {
  return type(o) === 'object';
};
const _getParamValue = function (ps) {
  if (ps.length == 3 && _isObject(ps[0]) && _isObject(ps[1])) {
    return ps[2];
  }
  return ps[0];
};
const formFormaterFn = {
  /**
  * 金额格式化（默认两位小数）
  * @param {String|Number|Object} money 填写的金额值,用到表格上时为字段对象
  * @param {Undefined|Object} col 填写的金额值,用到表格上时为字段对象
  * @param {Undefined|String|Number} val 填写的金额值,用到表格上时为字段值
  */
  Currency: function (money, col, val) {
    if(type(money) === 'object') { // 表格字段格式化
      return val && moneyFormatter(val, 2);
    }
    money = _getParamValue([money]);
    return money && moneyFormatter(money, 2);
  },
  /**
  * 利率格式化（默认8位小数）
  * @param {String|Number} money 填写的金额值
  */
  Rate: function (money) {
    money = _getParamValue([money]);
    return money && moneyFormatter(money, 8);
  },
  /**
  * 金额格式化（自定义小数点位数）
  * @param {String|Number} money 填写的金额值
  * @param {String|Number} num 保留小数点位数
  */
  CurrencyDef: function (money, num) {
    money = _getParamValue([money, num]);
    return money && moneyFormatter(money, num);
  },
  /**
  * 机构号格式化为机构名称
  * @param {String|Number} 机构号
  */
  OrgnName: function (orgId) {
    orgId = _getParamValue([orgId]);
    return orgId && getOrgName(orgId);
  },
  /**
  * 用户编号格式化为用户名称
  * @param {String|Number} 机构号
  */
  UserName: function (userId) {
    userId = _getParamValue([userId]);
    return userId && getUserName(userId);
  }
  // 可以扩展其他数据格式化方法，如利率
};
export default {
  data: function () {
    return formFormaterStr;
  },
  methods: formFormaterFn
};

export const vueMixinButton = {
  methods: {
    buttonLogs: function (btnObj, buttonName) {
      // development开发环境关闭记录按钮日志信息
      // if (!devEnv) {
      //   return;
      // }
      var zhCn = {
        button: '按钮',
        accessMenu: '访问菜单：',
        path: '路径：',
        buttonName: '按钮名称：',
      };
      var en = {
        button: 'Button',
        accessMenu: 'Access menu:',
        path: 'Path:',
        buttonName: 'Button name:',
      }
      var lang = getLanguage() === 'en' ? en : zhCn;
      var tab = this.$route || router.history.current;
      if(!tab || !tab.meta.id) { // toto confirm q确认框无法获取当前页面路由
        return;
      }
      buttonName = buttonName || (!btnObj.$slots.default ? '' : btnObj.$el.innerText.trim());
      var log = {
        'menuId': tab.meta.id,
        'operFlag': lang.button,
        'operObjId': tab.meta.title,
        'logTypeId': '4',
        'content': lang.accessMenu + tab.meta.title + ',' + lang.path + tab.meta.routeUrl + ',' + lang.buttonName + buttonName
      };
      logInfo(log);
    },
    checkCtrl: function (ctrlCode) {
      // 检查是否有控制点权限，有权限返回false，无权限返回true
      return !checkCtrl(ctrlCode);
    }
  }
}
// 使用方法，在具体的页面中增加：
// 1、 import xymixin from '@/utils/mixin'
// 2、 export default {
//        mixins: [xymixin]
//    }

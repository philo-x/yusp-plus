/**
 * Created by jiachenpan on 16/11/18.
 */
import {getI18nMessage} from "@/locale";

/* eslint-disable */
/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const validMap = ['admin', 'editor'];
  return validMap.indexOf(str.trim()) >= 0
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/;
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/;
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/;
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}
export const validator = {
  /**
   * 数字验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'number': function (rule, value, callback) {
    var reg = /^\d+$/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_num_001')));
    } else {
      callback();
    }
  },

  /**
   * 年龄验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'age': function (rule, value, callback) {
    var reg = /^\d+$/;
    if (value && reg.test(value)) {
      var _age = parseInt(value);
      if (_age < 200) {
        callback();
      } else {
        callback(new Error(getI18nMessage('component.valid_age_001')));
      }
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_num_001')));
    } else {
      callback();
    }
  },

  /**
   * 邮编验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'postcode': function (rule, value, callback) {
    var reg = /^[1-9]\d{5}$/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_code_001')));
    } else {
      callback();
    }
  },

  /**
   * ip验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'ip': function (rule, value, callback) {
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    var reg1 = /^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$/;
    if (value) {
      if (value.indexOf(',') == -1) {
        if (reg.test(value) || reg1.test(value)) {
          callback();
        } else if (!reg.test(value) && !reg1.test(value)) {
          callback(new Error(getI18nMessage('component.valid_ip_001')));
        }
      } else {
        var temp = value.split(',');
        var count = 0;
        for (var i in temp) {
          if (temp[i] && !reg.test(temp[i]) && !reg1.test(temp[i])) {
            count++;
            callback(new Error(getI18nMessage('component.valid_ip_001')));
          }
        }
        if (count == 0) {
          callback();
        }
      }
    } else {
      callback();
    }
  },

  /**
   * 固定电话和小灵通验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'telephone': function (rule, value, callback) {
    // var reg = /(^\d{3}-\d{7,8}$)|(^\d{4}-\d{7,8}$)|(^\d{3}\d{7,8}$)|(^\d{4}\d{7,8}$)|(^\d{7,8}$)/;
    var reg = /^((0\d{2,3})-)?(\d{7,8})(-(\d{3,4}))?$/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_phone_001')));
    } else {
      callback();
    }
  },

  /**
   * 手机号码验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'phone': function (rule, value, callback) {
    var reg = /(^\d{3}-1[3458][0-9]\d{8}$)|(^\d{2}-1[3458][0-9]\d{8}$)/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_phone_002')));
    } else {
      callback();
    }
  },

  /**
   * 数字和字母验证，只能接受输入项为数字和字母
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'numberAndLetter': function (rule, value, callback) {
    var reg = /(^[A-Za-z0-9]+$)|([A-Za-z]+$)|([0-9]+$)/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_text_007')));
    } else {
      callback();
    }
  },

  /**
   * 手机号码验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'mobile': function (rule, value, callback) {
    var reg = /^1[3-9][0-9]\d{8}$/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_phone_003')));
    } else {
      callback();
    }
  },

  /**
   * 身份证号码验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'IDCard': function (rule, value, callback) {
    if (!value) {
      callback();
    } else {
      var area = {
        11: '北京',
        12: '天津',
        13: '河北',
        14: '山西',
        15: '内蒙古',
        21: '辽宁',
        22: '吉林',
        23: '黑龙江',
        31: '上海',
        32: '江苏',
        33: '浙江',
        34: '安徽',
        35: '福建',
        36: '江西',
        37: '山东',
        41: '河南',
        42: '湖北',
        43: '湖南',
        44: '广东',
        45: '广西',
        46: '海南',
        50: '重庆',
        51: '四川',
        52: '贵州',
        53: '云南',
        54: '西藏',
        61: '陕西',
        62: '甘肃',
        63: '青海',
        64: '宁夏',
        65: '新疆',
        71: '台湾',
        81: '香港',
        82: '澳门',
        91: '国外'
      };
      var Y, JYM;
      var S, M;
      var idCardArray = [];
      idCardArray = value.split('');
      if (area[parseInt(value.substr(0, 2))] == null) {
        callback(new Error(getI18nMessage('component.valid_card_001')));
      }
      var ereg;
      // 身份号码位数及格式检验
      switch (value.length) {
        case 15:
        if ((parseInt(value.substr(6, 2)) + 1900) % 4 == 0 ||
            ((parseInt(value.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(value
              .substr(6, 2)) + 1900) %
              4 == 0)) {
          ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; // 测试出生日期的合法性
        } else {
          ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; // 测试出生日期的合法性
        }
        if (ereg.test(value)) {
          callback();
        } else {
          callback(new Error('component.valid_card_002'));
        }
        break;
        case 18:
        // 18位身份号码检测
        // 出生日期的合法性检查
        // 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
        // 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
        if (parseInt(value.substr(6, 4)) % 4 == 0 ||
            (parseInt(value.substr(6, 4)) % 100 == 0 && parseInt(value.substr(6, 4)) % 4 == 0)) {
          ereg = /^[1-9][0-9]{5}(19|20|21)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; // 闰年出生日期的合法性正则表达式
        } else {
          ereg = ereg = /^[1-9][0-9]{5}(19|20|21)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; // 平年出生日期的合法性正则表达式
        }
        if (ereg.test(value)) { // 测试出生日期的合法性
          // 计算校验位
          S = ((parseInt(idCardArray[0]) + parseInt(idCardArray[10])) * 7) +
              ((parseInt(idCardArray[1]) + parseInt(idCardArray[11])) * 9) +
              ((parseInt(idCardArray[2]) + parseInt(idCardArray[12])) * 10) +
              ((parseInt(idCardArray[3]) + parseInt(idCardArray[13])) * 5) +
              ((parseInt(idCardArray[4]) + parseInt(idCardArray[14])) * 8) +
              ((parseInt(idCardArray[5]) + parseInt(idCardArray[15])) * 4) +
              ((parseInt(idCardArray[6]) + parseInt(idCardArray[16])) * 2) +
              (parseInt(idCardArray[7]) * 1) +
              (parseInt(idCardArray[8]) * 6) +
              (parseInt(idCardArray[9]) * 3);
          Y = S % 11;
          M = 'F';
          JYM = '10X98765432';
          M = JYM.substr(Y, 1); // 判断校验位
          if (M == idCardArray[17]) {
            callback();
          } else {
            callback(new Error('component.valid_card_003'));
          }
        } else {
          callback(new Error('component.valid_card_002'));
        }
        break;
      default:
        callback(new Error(getI18nMessage('component.valid_card_004')));
        break;
      }
    }
  },

  /**
   * 是否为中文验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'isChnChar': function (rule, value, callback) {
    var reg = /[\u4E00-\u9FA5]/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_zh_001')));
    } else {
      callback();
    }
  },

  /**
   * 输入项收尾空格验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'trim': function (rule, value, callback) {
    if (value != value.trim()) {
      callback(new Error(getI18nMessage('component.valid_trim_001')));
    } else {
      callback();
    }
  },

  /**
   * 邮箱验证
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'email': function (rule, value, callback) {
    // var reg = /[A-Za-z0-9_-]+[@](\S*)(net|com|cn|org|cc|tv|[0-9]{1,3})(\S*)/g;
    var reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_email_001')));
    } else {
      callback();
    }
  },

  /**
   * 小数验证，输入结果必须为小数
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'digit': function (rule, value, callback) {
    var reg = /^-?\d+(\.\d+)?$/g;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_num_002')));
    } else {
      callback();
    }
  },

  /**
   * 非零正整数
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'pInt': function (rule, value, callback) {
    var reg = /^\+?[1-9][0-9]*$/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_num_003')));
    } else {
      callback();
    }
  },

  /**
   * 0 整数和浮点数
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'gZero': function (rule, value, callback) {
    var reg = /^[+]?[0-9]*\d(\.\d+)?$/;
    if (value && reg.test(value)) {
      callback();
    } else if (value && !reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_num_003')));
    } else {
      callback();
    }
  },

  /**
   * 特殊字符
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'speChar': function (rule, value, callback) {
    var reg = new RegExp('[`~!@#$^&*()=|{}\':;\',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“\'。，、？+]');
    if (value && reg.test(value)) {
      callback(new Error(getI18nMessage('component.valid_msg_001')));
    } else {
      callback();
    }
  },

  /**
   * @description 组织机构代码校验 organizing institution bar code
   * rule为form表单当前验证的filed对应的验证rule规则
   * value为当前输入框返回值
   * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
   */
  'orgCode': function(rule, value, callback) {
    if (!value) {
      callback(new Error('非法组织机构代码'))
    } else {
      const firstarray = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
      const firstkeys = [3, 7, 9, 10, 5, 8, 4, 2];
      const secondarray = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'T', 'U', 'W', 'X', 'Y'];
      const secondkeys = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];
      function calc(code, array1, array2, b) {
        var count = 0;
        for (var i = 0; i < array2.length; i++) {
          count += array2[i] * array1.indexOf(code[i]);
        }
        return b - count % b;
      }
      function verify(str) {
        var code = str.toUpperCase();
        /**
         * 统一代码由十八位的阿拉伯数字或大写英文字母（不使用I、O、Z、S、V）组成。
         * 第1位：登记管理部门代码（共一位字符）
         * 第2位：机构类别代码（共一位字符）
         * 第3位~第8位：登记管理机关行政区划码（共六位阿拉伯数字）
         * 第9位~第17位：主体标识码（组织机构代码）（共九位字符）
         * 第18位：校验码​（共一位字符）
         */
        if (code.length != 18) {
          return false;
        }
        var reg = /^\w\w\d{6}\w{9}\w$/;
        if (!reg.test(code)) {
          return false;
        }
        // 登记管理部门代码：使用阿拉伯数字或大写英文字母表示。 机构编制：1​ 民政：5​ 工商：9​ 其他：Y
        reg = /^[1,5,9,Y]\w\d{6}\w{9}\w$/;
        if (!reg.test(code)) {
          return false;
        }
        /**
         * 机构类别代码：使用阿拉伯数字或大写英文字母表示。​
         * 机构编制机关：11打头​​
         * 机构编制事业单位：12打头​
         * 机构编制中央编办直接管理机构编制的群众团体：13打头​​
         * 机构编制其他：19打头​
         * 民政社会团体：51打头​
         * 民政民办非企业单位：52打头​
         * 民政基金会：53打头​
         * 民政其他：59打头​
         * 工商企业：91打头​
         * 工商个体工商户：92打头​
         * 工商农民专业合作社：93打头​
         * 其他：Y1打头​
         */
        reg = /^(11|12|13|19|51|52|53|59|91|92|93|Y1)\d{6}\w{9}\w$/;
        if (!reg.test(code)) {
          return false;
        }
        // 登记管理机关行政区划码：只能使用阿拉伯数字表示。按照GB/T 2260编码。​例如：四川省成都市本级就是510100；四川省自贡市自流井区就是510302。
        reg = /^(11|12|13|19|51|52|53|59|91|92|93|Y1)\d{6}\w{9}\w$/;
        if (!reg.test(code)) {
          return false;
        }
        /**
         * 主体标识码（组织机构代码）：使用阿拉伯数字或英文大写字母表示。按照GB 11714编码。
         * 在实行统一社会信用代码之前，以前的组织机构代码证上的组织机构代码由九位字符组成。格式为XXXXXXXX-Y。前面八位被称为“本体代码”；最后一位被称为“校验码”。校验码和本体代码由一个连字号（-）连接起来。以便让人很容易的看出校验码。但是三证合一后，组织机构的九位字符全部被纳入统一社会信用代码的第9位至第17位，其原有组织机构代码上的连字号不带入统一社会信用代码。
         * 原有组织机构代码上的“校验码”的计算规则是：
         * 例如：某公司的组织机构代码是：59467239-9。那其最后一位的组织机构代码校验码9是如何计算出来的呢？
         * 第一步：取组织机构代码的前八位本体代码为基数。5 9 4 6 7 2 3 9
         * 提示：如果本体代码中含有英文大写字母。则A的基数是10，B的基数是11，C的基数是12，依此类推，直到Z的基数是35。
         * 第二步：​​取加权因子数值。因为组织机构代码的本体代码一共是八位字符。则这八位的加权因子数值从左到右分别是：3、7、9、10、5、8、4、2。​
         * 第三步：本体代码基数与对应位数的因子数值相乘。​
         * 5×3＝15，9×7＝63，4×9＝36，6×10＝60，
         * 7×5＝35，2×8＝16，3×4=12，9×2＝18​​
         * 第四步：将乘积求和相加。​
          * 15+63+36+60+35+16+12+18=255
          * 第五步：​将和数除以11，求余数。​​
          * 255÷11=33，余数是2。​​
          */
          var firstkey = calc(code.substr(8), firstarray, firstkeys, 11);
          /**
           * 第六步：用阿拉伯数字11减去余数，得求校验码的数值。当校验码的数值为10时，校验码用英文大写字母X来表示；当校验码的数值为11时，校验码用0来表示；其余求出的校验码数值就用其本身的阿拉伯数字来表示。​
           * 11-2＝9，因此此公司完整的组织机构代码为 59467239-9。​​
           */
          var firstword;
          if (firstkey < 10) {
            firstword = firstkey;
          }
          if (firstkey == 10) {
            firstword = 'X';
          } else if (firstkey == 11) {
            firstword = '0';
          }
          if (firstword != code.substr(16, 1)) {
            return false;
          }
          /**
           * 校验码：使用阿拉伯数字或大写英文字母来表示。校验码的计算方法参照 GB/T 17710。
           * 例如：某公司的统一社会信用代码为91512081MA62K0260E，那其最后一位的校验码E是如何计算出来的呢？
           * 第一步：取统一社会信用代码的前十七位为基数。9 1 5 1 2 0 8 1 21 10 6 2 19 0 2 6 0提示：如果前十七位统一社会信用代码含有英文大写字母（不使用I、O、Z、S、V这五个英文字母）。则英文字母对应的基数分别为：A=10、B=11、C=12、D=13、E=14、F=15、G=16、H=17、J=18、K=19、L=20、M=21、N=22、P=23、Q=24、R=25、T=26、U=27、W=28、X=29、Y=30​
           * 第二步：​​取加权因子数值。因为统一社会信用代码前面前面有十七位字符。则这十七位的加权因子数值从左到右分别是：1、3、9、27、19、26、16、17、20、29、25、13、8、24、10、30、2​8
           * 第三步：基数与对应位数的因子数值相乘。​
           * 9×1=9，1×3=3，5×9=45，1×27=27，2×19=38，0×26=0，8×16=128​
           * 1×17=17，21×20=420，10×29=290，6×25=150，2×13=26，19×8=152
           * 0×23=0，2×10=20，6×30=180，0×28=0
           * 第四步：将乘积求和相加。​9+3+45+27+38+0+128+17+420+290+150+26+152+0+20+180+0=1495
           * 第五步：​将和数除以31，求余数。​​
           * 1495÷31=48，余数是17。​​
           */
          var secondkey = calc(code, secondarray, secondkeys, 31);
          /**
           * 第六步：用阿拉伯数字31减去余数，得求校验码的数值。当校验码的数值为0~9时，就直接用该校验码的数值作为最终的统一社会信用代码的校验码；如果校验码的数值是10~30，则校验码转换为对应的大写英文字母。对应关系为：A=10、B=11、C=12、D=13、E=14、F=15、G=16、H=17、J=18、K=19、L=20、M=21、N=22、P=23、Q=24、R=25、T=26、U=27、W=28、X=29、Y=30
           * 因为，31-17＝14，所以该公司完整的统一社会信用代码为 91512081MA62K0260E。​​
           */
          var secondword = secondarray[secondkey];
          if (!secondword || secondword != code.substr(17, 1)) {
            return false;
          }
          var word = code.substr(0, 16) + firstword + secondword;
          if (code != word) {
            return false;
          }
        return true
      }

      if (verify(value)) {
        callback();
      } else {
        callback(new Error('非法组织机构代码'))
      }
    }
  }
};

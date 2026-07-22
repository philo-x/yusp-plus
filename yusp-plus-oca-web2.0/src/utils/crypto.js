/**
 * @created by helin3 2018-10-29
 * @updated by
 * @description 加密实现工具方法,后续有新增实现，请增加加密算法-将rollups目录下内容复制到本文件末尾的TODO imports区域追加，该工具支持加密算法有：
 * aes,hmac-md5,hmac-ripemd160,hmac-sha1,hmac-sha224,hmac-sha256,hmac-sha3,hmac-sha384,hmac-sha512,md5,pbkdf2,rabbit,rabbit-legacy,rc4,ripemd160,sha1,sha224,sha256,sha3,sha384,sha512,tripledes,
 */
/**
crypto-js/core
crypto-js/x64-core
crypto-js/lib-typedarrays
crypto-js/md5
crypto-js/sha1
crypto-js/sha256
crypto-js/sha224
crypto-js/sha512
crypto-js/sha384
crypto-js/sha3
crypto-js/ripemd160
crypto-js/hmac-md5
crypto-js/hmac-sha1
crypto-js/hmac-sha256
crypto-js/hmac-sha224
crypto-js/hmac-sha512
crypto-js/hmac-sha384
crypto-js/hmac-sha3
crypto-js/hmac-ripemd160
crypto-js/pbkdf2
crypto-js/aes
crypto-js/tripledes
crypto-js/rc4
crypto-js/rabbit
crypto-js/rabbit-legacy
crypto-js/evpkdf
crypto-js/format-openssl
crypto-js/format-hex
crypto-js/enc-latin1
crypto-js/enc-utf8
crypto-js/enc-hex
crypto-js/enc-utf16
crypto-js/enc-base64
crypto-js/mode-cfb
crypto-js/mode-ctr
crypto-js/mode-ctr-gladman
crypto-js/mode-ofb
crypto-js/mode-ecb
crypto-js/pad-pkcs7
crypto-js/pad-ansix923
crypto-js/pad-iso10126
crypto-js/pad-iso97971
crypto-js/pad-zeropadding
crypto-js/pad-nopadding
 */

import logger from './logger';
import request from './request';
import hmacSHA256 from 'crypto-js/hmac-sha256';
import hmacSHA512 from 'crypto-js/hmac-sha512';
import AES from 'crypto-js/aes';
import Pkcs7 from 'crypto-js/pad-pkcs7';
import Utf8 from 'crypto-js/enc-utf8';
import Base64 from 'crypto-js/enc-base64';
import ECB from 'crypto-js/mode-ecb';
import { mode } from 'crypto-js';
import TripleDES from 'crypto-js/tripledes';

import { sessionStore } from './storage';

const backend = {
  uaaService: ''
};
/**
   * 报文安全处理
   */
function Crypto () {
  this.initialCfg = {
    storeKey: 'UFP-ENCRYPT-KEY', // 存储密钥KEY
    hmac: 'SHA256', // 采用哪种hmac算法：SHA256、SHA512
    AESkey: '', // 十六位十六进制数作为密钥
    AESAESiv: '', // 十六位十六进制数作为密钥偏移量
    hmacKey: null, // hmac默认密钥
    hmacUrl: backend.uaaService + '/ua/n/secretkey/exchange', // 握手确认签名密钥，即握手生成上述hmacKey
    rsaPubKey: null, // rsa默认公钥
    rsaPrivKey: null // rsa默认私钥
  };
};

/**
   * 设置测试RSA公私钥，真正使用时，考虑性能优化时，可删除此方法
   */
Crypto.prototype.setTestRsaKey = function () {
  var rsaPubKey = '-----BEGIN PUBLIC KEY-----' +
      'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDlOJu6TyygqxfWT7eLtGDwajtN' +
      'FOb9I5XRb6khyfD1Yt3YiCgQWMNW649887VGJiGr/L5i2osbl8C9+WJTeucF+S76' +
      'xFxdU6jE0NQ+Z+zEdhUTooNRaY5nZiu5PgDB0ED/ZKBUSLKL7eibMxZtMlUDHjm4' +
      'gwQco1KRMDSmXSMkDwIDAQAB' +
      '-----END PUBLIC KEY-----';

  var rsaPrivKey = '-----BEGIN RSA PRIVATE KEY-----' +
      'MIICXQIBAAKBgQDlOJu6TyygqxfWT7eLtGDwajtNFOb9I5XRb6khyfD1Yt3YiCgQ' +
      'WMNW649887VGJiGr/L5i2osbl8C9+WJTeucF+S76xFxdU6jE0NQ+Z+zEdhUTooNR' +
      'aY5nZiu5PgDB0ED/ZKBUSLKL7eibMxZtMlUDHjm4gwQco1KRMDSmXSMkDwIDAQAB' +
      'AoGAfY9LpnuWK5Bs50UVep5c93SJdUi82u7yMx4iHFMc/Z2hfenfYEzu+57fI4fv' +
      'xTQ//5DbzRR/XKb8ulNv6+CHyPF31xk7YOBfkGI8qjLoq06V+FyBfDSwL8KbLyeH' +
      'm7KUZnLNQbk8yGLzB3iYKkRHlmUanQGaNMIJziWOkN+N9dECQQD0ONYRNZeuM8zd' +
      '8XJTSdcIX4a3gy3GGCJxOzv16XHxD03GW6UNLmfPwenKu+cdrQeaqEixrCejXdAF' +
      'z/7+BSMpAkEA8EaSOeP5Xr3ZrbiKzi6TGMwHMvC7HdJxaBJbVRfApFrE0/mPwmP5' +
      'rN7QwjrMY+0+AbXcm8mRQyQ1+IGEembsdwJBAN6az8Rv7QnD/YBvi52POIlRSSIM' +
      'V7SwWvSK4WSMnGb1ZBbhgdg57DXaspcwHsFV7hByQ5BvMtIduHcT14ECfcECQATe' +
      'aTgjFnqE/lQ22Rk0eGaYO80cc643BXVGafNfd9fcvwBMnk0iGX0XRsOozVt5Azil' +
      'psLBYuApa66NcVHJpCECQQDTjI2AQhFc1yRnCU/YgDnSpJVm1nASoRUnU8Jfm3Oz' +
      'uku7JUXcVpt08DFSceCEX9unCuMcT72rAQlLpdZir876' +
      '-----END RSA PRIVATE KEY-----';

  this.setRsaPubKey(rsaPubKey);
  this.setRsaPrivKey(rsaPrivKey);
};

/**
   * 存储相关密钥
   */
Crypto.prototype.storeEncryptKey = function () {
  sessionStore.put(this.initialCfg.storeKey, JSON.stringify(this.initialCfg));
};

/**
   * 移除相关密钥
   */
Crypto.prototype.removeEncryptKey = function () {
  sessionStore.remove(this.initialCfg.storeKey);
};

/**
   * 刷新时获取/初始化相关密钥
   */
Crypto.prototype.readEncryptKey = function () {
  var storeKey = sessionStore.get(this.initialCfg.storeKey);
  if (storeKey) {
    this.initialCfg = JSON.parse(storeKey);
  }
  return storeKey;
};

/**
   * hmac默认密钥
   * @param {string} hmacKey 密钥
   */
Crypto.prototype.setHmacKey = function (hmacKey) {
  this.initialCfg.hmacKey = hmacKey;
};

/**
   * 设置RSA公钥
   * @param {string} rsaPubKey 公钥
   */
Crypto.prototype.setRsaPubKey = function (rsaPubKey) {
  this.initialCfg.rsaPubKey = rsaPubKey;
};

/**
   * 设置RSA私钥
   * @param {string} rsaPrivKey 私钥
   */
Crypto.prototype.setRsaPrivKey = function (rsaPrivKey) {
  this.initialCfg.rsaPrivKey = rsaPrivKey;
};

/**
   * 密钥相关的哈希运算加密，目前默认使用HMAC-SHA256
   * @param {string} message 报文体
   * @param {string} hmacKey 密钥
   */
Crypto.prototype.hmacEncrypt = function (message, hmacKey) {
  var encrypted;
  if (this.initialCfg.hmac === 'SHA256') {
    encrypted = this.hmacSha256(message, hmacKey);
  } else if (this.initialCfg.hmac === 'SHA512') {
    encrypted = this.hmacSha512(message, hmacKey);
  }
  return encrypted;
};

/**
   * HMAC-SHA256 加密哈希
   * @param {string} message 报文体
   * @param {string} hmacKey 密钥
   */
Crypto.prototype.hmacSha256 = function (message, hmacKey) {
  var hmac;
  try {
    hmacKey = hmacKey || this.initialCfg.hmacKey;
    hmac = Base64.stringify(hmacSHA256(message, hmacKey));
  } catch (e) {
    logger.error('Crypto: hmac compute error! ', e);
  }
  return hmac;
};

/**
   * HMAC-SHA512 加密哈希
   * @param {string} message 报文体
   * @param {string} hmacKey 密钥
   */
Crypto.prototype.hmacSha512 = function (message, hmacKey) {
  var hmac;
  try {
    hmacKey = hmacKey || this.initialCfg.hmacKey;
    hmac = Base64.stringify(hmacSHA512(message, hmacKey));
  } catch (e) {
    logger.error('Crypto: hmac compute error! ', e);
  }
  return hmac;
};

/**
   * rsa 公钥加密
   * @param {string} message 加密报文体
   * @param {string} rsaPubKey 公钥，可选项
   */
Crypto.prototype.rsaEncrypt = function (message, rsaPubKey) {
  var text;
  try {
    rsaPubKey = rsaPubKey || this.initialCfg.rsaPubKey;
    const jsencrypt = new window.JSEncrypt();
    jsencrypt.setPublicKey(rsaPubKey);
    text = jsencrypt.encrypt(message);
  } catch (e) {
    logger.error('Crypto: rsa encrypt compute error! ', e);
  }
  return text;
};

/**
   * rsa 私钥解密
   * @param {string} message 解密报文
   * @param {string} rsaPrivKey 私钥，可选项
   */
Crypto.prototype.rsaDecrypt = function (message, rsaPrivKey) {
  try {
    rsaPrivKey = rsaPrivKey || this.initialCfg.rsaPrivKey;
    const jsencrypt = new window.JSEncrypt();
    jsencrypt.setPrivateKey(rsaPrivKey);
    return jsencrypt.decrypt(message);
  } catch (e) {
    logger.error('Crypto: rsa decrypt compute error! ', e);
  }
  return undefined;
};
Crypto.prototype.aesInit = function () {
  this.initialCfg.AESkey = 'aaaabbbbccccddddeeeeffffgggghhhh'; // 十六位十六进制数作为密钥
  this.initialCfg.AESAESiv = '1234567812345678'; // 十六位十六进制数作为密钥偏移量
  // console.log('kongqf', this.aesEncrypt('kongqf'), this.aesDecrypt(this.aesEncrypt('kongqf')));
};
/**
   * AES解密
   * @param {String} word 待解密字符串
   */
Crypto.prototype.aesDecrypt = function (word) {
  return AES.decrypt(word, Utf8.parse(this.initialCfg.AESkey), {
    iv: Utf8.parse(this.initialCfg.AESAESiv),
    mode: mode.CBC,
    padding: Pkcs7
  }).toString(Utf8);
};

/**
   * 加密方法,取值时，需要使用toString()转换成字符串
   * @param {String} word 待加密字符串
   */
Crypto.prototype.aesEncrypt = function (word) {
  return AES.encrypt(word, Utf8.parse(this.initialCfg.AESkey), {
    iv: Utf8.parse(this.initialCfg.AESAESiv),
    mode: mode.CBC,
    padding: Pkcs7
  }).toString();
};

/**
   * 握手确认交易请求密钥
   * @param {string} tokenId 公钥
   * @param {string} rsaPubKey 公钥
   * @param {function} callback 回调方法
   */
Crypto.prototype.sign = function (tokenId, rsaPubKey, callback) {
  // 设置保存客户端TokenId
  // zyfp.service.putToken(tokenId);
  // 设置保存客户端公钥
  this.setRsaPubKey(rsaPubKey);
  const hmacKeyA = this.uuid();
  request({
    needHmac: false,
    url: this.initialCfg.hmacUrl,
    method: 'post',
    data: {
      tokenId: tokenId,
      clientSecretKey: this.rsaEncrypt(hmacKeyA)
    }
  }).then(res => {
    const { data } = res;
    var serverSecretKey = data.serverSecretKey || '';
    if (serverSecretKey) {
      const hmacKeyB = this.tripleDesDecrypt(serverSecretKey, hmacKeyA);
      this.setHmacKey(hmacKeyA + hmacKeyB);
      this.storeEncryptKey();
      if (typeof callback === 'function') {
        callback();
      }
    } else {
      logger.error('密钥接口返回数据异常');
    }
  });
};

/**
   * 3des加密
   * @param {string} message 明文
   * @param {string} desKey 密钥
   */
Crypto.prototype.tripleDesEncrypt = function (message, desKey) {
  try {
    const keyHex = Utf8.parse(desKey);
    return TripleDES.encrypt(message, keyHex, {
      mode: ECB,
      padding: Pkcs7
    }).toString();
  } catch (e) {
    logger.error('Crypto: 3des encrypt compute error! ', e);
  }
  return undefined;
};

/**
   * 3des解密
   * @param {string} ciphertext 密文
   * @param {string} desKey 密钥
   */
Crypto.prototype.tripleDesDecrypt = function (ciphertext, desKey) {
  try {
    const keyHex = Utf8.parse(desKey);
    return TripleDES.decrypt({
      ciphertext: Base64.parse(ciphertext)
    }, keyHex, {
      mode: ECB,
      padding: Pkcs7
    }).toString(Utf8);
  } catch (e) {
    logger.error('Crypto: 3des decrypt compute error! ', e);
  }
  return undefined;
};

// 随机数UUID生成器集成
const hexBytes = [];
// Cache toString(16)
// This is massively impactful on performance
for (let i = 0; i < 256; i++) {
  // This is a fast way to ensure a 2 char hex byte
  hexBytes[i] = (i + 0x100).toString(16).substr(1);
}
const getRandomInt = function (min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
};
const uuidbin = function () {
  const b = [];
  for (let i = 0; i < 16; i++) {
    b.push(getRandomInt(0, 255));
  };
  b[6] = (b[6] & 0x0f) | 0x40;
  b[8] = (b[8] & 0x3f) | 0x80;
  return b;
};
/**
 * 随机数32位UUID
 */
Crypto.prototype.uuid = function () {
  var b = uuidbin();
  return hexBytes[b[0]] + hexBytes[b[1]] +
      hexBytes[b[2]] + hexBytes[b[3]] + // '-' +
      hexBytes[b[4]] + hexBytes[b[5]] + // '-' +
      hexBytes[b[6]] + hexBytes[b[7]] + // '-' +
      hexBytes[b[8]] + hexBytes[b[9]] + // '-' +
      hexBytes[b[10]] + hexBytes[b[11]] +
      hexBytes[b[12]] + hexBytes[b[13]] +
      hexBytes[b[14]] + hexBytes[b[15]];
};

export default new Crypto();
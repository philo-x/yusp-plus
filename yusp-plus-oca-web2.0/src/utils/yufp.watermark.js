/**
 * @created by wy 2018-11-14
 * @updated by
 * @description 水印
 */
import { extend, sessionStore } from '@/utils'
import { USER_STORE_KEY } from '@/config/constant/app.data.common'

class Watermark {
  constructor() {
    const _this = this;
    const _options = {
      watermarkText: this._getUserInfo(), // 水印内容，默认为用户编码_用户姓名
      fontFamily: '微软雅黑', // 文字字体，默认为微软雅黑
      fontColor: 'rgba(0,0,0,0.20)', // 文字颜色，默认为黑色20%透明
      fontSize: 14, // 水印文字字号，默认14px
      watermarkBox: 'yu-frame-root', // 水印显示区域为组件或容器的id或className，默认已设置tab工作区、dialog弹出窗口等容器、面板类组件，需根据客户化需求配置
      x: 20, // 水印起始位置x轴坐标
      y: 20, // 水印起始位置Y轴坐标
      rows: 20, // 水印行数
      cols: 20, // 水印列数
      xSpace: 200, // 水印x轴间隔
      ySpace: 100, // 水印y轴间隔
      alpha: 0.5, // 水印透明度
      width: 180, // 水印宽度
      height: 60,
      angle: 15, // 水印倾斜度数
      pic: ''
    };
    extend(_this, _options);
    this.init();
  }
  _getUserInfo() {
    if(sessionStore) {
      const user = sessionStore.get(USER_STORE_KEY) || {userCode: '', userName: ''}
      return user.userCode + '_' + user.userName
    }
    return ''
  }
  /**
   * 初始化
   */
  init() {
    // 开启水印时默认给body加入yu-watermark类名，便于UI扩展控制
    // document.querySelector('body').className += ' yu-watermark';
    // this.setWatermark();
    // 增加样式
    const style = document.createElement('style');
    style.innerHTML = `.watermark-mask{-webkit-transform:rotate(-${this.angle}deg);-moz-transform:rotate(-${this.angle}deg);-ms-transform:rotate(-${this.angle}deg);-o-transform:rotate(-${this.angle}deg);transform:rotate(-${this.angle}deg);visibility:visible;position:absolute;z-index:9999;opacity:${this.alpha};font-size:${this.fontSize}px;font-family:'${this.fontFamily}';color:${this.fontColor};text-align:center;width:${this.width}px;height:${this.height}px;display:block;pointer-events:none;}`;
    document.querySelector('head').appendChild(style);
    this.createWatermark();
  }
  /**
   * @description 创建水印遮罩
   */
  createWatermark() {
    this.watermarkText = this._getUserInfo()
    var oTemp = document.createDocumentFragment();
    // 获取页面最大宽度
    // var page_width = Math.max(document.body.scrollWidth,document.body.clientWidth);
    // 获取页面最大长度
    // var page_height = Math.max(document.body.scrollHeight,document.body.clientHeight);

    var div = document.getElementsByClassName(this.watermarkBox)[0];
    // 获取页面最大宽度
    var pageWidth = Math.max(div.scrollWidth, div.clientWidth);
    // 获取页面最大长度
    var pageHeight = Math.max(div.scrollHeight, div.clientHeight);
    // 如果将水印列数设置为0，或水印列数设置过大，超过页面最大宽度，则重新计算水印列数和水印x轴间隔
    if (this.cols == 0 || (parseInt(this.x + this.width * this.cols + this.xSpace * (this.cols - 1)) > pageWidth)) {
      this.cols = parseInt((pageWidth - this.x + this.xSpace) / (this.width + this.xSpace));
      this.xSpace = parseInt((pageWidth - this.x - this.width * this.cols) / (this.cols - 1));
    }

    // 如果将水印行数设置为0，或水印行数设置过大，超过页面最大长度，则重新计算水印行数和水印y轴间隔
    if (this.rows == 0 || (parseInt(this.y + this.height * this.rows + this.ySpace * (this.rows - 1)) > pageHeight)) {
      this.rows = parseInt((this.ySpace + pageHeight - this.y) / (this.height + this.ySpace));
      this.ySpace = parseInt((pageHeight - this.y - this.height * this.rows) / (this.rows - 1));
    }
    var arr = [];
    for (var i = 1; i < this.rows - 1; i++) {
      var y = this.y + (this.ySpace + this.height) * i;
      for (var j = 0; j < this.cols; j++) {
        var x = this.x + (this.width + this.xSpace) * j;
        var maskDiv = document.createElement('div');
        maskDiv.id = 'mask_div' + i + j;
        maskDiv.setAttribute('class', 'watermark-mask')
        maskDiv.appendChild(document.createTextNode(this.watermarkText));
        // 设置水印div倾斜显示
        maskDiv.style.cssText = `left:${x}px;top:${y}px;`
        if (this.pic) {
          maskDiv.style.backgroundImage = this.pic ? 'url(' + this.pic + ')' : '';
          maskDiv.style.backgroundSize = '100% 100%';
        }
        oTemp.appendChild(maskDiv);
        arr.push(maskDiv.id);
      };
    };

    document.body.appendChild(oTemp);
  }
  clearWatermark() {
    var watermarkList = document.querySelectorAll('.watermark-mask')
    var bodyContainter = document.body
    for(var i = watermarkList.length - 1; i >= 0; i--) { 
      bodyContainter.removeChild(watermarkList[i]); 
    }
  }
  /**
   * 获取水印图片
   */
  getWatermarkPic(text, fontColor, fontSize, fontFamily) {
    const canvas = document.createElement('canvas');
    const context = canvas.getContext('2d');
    canvas.width = context.measureText(text).width;
    canvas.height = fontSize;
    context.font = fontSize + 'px ' + fontFamily;
    context.textBaseline = 'middle';
    context.fillText(text, 0, fontSize / 2);
    canvas.width = context.measureText(text).width;
    canvas.height = canvas.width;
    context.fillStyle = fontColor;
    context.font = fontSize + 'px ' + fontFamily;
    context.textBaseline = 'middle';
    context.rotate(-45 * Math.PI / 180);
    context.fillText(text, -canvas.width / 2, (canvas.width / 2) + (fontSize * 2));
    context.fillText(text, -fontSize, -fontSize / 2);
    return canvas.toDataURL('image/png');
  }

  /**
   * 设置水印
   * @param options {text,fontSize,fontColor,fontFamily,dom}
   * @param text  水印文字内容，可选
   * @param fontSize  水印字号，可选
   * @param fontColor  水印文字颜色，可选
   * @param fontFamily  水印文字字体，可选
   * @param dom  待显示水印的dom对象，可选
   */
  setWatermark(options) {
    const _options = options || {};
    this.watermarkText = this._getUserInfo()
    const _text = _options.text || this.watermarkText;
    const _fontColor = _options.fontColor ? _options.fontColor : this.fontColor;
    const _fontSize = _options.fontSize ? _options.fontSize : this.fontSize;
    const _fontFamily = _options.fontFamily ? _options.fontFamily : this.fontFamily;
    this.clearWatermark()
    this.createWatermark()
  }
}
window.watermark = new Watermark()

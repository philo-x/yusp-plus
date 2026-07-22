<!-- 
  @Created by panglx@yusys.com.cn 2021-03-18
  @updated by
  @description 在弹框中使用改组件时，要加v-if,当弹框显示的时候，tinymce才创建，销毁时，tinymce也销毁，eg:
    <yu-xdialog title="测试" :visible.sync="dialogVisible">
      <yu-tinymce ref="tinymce" v-model="context" :id="tinymceId" v-if="dialogVisible"></yu-tinymce>
    </yu-xdialog
-->
<template>
  <textarea class="tinymce-textarea" :id="id" :action="action"></textarea>
</template>
<script>
/* eslint camelcase:0 */
import { getLanguage } from '@/utils/i18n'
import { showMessage, parseTime } from '@/utils/util'
import { extend } from '@/utils'
import tinymce from "tinymce/tinymce";
import 'tinymce/themes/silver';
import 'tinymce/icons/default/icons'
import 'tinymce/plugins/image'// 插入上传图片插件
import 'tinymce/plugins/media'// 插入视频插件
import 'tinymce/plugins/table'// 插入表格插件
import 'tinymce/plugins/lists'// 列表插件
import 'tinymce/plugins/wordcount'// 字数统计插件
import 'tinymce/plugins/autolink';
import 'tinymce/plugins/paste';
import 'tinymce/plugins/code';
import 'tinymce/plugins/fullscreen';
import 'tinymce/plugins/emoticons';
import 'tinymce/plugins/charmap';
import 'tinymce/plugins/imagetools';
import 'tinymce/plugins/preview';
import 'tinymce/plugins/codesample';
import 'tinymce/plugins/link';
import 'tinymce/plugins/autosave';

export default {
  name: 'YuTinymce',
  props: {
    value: {
      type: String,
      default: ""
    },
    id: {
      type: String,
      default: 'tinymce_'
    }, // 富文本id
    disabled: { // 是否禁用
      type: Boolean,
      default: false
    },
    // 富文本模块名称
    modular: {
      type: String,
      default: ''
    },
    // 富文本最小输入字符
    minLength: Number,
    // 富文本最大输入字符
    maxLength: Number,
    // 上传文件的最大值 默认2M
    maxSize: {
      default: 2097152,
      type: Number
    },
    mediaMaxSize: {
      default: 104857600,
      type: Number
    },
    // 文件上传地址
    action:{
      type: String,
      // default: "https://yusp-oss.oss-cn-chengdu.aliyuncs.com/"
      default: yufp.util.addTokenInfo(
        backend.fileService + '/api/file/provider/richedituploadfile'
      )
    },
    policyUrl: {
      type: String,
      default:  backend.appOcaService + "/api/oss/policy"
    },
    deleteFileUrl: {
      type: String,
      default: backend.appOcaService + '/api/oss/delete'
    },
   
    // 富文本高度
    height: {
      type: Number,
      required: false,
      default: 240
    },
    // 富文本宽度
    width: {
      type: [Number, String],
      required: false,
    },
    // 文件选择器的使用场景
    filePickerTypes: {
      type: String,
      default: "image, media"
    },
    menubar:  {
      type: String,
      default: ""
    },
    // 默认插件
    plugins: {
      type: [String, Array],
      default: "autolink,paste,code,fullscreen,link,lists,media,wordcount,image,imagetools,table,preview,codesample,charmap,emoticons"
    },
    // 默认工具栏
    toolbar: {
      type: [String, Array],
      default: " undo redo removeformat formatpainter | code blockquote outdent indent | fontselect fontsizeselect bold italic underline strikethrough | forecolor backcolor alignleft aligncenter alignright alignjustify | bullist numlist codesample | image media table link | charmap preview | fullscreen"
    }
  },
  data() {
    return {
      readonly: this.disabled,
      hasChange: false,
      hasInit: false,
      // 保存上传文件对象
      filedata: [],
      uploadData:{},
      // 富文本编辑前内容(保存删除内容前的内容)
      content: "",
      filePickType: "",
      currentFileName: ''
    }
  },
  watch: {
    value: function (val) {
      let content = '';
      // 获取富文本去标签的内容
      val !== undefined && val !== null && (content = val.replace(/<[^>]*>|<\/[^>]*>/gm, ""));
      if (content.length > 0) {
        //同时去掉空格和换行
        content = content.replace(/[ ]|[\r\n]/g, "");
        content = content.replace(/&nbsp;/g, "");
      }
      this.$emit('count-valid', content.length); 
      if (!this.hasChange && this.hasInit) {
        if (!tinyMCE.editors[this.id]) {
          this.init();
        }
        this.$nextTick(function () {
          tinyMCE.editors[this.id] && tinyMCE.editors[this.id].setContent(val || '');
        });
      }
    },
    disabled(val) {
      var _this = this;
      _this.readonly = val;
      _this.destroy();
      _this.$nextTick(() => {
        setTimeout(() => {
          _this.init();
        });
      });
    }
  },
  mounted() {
    this.init();
  },
  activated() {
    !tinyMCE.editors[this.id] && this.init();
  },
  deactivated() {
    this.destroy();
  },
  beforeDestroy () {
    this.$emit('on-destroy')
    this.destroy();
  },
  methods: {
    isMedia(type) {
      return (["mp4", "mov"].indexOf(type) !== -1);
    },
    isImage(type) {
      return (["png", "jpg", "jpeg", "bmp", "gif", "webp", "psd", "svg", "tiff", "raw"].indexOf(type) !== -1);
    },
    getUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (c) => {
        return (c === "x" ? (Math.random() * 16) | 0 : "r&0x3" | "0x8").toString(16);
      });
    },
    destroy() {
      tinyMCE.editors[this.id] && tinyMCE.editors[this.id].destroy();
    },
    init() {
      const _this = this;
      const isZh = getLanguage() === 'zh_CN';
      window.tinymce.init({
        selector: "#" + _this.id,
        body_class: 'panel-body ',
        content_css: 'static/tinymce/skins/content/default/content.css',
        height: _this.height,
        width: _this.width,
        readonly: _this.readonly,
        object_resizing: false,
        toolbar: _this.toolbar,
        menubar: _this.menubar,
        skin_url: '/static/tinymce/skins/ui/oxide',
        language_url: isZh && "/static/tinymce/langs/zh_CN.js",
        emoticons_database_url: '/static/tinymce/plugins/emoticons/js/emojis.js',
        language: isZh && "zh_CN",
        plugins: _this.plugins,
        statusbar: false, // 去掉底部字数统计
        fontsize_formats: '12px 14px 16px 18px 24px 36px 48px 56px 72px',
        font_formats: isZh && '微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;',
        content_style: `
          *                         { padding:0; margin:0; }
          img                       { max-width:100%; display:block;height:auto; }
          a                         { cursor: pointer; color: #2877FF; }
          iframe                    { width: 100%; }
          p                         { line-height:1.6; margin: 0px; }
          .mce-object-iframe        { width:100%; box-sizing:border-box; margin:0; padding:0; }
          ul,ol                     { list-style-position:inside; }
        `,
        // CONFIG: Paste
        paste_retain_style_properties: 'all',
        paste_word_valid_elements: '*[*]', // word需要它
        paste_data_images: true, // 粘贴的同时能把内容里的图片自动上传，非常强力的功能
        paste_convert_word_fake_lists: false, // 插入word文档需要该属性
        paste_webkit_styles: 'all',
        paste_merge_formats: true,
        nonbreaking_force_tab: false,
        paste_auto_cleanup_on_paste: false,
        // link、img、media
        link_title: false,
        target_list: [{ title: '新窗口', value: '_blank' }],
        media_live_embeds: true,
        media_alt_source: false,
        media_poster: false,
        imagetools_toolbar: 'watermark',
        image_title: true,
        image_description: false,
        file_picker_types: this.filePickerTypes,
        audio_template_callback(data) {
          return '<audio width="' + data.width + '" height="' + data.height + '" src="' + data.source + '" controls="controls" controls>\n ' + '</audio>';
        },
        video_template_callback(data) {
          return '<video width="' + data.width + '" height="' + data.height + '" src="' + data.source + '" controls="controls">\n' + '</video>';
        },
        images_upload_handler(blobInfo, success, failure) {
          if (blobInfo.blob().size > _this.maxSize) {
            showMessage(_this.$t('component.sctpccgd'));
            return; 
          }
          _this.imgUpload(blobInfo, success, failure);
          // _this.$request({
          //   method: "get",
          //   url: _this.policyUrl,
          // }).then(({code, message, data}) => {
          //   _this.uploadData = extend({}, data);
          //   const fileDir = _this.modular ? (data.dir + _this.modular + '/') : data.dir;
          //   _this.uploadData.key = fileDir + parseTime(new Date(), '{y}-{m}-{d}') + '/' + _this.getUUID() + '_' + _this.currentFileName;
          //   _this.imgUpload(blobInfo, success, failure);
          // });
        },
        file_picker_callback(cb, value, meta) {
          _this.filePickType = meta.filetype;
          var input = document.createElement('input');
          input.setAttribute('type', 'file');
          input.setAttribute('id', 'tinymceUpload');
          input.onchange = function () {
            var file = this.files[0];
            var type = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase();
            var reader = new FileReader();
            _this.currentFileName = file.name || '';
            if (_this.filePickType === 'media' && _this.isMedia(type)) {
              if (file.size > _this.mediaMaxSize) {
                showMessage(_this.$t('component.sctpccgd'));
                return; 
              }
              _this.mediaUpload(file, cb);
              // _this.$request({
              //   method: "get",
              //   url: _this.policyUrl,
              // }).then(({code, message, data}) => {
              //   _this.uploadData = extend({}, data);
              //   const fileDir = _this.modular ? (data.dir + _this.modular + '/') : data.dir;
              //   _this.uploadData.key = fileDir + parseTime(new Date(), '{y}-{m}-{d}') + '/' + _this.getUUID() + '_' + file.name;
              //   _this.mediaUpload(file, cb);
              // });
            } else if (_this.filePickType === 'image' && _this.isImage(type)) {
              reader.onload = function () {
                var id = 'blobid' + new Date().getTime();
                var blobCache = tinymce.activeEditor.editorUpload.blobCache;
                var base64 = reader.result.split(',')[1];
                var blobInfo = blobCache.create(id, file, base64);
                blobCache.add(blobInfo); // call the callback and populate the Title field with the file name
                cb(blobInfo.blobUri(), {
                  title: file.name
                });
              };
              reader.readAsDataURL(file);
            } else {
              showMessage(_this.$t('component.qsczqgs'));
            }
            document.body.removeChild(input);
          };
          document.body.appendChild(input);
          document.getElementById('tinymceUpload').click();
        },
        init_instance_callback (editor) {
          if (_this.value !== undefined && _this.value !== null) {
            editor.setContent(_this.value);
            // // 初始化的时候校验字数是否通过
            // var wordcount = tinymce.activeEditor.plugins.wordcount;
            // var editNum = wordcount.body.getCharacterCountWithoutSpaces();
            // _this.$emit('count-valid', editNum); 
          }
          _this.hasInit = true;
          editor.on('Change KeyUp', (event) => {
            _this.hasChange = true;
            _this.$emit(
              'input',
              editor.getContent({
                format: 'raw'
              })
            );

            _this.content = editor.getContent({
              format: 'raw'
            });
          });
          editor.on('KeyDown', (event) => {
            if (event.keyCode == '08' || event.keyCode == 46) {
              // 匹配img标签
              var imgReg = /<img.*?(?:>|\/>)/gi;
              // 匹配src属性
              var srcReg = /src=['"]?([^'"]*)['"]?/i;

              var img = _this.content.match(imgReg);
              if (img != null) {
                var removeImg = [];
                var curImg = event.target.innerHTML.match(imgReg)
                  ? event.target.innerHTML.match(imgReg)
                  : [];
                for (var i = 0; i < img.length; i++) {
                  var exists = false;
                  for (var j = 0; j < curImg.length; j++) {
                    if (img[i] === curImg[j]) {
                      exists = true;
                    }
                  }
                  if (!exists) {
                    removeImg.push(img[i]);
                  }
                }
                if (removeImg.length) {
                  const deleteIds = [];
                  for(let a = 0;a < removeImg.length; a++) {
                    const curSrc = removeImg[a].match(srcReg)[1];
                    if (curSrc.indexOf('RichText') != -1) {
                      deleteIds.push(curSrc.replace(_this.action, ''));
                    }
                  }
                  if(deleteIds.length) {
                    _this.$request({
                      url: _this.deleteFileUrl,
                      method: 'post',
                      data: deleteIds
                    }).then(({code, message, data}) => {
                      if (code === '0000') {
                        console.log(_this.$t('component.EITinymceX_msg_002'));
                      } else {
                        showMessage(message);
                      }
                    });
                  }
                }
              }
            }
          });
        }
      });
    },
    mediaUpload(file, cb) {
      const _this = this;
      if (!file) {
        showMessage(_this.$t('component.myhqwj'));
        return;
      }
      const loading = _this.$loading({
        target: '#tinymceEditor',
        body: true,
        text: _this.$t('component.isloading')
      });
      const audioData = new window.FormData();
      Object.keys(_this.uploadData).forEach(key => {
        audioData.append(key, _this.uploadData[key]);
      });
      audioData.append('file', file);
      _this.$request({
        url: _this.action,
        method: 'post',
        data: audioData,
        // headers: {
        //   transformRequest: [data => data]
        // },
        needToken: false,
      }).then(({code, message, data}) => {
        loading.close();
        // const path = _this.uploadData.host + "/" + _this.uploadData.key;
         const path = yufp.util.addTokenInfo(
            backend.fileService +
              '/api/file/provider/download?fileId=' +
              data.filePath
          );
        cb(path);
      }).catch(() => {
        loading.close();
      });
    },
    imgUpload(blobInfo, success, failure) {
      const _this = this;
      const formData = new window.FormData();
      Object.keys(_this.uploadData).forEach(key => {
        formData.append(key, _this.uploadData[key]);
      });
      formData.append('file', blobInfo.blob(), blobInfo.filename());
      _this.$request({
        url: _this.action,
        method: 'post',
        data: formData,
        // headers: {
        //   transformRequest: [data => data]
        // },
        needToken: false,
      }).then(({code, message, data}) => {
        // const path = _this.uploadData.host + "/" + _this.uploadData.key;
        const path = yufp.util.addTokenInfo(
            backend.fileService +
              '/api/file/provider/download?fileId=' +
              data.filePath
          );
        success(path);
      });
    }
  }
}
</script>
<style lang="scss">
.el-loading-mask.is-fullscreen{
  z-index: 9999999;
}
</style>

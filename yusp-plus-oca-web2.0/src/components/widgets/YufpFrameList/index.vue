<!--
 * @FileDescription: 查询表组件
 * @Author: lvzl
 * @Date: 2020/09/25
 * @LastEditors: lvzl
 * 选项说明
 * @noBtnArr 增删改查按钮默认提供, 不需要哪个可配置此选项隐藏['add', 'edit', 'info', 'delete'],不配置或改配置配置为空，显示全部
 * @btnAttr 额外按钮配置，不用配置方法，如['aaa', 'bbb'],只需要在组件配置@btn-click="func"事件，会将按钮名字，选中的数据抛出，即可写自己的对应逻辑
 * @otherSettings 此选项主要是删除按钮，selectManyToDle代表是否支持多选删除，deleteWords代表请求时的请求参数字段比如'id'
 * @panelAttr panle组件相关配置
 * @requestUrls url的配置选项，（不配置时，默认自己实现这三个方法，可通过配置如@save-fn @edit-fn @delete-fn事件,参数为选中的数据）
 * @xdialogFormAttr 弹出框的xform属性配置（column, labelWidth）
 * @xTableAttr xtable 的属性配置选项，支持现有的属性，事件，但需要注意写法：比如row-number - rowNumber
 * @itemAttr 此选项包括xtable，xform-item的属性配置，其中isQFiled为true是form查询项，isTFiled为true是table列，isDFiled为true是dialog中的formitem
 * @xformAttr 查询表单的配置选项，column：一行几个查询条件，customSearchFn：自定义查询方法
 * @xdialogAttr 弹出框的width, title属性配置。
 * @add 自定义新增方法（可选配置）
 * @edit 自定义编辑方法（可选配置）
 * 事件
 * @btn-click 额外按钮的处理事件，参数为选中数据
 * @delete-fn 可自定义删除事件，不配置对应接口生效
 * @save-fn 可自定义保存事件，不配置对应接口生效
 * @edit-Fn 可自定义修改时保存事件，不配置对应接口生效
 -->
<script>
import { request, clone } from '@/utils'
import Vue from 'vue'
export default {
  name: 'YufpFrameList',
  componentName: 'YufpFrameList',
  components: {},
  props: {
    add: Function,
    edit: Function,
    noBtnArr: {
      type: Array,
      default: () => []
    },
    xTableAttr: {
      type: Object,
      default: () => {}
    },
    otherSettings: {
      type: Object,
      default: () => {}
    },
    xdialogFormAttr: {
      type: Object,
      default: () => {}
    },
    itemAttr: {
      type: Array,
      default: () => []
    },
    xformAttr: {
      type: Object,
      default: () => {}
    },
    btnAttr: { // 自定义的按钮配置选项
      type: Array,
      default: () => []
    },
    panelAttr: { // panel属性配置选项
      type: Object,
      default: () => {}
    },
    optionQueryArr: { // panel属性配置选项
      type: Object,
      default: () => {
        return {events: {}}
      }
    },
    requestUrls: {
      type: Object,
      default: () => {}
    },
    xdialogAttr: {
      type: Object,
      default: () => {}
    },
    openOptionQuery: {
      type: Boolean,
      default: false
    }
  },
  data: function() {
    var str = new Date().valueOf();
    return {
      optionQueryForm: {
        inputVal: '',
        selectVal: ''
      },
      qFileds: [],
      tFileds: [],
      dFileds: [],
      formdata: {},
      refTable: 'refTable' + str,
      refForm: 'refForm' + str,
      labelWidth: '100px',
      needSearchQuery: true,
      viewType: 'INFO',
      viewTitle: {
        'ADD': '新增',
        'EDIT': '修改',
        'INFO': '查看'
      },
      dialogVisible: false,
      formDisabled: false,
      formdataDialog: {},
      xdialogRefForm: `refForm${str}dialog`
    }
  },
  computed: {
    column: function () {
      if (this.xformAttr) {
        return Number.isNaN(Number(this.xformAttr.column)) ? 3 : Number(this.xformAttr.column);
      } else {
        return 3;
      }
    },
    dialogTitle: function () {
      if (this.xdialogAttr && this.xdialogAttr.title) {
        return `${this.xdialogAttr.title}${this.viewTitle[this.viewType]}`;
      }
      return `${this.viewTitle[this.viewType]}`;
    }
  },
  watch: {
    'needSearchQuery': function (val) {
      if (!val) {
        this.hidSearchIcon();
      }
    }
  },
  mounted: function() {
    this.init();
  },
  created() {
    this.handleData();
  },
  beforeDestroy: function() {

  },
  destroyed: function() {
    
  },
  methods: {
    // 处理数据
    handleData: function () {
      const qFileds = [];
      const tFileds = [];
      const dFileds = [];
      const tempqFileds = [];
      const temptFileds = [];
      const tempdFileds = [];
      if (Array.isArray(this.itemAttr) && this.itemAttr.length > 0) {
        this._l(this.itemAttr, (item, index) => {
          item.name = item.prop;
          if (item.isQFiled) { // isQFiled是form查询项，isTFiled是table列，isDFiled是dialog中的formitem
            qFileds.push(item);
          }
          if (item.isTFiled) {
            tFileds.push(item);
          }
          if (item.isDFiled) {
            dFileds.push(item);
          }
        });
      }
      if (qFileds.length > 0) {
        this._l(qFileds, item => {
          tempqFileds.push(item);
        });
      }
      if (tFileds.length > 0) {
        this._l(tFileds, item => {
          temptFileds.push(item);
        });
      }
      if (dFileds.length > 0) {
        this._l(dFileds, item => {
          tempdFileds.push(item);
        });
      }
      // this.removeAttr(temptFileds, '');
      this.qFileds = tempqFileds;
      this.tFileds = temptFileds;
      this.dFileds = tempdFileds;
    },
    removeAttr: function (obj, type) {
      if (obj instanceof Object) {
        if (type === 'q' || type === 'd') {
          Reflect.deleteProperty(obj, 'prop');
        } else {
          Reflect.deleteProperty(obj, 'ctype');
        }
      }
      return obj;
    },
    getSelections: function () {
      return this.$refs[this.refTable].selections || [];
    },
    remoteData: function () {
      const _this = this;
      _this.$refs[_this.refTable].remoteData();
    },
    renderXtableColumn: function (h) {
      const xtableColumn = [];
      const _this = this;
      if (this.tFileds.length > 0) {
        this._l(this.tFileds, (item, index) => {
          let temp;
          if (item.template) {
            temp = function(scope) {
              _this.scope = scope;
              return Vue.compile(item.template && item.template()).render.call(_this);
            }
          }
          const scopedSlots = {
            default: temp
          };
          if (this.xTableAttr.editable) {
            xtableColumn.push(<yu-xtable-column ctype={item.itemtype || 'input'} {...{props: item, attrs: item, on: item.events}} scopedSlots={scopedSlots}/>);
          } else {
            xtableColumn.push(<yu-xtable-column {...{props: item, on: item.events}} scopedSlots={scopedSlots}/>);
          }
        });
        return xtableColumn;
      } else {
        return null;
      }
    },
    renderDialog: function(h) {
      if(this.dFileds.length > 0 && this.xdialogAttr) {
        const dialogFormItem = [];
        this.dFileds.forEach((item, index) => {
          dialogFormItem.push(<yu-xform-item key={index} value={item.value} {...{ props: item, on: item.events }} name={item.name} colspan={item.colspan} ctype={item.itemtype}></yu-xform-item>);
        });
        return (
          <yu-xdialog title={this.dialogTitle} visible={this.dialogVisible} {...{on:{'update:visible': val => this.dialogVisible = val}}} width={this.xdialogAttr.width ? this.xdialogAttr.width : '650px'}>
            <yu-xform ref={this.xdialogRefForm} label-width={this.xdialogFormAttr ? this.xdialogFormAttr.labelWidth || '90px' : '90px'} vModel={this.formdataDialog} form-type={this.formDisabled ? 'details' : ''}>
              <yu-xform-group column={this.xdialogFormAttr ? this.xdialogFormAttr.column || '2' : '2'}>
                {dialogFormItem}
              </yu-xform-group>
            </yu-xform>
            <div slot="footer">
              {!this.formDisabled ? <el-button on-click={this.saveFn}>保存</el-button> : null}
              <el-button on-click={this.canelFn}>{this.viewType === 'INFO' ? '关闭' : '取消'}</el-button>
            </div>
          </yu-xdialog>
        );
      } else {
        return null;
      }
    },
    renderForm: function(h) {
      let customSearchFn = null;
      if (this.xformAttr) {
        customSearchFn = typeof this.xformAttr.customSearchFn === 'function' ? this.xformAttr.customSearchFn : null;
      }
      const xformQuery = [];
      if (this.qFileds.length > 0) {
        this.qFileds.forEach((formitem, index) => {
          xformQuery.push(<yu-xform-item {...{ props: formitem, on: formitem.events }} value={formitem.value} name={formitem.name} colspan={formitem.colspan} ctype={formitem.itemtype}></yu-xform-item>);
        });
      } else {
        this.needSearchQuery = false;
      }
      if(this.needSearchQuery) {
        return (
          <yu-xform slot="filter" vModel={this.formdata} ref={this.refForm} related-table-name={this.refTable} label-width={this.labelWidth} form-type="search" custom-search-fn={customSearchFn}>
            <yu-xform-group column={this.column}>
              {xformQuery}
            </yu-xform-group>
          </yu-xform>
        );
      } else {
        return null;
      }
    },
    renderBtn: function(h) {
      var _this = this;
      const btnarr = ['add', 'edit', 'info', 'delete'];
      const btn = [
        <el-button on-click={(event) => _this.add ? _this.add(event) : _this.addFn(event)}>新增</el-button>,
        <el-button on-click={(event) => _this.edit ? _this.edit(event) : _this.editFn(event)}>修改</el-button>,
        <el-button on-click={_this.infoFn}>查看</el-button>,
        <el-button on-click={_this.deleteFn}>删除</el-button>
      ];
      if (Array.isArray(_this.noBtnArr) && _this.noBtnArr.length > 0) {
        _this.noBtnArr.forEach(item => {
          const i = btnarr.indexOf(item);
          btn.splice(i, 1);
          btnarr.splice(i, 1);
        });
      }
      if (Array.isArray(_this.btnAttr) && _this.btnAttr.length > 0) {
        _this.btnAttr.forEach((item, index) => {
          btn.push(<el-button key={index} on-click={_this.btnFunc}>{item}</el-button>);
        });
      }
      return btn;
    },
    filterXtableColumnAttr: function (arr) {
      const _this = this;
      if (arr.length > 0) {
        for (let i = 0; i < arr.length; i++) {
          const element = arr[i];
          if (element.dataCode) {
            if (_this.dataCodeArr.indexOf(element.dataCode) === -1) {
              _this.dataCodeArr.push(element.dataCode);
            }
          }
          if (Array.isArray(element.children)) {
            this.filterXtableColumnAttr(element.children);
          }
        }
      }
    },
    btnFunc: function () { // 统一处理按钮点击事件
      const _this = this;
      const btnName = window.event.currentTarget.innerText;
      const selections = _this.$refs[_this.refTable].selections;
      if (selections.length <= 0) {
        _this.$message('请选择一条数据！', 'warning');
        return;
      }
      const obj = {
        selections: []
      };
      obj.btn = btnName;
      clone(selections, obj.selections);
      _this.$emit('btn-click', obj);
    },
    // 新增
    addFn: function() {
      const _this = this;
      _this.dialogVisible = true;
      _this.formDisabled = false;
      _this.viewType = 'ADD';
      _this.$nextTick(() => {
        _this.$refs[_this.xdialogRefForm].resetFields();
      });
    },
    // 编辑
    editFn: function() {
      const _this = this;
      const selections = _this.$refs[_this.refTable].selections;
      if (selections.length !== 1) {
        _this.$message('请选择一条数据！', 'warning');
        return;
      }
      _this.viewType = 'EDIT';
      _this.formDisabled = false;
      _this.dialogVisible = true;
      _this.$nextTick(() => {
        _this.$refs[_this.xdialogRefForm].resetFields();
        clone(selections[0], _this.formdataDialog);
      });
    },
    // 查看
    infoFn: function() {
      const _this = this;
      const selections = _this.$refs[_this.refTable].selections;
      if (selections.length !== 1) {
        _this.$message('请选择一条数据！', 'warning');
        return;
      }
      _this.viewType = 'INFO';
      _this.formDisabled = true;
      _this.dialogVisible = true;
      _this.$nextTick(() => {
        _this.$refs[_this.xdialogRefForm].resetFields();
        clone(selections[0], _this.formdataDialog);
      });
    },
    // 删除
    deleteFn: function() {
      const _this = this;
      const arr = [];
      const obj = {};
      const requestObj = {};
      const selections = _this.$refs[_this.refTable].selections;
      if (_this.otherSettings.selectManyToDle) { // 支持多选删除
        if (selections.length < 1) {
          _this.$message({ message: '请先选择要删除的记录', type: 'warning' });
          return;
        }
        selections.forEach((item, index) => {
          arr.push(item[_this.otherSettings.deleteWords]);
        });
        obj[_this.otherSettings.deleteWords] = arr.join(',');
      } else {
        if (selections.length !== 1) {
          _this.$message({ message: '请选择一条记录', type: 'warning' });
          return;
        }
        if (_this.otherSettings.deleteWords) {
          obj[_this.otherSettings.deleteWords] = selections[0][_this.otherSettings.deleteWords];
        } else {
          clone(selections, obj);
        }
      }
      clone(_this.requestUrls, requestObj);
      const url = requestObj.delete ? requestObj.delete['url'] : '';
      const method = requestObj.delete ? requestObj.delete['method'] : 'POST';
      _this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        callback: function (action) {
          if (action === 'confirm') {
            if (!url) {
              _this.$emit('delete-fn', obj);
            } else {
              request({
                method: method,
                url: url,
                data: obj
              }).then(res => {
                _this.$refs[_this.refTable].remoteData();
                _this.$message('操作成功');
              });
            }
          }
        }
      });
    },
    // 保存
    saveFn: function() {
      const _this = this;
      const requestObj = {};
      const model = {};
      let url = '';
      let method = '';
      clone(_this.requestUrls, requestObj);
      clone(_this.formdataDialog, model);
      if(_this.viewType === 'ADD') {
        url = requestObj.save ? requestObj.save['url'] : '';
        method = requestObj.save ? requestObj.save['method'] : 'POST';
      } else {
        url = requestObj.edit ? requestObj.edit['url'] : '';
        method = requestObj.edit ? requestObj.edit['method'] : 'POST';
      }
      let validate = false;
      _this.$refs[_this.xdialogRefForm].validate(function(valid) {
        validate = valid;
      })
      if (!validate) {
        _this.$message('表单校验未通过，请检查！');
        return;
      }
      if (!url) { // 如果不传递url,method 代表要自己实现保存方法
        if (_this.viewType === 'ADD') {
          _this.$emit('save-fn', model);
        } else {
          _this.$emit('edit-fn', model);
        }
        return;
      }
      request({
        url: url,
        method: method,
        data: model
      }).then(res => {
        _this.$refs[_this.refTable].remoteData();
        _this.$message('操作成功');
        _this.dialogVisible = false;
      })
    },
    canelFn: function() {
      this.dialogVisible = false;
    },
    init: function () {
      const _this = this;
      if (_this.xformAttr) {
        _this.labelWidth = _this.xformAttr.labelWidth ? _this.xformAttr.labelWidth : _this.labelWidth;
      }
    },
    fuzzyQuery: function (data) {
    },
    // 当没有查询条件时隐藏icon
    hidSearchIcon: function () {
      const _this = this;
      const panelDom = _this.$el.querySelector('.yu-base-panel-collapse');
      if (panelDom) {
        panelDom.style.display = 'none';
      }
    },
    // 选项查询事件
    optionQuerySearch() {
      var params = {};
      params[this.optionQueryForm.selectVal] = this.optionQueryForm.inputVal;
      this.$refs[this.refTable].remoteData(params);
      this.optionQueryForm.selectVal = '';
      this.optionQueryForm.inputVal = '';
    },
    renderOptionQuery() {
      var _this = this;
      var query = <div class="el-input-comb">
        <yu-input placeholder="请输入内容" vModel={this.optionQueryForm.inputVal}>
          <yu-xselect vModel={this.optionQueryForm.selectVal} {...{ props: _this.optionQueryArr, on: _this.optionQueryArr.events }} slot="prepend" placeholder="请选择">
          </yu-xselect>
          <i slot="suffix" class="el-input__icon yu-icon-search1" on-click={() => {this.optionQuerySearch()}}></i>
        </yu-input>
      </div>
      return query;
    }
  },
  render: function(h) {
    const btn = this.renderBtn(h);
    const xform = this.renderForm(h);
    const dialog = this.renderDialog(h);
    const xtableColumn = this.renderXtableColumn(h);
    if (this.panelAttr) {
      const optionQuery = this.renderOptionQuery(h);
      return (
        <div class="yu-frame-list">
          <yu-panel {...{props: this.panelAttr}} on-search={(event) => {this.fuzzyQuery(event)}}>
            <div slot="right" style={{float: 'left'}}>
              {btn.length === 0 ? null : <yu-button-drop>
                { btn }
              </yu-button-drop>}
              {this.openOptionQuery ? optionQuery : ''}
            </div>
            { xform }
            <yu-xtable request-type="POST" ref={this.refTable} {...{props: this.xTableAttr, on: this.xTableAttr.events}}>
              { xtableColumn }
            </yu-xtable>
          </yu-panel>
          { dialog }
        </div>
      );
    } else {
      return (
        <div>
          { xform }
          { btn }
          <yu-xtable request-type="POST" ref={this.refTable} {...{props: this.xTableAttr, on: this.xTableAttr.events}}>
            { xtableColumn }
          </yu-xtable>
          { dialog }
        </div>
      );
    }
  }
}
</script>
<style>
.el-input-comb .el-select .el-input.el-input--suffix{
  width: 90px;
  padding-left: 0;
}
.yu-frame-list .el-input-comb{
  float: left;
  padding: 10px 0px;
}
.yu-frame-list .el-input-comb .el-select{
  float: left;
}
.yu-base-panel-right-content .el-input-comb .el-input{
  width: auto;
}
</style>
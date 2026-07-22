<!--
 * @FileDescription: 表格行新增组件
 * @Author: lvzl
 * @Date: 2020/09/25
 * @LastEditors: lvzl
 * 选项说明
 * @noBtnArr 增删保存按钮默认提供, 不需要哪个可配置此选项隐藏['add', 'delete', 'save'],不配置或改配置配置为空，显示全部
 * @btnAttr 额外按钮配置，不用配置方法，如['aaa', 'bbb'],只需要在组件配置@btn-click="func"事件，会将按钮名字，选中的数据抛出，即可写自己的对应逻辑
 * @otherSettings 此选项主要是删除按钮，selectManyToDle代表是否支持多选删除，deleteWords代表请求时的请求参数字段比如'id'
 * @panelAttr panle组件相关配置
 * @requestUrls url的配置选项，（不配置时，默认自己实现这三个方法，可通过配置如@save-fn @edit-fn @delete-fn事件,参数为选中的数据）
 * @editTableCloumn 可编辑表格的列属性配置，参考一下配置
 * @xtableAttr xtable 的属性配置选项，支持现有的xtable属性，事件，但需要注意写法：比如row-number - rowNumber
 * 事件
 * @btn-click 额外按钮的处理事件，参数为选中数据
 * @delete-fn 可自定义删除事件，不配置删除接口生效
 * @save-fn 可自定义保存事件，不配置保存接口生效
 * @row-click 行点击事件
 -->
<script>
const str = new Date().valueOf();
import { request, clone } from '@/utils'
export default {
  name: 'YufpEditTable',
  componentName: 'YufpEditTable',
  props: {
    noBtnArr: {
      type: Array,
      default: () => []
    },
    btnAttr: { // 自定义的按钮配置选项
      type: Array,
      default: () => []
    },
    otherSettings: {
      type: Object,
      default: () => {}
    },
    panelAttr: { // panel属性配置选项
      type: Object,
      default: () => {}
    },
    requestUrls: {
      type: Object,
      default: () => {}
    },
    editTableCloumn: {
      type: Array,
      default: () => []
    },
    xtableAttr: {
      type: Object,
      default: () => {}
    }
  },
  data: function() {
    return {
      refTable: `reftable${str}`,
      tableTopAdd: false
    };
  },
  mounted: function () {
    this.setIsEditFlag();
  },
  methods: {
    // 设置是否可编辑标识
    setIsEditFlag: function () {
      const arr = this.$refs[this.refTable].tabledata;
      this._l(arr, (item) => {
        Reflect.defineProperty(item, '__isEditFlag', {value: false, enumerable: true});
      });
    },
    // 新增
    addFn: function () {
      if (this.tableTopAdd) {
        this.$refs[this.refTable].tabledata.unshift({});
      } else {
        this.$refs[this.refTable].tabledata.push({});
      }
    },
    // 删除
    deleteFn: function() {
      const _this = this;
      const arr = [];
      const obj = {};
      const requestObj = {};
      const selections = _this.$refs[_this.refTable].selections;
      if (this.otherSettings.selectManyToDle) {
        if (!(selections.length > 0)) {
          _this.$message({ message: '请至少选择一条记录', type: 'warning' });
          return;
        }
      } else {
        if (selections.length !== 1) {
          _this.$message({ message: '请选择一条记录', type: 'warning' });
          return;
        }
      }
      if (_this.otherSettings && _this.otherSettings.deleteWords) {
        obj[_this.otherSettings.deleteWords] = selections[0][_this.otherSettings.deleteWords];
      } else {
        clone(selections, obj);
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
                // 模拟删除
                // var index = _this.$refs[_this.refTable].tabledata.indexOf(selections);
                // _this.$refs[_this.refTable].tabledata.splice(index, 1);
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
      let valid;
      _this.$refs[_this.refTable].validate((fields) => {
        valid = fields;
      }, true);
      if (valid !== null) {
        _this.$message({ message: '校验未通过，请检查！', type: 'warning' });
        return;
      }
      const selections = _this.$refs[_this.refTable].selections;
      if (selections.length !== 1) {
        _this.$message({ message: '请选择一条记录', type: 'warning' });
        return;
      }
      const requestObj = {};
      const model = {};
      let url = '';
      let method = '';
      clone(_this.requestUrls, requestObj);
      clone(selections[0], model);
      Object.keys(model).forEach((item) => {
        if (item.startsWith('__')) {
          Reflect.deleteProperty(model, item);
        }
      });
      url = requestObj.save ? requestObj.save['url'] : '';
      method = requestObj.save ? requestObj.save['method'] : 'POST';
      if (!url) { // 如果不传递url,method 代表要自己实现保存方法
        _this.$emit('save-fn', model);
        return;
      }
      request({
        url: url,
        method: method,
        data: model
      }).then(res => {
        _this.$message('操作成功');
        _this.setIsEditFlag();
      })
    },
    // 额外配置按钮处理函数
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
    renderBtn: function(h) {
      const _this = this;
      const btn = [
        <yu-button on-click={_this.addFn}>新增</yu-button>,
        <yu-button on-click={_this.saveFn}>保存</yu-button>,
        <yu-button on-click={_this.deleteFn}>删除</yu-button>
      ];
      const btnarr = ['add', 'save', 'delete'];
      if (_this.noBtnArr.length > 0) {
        _this._l(_this.noBtnArr, (item) => {
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
      return (<yu-button-group>{ btn }</yu-button-group>);
    },
    renderTableCloumn: function (h) {
      const _this = this;
      const editTableCloumns = [];
      if (_this.editTableCloumn.length > 0) {
        _this._l(_this.editTableCloumn, (item, index) => {
          editTableCloumns.push(<yu-xtable-column key={index} {...{props: item, on: item.events}} ctype={item.ctype}></yu-xtable-column>);
        });
      }
      return editTableCloumns;
    },
    rowClick: function (data) {
      this.$refs[this.refTable].$refs.table.$refs.refTableBody.type = data['__isEditFlag'] === false ? 'default' : 'edit';
      this.$emit('row-click', data);
    }
  },
  render: function (h) {
    const btn = this.renderBtn(h);
    const tableCloumn = this.renderTableCloumn(h);
    return (
      <div>
        <yu-panel {...{props: this.panelAttr}}>
          <yu-button-drop slot="right">
            { btn }
          </yu-button-drop>
          <yu-xtable request-type="POST" {...{props: this.xtableAttr, on: this.xtableAttr.events}} ref={this.refTable} style={this.xtableAttr.style} on-row-click={this.rowClick}>
            { tableCloumn }
          </yu-xtable>
        </yu-panel>
      </div>
    );
  }
};
</script>
<style scoped>
.yu-base-panel-right-content .yu-buttons {
    float: left;
    padding: 0;
}
</style>
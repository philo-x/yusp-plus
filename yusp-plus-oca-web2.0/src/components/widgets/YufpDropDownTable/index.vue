<!--
 * @FileDescription: 下拉表格组件
 * @Author: lvzl
 * @Date: 2020/09/25
 * @LastEditors: lvzl
 * 选项说明
 * xformQueryFileds 查询条件配置选项，不配置时表明不需要查询功能，参考如下
 * xformAttr 查询表单的配置选项，column：一行几个查询条件，customSearchFn：自定义查询方法
 * @elTablexAttr el-table-x 的属性配置选项，支持现有的属性，事件，但需要注意写法：比如row-number - rowNumber
 * 其余选项见combotable的属性说明 http://192.168.251.163:9527/yufp-widgets-pc/1.4/index.html#/zh-CN/component/combo-table
 * 选取返回按钮只有当多选时才会展示，单选时自动隐藏，配置@select-line="selectLine"，可获得选中的数据，使用见示例
 * @select-line 多选时的选取返回事件
 -->
<script>
const trim = function (string) {
  return (string || '').replace(/^[\s\uFEFF]+|[\s\uFEFF]+$/g, '');
};
const hasClass = function (el, cls) {
  if (!el || !cls) { return false }
  if (cls.indexOf(' ') !== -1) { throw new Error('className should not contain space.') }
  if (el.classList) {
    return el.classList.contains(cls);
  } else {
    return (' ' + el.className + ' ').indexOf(' ' + cls + ' ') > -1;
  }
};
const removeClass = function (el, cls) {
  if (!el || !cls) { return }
  var classes = cls.split(' ');
  var curClass = ' ' + el.className + ' ';

  for (var i = 0, j = classes.length; i < j; i++) {
    var clsName = classes[i];
    if (!clsName) { continue }

    if (el.classList) {
      el.classList.remove(clsName);
    } else if (hasClass(el, clsName)) {
      curClass = curClass.replace(' ' + clsName + ' ', ' ');
    }
  }
  if (!el.classList) {
    el.className = trim(curClass);
  }
};
const addClass = function (el, cls) {
  if (!el) { return }
  var curClass = el.className;
  var classes = (cls || '').split(' ');

  for (var i = 0, j = classes.length; i < j; i++) {
    var clsName = classes[i];
    if (!clsName) { continue }

    if (el.classList) {
      el.classList.add(clsName);
    } else if (!hasClass(el, clsName)) {
      curClass += ' ' + clsName;
    }
  }
  if (!el.classList) {
    el.className = curClass;
  }
};
export default {
  name: 'YufpDropDownTable',
  xtype: 'YufpDropDownTable',
  props: {
    width: {
      type: Number,
      default: 502
    },
    xformQueryFileds: {
      type: Array,
      default: () => []
    },
    xformAttr: {
      type: Object,
      default: () => {}
    },
    elTablexAttr: {
      type: Object,
      default: () => {}
    },
    value: null,
    dataLabel: {
      type: String,
      default: 'label',
      required: true
    },
    placeholder: {
      type: String,
      default: '请选择'
    },
    clearable: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    details: Boolean
  },
  data: function () {
    var selValue = this.multiple ? [] : '';
    selValue = this.value ? this.value : selValue;
    return {
      formdata: {},
      needSearchQuery: true,
      refForm: 'refForm' + new Date().valueOf(),
      refTable: 'refTable' + new Date().valueOf(),
      labelWidth: '90px',
      inputHovering: false,
      tempSelVal: selValue,
      selectedLabel: '',
      visible: false,
      tableSelections: [],
      currentRowKey: '',
      multiple: this.elTablexAttr.checkbox,
      dataId: this.elTablexAttr.rowKey
    };
  },
  computed: {
    divDisPlay: function () {
      return {display: this.visible ? 'block' : 'none'};
    },
    column: function () {
      return Number.isNaN(Number(this.xformAttr.column)) ? 3 : Number(this.xformAttr.column);
    },
    iconClass: function () {
      var criteria =
        this.clearable &&
        !this.disabled &&
        this.inputHovering &&
        this.value !== undefined &&
        (this.multiple ? this.value.length > 0 : this.value !== '');
      return criteria ? 'circle-close is-show-close' : 'caret-top';
    }
  },
  watch: {
    tableSelections: function (selections) {
      if (selections.length > 0) {
        var tempArryVal = [];
        var tempArryLabel = [];
        for (var i = 0, length = selections.length; i < length; i++) {
          tempArryVal.push(selections[i][this.dataId]);
          tempArryLabel.push(selections[i][this.dataLabel]);
        }
        this.tempSelVal = this.multiple ? tempArryVal : tempArryVal[0];
        this.selectedLabel = this.multiple
          ? tempArryLabel.join(',')
          : tempArryLabel[0];
      } else {
        var blankVal = this.multiple ? [] : '';
        this.tempSelVal = blankVal;
        this.selectedLabel = '';
      }
    },
    value: function (val) {
      if (val === undefined) {
        this.clear();
      }
      if (this.tempSelVal !== val) {
        this.tempSelVal = val;
      }
      this.$nextTick(function () {
        this.setSelected();
      });
    },
    tempSelVal: function (val, oldVal) {
      this.$emit('input', val);
      this.$emit('change', val, oldVal);
    },
    visible: function (val) {
      if (!val) {
        this.handleIconHide();
      } else {
        this.handleIconShow();
      }
      this.$emit('visible-change', val);
    }
  },
  mounted: function () {
    const _this = this;
    _this.labelWidth = _this.xformAttr ? _this.xformAttr.labelWidth || _this.labelWidth : _this.labelWidth;
  },
  methods: {
    loaded: function (data, total, res, type) {
      // this.setSelected();
      if (!this.elTablexAttr.reserveSelection || type !== 'pageGo') {
        this.selectedLabel = '';
      }
    },
    selectionsChange: function (selections) {
      this.tableSelections = selections;
    },
    rowClickFn: function (row, event, column) {
      if (!this.multiple) {
        this.tableSelections = this.$refs[this.refTable].selections;
        if (this.tableSelections.length > 0) {
          this.visible = false;
        }
        // this.selectAndReturn();
      }
    },
    setSelected: function () {
      if (!this.$refs[this.refTable]) { return }
      var tableData = this.$refs[this.refTable].data;
      var vals = this.tempSelVal;
      var dataId = this.dataId;
      var rows = [];
      if (this.multiple) {
        if (vals.length > 0) {
          for (var j = 0, length2 = vals.length; j < length2; j++) {
            for (var i = 0, length = tableData.length; i < length; i++) {
              if (vals[j] === tableData[i][dataId]) {
                rows.push(tableData[i]);
                break;
              }
            }
          }
        }
      } else if (vals) {
        for (var n = 0, len = tableData.length; n < len; n++) {
          if (vals === tableData[n][dataId]) {
            rows.push(tableData[n]);
            break;
          }
        }
      }
      this.$nextTick(function () {
        if (!this.multiple) {
          this.currentRowKey = vals;
        }
        if (rows.length > 0) {
          for (let i = 0; i < rows.length; i++) {
            const element = rows[i];
            if (this.multiple) { this.$refs[this.refTable].toggleRowSelection(element, true) } else { this.$refs[this.refTable].setCurrentRow(element) }
          }
        }
        this.setSelectedLabel(rows);
      });
    },
    setSelectedLabel: function (selections) {
      this.$nextTick(function () {
        if (!this.selectedLabel) { this.selectedLabel = this.value }
        if (selections.length > 0) {
          var tempArryLabel = [];
          for (var i = 0, length = selections.length; i < length; i++) {
            tempArryLabel.push(selections[i][this.dataLabel]);
          }
          this.selectedLabel = this.multiple
            ? tempArryLabel.join(',')
            : tempArryLabel[0];
        }
        if (this.multiple && this.elTablexAttr.reserveSelection) {
          this.selectedLabel = this.getLabel(this.tableSelections);
        }
      });
    },
    getLabel: function (arr) {
      var label = [];
      if (Array.isArray(arr)) {
        arr.forEach(item => {
          label.push(item[this.dataLabel]);
        });
        return label.join(',');
      }
      return '';
    },
    handleIconClick: function (event) {
      event.stopPropagation();
      if (this.iconClass.indexOf('circle-close') > -1) {
        this.clear();
      } else {
        this.toggleMenu(event);
      }
    },
    toggleMenu: function () {
      if (!this.disabled) {
        this.visible = !this.visible;
      }
    },
    handleIconHide: function () {
      var icon = this.$el.querySelector('.is-reverse');
      if (icon) {
        removeClass(icon, 'is-reverse');
      }
    },
    handleIconShow: function () {
      var icon = this.$el.querySelector('.el-input__icon');
      if (icon && !hasClass(icon, 'el-icon-circle-close')) {
        addClass(icon, 'is-reverse');
      }
    },
    getSelectedObjs: function () {
      var _this = this;
      var resultObj;
      if (_this.multiple) {
        // 多选
        resultObj = _this.tableSelections;
      } else {
        // 单选
        resultObj = _this.tableSelections[0];
      }
      return resultObj;
    },
    clear: function () {
      if (this.multiple) {
        this.$refs[this.refTable].clearSelection();
      } else {
        this.$refs[this.refTable].setCurrentRow();
        this.tempSelVal = '';
        this.selectedLabel = '';
      }
      this.visible = false;
      this.$emit('clear');
    },
    renderForm: function (h) {
      let customSearchFn;
      if (this.xformAttr) {
        customSearchFn = typeof this.xformAttr.customSearchFn === 'function' ? this.xformAttr.customSearchFn : null;
      }
      const xformQuery = [];
      if (this.xformQueryFileds.length > 0) {
        this.xformQueryFileds.forEach((formitem, index) => {
          xformQuery.push(<yu-xform-item {...{ props: formitem, on: formitem.events }} value={formitem.value} name={formitem.name} colspan={formitem.colspan} ctype={formitem.ctype}></yu-xform-item>);
        });
      } else {
        this.needSearchQuery = false;
      }
      if (this.needSearchQuery) {
        return (
          <yu-xform class="xform-q" vModel={this.formdata} ref={this.refForm} related-table-name={this.refTable} label-width={this.labelWidth} form-type="search" custom-search-fn={customSearchFn}>
            <yu-xform-group column={this.column}>
              {xformQuery}
            </yu-xform-group>
          </yu-xform>
        );
      } else {
        return null;
      }
    }
  },
  render: function (h) {
    const _this = this;
    const xform = _this.renderForm(h);
    const inputAttr = {
      props: {
        'on-icon-click': _this.handleIconClick
      },
      on: {
        'mouseenter.native': function () {
          this.inputHovering = true;
        },
        'mouseleave.native': function () {
          this.inputHovering = false;
        }
      }
    };
    return (
      <div class="el-combo-table el-select">
        <el-popover
          ref="selectPopover"
          placement="bottom-start"
          trigger="click"
          width={this.width}
          vModel={this.visible}
          visible-arrow={false}>
          <div style={this.divDisPlay}>
            { xform }
            <el-table-x ref={this.refTable} {...{props: this.elTablexAttr, on: this.elTablexAttr.events}}
              on-row-dblclick={this.rowClickFn}
              on-row-click={this.rowClickFn}
              on-loaded={this.loaded}
              on-selection-change={this.selectionsChange}
            ></el-table-x>
          </div>
        </el-popover>
        { this.disabled ? <el-input
          placeholder={this.placeholder}
          vModel={this.selectedLabel}
          readonly={true}
          details={this.details}
          icon={this.iconClass}
          disabled={this.disabled || this.details}
          {...{props: inputAttr.props, on: inputAttr.on}}>
        </el-input> : <el-input
          placeholder={this.placeholder}
          vModel={this.selectedLabel}
          vPopover:selectPopover
          readonly={true}
          details={this.details}
          icon={this.iconClass}
          disabled={this.disabled || this.details}
          {...{props: inputAttr.props, on: inputAttr.on}}>
        </el-input> }
      </div>
    );
  }
};
</script>



<template>
  <div class="content">
    <div v-show="componentData === '1'">
      <yu-button-group class="btn-group">
        <yu-button @click="hiddenNumFn">隐藏"联系人电话"输入框</yu-button>
        <yu-button @click="btnValidateFn">校验数据</yu-button>
      </yu-button-group>
      <yu-xform ref="refFormDemo" label-width="120px" :hidden-rule="true" v-model="formdata" form-type="edit" hidden-del-val>
        <yu-xform-group>
          <yu-xform-item label="信息标题" notice="这是一条提示信息" ctype="input" value="标题" name="title" placeholder="5到25个字符" @focus="change"></yu-xform-item>
          <yu-xform-item label="类型" ctype="select" multiple :options="options" name="type" @change="change"></yu-xform-item>
          <yu-xform-item label="状态" ctype="select" :options="options1" multiple name="status"></yu-xform-item>
          <yu-xform-item label="作者" ctype="input" name="author"></yu-xform-item>
          <yu-xform-item label="联系人电话" :hidden="telHidden" required ctype="input" name="tel" placeholder="编辑人联系电话"></yu-xform-item>
          <yu-xform-item label="发布时间" ctype="timepicker" name="time" placeholder="设置自动发布时间"></yu-xform-item>
          <yu-xform-item label="数字" ctype="num" name="txtnum" placeholder="数字负数验证"></yu-xform-item>
          <yu-xform-item label="单选组过滤" ctype="radio" name="redradio" :datacode-filter="datacodeFilterFn" data-code="OBJECT_TYPE"></yu-xform-item>
          <yu-xform-item label="复选组过滤" ctype="checkbox" name="redcheckbox" :datacode-filter="datacodeFilterFn" data-code="OBJECT_TYPE"></yu-xform-item>
          <yu-xform-item label="下拉框过滤" ctype="select" name="redselect" :datacode-filter="datacodeFilterFn" data-code="OBJECT_TYPE"></yu-xform-item>
          <yu-xform-item label="所属标签" ctype="checkbox" :options="options" name="label"></yu-xform-item>
          <yu-xform-item label="是否置顶" ctype="radio" :options="options2" name="top"></yu-xform-item>
          <yu-xform-item label="内容" colspan="24" name="content" ctype="textarea" placeholder="2000个字符以内"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
    </div>
    <yu-xform v-show="componentData === '2'" ref="refFormDemo" label-width="120px" v-model="detailForm" form-type="details">
      <yu-xform-group>
        <yu-xform-item label="信息标题" notice="这是一条提示信息" notice-title="noticeTitle" ctype="input" name="title" placeholder="5到25个字符" value="信息"></yu-xform-item>
        <yu-xform-item label="类型" ctype="select" data-code="NATIONALITY" name="type" value="US"></yu-xform-item>
        <yu-xform-item label="状态" ctype="select" :options="options1" name="status" value="01"></yu-xform-item>
        <yu-xform-item label="作者" ctype="input" name="author" value="zhangkun"></yu-xform-item>
        <yu-xform-item label="联系人电话" :hidden="telHidden" required ctype="input" name="tel" placeholder="编辑人联系电话" value="信息"></yu-xform-item>
        <yu-xform-item label="发布时间" ctype="timepicker" name="time" placeholder="设置自动发布时间" value="2019-04-22"></yu-xform-item>
        <yu-xform-item label="所属标签" ctype="checkbox" data-code="NATIONALITY" name="label" :value="['US']"></yu-xform-item>
        <yu-xform-item label="是否置顶" ctype="radio" :options="options2" name="top" value="02"></yu-xform-item>
        <yu-xform-item label="内容" colspan="24" name="content" show-overflow-tooltip ctype="textarea" placeholder="2000个字符以内" value="信息信息信息信息信息信息信息信息信息信息信息信息信息信息"></yu-xform-item>
      </yu-xform-group>
    </yu-xform>
  </div>
</template>

<script>
export default {
  props: {
    componentData: {
      type: String,
      default: "1"
    }
  },
  data() {
    return {
      formdata: {},
      detailForm: {},
      disabled: false,
      telHidden: false,
      options: [
        {key: 'US', value: '美国'},
        {key: 'CN', value: '中国'},
        {key: 'JP', value: '日本'},
        {key: 'EU', value: '欧元区'}
      ],
      options1: [
        {key: '01', value: '草稿'},
        {key: '02', value: '已发布'},
        {key: '03', value: '已删除'}
      ],
      options2: [
        {key: '01', value: '是'},
        {key: '02', value: '否'}
      ]
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    change() {
      this.disabled = true;
      this.formdata.author = '张坤';
    },
    hiddenNumFn() {
      this.telHidden = !this.telHidden;
    },
    datacodeFilterFn(opts, datacode, name) {
      // console.log('过滤结果为', arguments);
      return opts.filter(function(op) {
        if (op.key === 'R' || op.key === 'G') return true;
        return false;
      });
    },
    btnValidateFn() {
      var validate = false, _this = this;
      _this.$refs.refFormDemo.validate(function (valid) {
        validate = valid;
        valid ? _this.$message({message: '验证通过', type:'success'}) : _this.$message({message: '验证失败', type:'error'})
      });
      if (!validate) {
        return;
      }
    },
    getData() {}
  }
}
</script>
<style>

</style>

<template>
  <div class="content">
    <yu-xtable
      :data="tableData4"
      stripe
      :pageable="false"
      style="width: 100%"
      v-show="componentData === '1' || componentData === '3'">
      <yu-xtable-column
        prop="date"
        label="日期"
        width="180">
      </yu-xtable-column>
      <yu-xtable-column
        prop="name"
        label="姓名"
        width="180">
      </yu-xtable-column>
      <yu-xtable-column
        prop="address"
        label="地址">
      </yu-xtable-column>
    </yu-xtable>
    <yu-xtable
      :data="tableData11"
      style="width: 100%"
      height="250"
      v-show="componentData === '2'">
      <yu-xtable-column
        fixed
        prop="date"
        label="日期"
        width="150">
      </yu-xtable-column>
      <yu-xtable-column
        prop="name"
        label="姓名"
        width="120">
      </yu-xtable-column>
      <yu-xtable-column
        prop="province"
        label="省份"
        width="120">
      </yu-xtable-column>
      <yu-xtable-column
        prop="city"
        label="市区"
        width="120">
      </yu-xtable-column>
      <yu-xtable-column
        prop="address"
        label="地址"
        width="300">
      </yu-xtable-column>
      <yu-xtable-column
        prop="zip"
        label="邮编"
      >
      </yu-xtable-column>
    </yu-xtable>
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
      tableData4: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }],
      tableData11: [{
        date: '2016-05-03',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-02',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-04',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-01',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-08',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-06',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-07',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }]
    }
  },
  /* watch: {
    componentData(nv, ov) {
      console.log(nv, 'nv');
    }
  }, */
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      // console.log(this.componentData, 'componentData');
    }
  }
}
</script>
<style>
.content {
  background-color: #fff;
}
</style>

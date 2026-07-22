<template>
  <div class="role-box frame-height">
    <yu-tabs v-model="activeName" @tab-click="tabClick">
      <yu-tab-pane :label="tab.name" :name="tab.type" :key="index" v-for="(tab, index) in contentTabs">
        <yu-input
          v-model="listSearchVal"
          :placeholder=" isRoleTab? $t('authDataPowerManager.srgjzssjs') : $t('authDataPowerManager.srgjzssyh')"
          suffix-icon="yu-icon-search1"
          class="search-input"
          @suffix-click="getRoleList"
          @keyup.enter.native="getRoleList"
          @clear="clearRoleList"
          clearable
        ></yu-input>
        <div class="role-list">
          <div class="role-list-item" v-for="roleItem in roleList" :key="isRoleTab ? roleItem.roleId : roleItem.userId" @click="chooseRoleFn(roleItem)" :class="{ 'is-selected': roleItem.isChoose }">
            <span class="list-icon"><i :class="isRoleTab ? 'yu-icon-contacts2' : 'yu-icon-group1'"></i></span>
            <div v-if="isRoleTab" class="list-info">
              <p>{{ roleItem.roleName }}（{{ roleItem.roleCode }}）</p>
              <p class="org">
                {{ $t("authDataPowerManager.ssjg") }}：{{ roleItem.orgName }}
              </p>
            </div>
            <div v-else class="list-info">
              <p>{{ roleItem.userName }}（{{ roleItem.loginCode }}）</p>
              <p class="org">
                {{ $t("authDataPowerManager.ssjg") }}：{{ roleItem.orgName }}
              </p>
            </div>
          </div>
          <yu-pagination v-if="roleList.length && pageTotal > pageSize" :current-page.sync="currentPage" :page-size="pageSize" layout="total, prev, pager, next" :total="pageTotal" @current-change="getRoleList">
          </yu-pagination>
        </div>
      </yu-tab-pane>
    </yu-tabs>
    <div class="no-choose" v-show="!pageTotal">
      <img src="@/assets/common/images/no-data.svg" style="width: 42%" />
      <p>{{ $t("authDataPowerManager.zwsj") }}</p>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { extend } from "@/utils";
export default {
  name: "YuAuthList",
  componentName: "YuAuthList",
  data() {
    return {
      roleUrl: backend.appOcaService + '/api/adminsmrole/auth/page', // 角色列表请求url地址
      userUrl: backend.appOcaService + '/api/adminsmuser/auth/page', // 用户列表请求url地址
      contentTabs: [
        { name: this.$t('roleDataPowerManager.jssq'), type: 'R', isChoose: true },
        { name: this.$t('roleDataPowerManager.yhsq'), type: 'U', isChoose: false }
      ], // tab数组对象
      activeName: "R", // 当前被激活的页签
      isRoleTab: true, // 当前页签是否是角色授权
      listSearchVal: "", // 角色或用户授权列表搜索框
      roleList: [], // 角色或用户授权列表
      pageTotal: 1, // 列表总条目数
      pageSize: 10, // 每页显示条目个数
      currentPage: 1, // 当前页数，支持 .sync 修饰符
      chooseRoleId: '', // 已选择的角色/用户id
      // 数据授权列表阐述
      tableParams:{
        userRoleId: '',
        authObjId: ''
      }
    }
  },
  computed: {
    ...mapGetters(["roles"]),
  },
  created() {
    this.tableParams.userRoleId = this.roles.length && this.roles[0].id;
    this.$emit('chooseUserRoleId', this.tableParams.userRoleId);
    this.getRoleList();
  },
  methods: {
    /**
    * 点击页签
    * @param t-被点击页签的参数，index--当前的索引
    */
    tabClick(t, e) {
      this.isRoleTab = t.name === 'R';
      this.roleList = [];
      this.listSearchVal = '';
      this.currentPage = 1;
      this.getRoleList();
    },

    /**
    * 获取角色/用户授权列表
    * activeName为'R'时获取角色列表，u为用户列表
    */
    getRoleList() {
      var _this = this;
      _this.$request({
        url: _this.isRoleTab ? _this.roleUrl : this.userUrl,
        method: 'post',
        data: {
          keyWord: _this.listSearchVal,
          page: _this.currentPage,
          size: _this.pageSize
        }
      }).then(({code, message, data, total}) => {
        if (code === '0000') {
          for (var i = 0; i < data.length; i++) {
            if (_this.isRoleTab && _this.chooseRoleId == data[i].roleId) {
              data[i].isChoose = true;
            } else if (!_this.isRoleTab && _this.chooseRoleId == data[i].userId) {
              data[i].isChoose = true;
            } else {
              data[i].isChoose = false;
            }
          }
          _this.pageTotal = total;
          _this.roleList = extend([], data);
          console.log(_this.roleList)
        } else {
          _this.$message({ message: message, type: 'error' });
          
        }
      });
    },

    /**
    * 清空角色/用户授权列表
    */
    clearRoleList() {
      this.roleList = [];
      this.listSearchVal = '';
      this.currentPage = 1;
      this.getRoleList();
    },
    
    /**
    * 用户/角色选择事件
    * @param item 当前选中事件
    */
    chooseRoleFn(item) {
      this.chooseRoleId = this.isRoleTab ? item.roleId : item.userId;
      for (var i = 0; i < this.roleList.length; i++) {
        if (this.isRoleTab && this.roleList[i].roleId === this.chooseRoleId) {
          this.roleList[i].isChoose = true;
          this.expectedRoleId = this.chooseRoleId;
          this.expectedUserId = "";
        } else if (
          !this.isRoleTab &&
          this.roleList[i].userId === this.chooseRoleId
        ) {
          this.roleList[i].isChoose = true;
          this.expectedUserId = this.chooseRoleId;
          this.expectedRoleId = "";
        } else {
          this.roleList[i].isChoose = false;
        }
      }
      this.$nextTick(function () {
        this.tableParams.authObjId = this.chooseRoleId;
        this.$emit('chooseAuthObjId', this.tableParams.authObjId, this.isRoleTab, this.activeName);
      });
    },
  }
}
</script>
<style>
.role-box {
  background: #ffffff !important;
  float: left;
  width: 400px;
  margin-right: 24px;
  position: fixed;
}

/* 紧凑模式start */
.compact .role-box {
  margin-right: 16px;
}

/* 紧凑模式end */
.role-box .el-tabs {
  height: 100%;
}

.role-box .el-tabs__item {
  height: 42px;
  line-height: 42px;
}

.compact .role-box .el-tabs__item {
  height: 36px;
  line-height: 36px;
}

.role-box .el-tabs__content {
  padding: 0 24px 15px;
  height: calc(100% - 78px);
  overflow: auto;
}

.role-box .el-input__suffix-inner {
  line-height: 36px;
}

.compact .el-input__suffix-inner {
  line-height: 28px;
}

.role-list {
  margin-top: 20px;
}

.role-list .role-list-item {
  margin-bottom: 10px;
  position: relative;
  padding: 10px 16px;
}
.compact .role-list .role-list-item{
  margin-bottom: 4px;
  padding: 8px 10px;
}

.role-list .role-list-item.is-selected {
  background: rgba(0, 0, 0, 0.05);
}
.blue .is-selected .list-info > p:first-child {
  color: #2877ff;
}

.orange .is-selected .list-info > p:first-child {
  color: #fb8d12;
}

.purple .is-selected .list-info > p:first-child {
  color: #5557b9;
}

.blue .role-list .role-list-item:active {
  background-color: rgba(40, 119, 255, 0.1);
}
.orange .role-list .role-list-item:active {
  background-color: rgba(41, 3, 3, 0.05);
}

.purple .role-list .role-list-item:active {
  background-color: rgba(85, 87, 185, 0.1);
}

.role-list-item:before {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  display: block;
  -webkit-transition: 0.15s;
  transition: 0.15s;
  width: 0;
  height: 0;
  border-top: 31px solid #ffffff;
  border-left: 31px solid transparent;
}

.role-list-item:after {
  content: "";
  position: absolute;
  bottom: 0;
  right: 0;
  display: block;
  -webkit-transition: 0.15s;
  transition: 0.15s;
  width: 0;
  height: 0;
  border-bottom: 31px solid #ffffff;
  border-left: 31px solid transparent;
}

.role-list .role-list-item:hover {
  cursor: pointer;
  background: rgba(0, 0, 0, 0.05);
}

.blue .role-list .role-list-item:hover .list-info > p:first-child {
  color: #2877ff;
}

.orange .role-list .role-list-item:hover .list-info > p:first-child {
  color: #fb8d12;
}

.purple .role-list .role-list-item:hover .list-info > p:first-child {
  color: #5557b9;
}

.role-list .list-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(153deg, #3187ff 0%, #75afff 100%);
  border-radius: 50%;
  line-height: 36px;
  text-align: center;
  display: inline-block;
  margin-right: 8px;
  vertical-align: middle;
  color: #ffffff;
}
.role-list .list-icon > i {
  font-size: 20px;
}

.role-list .list-info {
  display: inline-block;
  width: calc(100% - 68px);
  color: #333333;
  vertical-align: middle;
}

.role-list .list-info > p:first-child {
  margin-bottom: 5px;
  font-weight: 600;
  font-size: 15px;
}

.role-list .list-info > .org {
  color: #999999;
  font-size: 12px;
}

.list-no-data {
  text-align: center;
}

.list-no-data > img {
  float: none;
  width: 50%;
  margin-bottom: 16px;
}

.authorize-manage .no-choose {
  text-align: center;
  font-size: 14px;
  width: 100%;
  position: absolute;
  top: 36%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}

.authorize-manage .no-choose > img {
  float: none;
  width: 25%;
  margin-bottom: 16px;
}
.authorize-manage .role-box .el-pagination {
  padding-top: 2px;
}
.authorize-manage .role-box .el-pagination .btn-prev,
.authorize-manage .role-box .el-pagination .btn-next,
.authorize-manage .role-box .el-pager li,
.authorize-manage .role-box .el-pager li:last-child {
  border: none;
}

.authorize-manage .role-box .el-pager li.active {
  background: transparent !important;
  border: none !important;
}
.blue .authorize-manage .role-box .el-pager li.active,
.blue .authorize-manage .role-box .el-pager li.active:hover {
  color: #2877ff;
}
.orange .authorize-manage .role-box .el-pager li.active,
.orange .authorize-manage .role-box .el-pager li.active:hover {
  color: #fb8d12;
}
.purple .authorize-manage .role-box .el-pager li.active,
.purple .authorize-manage .role-box .el-pager li.active:hover {
  color: #5557b9;
}
</style>

<template>
  <div>
    <yu-xdialog :title="$t('component.personalData')" :visible.sync="dialogVisibleProps" @close="close">
      <yu-xform ref="refDetailForm" label-width="120px" v-model="detailForm" form-type="details">
        <yu-row>
          <yu-col :span="12">
            <yu-xform-item :label="$t('sysUserManager.yhmc')" name="userName"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.xb')" name="userSex" ctype="select" :options="sexOptions"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.gh')" name="userCode"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.yddh')" name="userMobilephone"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.yx')" name="userEmail"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.sr')" name="userBirthday" ctype="datepicker" value-format="yyyy-MM-dd"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.ssbm')" name="dptName"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.ssjg')" name="orgName"></yu-xform-item>
          </yu-col>
          <yu-col :span="12">
            <div class="yu-user-pic search-form yu-user-pic-cust">
              <div class="yu-user-pic-box">
                <img v-if="avatar" :src="avatar" />
                <template v-else>
                  <div class="yu-icon-user"></div>
                  <label>头像照片</label>
                </template>
              </div>
            </div>
          </yu-col>
        </yu-row>
      </yu-xform>
    </yu-xdialog>
  </div>
</template>
<script>
import { clone, lookup } from "@/utils";
import { mapGetters } from "vuex";
export default {
  name: "YufpPersonalData",
  componentName: "YufpPersonalData",
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      detailForm: {},
      dialogVisibleProps: false,
      avatar: "",
      personInfoUrl: backend.appOcaService + "/api/adminsmuser/info",
      sexOptions: lookup.lookupMgr.SEX_TYPE
    };
  },
  computed: {
    ...mapGetters(["userId", 'userAvatar'])
  },
  watch: {
    dialogVisible(newVal, oldVal) {
      this.dialogVisibleProps = newVal;
    }
  },
  mounted() {
    this.getUserInfo();
    if (this.userAvatar) {
      this.avatar = yufp.util.addTokenInfo(backend.fileService + '/api/file/provider/download?fileId=' + this.userAvatar)
    }
  },
  methods: {
    /**
     * 关闭弹出框
     */
    close: function() {
      this.$emit("update:dialogVisible", false); // 关闭个人资料弹出框
    },
    getUserInfo() {
      var _this = this;
      _this
        .$request({
          url: _this.personInfoUrl,
          method: "post"
        })
        .then(({ code, message, data }) => {
          if (code === "0") {
            clone(data, _this.detailForm);
          }
        });
    }
  }
};
</script>

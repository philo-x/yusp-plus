<script>
/**
 * @description 行业树选择 YuIndustryClassfy
 * @param data-url 表格请求URL, 默认值：'' , 如: /yusp_lp/api/util/getOrg?needFin=true&needManage=true&needDpt=true
 * @param showQuery 是否显示查询条件框,默认值：true
 * @param icon 输入框图标,默认值：search
 * @param placeholder 输入框提示文本,默认值：''
 * @param disabled 输入框是否禁止输入,默认值：false
 * @param size 输入框大小,默认值：
 * @param rawValue 输入框值,默认值：
 * @param value 输入框值,默认值：
 * @param name 输入框名称,默认值：
 * @authors kongqf
 * @date    2020-09-23 19:20:41
 * @version $1.0$
 */
import industryData from "@/components/widgets/YuIndustryClassfy/industry.json";
/**
 * 业务组件-行政区域
 */
export default {
  name: "YuIndustryClassfy",
  xtype: "YuIndustryClassfy",

  props: {
    inputConfig: {
      type: Object,
      default: () => {
        return { events: {} };
      }
    },
    xtreeConfig: {
      type: Object,
      default: () => {
        return { events: {} };
      }
    },
    showQuery: {
      type: Boolean,
      default: true
    }
  },

  data() {
    return {
      filterText: "",
      industryData: industryData
    };
  },
  watch: {
    filterText: function(val) {
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    /**
     * 树节点过滤方法
     */
    filterNode: function(value, data) {
      if (!value) {
        return true;
      }
      return data.label.indexOf(value) !== -1;
    }
  },
  /**
   * render方法
   */
  render: function(h) {
    var _this = this;
    return (
      <div>
        {_this.showQuery && (
          <yu-input
            placeholder="输入关键字进行过滤"
            vModel={this.filterText}
            {...{ props: this.inputConfig, on: this.inputConfig.events }}
          />
        )}
        {this.xtreeConfig.dataUrl ? (
          <yu-xtree
            class="filter-tree"
            filter-node-method={this.xtreeConfig.filterNode || this.filterNode}
            ref="tree"
            {...{ props: this.xtreeConfig, on: this.xtreeConfig.events }}
          />
        ) : (
          <yu-xtree
            class="filter-tree"
            local-data={this.industryData}
            filter-node-method={this.xtreeConfig.filterNode || this.filterNode}
            ref="tree"
            {...{ props: this.xtreeConfig, on: this.xtreeConfig.events }}
          />
        )}
      </div>
    );
  }
};
</script>

<style scoped>
.filter-tree {
  margin-top: 20px;
}
</style>

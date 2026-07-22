<template>
  <div class="yu-dashboard-container">
    <yu-row :gutter="24">
      <yu-col :span="16">
        <div class="yu-dashboard-box">
          <!--<button @click="jump">点击跳转</button>-->
          <div class="yu-dashboard-user">
            <div class="yu-dashboard-user-pic">
              <img :src="defaultAvatar" title="张林航，副总经理" />
            </div>
            <div class="yu-dashboard-user-info" title="张林航，副总经理">
              <b>{{ $t('dashboard.why') }}</b>
              <span>{{ $t('dashboard.positon') }}</span>
            </div>
          </div>
          <div class="yu-dashboard-user-tips">
            <a href="javascript:void(0);">
              <i class="yu-icon-finish">
                <yu-badge :value="26" :max="50"></yu-badge>
              </i>
              <span>{{ $t('dashboard.wddb') }}</span>
            </a>
            <a href="javascript:void(0);">
              <i class="yu-icon-calendar1">
                <yu-badge :value="12" :max="50"></yu-badge>
              </i>
              <span>{{ $t('dashboard.dtrc') }}</span>
            </a>
            <a href="javascript:void(0);">
              <i class="yu-icon-message3">
                <yu-badge :value="18" :max="50"></yu-badge>
              </i>
              <span>{{ $t('dashboard.wdxx') }}</span>
            </a>
          </div>
        </div>
      </yu-col>
      <yu-col :span="8">
        <div class="yu-dashboard-box">
          <div class="yu-dashboard-title">
            <h1>{{ $t('dashboard.hyzx') }}</h1>
            <a href="javascript:void(0);">{{ $t('dashboard.gd') }}</a>
          </div>
          <div class="yu-dashboard-content">
            <ul class="yu-dashboard-simple-list">
              <li>
                <a href="javascript:void(0);" title="中国拟立密码法 明确任何组织或个人不得窃取他人加密信息">
                  <i class="information">{{ $t('dashboard.zx') }}</i>
                  <span>{{ $t('dashboard.zxnr') }}</span>
                </a>
              </li>
              <li>
                <a href="javascript:void(0);" title="31省份最新城镇化率：4省份已超70％ ，这两大地区潜力最大">
                  <i class="notice">{{ $t('dashboard.tz') }}</i>
                  <span>{{ $t('dashboard.tznr') }}</span>
                </a>
              </li>
              <li>
                <a href="javascript:void(0);" title="共享电单车：要低调，不要成为下一个ofo">
                  <i class="announcement">{{ $t('dashboard.gg') }}</i>
                  <span>{{ $t('dashboard.ggnr') }}</span>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </yu-col>
      <yu-col :span="8">
        <div class="yu-dashboard-box">
          <div class="yu-dashboard-title">
            <h1>{{ $t('dashboard.ckgm') }}</h1>
            <!-- <a href="javascript:void(0);">更多</a> -->
          </div>
          <div class="yu-dashboard-content">
            <div class="yu-dashboard-scale-left">
              <h2 title="18,873亿元">18,873
                <span>{{ $t('dashboard.yy') }}</span>
              </h2>
              <div>
                <span class="up">{{ $t('dashboard.tb') }}
                  <b>+1.29</b>
                  <i class="yu-icon-up"></i>
                </span>
                <span class="down">{{ $t('dashboard.hb') }}
                  <b>-1.03</b>
                  <i class="yu-icon-down"></i>
                </span>
              </div>
            </div>
            <div class="yu-dashboard-scale-right">
              <yu-echarts :option="echartOption1"></yu-echarts>
            </div>
          </div>
        </div>
      </yu-col>
      <yu-col :span="8">
        <div class="yu-dashboard-box">
          <div class="yu-dashboard-title">
            <h1>{{ $t('dashboard.dkgm') }}</h1>
            <!-- <a href="javascript:void(0);">更多</a> -->
          </div>
          <div class="yu-dashboard-content">
            <div class="yu-dashboard-scale-left">
              <h2 title="16,051亿元">16,051
                <span>{{ $t('dashboard.yy') }}</span>
              </h2>
              <div>
                <span class="up">{{ $t('dashboard.tb') }}
                  <b>+1.29</b>
                  <i class="yu-icon-up"></i>
                </span>
                <span class="down">{{ $t('dashboard.hb') }}
                  <b>-1.03</b>
                  <i class="yu-icon-down"></i>
                </span>
              </div>
            </div>
            <div class="yu-dashboard-scale-right">
              <yu-echarts :option="echartOption2"></yu-echarts>
            </div>
          </div>
        </div>
      </yu-col>
      <yu-col :span="8">
        <div class="yu-dashboard-box">
          <div class="yu-dashboard-title">
            <h1>{{ $t('dashboard.zsgm') }}</h1>
            <!-- <a href="javascript:void(0);">更多</a> -->
          </div>
          <div class="yu-dashboard-content">
            <div class="yu-dashboard-scale-left">
              <h2 title="1,729亿元">1,729
                <span>{{ $t('dashboard.yy') }}</span>
              </h2>
              <div>
                <span class="up">{{ $t('dashboard.tb') }}
                  <b>+1.29</b>
                  <i class="yu-icon-up"></i>
                </span>
                <span class="down">{{ $t('dashboard.hb') }}
                  <b>-1.03</b>
                  <i class="yu-icon-down"></i>
                </span>
              </div>
            </div>
            <div class="yu-dashboard-scale-right">
              <yu-echarts :option="echartOption3"></yu-echarts>
            </div>
          </div>
        </div>
      </yu-col>
      <yu-col :span="12">
        <div class="yu-dashboard-box">
          <div class="yu-dashboard-title">
            <h1>{{ $t('dashboard.ckqs') }}</h1>
            <div class="yu-dashboard-tools">
              <span class="selected">{{ $t('dashboard.jyn') }}</span>
              <span>{{ $t('dashboard.jlgy') }}</span>
              <span>{{ $t('dashboard.jsgy') }}</span>
            </div>
          </div>
          <div class="yu-dashboard-content">
            <div class="yu-dashboard-echart-box">
              <yu-echarts ref="echart4" :option="echartOption4"></yu-echarts>
            </div>
          </div>
        </div>
      </yu-col>
      <yu-col :span="12">
        <div class="yu-dashboard-box">
          <div class="yu-dashboard-title">
            <h1>{{ $t('dashboard.dkqs') }}</h1>
            <div class="yu-dashboard-tools">
              <span class="selected">{{ $t('dashboard.jyn') }}</span>
              <span>{{ $t('dashboard.jlgy') }}</span>
              <span>{{ $t('dashboard.jsgy') }}</span>
            </div>
          </div>
          <div class="yu-dashboard-content">
            <div class="yu-dashboard-echart-box">
              <yu-echarts ref="echart5" :option="echartOption5"></yu-echarts>
            </div>
          </div>
        </div>
      </yu-col>

      <yu-col :span="24">
        <div class="yu-dashboard-box">
          <div class="yu-dashboard-title">
            <h1>{{ $t('dashboard.zsqs') }}</h1>
            <div class="yu-dashboard-tools">
              <span class="selected">{{ $t('dashboard.jyn') }}</span>
              <span>{{ $t('dashboard.jlgy') }}</span>
              <span>{{ $t('dashboard.jsgy') }}</span>
            </div>
          </div>
          <div class="yu-dashboard-content">
            <div class="yu-dashboard-echart-box" style="width:calc(40% - 48px);float: left;">
              <yu-echarts :option="echartOption6"></yu-echarts>
            </div>
            <div class="yu-dashboard-echart-box" style="width:calc(60% - 48px);float: left;">
              <yu-echarts ref="echart7" :option="echartOption7"></yu-echarts>
            </div>
          </div>
        </div>
      </yu-col>

      <yu-col :span="24">
        <div class="yu-dashboard-box" style="margin-bottom: 0;">
          <div class="yu-dashboard-title">
            <h1>
              <span :class="{'selected':isSelected}" @click="tableToggle">{{ $t('dashboard.xsbb') }}</span>
              <span :class="{'selected':isSelected1}" @click="tableToggle">{{ $t('dashboard.rscp') }}（TOP6）</span>
            </h1>
            <a href="javascript:void(0);">{{ $t('dashboard.gd') }}</a>
          </div>
          <div class="yu-dashboard-content" v-show="isSelected">
            <yu-xtable request-type="POST" :data="tableData" :pageable="false" class="yu-dashboard-table" :highlight-current-row="false">
              <yu-xtable-column prop="date" :label="$t('dashboard.yf')"></yu-xtable-column>
              <yu-xtable-column prop="sales" :label="$t('dashboard.xsje')" align="right"></yu-xtable-column>
              <yu-xtable-column prop="onYear" :label="$t('dashboard.tb')" align="right">
                <template slot-scope="scope">
                  <span :class="[scope.row.onYear.indexOf('-')>-1 ? 'yu-dashboard-down' : 'yu-dashboard-up']">{{ scope.row.onYear }}
                    <i :class="[scope.row.onYear.indexOf('-')>-1 ? 'yu-icon-down' : 'yu-icon-up']"></i>
                  </span>
                </template>
              </yu-xtable-column>
              <yu-xtable-column prop="onMonth" :label="$t('dashboard.hb')" align="right">
                <template slot-scope="scope">
                  <span :class="[scope.row.onMonth.indexOf('-')>-1 ? 'yu-dashboard-down' : 'yu-dashboard-up']">{{ scope.row.onMonth }}
                    <i :class="[scope.row.onMonth.indexOf('-')>-1 ? 'yu-icon-down' : 'yu-icon-up']"></i>
                  </span>
                </template>
              </yu-xtable-column>
              <yu-xtable-column prop="update" :label="$t('dashboard.gxrq')"></yu-xtable-column>
              <yu-xtable-column :label="$t('dashboard.cz')">
                <template>
                  <yu-button type="text">{{ $t('dashboard.ckxq') }}</yu-button>
                  <yu-button type="text">{{ $t('dashboard.xzbb') }}</yu-button>
                </template>
              </yu-xtable-column>
            </yu-xtable>
          </div>
          <div v-show="isSelected1" class="yu-dashboard-content">
            <yu-xtable request-type="POST" :data="tableData1" :pageable="false" class="yu-dashboard-table" :highlight-current-row="false">
              <yu-xtable-column prop="title" :label="$t('dashboard.cpmc')" width="450"></yu-xtable-column>
              <yu-xtable-column prop="sum" :label="$t('dashboard.xssl')" align="right"></yu-xtable-column>
              <yu-xtable-column prop="sales" :label="$t('dashboard.xsje')" align="right"></yu-xtable-column>
              <yu-xtable-column prop="finished" :label="$t('dashboard.wcl')" align="right"></yu-xtable-column>
              <yu-xtable-column :label="$t('dashboard.cz')">
                <template>
                  <yu-button type="text">{{ $t('dashboard.ckxq') }}</yu-button>
                </template>
              </yu-xtable-column>
            </yu-xtable>
          </div>
        </div>
      </yu-col>
    </yu-row>
  </div>
</template>

<script>
import defaultAvatar from '@/assets/common/images/photo.svg'
import { mapGetters } from "vuex"
export default {
  name: 'DashboardEditor',
  data: function() {
    return {
      defaultAvatar: defaultAvatar,
      echartOption1: {
        tooltip: {
          trigger: 'none',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          show: false,
          type: 'category',
          data: ['2019-01', '2019-02', '2019-03', '2019-04', '2019-05', '2019-06']
        },
        yAxis: {
          show: false,
          type: 'value',
          min: 'dataMin'
        },
        grid: {
          top: 10,
          right: 0,
          bottom: 0,
          left: '-30',
          containLabel: true
        },
        series: [{
          data: [54, 50, 52, 49, 48, 52],
          type: 'line',
          showSymbol: false,
          smooth: true,
          itemStyle: {
            normal: {
              lineStyle: {
                width: 3,
                color: {
                  type: 'linear',
                  x: 0.5,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: '#5073FF'
                  }, {
                    offset: 1, color: '#B77CFF'
                  }]
                }
              }
            }
          }
        }]
      },
      echartOption2: {
        tooltip: {
          trigger: 'none',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          show: false,
          type: 'category',
          data: ['2019-01', '2019-02', '2019-03', '2019-04', '2019-05', '2019-06']
        },
        yAxis: {
          show: false,
          type: 'value',
          min: 'dataMin'
        },
        grid: {
          top: 10,
          right: 0,
          bottom: 0,
          left: '-30',
          containLabel: true
        },
        series: [{
          data: [47, 49, 45, 52, 48, 47],
          type: 'line',
          showSymbol: false,
          smooth: true,
          itemStyle: {
            normal: {
              lineStyle: {
                width: 3,
                color: {
                  type: 'linear',
                  x: 0.5,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: '#3AB0EB'
                  }, {
                    offset: 1, color: '#20C098'
                  }]
                }
              }
            }
          }
        }]
      },
      echartOption3: {
        tooltip: {
          trigger: 'none',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          show: false,
          type: 'category',
          data: ['2019-01', '2019-02', '2019-03', '2019-04', '2019-05', '2019-06']
        },
        yAxis: {
          show: false,
          type: 'value',
          min: 'dataMin'
        },
        grid: {
          top: 10,
          right: 0,
          bottom: 0,
          left: '-30',
          containLabel: true
        },
        series: [{
          data: [52, 50, 49, 52, 48, 54],
          type: 'line',
          showSymbol: false,
          smooth: true,
          itemStyle: {
            normal: {
              lineStyle: {
                width: 3,
                color: {
                  type: 'linear',
                  x: 0.5,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: '#DA2EFF'
                  }, {
                    offset: 1, color: '#FB8D12'
                  }]
                }
              }
            }
          }
        }]
      },
      echartOption4: {
        color: ['#687AEE'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: 8,
          left: 0,
          right: 0,
          bottom: 0,
          containLabel: true
        },
        textStyle: {
          color: '#949494'
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          },
          splitLine: {
            lineStyle: {
              color: '#f0f0f0'
            }
          }
        },
        series: [
          {
            type: 'bar',
            barWidth: '20%',
            data: [14410, 15452, 18090, 23843, 26390, 20330, 21020, 17410, 18452, 20200, 25043, 16090]
          }
        ]
      },
      echartOption5: {
        color: ['#687AEE'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: 8,
          left: 0,
          right: 0,
          bottom: 0,
          containLabel: true
        },
        textStyle: {
          color: '#949494'
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          },
          splitLine: {
            lineStyle: {
              color: '#f0f0f0'
            }
          }
        },
        series: [
          {
            type: 'bar',
            barWidth: '20%',
            data: [10410, 12452, 13090, 18843, 20390, 15330, 21020, 14410, 19452, 14200, 17043, 11090]
          }
        ]
      },
      echartOption6: {
        color: ['#687AEE', '#FB8D12', '#D66A6F', '#95D15F', '#3AB0EB', '#20C098'],
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          x: 'left',
          y: 'center',
          itemGap: 20,
          data: ['结算', '代理', '担保', '信托', '租赁', '融资']
        },
        series: [
          {
            name: '中收',
            type: 'pie',
            center: ['60%', '50%'],
            radius: ['70%', '85%'],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: 'center'
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: '24'
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [
              { value: 335, name: '结算' },
              { value: 310, name: '代理' },
              { value: 234, name: '担保' },
              { value: 135, name: '信托' },
              { value: 548, name: '租赁' },
              { value: 235, name: '融资' }
            ]
          }
        ]
      },
      echartOption7: {
        color: ['#687AEE'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: 8,
          left: 0,
          right: 0,
          bottom: 0,
          containLabel: true
        },
        textStyle: {
          color: '#949494'
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          },
          splitLine: {
            lineStyle: {
              color: '#f0f0f0'
            }
          }
        },
        series: [
          {
            type: 'line',
            smooth: true,
            data: [16410, 12452, 18090, 21843, 26390, 20330, 21020, 14410, 18452, 20200, 25043, 16090],
            itemStyle: {
              normal: {
                lineStyle: {
                  width: 3
                }
              }
            }
          }
        ]
      },
      tableData: [
        { date: '2019年6月', sales: '52,893,482.26', onYear: '+12.66', onMonth: '-6.80', update: '2019-06-01' },
        { date: '2019年5月', sales: '53,345,973.10', onYear: '+3.08', onMonth: '-0.12', update: '2019-05-31' },
        { date: '2019年4月', sales: '64,009,347.81', onYear: '+8.22', onMonth: '+5.29', update: '2019-04-30' },
        { date: '2019年3月', sales: '72,101,876.06', onYear: '-6.01', onMonth: '-4.62', update: '2019-03-29' },
        { date: '2019年2月', sales: '33,090,118.88', onYear: '-11.09', onMonth: '-1.01', update: '2019-02-28' },
        { date: '2019年1月', sales: '84,161,226.91', onYear: '+19.29', onMonth: '+14.02', update: '2019-01-31' }
      ],
      tableData1: [
        { title: '某银行理财之慧赢成长同行19034期净值型人民币理财产品', sum: '12832', sales: '2,893,482.26', finished: '89.92%' },
        { title: '某银行非凡资产管理净值型理财产品Q', sum: '33433', sales: '3,345,973.10', finished: '96.71%' },
        { title: '某银行封闭式净值型人民币非保本浮动收益理财产品', sum: '15542', sales: '4,009,347.81', finished: '100%' },
        { title: '某银行理财之慧赢成长同行1830期净值型人民币理财产品', sum: '24390', sales: '2,101,876.06', finished: '92.16%' },
        { title: '某银行“全鑫权益”智合-全天候多资产FOF-平衡型（第2期）', sum: '23443', sales: '3,090,118.88', finished: '76.10%' },
        { title: '某银行鼎盛财富双季盈定期开放净值型理财产品（G款）', sum: '86768', sales: '4,161,226.91', finished: '98.37%' }
      ],
      isSelected: true,
      isSelected1: false
    }
  },
  computed: {
    ...mapGetters([
      "theme"
    ])
  },
  watch:{
    theme: function(newVal) {
      this.theme = newVal;
      this.setThemeColor();
    }
  },
  mounted: function() {
    this.setThemeColor();
  },
  methods: {
    jump() {
      this.$router.routePush({
        path: 'content/systemManager/roleDataPowerManager/roleDataPowerManager',
      })
    },
    tableToggle: function() {
      if (this.isSelected) {
        this.isSelected = false
        this.isSelected1 = true
      } else {
        this.isSelected = true
        this.isSelected1 = false
      }
    },
    setThemeColor: function() {
      this.echartOption4.color = [this.theme.color]
      this.echartOption5.color = [this.theme.color]
      this.echartOption7.color = [this.theme.color]
      this.$nextTick(() => {
        this.$refs.echart4.echartsInstance.setOption(this.echartOption4)
        this.$refs.echart5.echartsInstance.setOption(this.echartOption5)
        this.$refs.echart7.echartsInstance.setOption(this.echartOption7)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .emptyGif {
    display: block;
    width: 45%;
    margin: 0 auto;
  }

  .dashboard-editor-container {
    background-color: #e3e3e3;
    min-height: 100vh;
    padding: 50px 60px 0px;
    .pan-info-roles {
      font-size: 12px;
      font-weight: 700;
      color: #333;
      display: block;
    }
    .info-container {
      position: relative;
      margin-left: 190px;
      height: 150px;
      line-height: 200px;
      .display_name {
        font-size: 48px;
        line-height: 48px;
        color: #212121;
        position: absolute;
        top: 25px;
      }
    }
  }
</style>

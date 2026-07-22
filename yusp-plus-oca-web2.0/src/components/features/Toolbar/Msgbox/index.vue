<template>
  <Toolbar tool-icon="yu-icon-message" :is-dot="true">
    <yu-tabs value="all">
      <yu-tab-pane v-for="(v,i) in msgData" :key="i" :label="v.title" :name="v.name">
        <ul class="yu-toolbar-msg-list">
          <li v-for="(item,index) in types[v.type]" :key="`msg_${index}`">
            <i :class="[item.type===0?'yu-icon-finish todo':'yu-icon-message3 msg']"></i>
            <p>
              <span :title="item.from+item.msg">
                <b>{{ item.from }}</b>{{ item.msg }}
              </span>
              <span>
                <i>{{ item.dateTime }}</i>
                <i v-if="item.state">{{ item.state }}</i>
                <a href="javascript:void(0);">
                  <template v-if="item.type===0">处理</template>
                  <template v-else>查看</template>
                </a>
              </span>
            </p>
          </li>
        </ul>
        <div class="yu-toolbar-msg-buttons">
          <yu-button type="text">清空全部</yu-button>
          <yu-button type="text">查看更多</yu-button>
        </div>
      </yu-tab-pane>
    </yu-tabs>
  </Toolbar>
</template>
<script>
import Toolbar from '@/components/layout1/base/ToolBar/index'
export default {
  name: 'MsgBox',
  components: {
    Toolbar
  },
  data () {
    return {
      types: [],
      msgData: [
        {
          title: '全部',
          name: 'all',
          type: 'all'
        },
        {
          title: '待办',
          name: 'to',
          type: 0
        },
        {
          title: '消息',
          name: 'message',
          type: 1
        }
      ],
      msgList: [
        { type: 0, from: '李林', msg: '发起了请假流程', dateTime: '1小时前', state: '待审批' },
        { type: 1, from: '李余则', msg: '回复了你的文章《2019年金融市场与大数据的紧密结合趋势》', dateTime: '2小时前', state: undefined },
        { type: 0, from: '陈可丰', msg: '发起了借款流程', dateTime: '5小时前', state: '待审批' },
        { type: 1, from: '刘伍', msg: '给你发了一封站内信', dateTime: '8小时前', state: undefined },
        { type: 0, from: '汪池宇', msg: '发起了借款款流程', dateTime: '1天前', state: '待审批' }
      ]
    }
  },
  created () {
    this.handleType();
  },
  methods: {
    handleType () {
      this.types = {
        all: []
      };
      this.msgList.forEach((v, i) => {
        if (!this.types[v.type]) this.types[v.type] = [];
        this.types.all.push(v);
        this.types[v.type].push(v);
      })
    }
  }
}
</script>
<style lang="scss">
.yu-toolbar-msg-list {
  display: block;
  position: relative;
  height: 420px;
  overflow: auto;
  li {
    display: block;
    height: 84px;
    padding: 10px 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    border-bottom: 1px #ededed solid;
    margin: 0;
    -webkit-transition: 0.2s;
    transition: 0.2s;
  }
  li > i {
    float: left;
    width: 42px;
    height: 42px;
    line-height: 42px;
    overflow: hidden;
    border-radius: 21px;
    font-size: 24px;
    text-align: center;
    margin: 11px 8px 0 24px;
  }
  i.todo {
    color: #fb8d12;
    background-color: #fce6ce;
  }
  i.msg {
    color: #5557b9;
    background-color: #cfd0f3;
  }
  p {
    display: block;
    margin: 0 24px 0 74px;
  }
  span {
    display: block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    line-height: 32px;
    height: 32px;
    font-size: 14px;
    color: #666;
    i {
      margin-right: 10px;
      font-size: 12px;
      font-style: normal;
      float: left;
    }
    a,
    a:visited,
    a:link {
      float: right;
      font-size: 12px;
      color: #64647a;
      padding: 0 10px;
      -webkit-transition: 0.2s;
      transition: 0.2s;
      border: 1px #babae3 solid;
      border-radius: 10px;
      height: 20px;
      line-height: 20px;
      margin-top: 6px;
    }
    a:hover {
      color: #5557b9;
      border: 1px #5557b9 solid;
    }
  }
  b {
    color: #444;
    font-weight: 400;
    padding-right: 10px;
  }
}
.yu-toolbar-msg-buttons {
  display: block;
  position: relative;
  border-top: 1px #ededed solid;
  overflow: hidden;
  .el-button--text {
    width: 50%;
    margin: 0;
    float: left;
    height: 20px;
    line-height: 20px;
    margin-top: 10px;
    color: #64647a;
    font-size: 14px;
    padding: 0;
    border-radius: 0;
    -webkit-transition: 0.2s;
    transition: 0.2s;
  }
}

.toolbar-item-content .el-tabs__header.is-top {
  line-height: 40px;
  margin-bottom: 0;
}

.yu-toolbar-msg-buttons:after {
  content: "";
  display: block;
  clear: both;
}
</style>


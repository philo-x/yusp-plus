<template>
  <div>
    <slot name="header"></slot>
    <div id="calendar"></div>
    <slot>
      <el-dialog-x :visible.sync="visible" :title="dialogTitle" v-if="showDialog">
        <slot name="content">
          <yu-xform ref="refForm" label-width="100px" label-suffix="：" v-model="formdata">
            <yu-xform-group>
              <yu-xform-item label="id" ctype="input" name="_id" :hidden="true"></yu-xform-item>
              <yu-xform-item label="主题" rules="required" ctype="input" name="title" :colspan="24"></yu-xform-item>
              <yu-xform-item label="开始时间" rules="required" ctype="datepicker" type="datetime" name="start" :colspan="10" :clearable="false"></yu-xform-item>
              <yu-xform-item label="结束时间" rules="required" ctype="datepicker" type="datetime" name="end" :colspan="10" :clearable="false"></yu-xform-item>
              <yu-xform-item ctype="checkbox" name="allDay" :colspan="4" :options="[{key:'1',value:'全天'}]" @change="allDayChange"></yu-xform-item>
              <yu-xform-item label="内容" ctype="textarea" name="content" :colspan="24"></yu-xform-item>
            </yu-xform-group>
            <div class="button-group" style="text-align:center">
              <el-button type="primary" @click="submitFn">提交</el-button>
              <el-button type="primary" @click="deleteFn" v-if="viewType == 'UPDATE'">删除</el-button>
              <el-button @click="cancelFn">取消</el-button>
            </div>
          </yu-xform>
        </slot>
      </el-dialog-x>
    </slot>
  </div>
</template>
<script>
/* eslint no-useless-concat:0 */
/* eslint no-lonely-if:0 */
/* eslint vue/require-prop-types:0 */
import { clone, dateFormat } from '@/utils'
export default {
  name: "YufpCalendar",
  props: {
    dialogVisible: Boolean,
    showDialog: {
      type: Boolean,
      default: true
    },
    view: {
      default: "month"
    },
    // event事件过多的时候出现 更多的链接
    eventLimit: {
      type: Boolean,
      default: true
    },
    editable: {
      type: Boolean
    }
  },
  data: function() {
    return {
      visible: this.dialogVisible,
      formdata: {},
      calendar: Object,
      // 临时存储窗口打开时的form数据
      tmpFormData: {},
      viewType: "ADD",
      dialogTitle: "新增",
      dateType: this.view
    };
  },
  watch: {
    dialogVisible: function(newval) {
      this.visible = newval;
    },
    visible: function(newval) {
      this.$emit("update:dialogVisible", newval);
    }
  },
  // 挂载后
  mounted: function() {
    var _this = this;
    var header = null;
    if (_this.$slots.header) {
      header = false;
    } else {
      header = {
        right: "prev,next today",
        center: "title",
        left: "month,agendaWeek,agendaDay"
      };
    }
    _this.calendar = window.$("#calendar");
    _this.calendar.fullCalendar({
      defaultView: _this.view,
      header: header,
      editable: _this.editable,
      allDayText: "全天",
      monthNames: [
        "一月",
        "二月",
        "三月",
        "四月",
        "五月",
        "六月",
        "七月",
        "八月",
        "九月",
        "十月",
        "十一月",
        "十二月"
      ],
      monthNamesShort: [
        "一月",
        "二月",
        "三月",
        "四月",
        "五月",
        "六月",
        "七月",
        "八月",
        "九月",
        "十月",
        "十一月",
        "十二月"
      ],
      dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
      dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
      today: ["今天"],
      firstDay: 1,
      eventLimit: _this.eventLimit,
      timeFormat: "H:mm",
      buttonText: {
        today: "今天",
        month: "月",
        week: "周",
        day: "日",
        prev: "<",
        next: ">"
      },
      // 生成日历
      events: function(start, end, timezone, callback) {
        callback([]);
        _this.$emit("events", start, end, timezone, callback);
      },
      viewRender: function(view, element) {
        _this.dateType = view;
        _this.$emit("view-change", view, element);
      },
      // 单机日历内空白 新增事件
      dayClick: function(date, allDay, jsEvent, view) {
        if (!_this.$slots.content && _this.showDialog) {
          if (!_this.checkInViewMonth(date._d)) {
            return;
          }
          _this.tmpFormData = { _id: "" };
          _this.viewType = "ADD";
          _this.dialogTitle = "新增";
          var timeDt = new Date();
          var time = dateFormat(timeDt, "{h}:{i}:{s}");
          var dt = dateFormat(date._d, "{y}-{m}-{d}");
          var dateTime = dt + " " + time;
          var day = new Date(dateTime);
          _this.tmpFormData.start = day;
          _this.tmpFormData.end = day;
          _this.visible = true;
          _this.$nextTick(function() {
            _this.$refs.refForm.resetFields();
            clone(_this.tmpFormData, _this.$refs.refForm.formdata);
          });
        }
        _this.$emit("day-click", date, allDay, jsEvent, view);
      },
      eventClick: function(event, jsEvent, view) {
        if (!_this.$slots.content && _this.showDialog) {
          // window.$(this).css('border-color', 'red');
          _this.visible = true;
          _this.viewType = "UPDATE";
          _this.dialogTitle = "修改";
          _this.dia;
          var data = {};
          data._id = event._id;
          data.title = event.title;
          var className = event.className[0];
          data.content = className.content;
          data.start = className.start;
          data.end = className.end;
          if (event.allDay === true) {
            var array = [undefined, "1"];
            data.allDay = array;
          }
          _this.$nextTick(function() {
            clone(data, _this.tmpFormData);
            _this.$refs.refForm.resetFields();
            clone(data, _this.$refs.refForm.formdata);
          });
        }
        _this.$emit("event-click", event, jsEvent, view);
      },
      windowResize: function(view) {
        _this.$emit("window-resize", view);
      },
      // 日程事件渲染之前触发
      eventRender: function(calEvent, element, view) {
        _this.$emit("event-render", calEvent, element, view);
      }
    });
  },
  methods: {
    cancelFn: function() {
      var _this = this;
      if (this.viewType === "ADD") {
        _this.formdata["_id"] = "";
      }
      _this.visible = false;
    },
    submitFn: function() {
      this.saveInfo();
    },
    saveInfo: function() {
      var validate = false;
      this.$refs.refForm.validate(function(valid) {
        validate = valid;
      });
      if (!validate) {
        return;
      }
      if (
        this.formdata.start.getTime() === this.formdata.end.getTime() &&
        !this.$refs.refForm.formdata.allDay[1]
      ) {
        this.$message({
          message: "日程开始时间和结束时间相同，如为全天日程请勾选“全天”",
          type: "error"
        });
        return;
      }
      var monthSource = {};
      if (this.formdata._id) {
        monthSource._id = this.formdata._id;
      }
      monthSource.title = this.formdata.title;
      monthSource.start = this.formdata.start;
      monthSource.end = this.formdata.end;
      monthSource.content = this.formdata.content;
      if (this.$refs.refForm.formdata.allDay[1] === 1) {
        monthSource.allDay = true;
        monthSource.start = new Date(
          this.formatDay(monthSource.start) + " " + " 00:00:00"
        );
        monthSource.end = new Date(
          this.formatDay(monthSource.end) + " " + " 00:00:00"
        );
      } else {
        monthSource.allDay = false;
      }
      monthSource.className = [clone(monthSource, {})];
      if (this.viewType === "ADD") {
        this.addEvent(monthSource);
      } else {
        this.updateEvent(monthSource);
      }
      this.visible = false;
    },
    deleteFn: function() {
      var _this = this;
      _this
        .$confirm("是否删除该日程？", "确认", {
          confirmButtonText: "是",
          cancelButtonText: "否",
          type: "warning"
        })
        .then(function() {
          _this.deleteEvent(_this.$refs.refForm.formdata._id);
          _this.visible = false;
        })
        .catch(function() {});
    },
    /**
     * 新增日程
     */
    addEvent: function(source, callback) {
      this.calendar.fullCalendar("renderEvent", source, true);
      if (typeof callback === "function") {
        callback(source);
      }
    },
    /**
     * 更新日程
     */
    updateEvent: function(source, callback) {
      this.calendar.fullCalendar("updateEvent", source);
      if (typeof callback === "function") {
        callback(source);
      }
    },
    /**
     * 删除日程
     */
    deleteEvent: function(id, callback) {
      this.calendar.fullCalendar("removeEvents", id);
      if (typeof callback === "function") {
        callback(id);
      }
    },
    allDayChange: function() {
      if (this.$refs.refForm.formdata.allDay[1] === 1) {
        var tmp = clone(this.$refs.refForm.formdata, {});
        if (this.$refs.refForm.formdata.start) {
          tmp.start = new Date(
            this.formatDay(this.$refs.refForm.formdata.start) +
              " " +
              " 00:00:00"
          );
        }
        if (this.$refs.refForm.formdata.end) {
          tmp.end = new Date(
            this.formatDay(this.$refs.refForm.formdata.end) + " " + " 00:00:00"
          );
        }
        clone(tmp, this.$refs.refForm.formdata);
      }
    },
    formatDay: function(date) {
      return dateFormat(date, "{y}-{m}-{d}");
    },
    /**
     * 判断点击日期是否在当前日期视图的月份区间中
     * @param 待判断日期
     * @return true表示在区间内，false表示不在区间内
     */
    checkInViewMonth: function(date) {
      var currentDate = this.calendar.fullCalendar("getView").calendar
        .currentDate._d;
      var year = currentDate.getFullYear();
      var month = currentDate.getMonth();
      var monthBegin = new Date(year, month, 1);
      var monthEnd = new Date(year, month + 1, 1);
      // 判断是否是日期类型
      if (date && date instanceof Date) {
        if (
          date.getTime() >= monthBegin.getTime() &&
          date.getTime() < monthEnd.getTime()
        ) {
          return true;
        } else {
          return false;
        }
      } else {
        if (date) {
          throw new Error("参数非日期类型！");
        }
      }
    }
  }
};
</script>

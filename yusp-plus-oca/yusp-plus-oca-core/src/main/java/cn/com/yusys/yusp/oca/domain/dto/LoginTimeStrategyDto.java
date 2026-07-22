package cn.com.yusys.yusp.oca.domain.dto;

import java.io.Serializable;

/**
 * @description: 类说明
 * @author: zhangsong
 * @date: 2021/3/31
 */
public class LoginTimeStrategyDto implements Serializable {
    private String weekdays;
    private String startTime;
    private String endTime;

    public LoginTimeStrategyDto() {
    }

    public String getWeekdays() {
        return this.weekdays;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setWeekdays(String weekdays) {
        this.weekdays = weekdays;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

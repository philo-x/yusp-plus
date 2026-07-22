package cn.com.yusys.yusp.job.core.utils;

/**
 * @description 常量
 * @author lty
 * @date 2021/3/1　　
 */
public class Constant {

    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private final int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


}

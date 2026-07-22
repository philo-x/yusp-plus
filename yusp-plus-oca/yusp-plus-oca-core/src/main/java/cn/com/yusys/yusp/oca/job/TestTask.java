/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package cn.com.yusys.yusp.oca.job;

import cn.com.yusys.yusp.job.core.task.ITask;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 * @author: zhangsong
 * @date: 2021/3/31
 */
@Component("testTask")
public class TestTask implements ITask {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestTask.class);

    @Override
	public void run(String params){
		log.info("TestTask定时任务正在执行，参数为：{}", params);
	}
}

package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.commons.progress.model.ProgressDto;
import cn.com.yusys.yusp.oca.domain.dto.AdminSmLogDto;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmLogForm;
import cn.com.yusys.yusp.oca.domain.query.AdminSmLogQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLogVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统操作日志表
 *
 * @author danyb1
 * @since 2020-12-02 22:18:19
 */
public interface AdminSmLogService extends IService<AdminSmLogEntity> {

    /**
     * 分页条件查询日志表
     *
     * @param adminSmLogForm: {"logTypeId":"7","user":"40","operObjId":"111","beginTime":"2020-12-06","endTime":"2020-12-07"}
     * @return 返回分页查询对象
     */
    IPage<AdminSmLogVo> pageLogByCondition(AdminSmLogForm adminSmLogForm);

    /**
     * excel 对应的查询方法
     *
     * @param logQuery
     * @return
     */
    IPage<AdminSmLogEntity> findLogByLogForm(AdminSmLogQuery logQuery);

    /**
     * 异步记录日志
     *
     * @param logDto 日志传输对象
     */
    void addLog(AdminSmLogDto logDto);

    /**
     * 异步导出文件
     *
     * @param logQuery 查询条件的封装对象
     * @return 异步任务封装类
     */
    ProgressDto translateFile(AdminSmLogQuery logQuery);

    /**
     * 清除指定天数前操作日志
     *
     * @param day
     */
    void clearAdminSmLog(int day);

}
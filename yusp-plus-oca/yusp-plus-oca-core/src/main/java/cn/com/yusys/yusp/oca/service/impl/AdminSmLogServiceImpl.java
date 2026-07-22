package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.excelcsv.ExcelUtils;
import cn.com.yusys.yusp.commons.excelcsv.FileExportPostProcessor;
import cn.com.yusys.yusp.commons.excelcsv.async.ExportContext;
import cn.com.yusys.yusp.commons.excelcsv.model.DataAcquisition;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.progress.model.ProgressDto;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmLogDao;
import cn.com.yusys.yusp.oca.domain.dto.AdminSmLogDto;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmLogForm;
import cn.com.yusys.yusp.oca.domain.query.AdminSmLogQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLogExcelVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLogVo;
import cn.com.yusys.yusp.oca.service.AdminSmLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author danyu
 */
@Service("adminSmLogService")
public class AdminSmLogServiceImpl extends ServiceImpl<AdminSmLogDao, AdminSmLogEntity> implements AdminSmLogService {

    private static final Logger logger = LoggerFactory.getLogger(AdminSmLogServiceImpl.class);

    private final static String LOG_OPER_TIME = "log.OPER_TIME";

    @Override
    public IPage<AdminSmLogVo> pageLogByCondition(AdminSmLogForm adminSmLogForm) {
        int page = adminSmLogForm.getPage();
        int size = adminSmLogForm.getSize();
        String logTypeId = adminSmLogForm.getLogTypeId();
        String operObjId = adminSmLogForm.getOperObjId();
        String beginTime = adminSmLogForm.getBeginTime();
        String endTime = adminSmLogForm.getEndTime();
        // 组装查询条件
        QueryWrapper<AdminSmLogVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(logTypeId), "log.LOG_TYPE_ID", logTypeId);
        queryWrapper.eq(!StringUtils.isEmpty(operObjId), "log.OPER_OBJ_ID", operObjId);
        queryWrapper.between((!StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime)), LOG_OPER_TIME, beginTime, endTime);
        // 组装page分页对象
        Page<AdminSmLogVo> pageList = new Page<>(page, size);
        pageList.addOrder(OrderItem.desc(LOG_OPER_TIME));

        IPage<AdminSmLogVo> pageResult = getBaseMapper().pageLogByCondition(pageList, queryWrapper);

        return pageResult;
    }

    @Override
    public IPage<AdminSmLogEntity> findLogByLogForm(AdminSmLogQuery logQuery) {
        int page = logQuery.getPage();
        int size = logQuery.getSize();
        List<String> logIds = logQuery.getLogIds();
        String logTypeId = logQuery.getLogTypeId();
        String operObjId = logQuery.getOperObjId();
        String beginTime = logQuery.getBeginTime();
        String endTime = logQuery.getEndTime();
        // 组装查询条件
        QueryWrapper<AdminSmLogEntity> queryWrapper = new QueryWrapper<>();
        if (logIds != null && logIds.size() > 0) {
            queryWrapper.in("log.log_id", logIds);
        } else {
            queryWrapper.eq(!StringUtils.isEmpty(logTypeId), "log.LOG_TYPE_ID", logTypeId);
            queryWrapper.eq(!StringUtils.isEmpty(operObjId), "log.OPER_OBJ_ID", operObjId);
            queryWrapper.between((!StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime)), LOG_OPER_TIME, beginTime, endTime);
        }
        // 组装page分页对象
        Page<AdminSmLogEntity> pageList = new Page<>(page, size);
        pageList.addOrder(OrderItem.desc(LOG_OPER_TIME));

        IPage<AdminSmLogEntity> pageResult = getBaseMapper().findLogByLogForm(pageList, queryWrapper);

        return pageResult;
    }

    @Override
    public void addLog(AdminSmLogDto logDto) {
        AdminSmLogEntity logEntity = new AdminSmLogEntity();
        BeanUtils.beanCopy(logDto, logEntity);
        logEntity.setLogId(IdWorker.get32UUID());
        logEntity.setOperTime(DateUtils.formatDateTimeByDef());
        this.save(logEntity);
        JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 异步导出文件
     *
     * @param logQuery
     * @return
     */
    @Override
    public ProgressDto translateFile(AdminSmLogQuery logQuery) {
        DataAcquisition dataAcquisition = (page, size, object) -> {
            AdminSmLogQuery queryVo = (AdminSmLogQuery) object;
            queryVo.setSize(size);
            queryVo.setPage(page);
            return selectByModelForExcel(queryVo);
        };
        ExportContext exportContext = ExportContext.of(AdminSmLogExcelVo.class).exportPostProcessor(new FileExportPostProcessor()).data(dataAcquisition, logQuery);
        return ExcelUtils.asyncExport(exportContext);
    }

    /**
     * 数据查询方法的返回集合类型，必须与HeadClass相同
     *
     * @param query
     * @return
     */
    private List<AdminSmLogExcelVo> selectByModelForExcel(AdminSmLogQuery query) {
        IPage<AdminSmLogEntity> smLogVoPage = findLogByLogForm(query);
        List<AdminSmLogExcelVo> adminSmLogExcelVos = (List<AdminSmLogExcelVo>) BeanUtils.beansCopy(smLogVoPage.getRecords(), AdminSmLogExcelVo.class);
        return adminSmLogExcelVos;
    }

    /**
     * 清除指定天数前操作日志
     *
     * @param day
     */
    @Override
    public void clearAdminSmLog(int day) {
        //当前时间
        String nowStr = DateUtils.formatDate("yyyy-MM-dd");
        Date now = DateUtils.parseDate(nowStr, "yyyy-MM-dd");
        Date before = DateUtils.addDay(now, (-day));
        //清理系统操作日志表
        LambdaQueryWrapper<AdminSmLogEntity> adminSmLogWrapper = new LambdaQueryWrapper<>();
        adminSmLogWrapper.lt(AdminSmLogEntity::getLastChgDt, before);
        this.remove(adminSmLogWrapper);
    }
}
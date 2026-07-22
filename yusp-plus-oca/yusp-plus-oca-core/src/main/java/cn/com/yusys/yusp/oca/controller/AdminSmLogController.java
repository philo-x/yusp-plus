package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.oca.annotation.Log;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.progress.model.ProgressDto;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.oca.domain.dto.AdminSmLogDto;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmLogForm;
import cn.com.yusys.yusp.oca.domain.query.AdminSmLogQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLogVo;
import cn.com.yusys.yusp.oca.service.AdminSmLogService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.log.CommonLogInfo;
import cn.com.yusys.yusp.oca.service.log.DispatchLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统操作日志表
 *
 * @author danyb1
 * @date 2020-12-02 22:18:19
 */
@Tag(name = "日志管理接口")
@RestController
@RequestMapping("/api/monitor")
public class AdminSmLogController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSmLogController.class);

    @Autowired
    private AdminSmLogService adminSmLogService;

    @Autowired
    private DispatchLogService dispatchLogService;

    @Autowired
    private AdminSmUserService adminSmUserService;

    /**
     * 分页条件查询日志表
     *
     * @param adminSmLogForm 查询条件封装的对象
     * @return 成功返回分页查询对象 IPage，失败返回 code 500 和异常信息
     */
    @Operation(summary = "分页条件查询日志表")
    @PostMapping("/auditlogdata/list")
    @Log(type = "4", operFlag = "按钮", content = "分页条件查询日志表", operObjId = "Log MGT")
    public JClientRspEntity<Object> pageLogByCondition(@RequestBody JClientReqEntity<AdminSmLogForm> adminSmLogForm) {
        IPage<AdminSmLogVo> page = adminSmLogService.pageLogByCondition(adminSmLogForm.getBody());
        return JClientRspEntity.buildSuccess(page);
    }

    /**
     * 异步记录日志
     *
     * @param req     日志存储封装的对象
     * @param request 请求对象
     * @return 成功返回 success，失败返回 code 500 和异常信息
     */
    @Operation(summary = "异步记录日志")
    @PostMapping("/auditlogdata")
    public JClientRspEntity<Object> addLog(@RequestBody JClientReqEntity<AdminSmLogDto> req, HttpServletRequest request) {
        AdminSmLogDto logDto = req.getBody();
        // 设置请求ip
        if (StringUtils.isEmpty(logDto.getLoginIp())) {
            logDto.setLoginIp(getIpAddress(request));
        }
        // 添加前端未传数据
        logDto.setOperTime(DateUtils.formatDateTimeByDef(new Date()));
        logDto.setUserId(SessionUtils.getUserId());
        logDto.setOrgId(SessionUtils.getUserOrganizationId());
        CommonLogInfo commonLogInfo = new CommonLogInfo();
        commonLogInfo.setLogInfo(logDto);
        dispatchLogService.handleLog(commonLogInfo);
        return JClientRspEntity.buildSuccess("记录操作到日志中！");
    }

    /**
     * 日志异步导出的方法
     *
     * @param logQuery 日志查询条件封装对象
     * @return 成功返回异步任务封装类，失败返回 code 500 返回异常信息
     */
    @Operation(summary = "日志异步导出")
    @PostMapping("/translatefile")
    public JClientRspEntity<Object> translateFile(@RequestBody JClientReqEntity<AdminSmLogQuery> logQuery) {
        ProgressDto progressDto = adminSmLogService.translateFile(logQuery.getBody());
        return JClientRspEntity.buildSuccess(progressDto);
    }

    /**
     * 批量删除日志
     *
     * @param logList 日志对象列表
     * @return 成功返回 code 0，失败返回 code 500 返回异常信息
     */
    @Operation(summary = "批量删除日志")
    @PostMapping("/auditlogdata/batchdelete")
    protected JClientRspEntity<Object> batchDelete(@RequestBody JClientReqEntity<List<AdminSmLogEntity>> logList) {
        List<String> collect = logList.getBody().stream().map(AdminSmLogEntity::getLogId).collect(Collectors.toList());
        adminSmLogService.removeByIds(collect);
        return JClientRspEntity.buildSuccess("成功");
    }


    public String getIpAddress(HttpServletRequest request) {
        String unknownIp = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || unknownIp.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || unknownIp.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || unknownIp.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || unknownIp.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || unknownIp.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}

package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.SessionService;
import cn.com.yusys.yusp.commons.session.user.MenuControl;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.job.core.service.ScheduleJobService;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmTenantUserRelEntity;
import cn.com.yusys.yusp.oca.job.UserLoadTask;
import cn.com.yusys.yusp.oca.service.AdminSmAuthRecoService;
import cn.com.yusys.yusp.oca.service.AdminSmTenantUserRelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 菜单和控制点控制层
 *
 * @author zhanyq
 * @date 2021-06-24 14:09
 */
@RestController
@RequestMapping("/api/account")
public class AdminMenuAndContrController {

    @Resource
    @Qualifier("scheduleTaskExecutor")
    private Executor scheduleTaskExecutor;

    private static final Logger log = LoggerFactory.getLogger(AdminMenuAndContrController.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    SessionService ocaSessionService;

    @Autowired
    AdminSmAuthRecoService adminSmAuthRecoService;


    @Autowired
    private ScheduleJobService scheduleJobService;

    @Autowired
    private UserLoadTask userLoadTask;

    @Autowired
    private AdminSmTenantUserRelService adminSmTenantUserRelService;

    /**
     * 用户登录时获取菜单和控制点
     * @return 菜单和控制点对象
     * @author zhanyq
     * @date 2021-06-24 14:27
     */
    @PostMapping("/menuandcontr")
    public JClientRspEntity<MenuControl> getMenuAndContra() {
        String userId = SessionUtils.getUserId();
        String clientId = SessionUtils.getClientId();
        String menuControlKey = "MenuControl:" + clientId + "-" + userId;
        String dataTmplControlKey = "DataControl:" + clientId + "-" + userId;
        String allControlKey = "Control:" + clientId + "-" + userId;
        try {
            stringRedisTemplate.delete(Arrays.asList(menuControlKey, dataTmplControlKey, allControlKey));
        } catch (Exception e) {
            log.warn("redis connect warning", e);
        }
        //只要是租户登录，就会去刷新
        List<String> list = adminSmTenantUserRelService.list(new QueryWrapper<>()).stream().map(AdminSmTenantUserRelEntity::getUserId).collect(Collectors.toList());
        if(list.contains(userId)){
            // TODO  在并发情况下，避免重复执行
            userLoadTask.run(null);
            CompletableFuture.runAsync(() -> scheduleJobService.run(new Long[]{3L,6L}), scheduleTaskExecutor);
        }

        // 2.异步加载数据权限,所有控制点
        CompletableFuture.runAsync(() -> ocaSessionService.getDataControl(clientId, userId));

        return JClientRspEntity.buildSuccess(ocaSessionService.getMenuControl(clientId, userId));
    }
}

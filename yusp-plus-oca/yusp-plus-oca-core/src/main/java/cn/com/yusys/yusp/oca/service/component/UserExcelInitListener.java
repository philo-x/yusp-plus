package cn.com.yusys.yusp.oca.service.component;

import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserExcelVo;
import cn.com.yusys.yusp.oca.service.AdminSmUserRoleRelService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户excel导入解析监听
 *
 * @author zhanyq
 * @date 2021-06-28 10:08
 */
public class UserExcelInitListener extends AnalysisEventListener<AdminSmUserExcelVo> {

    private static final Logger log = LoggerFactory.getLogger(UserExcelInitListener.class);

    private final AdminSmUserService adminSmUserService;

    private final AdminSmUserRoleRelService adminSmUserRoleRelService;


    /**
     * 标识，如果传入的是0 则执行导入，如果传入的非0的数据 则执行删除
     * 加这个标识是为了导入大量错误数据后能够顺利回退
     * 如果在页面已经对导入的角色受过权了 那么先将授权取消掉，再执行删除
     */
    private final int flag;


    /**
     * 解析后的用户结果
     */
    List<AdminSmUserEntity> list = new ArrayList<>();

    /**
     * 解析后的用户角色结果
     */
    List<AdminSmUserRoleRelEntity> relList = new ArrayList<>();

    public UserExcelInitListener(AdminSmUserService adminSmUserService, AdminSmUserRoleRelService adminSmUserRoleRelService, int flag) {
        this.adminSmUserService = adminSmUserService;
        this.adminSmUserRoleRelService = adminSmUserRoleRelService;
        this.flag = flag;
    }

    @Override
    public void invoke(AdminSmUserExcelVo adminSmUserExcelVo, AnalysisContext analysisContext) {
        if (!StringUtils.isEmpty(adminSmUserExcelVo.getLoginCode())
                && !StringUtils.isEmpty(adminSmUserExcelVo.getUserName())
                && !StringUtils.isEmpty(adminSmUserExcelVo.getOrgId())
                && !StringUtils.isEmpty(adminSmUserExcelVo.getRoleId())) {

            AdminSmUserEntity userEntity = BeanUtils.beanCopy(adminSmUserExcelVo, AdminSmUserEntity.class);
            AdminSmUserEntity user = adminSmUserService.getOne(new QueryWrapper<AdminSmUserEntity>().eq("login_code", adminSmUserExcelVo.getLoginCode()));
            String userId = user != null ? user.getUserId() : UUID.randomUUID().toString().replace("-", "");
            userEntity.setUserId(userId);
            userEntity.setUserPassword(new BCryptPasswordEncoder().encode("123456"));
            userEntity.setUserSts(AvailableStateEnum.ENABLED);
            //封装用户角色关系表
            AdminSmUserRoleRelEntity userRoleRelEntity = new AdminSmUserRoleRelEntity(userId, adminSmUserExcelVo.getRoleId());
            list.add(userEntity);
            relList.add(userRoleRelEntity);
            //list和relList的长度肯定是一致的，所以只拿list来判断就可以
            if (list.size() >= Constants.ExcelInitConstance.BATCH_COUNT) {
                if (flag == 0) {
                    adminSmUserService.saveOrUpdateBatch(list);
                    adminSmUserRoleRelService.saveOrUpdateBatch(relList);
                } else {
                    deleteData();
                }


            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (flag == 0) {
            adminSmUserService.saveOrUpdateBatch(list);
            adminSmUserRoleRelService.saveOrUpdateBatch(relList);
            log.info("用户数据初始化成功！");
        } else {
            deleteData();
            log.info("用户数据删除成功");
        }
    }


    /**
     * 删除数据
     *
     * @return void
     * @author zhanyq
     * @date 2021-06-28 10:11
     */
    private void deleteData() {
        Set<String> userCodes = list.stream().map(AdminSmUserEntity::getLoginCode).collect(Collectors.toSet());
        userCodes.forEach(userCode -> {
            Map<String, Object> userMap = new HashMap<>(1);
            userMap.put("login_code", userCode);
            adminSmUserService.getBaseMapper().deleteByMap(userMap);
        });

        Set<String> rels = relList.stream().map(AdminSmUserRoleRelEntity::getRoleId).collect(Collectors.toSet());
        rels.forEach(rel -> {
            Map<String, Object> relMap = new HashMap<>(1);
            relMap.put("role_id", rel);
            adminSmUserRoleRelService.getBaseMapper().deleteByMap(relMap);
        });


    }
}

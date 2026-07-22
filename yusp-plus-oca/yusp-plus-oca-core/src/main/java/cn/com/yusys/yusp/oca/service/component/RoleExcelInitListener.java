package cn.com.yusys.yusp.oca.service.component;

import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleExcelVo;
import cn.com.yusys.yusp.oca.service.AdminSmRoleService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 角色数据excel解析器
 *
 * @author zhanyq
 * @date 2021-06-28 10:05
 */
public class RoleExcelInitListener extends AnalysisEventListener<AdminSmRoleExcelVo> {


    private static final Logger log = LoggerFactory.getLogger(RoleExcelInitListener.class);
    
    private final AdminSmRoleService adminSmRoleService;

    /**
     * 标识，如果传入的是0 则执行导入，如果传入的非0的数据 则执行删除
     * 加这个标识是为了导入大量错误数据后能够顺利回退
     * 如果在页面已经对导入的角色受过权了 那么先将授权取消掉，再执行删除
     */
    private final int flag;

    /**
     * 解析后的结果
     */
    List<AdminSmRoleEntity> list = new ArrayList<>();


    public RoleExcelInitListener(AdminSmRoleService adminSmRoleService, int flag) {
        this.adminSmRoleService = adminSmRoleService;
        this.flag = flag;
    }


    @Override
    public void invoke(AdminSmRoleExcelVo adminSmRoleExcelVo, AnalysisContext analysisContext) {
        if (!StringUtils.isEmpty(adminSmRoleExcelVo.getRoleCode())
                && !StringUtils.isEmpty(adminSmRoleExcelVo.getRoleName())
                && !StringUtils.isEmpty(adminSmRoleExcelVo.getOrgId())) {

            AdminSmRoleEntity roleEntity = BeanUtils.beanCopy(adminSmRoleExcelVo, AdminSmRoleEntity.class);
            roleEntity.setRoleId(roleEntity.getRoleCode());
            roleEntity.setRoleSts(AvailableStateEnum.ENABLED);
            list.add(roleEntity);
            if (list.size() >= Constants.ExcelInitConstance.BATCH_COUNT) {
                if (flag == 0) {
                    adminSmRoleService.saveOrUpdateBatch(list);
                } else {
                    Set<String> ids = list.stream().map(AdminSmRoleEntity::getRoleId).collect(Collectors.toSet());
                    adminSmRoleService.getBaseMapper().deleteBatchIds(ids);
                }
                list.clear();
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (flag == 0) {
            adminSmRoleService.saveOrUpdateBatch(list);
            log.info("角色数据初始化完成！");
        } else {
            Set<String> ids = list.stream().map(AdminSmRoleEntity::getRoleId).collect(Collectors.toSet());
            adminSmRoleService.getBaseMapper().deleteBatchIds(ids);
            log.info("角色数据删除完成！");
        }

    }
}

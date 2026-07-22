package cn.com.yusys.yusp.oca.service.component;

import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgExcelVo;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 机构数据Excel解析器
 *
 * @author zhanyq
 * @date 2021-06-28 10:00
 */
public class OrgExcelInitListener extends AnalysisEventListener<AdminSmOrgExcelVo> {

    private static final Logger log = LoggerFactory.getLogger(OrgExcelInitListener.class);

    /**
     * 解析后的结果
     */
    List<AdminSmOrgEntity> list = new ArrayList<>();

    private final AdminSmOrgService adminSmOrgService;
    /**
     * 标识，如果传入的是0 则执行导入，如果传入的非0的数据 则执行删除
     * 加这个标识是为了导入大量错误数据后能够顺利回退
     * 如果在页面已经对导入的角色受过权了 那么先将授权取消掉，再执行删除
     */
    private final int flag;

    public OrgExcelInitListener(AdminSmOrgService adminSmOrgService, int flag) {
        this.adminSmOrgService = adminSmOrgService;
        this.flag = flag;
    }


    /**
     * 解析数据的方法，继承而来
     *
     * @param adminSmOrgExcelVo 从excel中读取到的数据
     * @param analysisContext   解析上下文对象
     * @return void
     * @author zhanyq
     * @date 2021-06-28 10:02
     */
    @Override
    public void invoke(AdminSmOrgExcelVo adminSmOrgExcelVo, AnalysisContext analysisContext) {
        //数据校验
        if (!StringUtils.isEmpty(adminSmOrgExcelVo.getOrgCode())
                && !StringUtils.isEmpty(adminSmOrgExcelVo.getOrgName())) {
            //组装数据库操作entity
            AdminSmOrgEntity orgEntity = BeanUtils.beanCopy(adminSmOrgExcelVo, AdminSmOrgEntity.class);
            orgEntity.setOrgId(orgEntity.getOrgCode());
            orgEntity.setOrgSts(AvailableStateEnum.ENABLED);
            list.add(orgEntity);
            //达到批量存储的阈值，则执行插入，执行完，清理list
            if (list.size() >= Constants.ExcelInitConstance.BATCH_COUNT) {
                if (flag == 0) {
                    adminSmOrgService.saveOrUpdateBatch(list);
                } else {
                    Set<String> ids = list.stream().map(AdminSmOrgEntity::getOrgId).collect(Collectors.toSet());
                    adminSmOrgService.getBaseMapper().deleteBatchIds(ids);
                }

                list.clear();
            }
        }

    }


    /**
     * 所有数据解析完之后，会调用该方法，防止遗漏数据
     *
     * @param analysisContext 解析上下文
     * @return void
     * @author zhanyq
     * @date 2021-06-28 10:03
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (flag == 0) {
            adminSmOrgService.saveOrUpdateBatch(list);
            log.info("机构数据初始化完成！");
        } else {
            Set<String> ids = list.stream().map(AdminSmOrgEntity::getOrgId).collect(Collectors.toSet());
            adminSmOrgService.getBaseMapper().deleteBatchIds(ids);
            log.info("机构数据删除完成！");
        }


    }


}

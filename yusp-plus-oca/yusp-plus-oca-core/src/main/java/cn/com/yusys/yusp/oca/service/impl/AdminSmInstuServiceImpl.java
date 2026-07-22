package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmInstuDao;
import cn.com.yusys.yusp.oca.dao.AdminSmOrgDao;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmInstuEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmInstuQuery;
import cn.com.yusys.yusp.oca.domain.vo.InstuExtVo;
import cn.com.yusys.yusp.oca.domain.vo.UserSessionVo;
import cn.com.yusys.yusp.oca.service.AdminSmInstuService;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author yusys
 */
@Service("adminSmInstuService")
public class AdminSmInstuServiceImpl extends ServiceImpl<AdminSmInstuDao, AdminSmInstuEntity> implements AdminSmInstuService {

    private static final Logger log = LoggerFactory.getLogger(AdminSmInstuServiceImpl.class);

    @Autowired
    private AdminSmOrgService adminSmOrgService;

    @Autowired
    private AdminSmOrgDao adminSmOrgDao;

    private static final String INSTU_ID = "instu_id";

    /**
     * 分页查询
     * @param query
     * @return
     */
    @Override
    public Page<InstuExtVo> queryPage(AdminSmInstuQuery query) {
        Page<InstuExtVo> page=query.getIPage();
        QueryWrapper<InstuExtVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(query.getInstuCde()), "i.instu_cde", query.getInstuCde());
        queryWrapper.like(!StringUtils.isEmpty(query.getInstuName()), "i.instu_name", query.getInstuName());
        queryWrapper.eq(!StringUtils.isEmpty(query.getInstuSts()), "i.instu_sts", query.getInstuSts());
        queryWrapper.orderByDesc("i.last_chg_dt");
        return getBaseMapper().pageInstuExtVo(page,queryWrapper);
    }

    /**
     *
     * @方法名称: save
     * @方法描述: 新增金融机构数据
     * @参数与返回说明:
     * @算法描述:
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Exception.class, RuntimeException.class })
    public boolean save(AdminSmInstuEntity entity, UserSessionVo userSessionVo) {
        if(StringUtils.isEmpty(entity.getInstuCde())){
            return false;
        }
        QueryWrapper<AdminSmInstuEntity> wrapper=Wrappers.query();
        Long exist= getBaseMapper().selectCount(wrapper.eq("INSTU_CDE",entity.getInstuCde()));
        if(exist>0){
            return false;
        }
        entity.setInstuId(entity.getInstuCde());
        entity.setInstuSts(Optional.ofNullable(entity.getInstuSts()).orElse("W"));
        log.info("New data of financial institutions: [new financial institutions: {}]", entity.getInstuName());
        getBaseMapper().insert(entity);
        AdminSmOrgEntity org=new AdminSmOrgEntity();
        org.setOrgId(entity.getInstuId()+"0001");
        org.setOrgCode(entity.getInstuId()+"0001");
        org.setOrgName(entity.getInstuName());
        org.setOrgLevel(1);
        org.setUpOrgId("1");
        org.setOrgSts(AvailableStateEnum.UNENABLED);
        org.setInstuId(entity.getInstuId());
        org.setContTel(entity.getContTel());
        org.setContUsr(entity.getContUsr());
        org.setOrgAddr(entity.getInstuAddr());
        org.setZipCde(entity.getZipCde());
        if(!adminSmOrgService.save(org)){
            throw BizException.error(null,"2","新增金融机构根机构失败",null);
        }
        return true;
    }

    /**
     * 批量启用
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchEnable(String ids) {
        if(StringUtils.nonEmpty(ids)){
            List<String> idList= Arrays.stream(ids.split(",")).filter(StringUtils::nonEmpty).collect(Collectors.toList());
            QueryWrapper<AdminSmInstuEntity> queryWrapper=new QueryWrapper<>();
            queryWrapper.in(INSTU_ID,idList);
            AdminSmInstuEntity model=new AdminSmInstuEntity();
            model.setInstuSts("A");
            getBaseMapper().update(model,queryWrapper);
            return  true;
        }
        return false;
    }

    @Override
    public boolean batchDisable(String ids) {
        if(StringUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.stream(ids.split(",")).filter(StringUtils::nonEmpty).collect(Collectors.toList());
            // 检查是否有关联未停用的机构
            QueryWrapper<AdminSmOrgEntity> orgWrapper=new QueryWrapper<>();
            orgWrapper.in(INSTU_ID,idList).eq("org_sts","A");
            Long countSon=this.adminSmOrgDao.selectCount(orgWrapper);
            if(countSon>0){
                return false;
            }
            //批量更新
            AdminSmInstuEntity model=new AdminSmInstuEntity();
            model.setInstuSts("I");
            QueryWrapper<AdminSmInstuEntity> instuWrapper=new QueryWrapper<>();
            instuWrapper.in(INSTU_ID,idList);
            getBaseMapper().update(model,instuWrapper);
            return  true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { Exception.class, RuntimeException.class })
    public boolean updateById(AdminSmInstuEntity entity) {
        AdminSmInstuEntity old = getBaseMapper().selectById(entity.getInstuId());
        if(!old.getInstuName().equals(entity.getInstuName())){
            //对应根机构也要改名
            AdminSmOrgEntity orgEntity=new AdminSmOrgEntity();
            orgEntity.setOrgId(entity.getInstuId()+"0001");
            orgEntity.setOrgName(entity.getInstuName());
            this.adminSmOrgDao.updateById(orgEntity);
        }
        entity.setInstuSts("W");
        log.info("Modified data of financial institutions: [modify financial institutions: {}] ", entity.getInstuName());
        getBaseMapper().updateById(entity);
        return false;
    }


    /**
     * 批量删除
     *  待删列表中存在【启用】状态的金融机构不得删除，整个删除操作失败
     * @param ids
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Exception.class, RuntimeException.class })
    public boolean batchDelete(String ids) {
        if(StringUtils.nonEmpty(ids)) {
            List<String> idList= Arrays.stream(ids.split(",")).filter(StringUtils::nonEmpty).collect(Collectors.toList());
            QueryWrapper<AdminSmInstuEntity> queryWrapper=new QueryWrapper<>();
            queryWrapper.in(INSTU_ID,idList).eq("org_sts","A");
            long blocked =this.count(queryWrapper);
            if(blocked>0){
                return false;
            }
            QueryWrapper<AdminSmInstuEntity> deleteInstuWrapper=new QueryWrapper<>();
            deleteInstuWrapper.in(INSTU_ID,idList);
            getBaseMapper().delete(deleteInstuWrapper);

            QueryWrapper<AdminSmOrgEntity> deleteOrgWrapper=new QueryWrapper<>();
            deleteOrgWrapper.in(INSTU_ID,idList);
            this.adminSmOrgDao.delete(deleteOrgWrapper);
            return true;
        }
        return false;
    }
}
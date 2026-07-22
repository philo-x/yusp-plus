package cn.com.yusys.yusp.workboard.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.com.yusys.yusp.workboard.dao.OcaWorkboardDao;
import cn.com.yusys.yusp.workboard.domain.entity.OcaWorkboardEntity;
import cn.com.yusys.yusp.workboard.domain.vo.OcaWorkboardQueryVo;
import cn.com.yusys.yusp.workboard.service.OcaWorkboardService;


/**
 * @author yusys
 * @version 1.0
 * @date 2021/1/26 10:04
 */
@Service("ocaWorkboardService")
public class OcaWorkboardServiceImpl extends ServiceImpl<OcaWorkboardDao, OcaWorkboardEntity> implements OcaWorkboardService {

    /**
     * queryPage
     */
    @Override
    public IPage<OcaWorkboardEntity> queryPage(OcaWorkboardQueryVo ocaWorkboardQueryVo) {
        LambdaQueryWrapper<OcaWorkboardEntity> queryWrapper = new LambdaQueryWrapper<>();
            if(ocaWorkboardQueryVo.getUserId() != null){
                queryWrapper.eq(OcaWorkboardEntity::getUserId, ocaWorkboardQueryVo.getUserId());
            }
            if(ocaWorkboardQueryVo.getUserName() != null){
                queryWrapper.eq(OcaWorkboardEntity::getUserName, ocaWorkboardQueryVo.getUserName());
            }
            if(ocaWorkboardQueryVo.getUserPhone() != null){
                queryWrapper.eq(OcaWorkboardEntity::getUserPhone, ocaWorkboardQueryVo.getUserPhone());
            }
            if(ocaWorkboardQueryVo.getUserAddress() != null){
                queryWrapper.eq(OcaWorkboardEntity::getUserAddress, ocaWorkboardQueryVo.getUserAddress());
            }
            if(ocaWorkboardQueryVo.getUserManager() != null){
                queryWrapper.eq(OcaWorkboardEntity::getUserManager, ocaWorkboardQueryVo.getUserManager());
            }
            if(ocaWorkboardQueryVo.getUserSex() != null){
                queryWrapper.eq(OcaWorkboardEntity::getUserSex, ocaWorkboardQueryVo.getUserSex());
            }
            if(ocaWorkboardQueryVo.getUserDesc() != null){
                queryWrapper.eq(OcaWorkboardEntity::getUserDesc, ocaWorkboardQueryVo.getUserDesc());
            }
            if(ocaWorkboardQueryVo.getCreateTime() != null){
                queryWrapper.eq(OcaWorkboardEntity::getCreateTime, ocaWorkboardQueryVo.getCreateTime());
            }
            if(ocaWorkboardQueryVo.getUpdateTime() != null){
                queryWrapper.eq(OcaWorkboardEntity::getUpdateTime, ocaWorkboardQueryVo.getUpdateTime());
            }

        return this.page(
            new Page<OcaWorkboardEntity>(ocaWorkboardQueryVo.getPage(), ocaWorkboardQueryVo.getSize()),
            queryWrapper
        );
    }
}
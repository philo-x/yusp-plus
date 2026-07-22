package cn.com.yusys.yusp.workboard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.com.yusys.yusp.workboard.domain.entity.OcaWorkboardEntity;
import cn.com.yusys.yusp.workboard.domain.vo.OcaWorkboardQueryVo;

/**
 *
 *
 * @author wx20220514
 * @date 2022-07-29 15:07:33
 */
public interface OcaWorkboardService extends IService<OcaWorkboardEntity> {

    /**
     * queryPage
     * @param ocaWorkboardQueryVo
     * @return
     */
    IPage<OcaWorkboardEntity> queryPage(OcaWorkboardQueryVo ocaWorkboardQueryVo);
}

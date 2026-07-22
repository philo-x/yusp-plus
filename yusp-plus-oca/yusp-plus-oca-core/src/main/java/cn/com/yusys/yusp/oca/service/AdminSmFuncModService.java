package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.bo.AdminSmFuncModBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmFuncModEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmFuncModQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmFuncModVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统功能模块表
 *
 * @author wujp4
 * @email wujp4@yusys.com.cn
 * @date 2020-11-26 10:50:57
 */
public interface AdminSmFuncModService extends IService<AdminSmFuncModEntity> {


    /**
     * 业务功能模块查询
     *
     * @param adminSmFuncModQuery 分页查询参数实体
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 9:58
     */
    Page<AdminSmFuncModVo> queryPageWithCondition(AdminSmFuncModQuery adminSmFuncModQuery);


    /**
     * 批量删除模块
     *
     * @param modId 模块ID
     * @return 删除成功条数
     * @author zhanyq
     * @date 2021-06-25 9:59
     */
    int removeByModId(String[] modId);

    /**
     * 检查模块名称是否存在
     *
     * @param modName 模块名称
     * @param modId   模块ID
     * @return 查询到的模块数据
     * @author zhanyq
     * @date 2021-06-25 10:00
     */
    List<String> checkName(String modName, String modId);


    /**
     * 系统模块保存
     *
     * @param adminSmFuncBo 要保存的数据
     * @return 保存成功条数
     * @author zhanyq
     * @date 2021-06-25 10:01
     */
    int saveFuncMod(AdminSmFuncModBo adminSmFuncBo);


    /**
     * 修改模块数据
     *
     * @param adminSmFuncBo 要修改的模块数据
     * @return 修改成功条数
     * @author zhanyq
     * @date 2021-06-25 10:02
     */
    int updateFuncMod(AdminSmFuncModBo adminSmFuncBo);

}


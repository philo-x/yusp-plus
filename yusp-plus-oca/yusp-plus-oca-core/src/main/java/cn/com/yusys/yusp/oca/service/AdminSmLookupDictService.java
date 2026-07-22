package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.bo.AdminSmLookupDictBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLookupDictEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmLookupDictQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLookupDictVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 数据字典内容表
 *
 * @author danyb1
 * @date 2021-01-15 16:44:33
 */
public interface AdminSmLookupDictService extends IService<AdminSmLookupDictEntity> {


    /**
     * 数据字典 列表分页查询
     * @param adminSmLookupDictQuery 查询条件
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 14:35
     */
    Page<AdminSmLookupDictVo> queryLookupDictWithCondition(AdminSmLookupDictQuery adminSmLookupDictQuery);


    /**
     * 字典项保存
     * @param adminSmLookupDictBo 要保存的数据字典数据
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:36
     */
    void saveLookupDictByDictBo(AdminSmLookupDictBo adminSmLookupDictBo);

    /**
     * 修改字典项
     * @param adminSmLookupDictBo 要修改的数据字典项
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:37
     */
    void updateLookupDictByDictBo(AdminSmLookupDictBo adminSmLookupDictBo);

    /**
     * 批量删除数据字典项
     * @param ids 数据字典ID
     * @return
     * @author zhanyq
     * @date 2021-06-25 14:39
     */
    void removeLookupDictByIds(String[] ids);


    /**
     * 字典详情
     * @param lookupItemId 数据字典项ID
     * @return
     * @author zhanyq
     * @date 2021-06-25 14:40
     */
    List<AdminSmLookupDictVo> queryLookupDictInfoById(String lookupItemId);


    /**
     * 初始化字典分类
     * @return 数据字典分类实体
     * @author zhanyq
     * @date 2021-06-25 14:40
     */
    List<AdminSmLookupDictVo> queryInitLookupDict();


    /**
     * 插入字典项
     * @param adminSmLookupDictBo 要保存的数据字典项
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:41
     */
    void insertLookupDictByDictBo(AdminSmLookupDictBo adminSmLookupDictBo);


    /**
     * 刷新数据字典缓存
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:41
     */
    void refreshLookupDict();


    /**
     * 查询数据字典
     * @param lookupCodes 数据字典Code编码，可以查多个，中间使用英文","分割
     * @return 数据字典查询结果
     * @author zhanyq
     * @date 2021-06-25 14:42
     */
    Map<String, List<Map<String, Object>>> queryLookupCode(String lookupCodes);
}


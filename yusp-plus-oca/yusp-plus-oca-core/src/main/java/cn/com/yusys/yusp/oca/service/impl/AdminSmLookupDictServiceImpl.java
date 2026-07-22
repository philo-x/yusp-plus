package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.common.utils.GenericBuilder;
import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.util.ArrayUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmLookupDictDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmLookupDictBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLookupDictEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmLookupDictQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLookupDictVo;
import cn.com.yusys.yusp.oca.service.AdminSmLookupDictService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.utils.I18nMpCacheKey;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotBlank;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 数据字典业务实现类
 *
 * @author zhanyq
 * @date 2021-06-25 14:44
 */
@Service("adminSmLookupDictService")
public class AdminSmLookupDictServiceImpl extends ServiceImpl<AdminSmLookupDictDao, AdminSmLookupDictEntity> implements AdminSmLookupDictService {


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomCacheService cacheService;

    private static final String LOOKUP_CODE = "lookup_code";

    private static final String LOOKUP_ITEM_ID = "lookup_item_id";

    private static final String UP_LOOKUP_ITEM_ID = "up_lookup_item_id";

    private static final String ERROR_CODE_50500001 = "50500001";

    private static final String ERROR_MESSAGE_50500001 = "字典项不能为空";

    private static final String ERROR_CODE_50500002 = "50500002";

    private static final String LOOKUP_ITEM_NAME = "lookup_item_name";

    private static final String LOOKUP_ITEM_CODE = "lookup_item_code";


    @Override
    public Page<AdminSmLookupDictVo> queryLookupDictWithCondition(AdminSmLookupDictQuery adminSmLookupDictQuery) {

        Page<AdminSmLookupDictEntity> page = adminSmLookupDictQuery.getIPage();
        // 搜索关键字
        String keyWord = adminSmLookupDictQuery.getKeyWord();
        QueryWrapper<AdminSmLookupDictEntity> lookupDictQuery = new QueryWrapper<>();
        lookupDictQuery.select(LOOKUP_ITEM_ID, LOOKUP_CODE, "lookup_name", "lookup_type_id", "lookup_type_name")
                .eq(UP_LOOKUP_ITEM_ID, "-1");
        // 模糊查
        if (StringUtils.nonEmpty(keyWord)) {
            lookupDictQuery.and(wrapper -> wrapper
                    .like(StringUtils.nonEmpty(keyWord), "lookup_name", keyWord)
                    .or()
                    .like(StringUtils.nonEmpty(keyWord), LOOKUP_CODE, keyWord)
                    .or()
                    .like(StringUtils.nonEmpty(keyWord), "lookup_type_name", keyWord)
            );
        }

        lookupDictQuery.orderByDesc("last_chg_dt");
        // 分页查询
        Page<AdminSmLookupDictEntity> dictEntityList = getBaseMapper().selectPage(page, lookupDictQuery);
        // entity 转成 vo类
        List<AdminSmLookupDictVo> dictVos = dictEntityList.getRecords().stream().map(dict -> GenericBuilder.of(AdminSmLookupDictVo::new)
                .with(AdminSmLookupDictVo::setLookupItemId, dict.getLookupItemId())
                .with(AdminSmLookupDictVo::setLookupCode, dict.getLookupCode())
                .with(AdminSmLookupDictVo::setLookupName, dict.getLookupName())
                .with(AdminSmLookupDictVo::setLookupTypeId, dict.getLookupTypeId())
                .with(AdminSmLookupDictVo::setLookupTypeName, dict.getLookupTypeName())
                .with(AdminSmLookupDictVo::setLookupItemsString, "")
                .build()).collect(Collectors.toList());
        // vo ipage 返回
        Page<AdminSmLookupDictVo> iPage = new PageQuery<AdminSmLookupDictVo>().getIPage();
        iPage.setRecords(dictVos);
        iPage.setTotal(page.getTotal());
        iPage.setSize(page.getSize());
        iPage.setPages(page.getPages());

        return iPage;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveLookupDictByDictBo(AdminSmLookupDictBo adminSmLookupDictBo) {

        // bean copy
        AdminSmLookupDictEntity dictEntity = BeanUtils.beanCopy(adminSmLookupDictBo, AdminSmLookupDictEntity.class);
        List<AdminSmLookupDictBo.LookupItemBo> itemBos = adminSmLookupDictBo.getLookupItemBos();

        if (CollectionUtils.isEmpty(itemBos)) {
            throw BizException.error(null, ERROR_CODE_50500001, ERROR_MESSAGE_50500001);
        }

        // 校验 lookupcode lookupname 是否已存在
        Long selectCount = getBaseMapper().selectCount(new QueryWrapper<AdminSmLookupDictEntity>()
                .eq(LOOKUP_CODE, dictEntity.getLookupCode())
        );
        // 校验传入item是否有重复
        Map<@NotBlank(message = "lookupItemCode can not be empty!") String, List<AdminSmLookupDictBo.LookupItemBo>> frontItemCodeMap =
                itemBos.stream().collect(Collectors.groupingBy(AdminSmLookupDictBo.LookupItemBo::getLookupItemCode));
        if (selectCount > 0 || frontItemCodeMap.size() < itemBos.size()) {
            throw BizException.error(null, ERROR_CODE_50500002, "字典代码:" + adminSmLookupDictBo.getLookupCode() + "已存在！");
        }
        List<AdminSmLookupDictEntity> dicts = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        itemBos.stream().forEach(lookupItemBo -> {
            dicts.add(
                    GenericBuilder.of(AdminSmLookupDictEntity::new)
                            .with(AdminSmLookupDictEntity::setLookupItemId, UUID.randomUUID().toString().replace("-", ""))
                            .with(AdminSmLookupDictEntity::setLookupCode, dictEntity.getLookupCode())
                            .with(AdminSmLookupDictEntity::setLookupName, dictEntity.getLookupName())
                            .with(AdminSmLookupDictEntity::setLookupItemCode, lookupItemBo.getLookupItemCode())
                            .with(AdminSmLookupDictEntity::setLookupItemName, lookupItemBo.getLookupItemName())
                            .with(AdminSmLookupDictEntity::setLookupItemOrder, atomicInteger.getAndAdd(1))
                            .with(AdminSmLookupDictEntity::setLookupTypeId, dictEntity.getLookupTypeId())
                            .with(AdminSmLookupDictEntity::setLookupTypeName, dictEntity.getLookupTypeName())
                            .with(AdminSmLookupDictEntity::setUpLookupItemId, uuid)
                            .build()
            );
        });
        dicts.add(GenericBuilder.of(AdminSmLookupDictEntity::new)
                .with(AdminSmLookupDictEntity::setLookupItemId, uuid)
                .with(AdminSmLookupDictEntity::setLookupCode, dictEntity.getLookupCode())
                .with(AdminSmLookupDictEntity::setLookupName, dictEntity.getLookupName())
                .with(AdminSmLookupDictEntity::setLookupItemCode, "0")
                .with(AdminSmLookupDictEntity::setLookupItemName, "0")
                .with(AdminSmLookupDictEntity::setLookupItemOrder, 0)
                .with(AdminSmLookupDictEntity::setLookupTypeId, dictEntity.getLookupTypeId())
                .with(AdminSmLookupDictEntity::setLookupTypeName, dictEntity.getLookupTypeName())
                .with(AdminSmLookupDictEntity::setUpLookupItemId, "-1")
                .build()
        );
        this.saveBatch(dicts);
        cacheService.clear(I18nMpCacheKey.DATA_DICT_CACHE_KEY);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateLookupDictByDictBo(AdminSmLookupDictBo adminSmLookupDictBo) {

        // bean copy
        AdminSmLookupDictEntity dictEntity = BeanUtils.beanCopy(adminSmLookupDictBo, AdminSmLookupDictEntity.class);
        List<AdminSmLookupDictBo.LookupItemBo> itemBos = adminSmLookupDictBo.getLookupItemBos();
        if (CollectionUtils.isEmpty(itemBos)) {
            throw BizException.error(null, ERROR_CODE_50500001, ERROR_MESSAGE_50500001);
        }
        Map<@NotBlank(message = "lookupItemCode can not be empty!") String, List<AdminSmLookupDictBo.LookupItemBo>> frontItemCodeMap =
                itemBos.stream().collect(Collectors.groupingBy(AdminSmLookupDictBo.LookupItemBo::getLookupItemCode));
        if (frontItemCodeMap.size() < itemBos.size()) {
            throw BizException.error(null, ERROR_CODE_50500002, "字典项不能重复!");
        }

        List<AdminSmLookupDictEntity> dicts = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        itemBos.forEach(lookupItemBo -> dicts.add(
                GenericBuilder.of(AdminSmLookupDictEntity::new)
                        .with(AdminSmLookupDictEntity::setLookupItemId, lookupItemBo.getLookupItemId())
                        .with(AdminSmLookupDictEntity::setLookupCode, dictEntity.getLookupCode())
                        .with(AdminSmLookupDictEntity::setLookupName, dictEntity.getLookupName())
                        .with(AdminSmLookupDictEntity::setLookupItemCode, lookupItemBo.getLookupItemCode())
                        .with(AdminSmLookupDictEntity::setUpLookupItemId, dictEntity.getLookupItemId())
                        .with(AdminSmLookupDictEntity::setLookupItemName, lookupItemBo.getLookupItemName())
                        .with(AdminSmLookupDictEntity::setLookupTypeId, adminSmLookupDictBo.getLookupTypeId())
                        .with(AdminSmLookupDictEntity::setLookupTypeName, dictEntity.getLookupTypeName())
                        .with(AdminSmLookupDictEntity::setLookupItemOrder, atomicInteger.getAndAdd(1))
                        .build()
        ));
        dicts.add(GenericBuilder.of(AdminSmLookupDictEntity::new)
                .with(AdminSmLookupDictEntity::setLookupItemId, dictEntity.getLookupItemId())
                .with(AdminSmLookupDictEntity::setLookupCode, dictEntity.getLookupCode())
                .with(AdminSmLookupDictEntity::setLookupName, dictEntity.getLookupName())
                .with(AdminSmLookupDictEntity::setLookupItemCode, "0")
                .with(AdminSmLookupDictEntity::setLookupItemName, "0")
                .with(AdminSmLookupDictEntity::setLookupItemOrder, 0)
                .with(AdminSmLookupDictEntity::setLookupTypeId, dictEntity.getLookupTypeId())
                .with(AdminSmLookupDictEntity::setLookupTypeName, dictEntity.getLookupTypeName())
                .with(AdminSmLookupDictEntity::setUpLookupItemId, "-1")
                .build()
        );
        // 清除之前保存的记录
        getBaseMapper().delete(new QueryWrapper<AdminSmLookupDictEntity>()
                .eq(LOOKUP_ITEM_ID, dictEntity.getLookupItemId())
                .or()
                .eq(UP_LOOKUP_ITEM_ID, dictEntity.getLookupItemId())
        );
        // 批量重新保存
        this.saveBatch(dicts);
        // 删除redis缓存,如果删除失败，手动刷新缓存
        cacheService.clear(I18nMpCacheKey.DATA_DICT_CACHE_KEY);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void removeLookupDictByIds(String[] lookupDictIds) {
        // 删除数据字典
        getBaseMapper().deleteBatchIds(Arrays.asList(lookupDictIds));
        // 删除上级数据字典
        getBaseMapper().delete(new QueryWrapper<AdminSmLookupDictEntity>()
                .in(!ArrayUtils.isEmpty(lookupDictIds), UP_LOOKUP_ITEM_ID, Arrays.asList(lookupDictIds))
        );
        // 删除redis缓存,如果删除失败，手动刷新缓存
        // 清空缓存
        cacheService.clear(I18nMpCacheKey.DATA_DICT_CACHE_KEY);
    }


    @Override
    public List<AdminSmLookupDictVo> queryLookupDictInfoById(String lookupItemId) {

        List<AdminSmLookupDictEntity> itemIds = getBaseMapper().selectList(new QueryWrapper<AdminSmLookupDictEntity>()
                .select(LOOKUP_ITEM_ID, LOOKUP_ITEM_CODE, LOOKUP_ITEM_NAME)
                .eq(StringUtils.nonEmpty(lookupItemId), UP_LOOKUP_ITEM_ID, lookupItemId).orderByAsc("lookup_item_order")
        );
        List<AdminSmLookupDictVo> adminSmLookupDictVos = itemIds.stream().filter(adminSmLookupDictEntity -> adminSmLookupDictEntity != null).map(
                dict -> GenericBuilder.of(AdminSmLookupDictVo::new)
                        .with(AdminSmLookupDictVo::setLookupItemId, dict.getLookupItemId())
                        .with(AdminSmLookupDictVo::setLookupItemCode, dict.getLookupItemCode())
                        .with(AdminSmLookupDictVo::setLookupItemName, dict.getLookupItemName())
                        .build()
        ).collect(Collectors.toList());

        return adminSmLookupDictVos;
    }


    @Override
    public List<AdminSmLookupDictVo> queryInitLookupDict() {

        List<AdminSmLookupDictEntity> dictList = getBaseMapper().selectList(new QueryWrapper<AdminSmLookupDictEntity>()
                .select(LOOKUP_ITEM_ID, LOOKUP_ITEM_CODE, LOOKUP_ITEM_NAME)
                .eq(UP_LOOKUP_ITEM_ID, "COMMON_INIT_PARAM")
        );
        List<AdminSmLookupDictVo> adminSmLookupDictVos = dictList.stream().filter(adminSmLookupDictEntity -> adminSmLookupDictEntity != null).map(
                dict -> GenericBuilder.of(AdminSmLookupDictVo::new)
                        .with(AdminSmLookupDictVo::setLookupItemId, dict.getLookupItemId())
                        .with(AdminSmLookupDictVo::setLookupItemCode, dict.getLookupItemCode())
                        .with(AdminSmLookupDictVo::setLookupItemName, dict.getLookupItemName())
                        .build()
        ).collect(Collectors.toList());

        return adminSmLookupDictVos;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void insertLookupDictByDictBo(AdminSmLookupDictBo adminSmLookupDictBo) {
        // bean copy
        AdminSmLookupDictEntity dictEntity = BeanUtils.beanCopy(adminSmLookupDictBo, AdminSmLookupDictEntity.class);
        List<AdminSmLookupDictBo.LookupItemBo> itemBos = adminSmLookupDictBo.getLookupItemBos();
        if (CollectionUtils.isEmpty(itemBos)) {
            throw BizException.error(null, ERROR_CODE_50500001, ERROR_MESSAGE_50500001);
        }
        List<AdminSmLookupDictEntity> insertdicts = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Map<@NotBlank(message = "lookupItemCode can not be empty!") String, List<AdminSmLookupDictBo.LookupItemBo>> frontItemCodeMap =
                itemBos.stream().collect(Collectors.groupingBy(AdminSmLookupDictBo.LookupItemBo::getLookupItemCode));
        if (frontItemCodeMap.size() < itemBos.size()) {
            throw BizException.error(null, ERROR_CODE_50500002, "字典项不能重复!");
        }
        // 删除原有的字典项
        Map param = new HashMap<String, Object>(1);
        param.put(LOOKUP_CODE, adminSmLookupDictBo.getLookupCode());
        getBaseMapper().deleteByMap(param);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        itemBos.stream().forEach(lookupItemBo -> {
            insertdicts.add(
                    GenericBuilder.of(AdminSmLookupDictEntity::new)
                            .with(AdminSmLookupDictEntity::setLookupItemId, UUID.randomUUID().toString().replace("-", ""))
                            .with(AdminSmLookupDictEntity::setLookupCode, dictEntity.getLookupCode())
                            .with(AdminSmLookupDictEntity::setLookupName, dictEntity.getLookupName())
                            .with(AdminSmLookupDictEntity::setLookupItemCode, lookupItemBo.getLookupItemCode())
                            .with(AdminSmLookupDictEntity::setLookupItemName, lookupItemBo.getLookupItemName())
                            .with(AdminSmLookupDictEntity::setLookupItemOrder, atomicInteger.addAndGet(1))
                            .with(AdminSmLookupDictEntity::setLookupItemOrder, itemBos.indexOf(lookupItemBo))
                            .with(AdminSmLookupDictEntity::setLookupTypeId, dictEntity.getLookupTypeId())
                            .with(AdminSmLookupDictEntity::setLookupTypeName, dictEntity.getLookupTypeName())
                            .with(AdminSmLookupDictEntity::setUpLookupItemId, uuid)
                            .build()
            );
        });
        insertdicts.add(GenericBuilder.of(AdminSmLookupDictEntity::new)
                .with(AdminSmLookupDictEntity::setLookupItemId, uuid)
                .with(AdminSmLookupDictEntity::setLookupCode, dictEntity.getLookupCode())
                .with(AdminSmLookupDictEntity::setLookupName, dictEntity.getLookupName())
                .with(AdminSmLookupDictEntity::setLookupItemCode, "0")
                .with(AdminSmLookupDictEntity::setLookupItemName, "0")
                .with(AdminSmLookupDictEntity::setLookupItemOrder, 0)
                .with(AdminSmLookupDictEntity::setLookupTypeId, dictEntity.getLookupTypeId())
                .with(AdminSmLookupDictEntity::setLookupTypeName, dictEntity.getLookupTypeName())
                .with(AdminSmLookupDictEntity::setUpLookupItemId, "-1")
                .build()
        );
        this.saveBatch(insertdicts);
        cacheService.clear(I18nMpCacheKey.DATA_DICT_CACHE_KEY);
    }


    @Override
    public void refreshLookupDict() {

        List<AdminSmLookupDictEntity> list = getBaseMapper().selectList(new QueryWrapper<AdminSmLookupDictEntity>()
                .select(LOOKUP_CODE, LOOKUP_ITEM_CODE, LOOKUP_ITEM_NAME)
                .ne(UP_LOOKUP_ITEM_ID, "-1"));
        // 使用hash类型存储字典 例如: redisKey -> datadict  HashKey -> 1 HashValue -> 男
        Map<String, List<AdminSmLookupDictEntity>> dictMap = list.stream().collect(Collectors.groupingBy(AdminSmLookupDictEntity::getLookupCode));
        Map<String, String> dictMaps = this.dict2HashStore(dictMap);
        // 删除缓存
        cacheService.clear(I18nMpCacheKey.DATA_DICT_CACHE_KEY);
        // 重新设置缓存
        cacheService.hashPut(I18nMpCacheKey.DATA_DICT_CACHE_KEY, dictMaps, -1);
    }


    /**
     * Map<String,List> 转 Map<String,String>
     *
     * @param dictMap 数据字典map
     * @return 转换后的数据字典
     * @author zhanyq
     * @date 2021-06-25 14:47
     */
    private Map<String, String> dict2HashStore(Map<String, List<AdminSmLookupDictEntity>> dictMap) {

        Map<String, String> dictMaps = new HashMap<>(dictMap.size());
        for (Map.Entry<String, List<AdminSmLookupDictEntity>> entry : dictMap.entrySet()) {
            StringBuilder jsonStr = new StringBuilder("[");
            for (AdminSmLookupDictEntity dict : entry.getValue()) {
                jsonStr.append("{" + "\"key\"" + ":\"" + dict.getLookupItemCode() + "\",");
                jsonStr.append("\"value\"" + ":\"" + dict.getLookupItemName() + "\"},");
            }
            String dictStr = jsonStr.substring(0, jsonStr.lastIndexOf(","));
            dictMaps.put(entry.getKey(), dictStr + "]");
        }
        return dictMaps;
    }


    @Override
    public Map<String, List<Map<String, Object>>> queryLookupCode(String lookupCodes) {
        List<String> codes = Arrays.asList(lookupCodes.replaceAll("^,*|,*$", "").split(","));
        Map<String, List<Map<String, Object>>> dictMap = new HashMap<>(codes.size());
        codes.stream().forEach(code -> {
            String val = cacheService.hashSingleKeyGet(I18nMpCacheKey.DATA_DICT_CACHE_KEY, code);
            if (!StringUtils.isEmpty(val)) {
                List<Map<String, Object>> maps = this.readValue(val);
                dictMap.put(code, maps);
            }
        });
        // redis 如果无数据，或只查询到部分数据，查询数据库，并存储到redis
        if (CollectionUtils.isEmpty(dictMap) || (codes.size() != dictMap.size())) {
            dictMap.clear();
            List<AdminSmLookupDictEntity> dictEntityListlist = getBaseMapper().selectList(new QueryWrapper<AdminSmLookupDictEntity>()
                    .select(LOOKUP_CODE, LOOKUP_ITEM_CODE, LOOKUP_ITEM_NAME)
                    .ne(UP_LOOKUP_ITEM_ID, "-1")
                    .in(LOOKUP_CODE, codes).orderByAsc("lookup_item_order")
            );
            codes.stream().forEach(lookupCode -> {
                        List<Map<String, Object>> keyValuelist = new ArrayList<>();
                        dictEntityListlist.stream().forEach(dictEntity -> {
                                    if (lookupCode.equals(dictEntity.getLookupCode())) {
                                        HashMap<String, Object> dictMaps = new HashMap<>(2);
                                        dictMaps.put("key", dictEntity.getLookupItemCode());
                                        dictMaps.put("value", dictEntity.getLookupItemName());
                                        keyValuelist.add(dictMaps);
                                    }
                                }
                        );
                        dictMap.put(lookupCode, keyValuelist);
                    }
            );
            // 分组存储到redis
            Map<String, List<AdminSmLookupDictEntity>> dictStrListMap = dictEntityListlist.stream().
                    collect(Collectors.groupingBy(AdminSmLookupDictEntity::getLookupCode));
            Map<String, String> redisHashStore = this.dict2HashStore(dictStrListMap);
            // 添加到缓存
            cacheService.hashPut(I18nMpCacheKey.DATA_DICT_CACHE_KEY, redisHashStore, -1);
        }

        return dictMap;
    }


    /**
     * json反序列化
     *
     * @param data json格式的字典项
     * @author zhanyq
     * @date 2021-06-25 14:49
     */
    private List<Map<String, Object>> readValue(String data) {
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Map.class);
            return objectMapper.readValue(data, listType);
        } catch (Exception e) {
            throw BizException.error(null, "50500003", "readValue Error");
        }
    }

}
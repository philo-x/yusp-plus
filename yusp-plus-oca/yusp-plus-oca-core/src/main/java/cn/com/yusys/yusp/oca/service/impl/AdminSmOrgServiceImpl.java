package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.excelcsv.ExcelUtils;
import cn.com.yusys.yusp.commons.excelcsv.FileExportPostProcessor;
import cn.com.yusys.yusp.commons.excelcsv.async.ExportContext;
import cn.com.yusys.yusp.commons.excelcsv.async.ImportContext;
import cn.com.yusys.yusp.commons.excelcsv.model.DataAcquisition;
import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.file.FileInfo;
import cn.com.yusys.yusp.commons.file.util.FileInfoUtils;
import cn.com.yusys.yusp.commons.progress.model.ProgressDto;
import cn.com.yusys.yusp.commons.session.user.User;
import cn.com.yusys.yusp.commons.session.user.UserIdentity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.*;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.oca.constants.OcaCommonConstants;
import cn.com.yusys.yusp.oca.dao.AdminSmOrgDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.CacheEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmOrgExtQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmOrgTreeQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgExcelVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgVo;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import cn.com.yusys.yusp.oca.service.AdminSmUserMgrOrgService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统机构表
 *
 * @author terry
 * @date 2020-11-27 18:06:35
 */
@Service("adminSmOrgService")
public class AdminSmOrgServiceImpl extends ServiceImpl<AdminSmOrgDao, AdminSmOrgEntity> implements AdminSmOrgService {

    private static final String ORG_ID_NOT_EMPTY = "orgId can not be empty!";

    @Autowired
    private AdminSmUserMgrOrgService adminSmUserMgrOrgService;

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    private CustomCacheService cacheService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 根据ID梳理ORG_SEQ
     */
    private static final int ORG_SEQ_BY_ID = 1;

    /**
     * 根据ORG_CODE梳理ORG_SEQ
     */
    private static final int ORG_SEQ_BY_CODE = 2;

    private static final String ROOT_ID = "1";

    @Override
    public Page<AdminSmOrgVo> queryPage(AdminSmOrgExtQuery query) {
        if (StringUtils.isEmpty(query.getUpOrgId())) {
            query.setUpOrgId(SessionUtils.getUserOrganizationId());
        }

        Page<AdminSmOrgVo> page = query.getIPage();

        LambdaQueryWrapper<AdminSmOrgEntity> queryWrapper = new LambdaQueryWrapper<>();

        // 机构名模糊匹配
        queryWrapper.like(StringUtils.nonEmpty(query.getOrgName()), AdminSmOrgEntity::getOrgName, query.getOrgName());
        //机构编码模糊匹配
        queryWrapper.like(StringUtils.nonEmpty(query.getOrgCode()), AdminSmOrgEntity::getOrgCode, query.getOrgCode());
        //机构状态精准匹配
        queryWrapper.eq(Objects.nonNull(query.getOrgSts()), AdminSmOrgEntity::getOrgSts, query.getOrgSts());

        if (StringUtils.nonEmpty(query.getKeyWord())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(StringUtils.nonEmpty(query.getKeyWord()), AdminSmOrgEntity::getOrgCode, query.getKeyWord())//关键字模糊匹配code
                    .or()
                    .like(StringUtils.nonEmpty(query.getKeyWord()), AdminSmOrgEntity::getOrgName, query.getKeyWord())//关键字模糊匹配名称
            );
        }

        AdminSmOrgEntity adminSmOrgEntity = this.getById(query.getUpOrgId());
        //机构序列右匹配
        queryWrapper.likeRight(AdminSmOrgEntity::getOrgSeq, adminSmOrgEntity.getOrgSeq());
        queryWrapper.orderByDesc(AdminSmOrgEntity::getLastChgDt);

        //查询OrgId对应机构及下级机构
        return getBaseMapper().selectOrgAccessible(page, queryWrapper);
    }

    @Override
    public AdminSmOrgDetailVo getDetailById(String orgId) {
        Asserts.nonBlank(orgId, "orgId cannot be null or empty!!");
        return getBaseMapper().getDetailById(orgId);
    }

    @Override
    public List<AdminSmOrgVo> getAllProgeny(String orgId) {
        AdminSmOrgExtQuery query = new AdminSmOrgExtQuery();
        query.setUpOrgId(orgId);
        return this.getAllProgeny(query);
    }

    @Override
    public List<AdminSmOrgVo> getAllProgeny(AdminSmOrgExtQuery query) {
        //参数准备
        if (StringUtils.isEmpty(query.getUpOrgId())) {
            query.setUpOrgId(SessionUtils.getUserOrganizationId());
        }
        // STEP 1 全表查询所有机构
        // 前端传的sort字段无效
        List<AdminSmOrgVo> allExtNodes = getBaseMapper().selectAllOrgExtVo(query.getSort());
        // STEP 2 依据rootId获取第一代子节点列表
        //字段可能为空字符串
        if (StringUtils.isEmpty(query.getUpOrgId())) {
            // 机构id为空时取虚拟根机构节点id
            query.setUpOrgId("1");
        }
        final String rootId = query.getUpOrgId();
        List<AdminSmOrgVo> firstGeneration = allExtNodes.stream().filter((item) -> item.getUpOrgId().equals(rootId)).collect(Collectors.toList());
        // STEP 3 递归将所有子孙节点加入到allProgeny中(包含firstGeneration)
        final List<AdminSmOrgVo> allProgeny = new ArrayList<>();
        firstGeneration.forEach((son) -> addToResult(son, allProgeny, allExtNodes));
        // 按日期降序
        List<AdminSmOrgVo> sortedProgeny = allProgeny.stream().sorted((node1, node2) -> DateUtils.compare(node2.getLastChgDt(), node1.getLastChgDt())).collect(Collectors.toList());
        // STEP 4 把自己也加进结果集(避免参数错误，排除机构号为1的虚拟根节点)
        if (!ROOT_ID.equals(rootId)) {
            AdminSmOrgVo self = allExtNodes.stream().filter((item) -> rootId.equals(item.getOrgId())).collect(Collectors.toList()).get(0);
            Optional.ofNullable(self).ifPresent((item) -> sortedProgeny.add(0, item));
        }
        return sortedProgeny;
    }

    /**
     * @param parent      当前父节点
     * @param result      待操作结果集合
     * @param allExtNodes 全集（数据源）
     */
    private void addToResult(AdminSmOrgVo parent, List<AdminSmOrgVo> result, List<AdminSmOrgVo> allExtNodes) {
        Optional.ofNullable(parent).ifPresent(result::add);
        List<AdminSmOrgVo> children = allExtNodes.stream().filter((item) -> item.getUpOrgId().equals(parent.getOrgId())).collect(Collectors.toList());
        children.forEach((child) -> addToResult(child, result, allExtNodes));
    }


    @Override
    public List<String> getAllAccessibleOrgIds(String userId) {
        AdminSmUserEntity user = adminSmUserService.getById(userId);
        // 用户所在机构以及所有子孙机构
        List<AdminSmOrgVo> allProgeny = this.getAllProgeny(user.getOrgId());

        // 用户所有授权机构列表
        List<String> relOrgIds = this.adminSmUserMgrOrgService.findOrgRelsByUser(userId);
        List<String> orgIds = allProgeny.stream().map(AdminSmOrgVo::getOrgId).collect(Collectors.toList());
        // 合并两个集合的orgId
        orgIds.addAll(relOrgIds);
        return orgIds;
    }

    @Override
    public List<String> getAllAccessibleOrgIds() {
        return this.getAllAccessibleOrgIds(SessionUtils.getUserId());
    }

    @Override
    public Set<AdminSmOrgTreeNodeBo> getAllAncestryOrgs(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            orgId = SessionUtils.getUserOrganizationId();
        }
        Asserts.nonBlank(orgId, ORG_ID_NOT_EMPTY);
        //这里的children属性其实是当前节点的父级节点列表
        AdminSmOrgTreeNodeBo leaf = getBaseMapper().getAllAncestryOrgs(orgId);
        Set<AdminSmOrgTreeNodeBo> res = new HashSet<>();
        addToResult(leaf, res);
        res.forEach((item) -> item.setChildren(null));
        return res;
    }

    private void addToResult(AdminSmOrgTreeNodeBo currentNode, Set<AdminSmOrgTreeNodeBo> res) {
        res.add(currentNode);
        List<AdminSmOrgTreeNodeBo> list = currentNode.getChildren();
        if (Objects.nonNull(list) && list.size() > 0) {
            addToResult(list.get(0), res);
        }
    }

    @Override
    public AdminSmOrgEntity getParentOrg(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            orgId = SessionUtils.getUserOrganizationId();
        }
        AdminSmOrgEntity self = getById(orgId);
        return getById(self.getUpOrgId());
    }

    @Override
    public Set<AdminSmOrgEntity> getSiblingOrgs(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            orgId = SessionUtils.getUserOrganizationId();
        }
        AdminSmOrgEntity self = getById(orgId);
        LambdaQueryWrapper<AdminSmOrgEntity> wrapper = new QueryWrapper<AdminSmOrgEntity>().lambda();
        wrapper.eq(AdminSmOrgEntity::getUpOrgId, self.getUpOrgId());
        List<AdminSmOrgEntity> siblingOrgs = getBaseMapper().selectList(wrapper);
        Asserts.nonEmpty(siblingOrgs, "orgId: " + self.getUpOrgId() + " does not exist!");
        return new HashSet<>(siblingOrgs);
    }

    @Override
    public Page<AdminSmOrgVo> getOrgsForWf(AdminSmOrgExtQuery query) {
        Page<AdminSmOrgVo> page = query.getIPage();
        QueryWrapper<AdminSmOrgVo> wrapper = new QueryWrapper<>();
        wrapper.eq("T1.ORG_STS", "A");
        creatWrapper(wrapper, "T1.UP_ORG_ID", query.getUpOrgId());
        creatWrapper(wrapper, "T1.ORG_CODE", query.getOrgCode());
        creatWrapper(wrapper, "T1.ORG_NAME", query.getOrgName());
        return getBaseMapper().getOrgsForWf(page, wrapper);
    }

    private void creatWrapper(QueryWrapper<AdminSmOrgVo> wrapper, String column, String value) {
        if (StringUtils.nonEmpty(value)) {
            boolean condition = value.startsWith("%") || value.endsWith("%");
            wrapper.like(condition, column, value);
            wrapper.eq(!condition, column, value);
        }
    }

    @Override
    public List<String> getLowerOrgId(String orgCode) {
        QueryWrapper<AdminSmOrgVo> wrapper = new QueryWrapper<>();
        List<AdminSmOrgVo> orgList = getBaseMapper().getChildOrgCode(wrapper);
        List<String> firstChilds = Collections.singletonList(orgCode);
        List<String> allChilds = new ArrayList<>();
        Map<String, String> familyNameList = orgList.stream().sorted(Comparator.comparing(AdminSmOrgVo::getOrgLevel)).collect(Collectors.toMap(AdminSmOrgVo::getOrgId, AdminSmOrgVo::getUpOrgId, (existing, replacement) -> existing));
        if (familyNameList.get(orgCode) == null) {
            return allChilds;
        } else {
            getChild(familyNameList, firstChilds, allChilds);
        }
        return allChilds;
    }

    @Override
    public void updateOrgSeqForTask(int level, boolean flag) {
        QueryWrapper<AdminSmOrgEntity> queryWrapper = new QueryWrapper<>();
        if (!flag) {
            queryWrapper.isNull("a.org_seq");
        }
        queryWrapper.eq("a.org_level", level);
        List<AdminSmOrgEntity> adminSmOrgEntities = getBaseMapper().getOrgSeqForUpdate(queryWrapper);
        if (CollectionUtils.nonEmpty(adminSmOrgEntities)) {
            adminSmOrgEntities.forEach(entity -> {
                String parentOrgSeq = StringUtils.isEmpty(entity.getOrgSeq()) ? "" : entity.getOrgSeq();
                entity.setOrgSeq(parentOrgSeq + entity.getOrgCode() + ",");
            });
            this.updateBatchById(adminSmOrgEntities);
        }

    }

    private void getChild(Map<String, String> familyNameList, List<String> checkPeoples, List<String> allChilds) {
        allChilds.addAll(checkPeoples);
        List<String> nextCheckPeoples = new ArrayList<>();
        Map<String, String> familyNameListCopy = new HashMap<>(familyNameList.size());
        //map不让动态修改。。。那就copy一个吧, 嗯 好办法
        familyNameListCopy.putAll(familyNameList);
        familyNameListCopy.forEach((people, parent) -> {
            if (checkPeoples.contains(parent)) {
                nextCheckPeoples.add(people);
                //要保证一个org只能有一个upOrg，有多个要注掉remove这句
                familyNameList.remove(people);
            }
        });
        if (nextCheckPeoples.size() > 0) {
            getChild(familyNameList, nextCheckPeoples, allChilds);
        }
    }

    public AdminSmOrgEntity getById(String orgId) {
        Asserts.nonBlank(orgId, ORG_ID_NOT_EMPTY);
        AdminSmOrgEntity res = super.getById(orgId);
        Asserts.nonNull(res, "orgId:" + orgId + " does not exist!");
        return res;
    }


    @Override
    public List<AdminSmOrgTreeNodeBo> getOrgTree(AdminSmOrgTreeQuery query) {
        //参数准备
        if (StringUtils.isEmpty(query.getOrgId())) {
            query.setOrgId(SessionUtils.getUserOrganizationId());
        }
        //STEP 1 用户所在机构组成的机构树
        List<AdminSmOrgTreeNodeBo> rootList = getOrgTreeByOrgId(query.getOrgId(), query.getOrgSts());
        List<AdminSmOrgTreeNodeBo> resList = new ArrayList<>(rootList);
        //STEP 2  用户授权机构构成的机构树
        if (StringUtils.nonEmpty(SessionUtils.getUserId())) {
            // 用户授权机构rel列表
            List<String> relOrgIds = adminSmUserMgrOrgService.findOrgRelsByUser(SessionUtils.getUserId());

            if (!relOrgIds.isEmpty()) {
                // 按状态过滤后的所有机构id
                List<String> filteredOrgIds = getAllOrgEntities(query.getOrgSts()).stream().map(AdminSmOrgEntity::getOrgId).toList();
                // 提取用户授权机构id列表
                List<String> userMgrOrgIds = relOrgIds.stream().filter(filteredOrgIds::contains).collect(Collectors.toList());
                // 从用户授权机构列表中排除当前登录用户所在机构（理论上没有这样的数据，万一呢？）
                userMgrOrgIds.remove(SessionUtils.getUserOrganizationId());
                List<AdminSmOrgTreeNodeBo> orgNodeList = this.listToTree(userMgrOrgIds);
                resList.addAll(0, orgNodeList);
            }
        }
        return resList;
    }

    /**
     * 以指定机构ID为根节点获取机构树
     *
     * @param orgId 必输
     * @return 机构树
     */
    private List<AdminSmOrgTreeNodeBo> getOrgTreeByOrgId(String orgId, AvailableStateEnum state) {
        Asserts.nonBlank(orgId, ORG_ID_NOT_EMPTY);
        // STEP 1 全表查询所有机构
        List<AdminSmOrgEntity> originList = getAllOrgEntities(state);
        // entity-》treeNode
        List<AdminSmOrgTreeNodeBo> allNodes = (List<AdminSmOrgTreeNodeBo>) BeanUtils.beansCopy(originList, AdminSmOrgTreeNodeBo.class);
        // STEP 2 以 orgId 指代的机构作为根节点
        List<AdminSmOrgTreeNodeBo> rootList = allNodes.stream().filter((node) -> node.getOrgId().equals(orgId)).collect(Collectors.toList());
        Asserts.nonEmpty(rootList, "orgId:" + orgId + " is not exist!!");
        this.appendChildren(rootList.get(0), allNodes, null);
        return rootList;
    }

    @Override
    public List<AdminSmOrgTreeNodeBo> getOrgTreeByOrgId(String orgId) {
        return getOrgTreeByOrgId(orgId, AvailableStateEnum.ENABLED);
    }


    private List<AdminSmOrgEntity> getAllOrgEntities(AvailableStateEnum state) {
        List<AdminSmOrgEntity> allOrgEntities = this.getAllOrgEntities();
        return Objects.nonNull(state) ? allOrgEntities.stream().filter(org -> org.getOrgSts().equals(state)).collect(Collectors.toList()) : allOrgEntities;
    }


    private List<AdminSmOrgTreeNodeBo> listToTree(List<String> orgIds) {
        List<AdminSmOrgTreeNodeBo> res = new ArrayList<>();
        List<AdminSmOrgEntity> sortedUserMgrOrgEntitys = this.lambdaQuery()
                .select()
                .in(AdminSmOrgEntity::getOrgId, orgIds)
                // 按级别排序
                .orderByAsc(AdminSmOrgEntity::getOrgLevel)
                .list();
        do {
            AdminSmOrgTreeNodeBo left = BeanUtils.beanCopy(sortedUserMgrOrgEntitys.get(0), AdminSmOrgTreeNodeBo.class);
            sortedUserMgrOrgEntitys = initChildren(left, sortedUserMgrOrgEntitys);
            res.add(left);
        } while (sortedUserMgrOrgEntitys.size() != 0);
        return res;
    }

    /**
     * 从origin中筛选出left的children，并从origin中删除这些child
     *
     * @param left   节点实体类
     * @param origin 机构列表
     */
    private List<AdminSmOrgEntity> initChildren(AdminSmOrgTreeNodeBo left, List<AdminSmOrgEntity> origin) {
        List<AdminSmOrgEntity> entityChildren = origin.stream()
                .filter(entity -> entity.getUpOrgId().equals(left.getOrgId()))
                .collect(Collectors.toList());
        List<AdminSmOrgTreeNodeBo> nodeChildren = entityChildren.stream().map(entity -> BeanUtils.beanCopy(entity, AdminSmOrgTreeNodeBo.class)).collect(Collectors.toList());
        left.setChildren(nodeChildren);
        //删除自身
        origin.removeIf(entity -> entity.getOrgId().equals(left.getOrgId()));
        //删除子节点
        origin.removeAll(entityChildren);
        nodeChildren.forEach(child -> initChildren(child, origin));
        return origin;
    }

    @Override
    public List<AdminSmOrgTreeNodeBo> getOrgTrees(List<String> orgIds, AvailableStateEnum orgSts, String... exceptOrgIds) {
        if (orgIds.size() > 0) {
            String userOrgId = SessionUtils.getUserOrganizationId();
            AdminSmOrgEntity entity = getBaseMapper().selectById(userOrgId);
            String orgSeq = entity.getOrgSeq();

            List<AdminSmOrgTreeNodeBo> res = new ArrayList<>();
            // STEP 1 全表查询所有机构
            LambdaQueryWrapper<AdminSmOrgEntity> wrapper = Wrappers.lambdaQuery();
            Optional.ofNullable(orgSts).ifPresent((sts) -> wrapper.eq(AdminSmOrgEntity::getOrgSts, sts));
            wrapper.likeRight(StringUtils.nonEmpty(orgSeq), AdminSmOrgEntity::getOrgSeq, orgSeq);
            wrapper.orderByDesc(AdminSmOrgEntity::getLastChgDt);
            List<AdminSmOrgEntity> originList = getBaseMapper().selectList(wrapper);
            // entity-》treeNode
            List<AdminSmOrgTreeNodeBo> allNodes = (List<AdminSmOrgTreeNodeBo>) BeanUtils.beansCopy(originList, AdminSmOrgTreeNodeBo.class);
            // STEP 2 遍历orgId
            orgIds.forEach(orgId -> {
                List<AdminSmOrgTreeNodeBo> rootList = allNodes.stream().filter((node) -> node.getOrgId().equals(orgId)).collect(Collectors.toList());
                if (rootList.size() > 0) {
                    this.appendChildren(rootList.get(0), allNodes, exceptOrgIds);
                    res.add(rootList.get(0));
                }
            });
            return res;
        }
        return new ArrayList<>();
    }

    private void appendChildren(AdminSmOrgTreeNodeBo parent, List<AdminSmOrgTreeNodeBo> all, String[] exceptOrgIds) {
        List<AdminSmOrgTreeNodeBo> children = all.stream().filter((node) -> node.getUpOrgId().equals(parent.getOrgId())).collect(Collectors.toList());
        if (exceptOrgIds != null && exceptOrgIds.length > 0) {
            //排除指定节点，不加入结果集
            children.removeIf((node) -> Arrays.asList(exceptOrgIds).contains(node.getOrgId()));
        }
        parent.setChildren(children);
        children.forEach((child) -> appendChildren(child, all, exceptOrgIds));
    }


    @Override
    public boolean save(AdminSmOrgEntity entity) {

        this.checkOrgCodeIsExist(entity);
        AdminSmOrgEntity parent = getById(entity.getUpOrgId());
        entity.setInstuId(parent.getInstuId());
        entity.setOrgLevel(parent.getOrgLevel() + 1);
        // 新增的数据默认是待启用的
        entity.setOrgSts(Optional.ofNullable(entity.getOrgSts()).orElse(AvailableStateEnum.UNENABLED));
        // 设置org_seq
        setOrgSeq(entity);
        boolean result = getBaseMapper().insert(entity) > 0;
        // 更新缓存
        refreshCache(entity);

        return result;
    }

    private void checkOrgCodeIsExist(AdminSmOrgEntity entity) {
        LambdaQueryWrapper<AdminSmOrgEntity> codeWrapper = new QueryWrapper<AdminSmOrgEntity>().lambda();
        codeWrapper.eq(AdminSmOrgEntity::getOrgCode, entity.getOrgCode());
        AdminSmOrgEntity check = getBaseMapper().selectOne(codeWrapper);
        if (Objects.nonNull(check)) {
            throw BizException.error("exist", "50700001", entity.getOrgCode());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEnable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                AdminSmOrgEntity entity = new AdminSmOrgEntity();
                entity.setOrgId(id);
                entity.setOrgSts(AvailableStateEnum.ENABLED);
                this.updateById(entity);
            });
            cacheService.clear(CacheEnum.ORG.getCacheName(), CacheEnum.ORG.getKey());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDisable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                if (haveEffectiveSon(id)) {
                    throw BizException.error(null, "50700003", "该机构有生效状态的子机构，请停用相关的机构后进行操作");
                } else {
                    AdminSmOrgEntity entity = new AdminSmOrgEntity();
                    entity.setOrgId(id);
                    entity.setOrgSts(AvailableStateEnum.DISABLED);
                    getBaseMapper().updateById(entity);
                }
            });
            cacheService.clear(CacheEnum.ORG.getCacheName(), CacheEnum.ORG.getKey());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                if (checkBlocked(id)) {
                    throw BizException.error(null, "50700002", "该机构有子机构或绑定有其他信息，请删除关联信息后操作");
                } else {
                    getBaseMapper().deleteById(id);
                    cacheService.hashKeyDelete(Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME, Constants.CacheConstance.DICT_TRANSLATOR, id);
                }
            });
        }
    }

    /**
     * 检查机构是否已关联其他信息
     *
     * @param orgId 上级机构记录编号
     * @return true 已关联其他信息，false无关联信息
     */
    private boolean checkBlocked(String orgId) {
        QueryWrapper<AdminSmOrgEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("UP_ORG_ID", orgId);
        // 是否有下级机构
        Long countSon = getBaseMapper().selectCount(wrapper);
        // 是否有关联用户、角色、岗位、部门
        Integer countRel = getBaseMapper().queryRelByOrgId(orgId);
        return countSon > 0 || countRel > 0;
    }

    private boolean haveEffectiveSon(String orgId) {
        QueryWrapper<AdminSmOrgEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("UP_ORG_ID", orgId);
        wrapper.eq("ORG_STS","A");
        // 是否有下级机构
        Long countSon = getBaseMapper().selectCount(wrapper);
        return countSon > 0;
    }



    @Override
    public boolean updateBy(AdminSmOrgEntity entity) {
        // 改为停用时要判断是否关联其他信息
        if (AvailableStateEnum.DISABLED.equals(entity.getOrgSts()) && haveEffectiveSon(entity.getOrgId())) {
            throw BizException.error(null, "50700003", "该机构有生效状态的子机构，请停用相关的机构后进行操作");
        }
        entity.setOrgSts(Optional.ofNullable(entity.getOrgSts()).orElse(AvailableStateEnum.UNENABLED));
        // 设置org_seq
        setOrgSeq(entity);

        boolean res = updateById(entity);
        refreshCache(entity);
        return res;
    }

    /**
     * 设置机构ORG_SEQ
     *
     * @param entity 要修改的机构实体
     * @author zhanyq
     * @date 2021-07-02 10:07
     */
    private void setOrgSeq(AdminSmOrgEntity entity) {
        StringBuffer buffer = new StringBuffer();
        reduceOrgSeq(buffer, entity.getUpOrgId(), ORG_SEQ_BY_CODE);
        buffer.append(entity.getOrgCode()).append(",");
        entity.setOrgSeq(buffer.toString());
    }


    private void reduceOrgSeq(StringBuffer res, String currentOrgId, int type) {
        if (StringUtils.isEmpty(currentOrgId)) {
            return;
        }
        if (ROOT_ID.equals(currentOrgId)) {
            return;
        }

        AdminSmOrgEntity org = getBaseMapper().selectById(currentOrgId);

        if (ORG_SEQ_BY_ID == type) {
            res.insert(0, currentOrgId + ",");
        } else {
            res.insert(0, org.getOrgCode() + ",");
        }
        reduceOrgSeq(res, org.getUpOrgId(), type);

    }


    private List<AdminSmOrgEntity> getAllOrgEntities() {
        Map<String, String> cacheMap = cacheService.hashGet(CacheEnum.ORG.getCacheName(), CacheEnum.ORG.getKey());
        // 目前强制走数据库
        int cacheSize = cacheMap != null && cacheMap.size() == 0 ? 0 : 0;
        List<AdminSmOrgEntity> list;
        // 没缓存就刷新缓存
        if (cacheSize == 0) {
            LambdaQueryWrapper<AdminSmOrgEntity> wrapper = Wrappers.lambdaQuery();
            //todo 机构树直接按更新时间排序,有需求再加
            wrapper.orderByDesc(AdminSmOrgEntity::getLastChgDt);
            list = getBaseMapper().selectList(wrapper);
            Map<String, String> collect = list.stream().collect(Collectors.toMap(AdminSmOrgEntity::getOrgId, JSON::toJSONString));
            cacheService.hashPut(CacheEnum.ORG.getCacheName(), CacheEnum.ORG.getKey(), collect, 24 * 60 * 60);
        } else {
            // 有缓存直接返回
            Collection<String> values = cacheMap.values();
            list = values.stream().map(value -> (AdminSmOrgEntity) JSON.parse(value)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 刷新缓存
     *
     * @param entity entity
     */
    private void refreshCache(AdminSmOrgEntity entity) {
        Map<String, String> value = new HashMap<>(1);
        value.put(entity.getOrgId(), entity.getOrgName());
        cacheService.hashPut(Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME, Constants.CacheConstance.DICT_TRANSLATOR, value, 24 * 3600);
    }

    @Override
    public List<String> getUpOrgIdList(String orgId) {
        AdminSmOrgEntity adminSmOrgEntity = this.getById(orgId);
        List<String> orgCodeList = Arrays.stream(adminSmOrgEntity.getOrgSeq().split(",")).filter(StringUtils::nonEmpty).collect(Collectors.toList());
        LambdaQueryWrapper<AdminSmOrgEntity> entityLambdaQueryWrapper = new QueryWrapper<AdminSmOrgEntity>().lambda().in(AdminSmOrgEntity::getOrgCode, orgCodeList);
        List<AdminSmOrgEntity> orgEntityList = this.list(entityLambdaQueryWrapper);
        return orgEntityList.stream().map(AdminSmOrgEntity::getOrgId).collect(Collectors.toList());
    }

    @Override
    public ProgressDto asyncExportTemplate() {
        // 数据检索规则——模板时返回空数据
        DataAcquisition dataAcquisition = new DataAcquisition() {
            @Override
            public Collection<?> getData(int size, int page, Object object) {
                return new ArrayList<>();
            }
        };
        ExportContext exportContext = ExportContext.of(AdminSmOrgExcelVo.class).exportPostProcessor(new FileExportPostProcessor()).data(dataAcquisition, null);
        return ExcelUtils.asyncExport(exportContext);
    }

    /**
     * 数据查询方法的返回集合类型，必须与HeadClass相同，即均为CusLstJjycExportVo
     *
     * @param query
     * @return
     */
    private List<AdminSmOrgExcelVo> selectByModelForExcel(AdminSmOrgExtQuery query) {
        Page<AdminSmOrgVo> smOrgVoPage = queryPage(query);
        List<AdminSmOrgExcelVo> adminSmOrgExcelVos = (List<AdminSmOrgExcelVo>) BeanUtils.beansCopy(smOrgVoPage.getRecords(), AdminSmOrgExcelVo.class);
        return adminSmOrgExcelVos;
    }

    /**
     * 异步导出数据
     *
     * @return 导出进度信息
     */
    @Override
    public ProgressDto asyncExportData(AdminSmOrgExtQuery query) {
        DataAcquisition dataAcquisition = new DataAcquisition() {
            @Override
            public Collection<?> getData(int page, int size, Object object) {
                AdminSmOrgExtQuery queryVo = (AdminSmOrgExtQuery) object;
                queryVo.setSize(size);
                queryVo.setPage(page);
                return selectByModelForExcel(queryVo);
            }
        };
        ExportContext exportContext = ExportContext.of(AdminSmOrgExcelVo.class).exportPostProcessor(new FileExportPostProcessor()).data(dataAcquisition, query);
        return ExcelUtils.asyncExport(exportContext);
    }

    @Override
    public ProgressDto asyncImportData(String fileId) throws IOException {
        FileInfo fileInfo = FileInfoUtils.fromIdentity(fileId);
        String fileName = fileInfo.getFileName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        File tempFile = File.createTempFile(IdUtils.getId(), "." + suffix, null);
        FileUtils.copyInputStreamToFile(FileInfoUtils.openDownloadStream(fileInfo), tempFile);
        // 将文件内容导入数据库，StudentScore为导入数据的类
        return ExcelUtils.asyncImport(ImportContext.of(AdminSmOrgExcelVo.class)
                // 批量操作需要将batch设置为true
                .batch(true)
                .file(tempFile)
                .dataStorage(ExcelUtils.batchConsumer(this::insertInBatch)));
    }

    /**
     * 批量插入
     *
     * @param orgList 解析出的Excel数据
     * @return 本次批量插入数据量
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertInBatch(List<AdminSmOrgExcelVo> orgList) {
        User user = SessionUtils.getUserInformation();
        String instuId = "";
        String userId = "";
        if (!Objects.isNull(user)) {
            UserIdentity instuOrg = user.getInstuOrg();
            instuId = instuOrg.getId();
            userId = user.getUserId();
        }
        List<AdminSmOrgEntity> cusLstJjycList = this.beansCopy(orgList, AdminSmOrgEntity.class);
        if (cusLstJjycList == null) {
            cusLstJjycList = new ArrayList<>();
        }
        try (SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH)) {
            AdminSmOrgDao adminSmOrgDao = sqlSession.getMapper(AdminSmOrgDao.class);
            for (AdminSmOrgEntity adminSmOrgEntity : cusLstJjycList) {
                this.checkOrgCodeIsExist(adminSmOrgEntity);
                adminSmOrgEntity.setOrgId(adminSmOrgEntity.getOrgCode());
                adminSmOrgEntity.setInstuId(instuId);
                adminSmOrgEntity.setLastChgUsr(userId);
                adminSmOrgDao.insert(adminSmOrgEntity);
            }
            sqlSession.flushStatements();
            sqlSession.commit();
        }
        return cusLstJjycList.size();
    }

    /**
     * Excel导入导出的机构信息复制为系统机构信息
     * @param orgList Excel导入导出的机构信息
     * @param c 系统机构信息
     * @return  系统机构表
     */
    private  List<AdminSmOrgEntity> beansCopy(List<AdminSmOrgExcelVo> orgList,Class<AdminSmOrgEntity> c) {
        if (!Objects.isNull(orgList) && !Objects.isNull(c)) {
            if (orgList.isEmpty()) {
                return Collections.emptyList();
            } else {
                BeanCopier beanCopier = BeanCopier.create(orgList.iterator().next().getClass(), c, false);
                return orgList.stream().map((s) -> {
                    AdminSmOrgEntity t = newInstance(c);
                    beanCopier.copy(s, t, null);
                    switch (s.getOrgSts()) {
                        case "A" -> t.setOrgSts(AvailableStateEnum.ENABLED);
                        case "I" -> t.setOrgSts(AvailableStateEnum.DISABLED);
                        case "W" -> t.setOrgSts(AvailableStateEnum.UNENABLED);
                        default -> {
                        }
                    }
                    return t;
                }).collect(Collectors.toList());
            }
        } else {
            return null;
        }
    }
    public  AdminSmOrgEntity newInstance(Class<AdminSmOrgEntity> classType) {
        try {
            return classType.getDeclaredConstructor().newInstance();
        } catch (Exception var2) {
            log.error("Class[" + classType + "]Exception in instantiation!", var2);
            return null;
        }
    }


    /**
     * 部门名称的缓存跟新
     *
     * @param entity 新增或者需要跟新的部门
     */
    @Override
    public void updateOrgCache(AdminSmOrgEntity entity) {
        String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        String dataTenantId=String.valueOf(SessionUtils.getUserInformation().getAdditional(OcaCommonConstants.SESSION_DATATENANT_KEY));

        CustomCacheService cacheService = SpringContextUtils.getBean(CustomCacheService.class);
        Map<String, String> map = new HashMap<>(8);
        map.put(entity.getOrgId(), entity.getOrgName());
        cacheService.hashPut(name,dataTenantId, redisKey, map, 24 * 15 * 3600);
    }

    /**
     * 删除部分部门缓存
     *
     * @param ids 需要被删除缓存的部门id
     */
    @Override
    public void deletePartOrgCache(String[] ids) {
        String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        CustomCacheService cacheService = SpringContextUtils.getBean(CustomCacheService.class);
        for (String id : ids) {
            cacheService.hashKeyDelete(name, redisKey, id);
        }
    }
}
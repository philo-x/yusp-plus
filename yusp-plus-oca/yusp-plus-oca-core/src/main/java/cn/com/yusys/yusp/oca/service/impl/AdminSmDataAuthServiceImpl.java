package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.common.utils.Query;
import cn.com.yusys.yusp.common.utils.RamPager;
import cn.com.yusys.yusp.commons.module.adapter.exception.YuspException;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmDataAuthDao;
import cn.com.yusys.yusp.oca.domain.entity.*;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDataTmplVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmResContrTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.ResControlDataAuthTmplVo;
import cn.com.yusys.yusp.oca.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author danyu
 */
@Service("adminSmDataAuthService")
public class AdminSmDataAuthServiceImpl extends ServiceImpl<AdminSmDataAuthDao, AdminSmDataAuthEntity> implements AdminSmDataAuthService {

    @Autowired
    AdminSmFuncModService adminSmFuncModService;

    @Autowired
    AdminSmBusiFuncService adminSmBusiFuncService;

    @Autowired
    AdminSmResContrService adminSmResContrService;

    @Autowired
    AdminSmDataAuthTmplService dataAuthTmplService;

    @Autowired
    AdminSmAuthRecoService adminSmAuthRecoService;

    @Autowired
    ObjectMapper objectMapper;

    private static final String CONTR_ID = "contr_id";

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AdminSmDataAuthEntity> page = this.page(
                new Query<AdminSmDataAuthEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }


    @Override
    public PageUtils getDataAuthTree(String nodeId) {

        ArrayList<AdminSmResContrTreeVo> adminSmResContrTreeVos = new ArrayList<>();

        if (StringUtils.isEmpty(nodeId)) {
            // 全量查
            // 1. 收集数据权限树-功能模块列表
            adminSmResContrTreeVos.addAll(getFuncModTreeVoList());
            // 2. 收集数据权限树-功能模块列表
            adminSmResContrTreeVos.addAll(getBusiFuncTreeVoList(""));
            // 3. 收集数据权限树-控制点列表
            adminSmResContrTreeVos.addAll(getResContrTreeVoList(null));
        } else if ("0".equals(nodeId)) {
            // 懒加载，只查功能模块
            adminSmResContrTreeVos.addAll(getFuncModTreeVoList());
        } else {
            // 根据模块Id查询所有功能点，及其下属权限控制点
            // 1. 条件查询指定模块下所有功能列表
            List<AdminSmResContrTreeVo> busiFuncTreeVoList = getBusiFuncTreeVoList(nodeId);
            // 2. 收集出功能id列表
            List<String> funcIdList = busiFuncTreeVoList.stream()
                    .map(item -> item.getNodeId()).collect(Collectors.toList());

            // 3. 条件查询所有funcIdList里的控制点列表
            List<AdminSmResContrTreeVo> resContrTreeVoList = getResContrTreeVoList(funcIdList);

            // 4. 组合数据
            adminSmResContrTreeVos.addAll(busiFuncTreeVoList);
            adminSmResContrTreeVos.addAll(resContrTreeVoList);
        }

        // 纯粹是为了得到PageUtils这个玩意，写死pageSize和currPage
        return new PageUtils(adminSmResContrTreeVos, adminSmResContrTreeVos.size(), 10, 1);
    }


    @Override
    public PageUtils pageResControlDataAuthTmpl(Map<String, String> params) {

        // 生成查询条件 condition: {"contrId":"7F1CE4F9EA624141A872EB634CEBBD1C","authTmplName":"示例","sqlString":"user_id"}
        Map<String, String> condition = new HashMap<>(8);
        condition = getQueryCondition(params, condition);

        String contrId = condition.get("contrId");
        String authTmplName = condition.get("authTmplName");
        String sqlString = condition.get("sqlString");

        // 组装查询条件
        QueryWrapper<ResControlDataAuthTmplVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(contrId), "a.contr_id", contrId);
        queryWrapper.like(!StringUtils.isEmpty(authTmplName), "t.auth_tmpl_name", authTmplName);
        queryWrapper.like(!StringUtils.isEmpty(sqlString), "t.sql_string", sqlString);

        // 组装page分页对象
        Page<ResControlDataAuthTmplVo> page = new Page<>(Long.parseLong(params.get("page")), Long.parseLong(params.get("size")));
        page.addOrder(OrderItem.desc("a.last_chg_dt"));

        IPage<ResControlDataAuthTmplVo> pageResult = getBaseMapper().pageResControlDataAuthTmpl(page, queryWrapper);

        return new PageUtils(pageResult.getRecords(), Long.valueOf(pageResult.getTotal()).intValue(), Integer.parseInt(params.get("size")), Integer.parseInt(params.get("page")));
    }

    @Override
    public PageUtils pageAuthTmplByContrId(Map<String, String> params) {

        // 生成查询条件 condition: {"contrId":"4291d4a1e6294100ba3d3aa19614c8ea","authTmplName":"示例"}
        Map<String, String> condition = new HashMap<>(16);
        condition = getQueryCondition(params, condition);

        String contrId = condition.get("contrId");
        String authTmplName = condition.get("authTmplName");

        // 组装查询条件
        QueryWrapper<AdminSmDataAuthTmplEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contr_include", "*");
        queryWrapper.or();
        queryWrapper.eq(!StringUtils.isEmpty(contrId), "contr_include", contrId);
        queryWrapper.like(!StringUtils.isEmpty(authTmplName), "auth_tmpl_name", authTmplName);

        // 1. 查询符合条件的全部授权模板
        List<AdminSmDataAuthTmplEntity> authTmplEntityList = dataAuthTmplService.list(queryWrapper);

        // 2. 查询当前contrId配置的授权模板
        List<AdminSmDataAuthEntity> dataAuthEntityList = this.list(new QueryWrapper<AdminSmDataAuthEntity>().eq(CONTR_ID, contrId));
        // 3. 收集授权模板idList
        List<String> authTmplIdList = dataAuthEntityList.stream().map(item -> item.getAuthTmplId()).collect(Collectors.toList());
        // 4. 过滤出不在authTmplIdList里的授权模板，并转换为ResControlDataAuthTmplVo再收集成List
        List<ResControlDataAuthTmplVo> collect = authTmplEntityList.stream()
                .filter(item -> !authTmplIdList.contains(item.getAuthTmplId()))
                .map(item -> {
                    ResControlDataAuthTmplVo tmplVo = new ResControlDataAuthTmplVo();
                    tmplVo.setAuthTmplId(item.getAuthTmplId());
                    tmplVo.setAuthTmplName(item.getAuthTmplName());
                    return tmplVo;
                }).collect(Collectors.toList());

        // 5. 内存分页
        RamPager<ResControlDataAuthTmplVo> pager = new RamPager<>(collect, 10);
        List<ResControlDataAuthTmplVo> currentRecords = pager.page(Integer.parseInt(params.get("page")));

        return new PageUtils(currentRecords, collect.size(), Integer.parseInt(params.get("size")), Integer.parseInt(params.get("page")));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataAuth(String[] ids) {
        if (ids != null && ids.length != 0) {
            List<String> authIdList = Arrays.asList(ids);
            // 1. 删除ADMIN_SM_AUTH_RECO表 资源对象授权记录表
            QueryWrapper<AdminSmAuthRecoEntity> queryWrapper = new QueryWrapper<AdminSmAuthRecoEntity>()
                    .eq("authres_type", 'D')
                    .in("authres_id", authIdList);
            adminSmAuthRecoService.remove(queryWrapper);

            // 2. 删除数据授权信息
            this.removeByIds(authIdList);
        }
    }


    @Override
    public void deleteByContrIds(String[] contrIds) {
        QueryWrapper<AdminSmDataAuthEntity> wrapper = new QueryWrapper<>();
        wrapper.in(CONTR_ID, contrIds);
        getBaseMapper().delete(wrapper);
    }


    @Override
    public void createDataAuth(AdminSmDataAuthEntity adminSmDataAuth) {
        this.save(adminSmDataAuth);
    }


    @Override
    public void deleteTmplWhithContr(String contrId) {
        QueryWrapper<AdminSmDataAuthEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(CONTR_ID, contrId);
        this.remove(wrapper);
    }


    @Override
    public List<AdminSmDataAuthEntity> getByContrId(String contrId) {
        QueryWrapper<AdminSmDataAuthEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(CONTR_ID, contrId);
        return this.list(wrapper);
    }


    @Override
    public List<AdminSmDataAuthEntity> getListByContrIds(List<String> idList) {
        QueryWrapper<AdminSmDataAuthEntity> wrapper = new QueryWrapper<>();
        wrapper.in(CONTR_ID, idList);
        List<AdminSmDataAuthEntity> list = this.list(wrapper);
        return list;
    }


    @Override
    public List<AdminSmDataTmplVo> selectWithTmplIds(List<String> idList) {
        QueryWrapper<AdminSmDataTmplVo> wrapper = new QueryWrapper<>();
        wrapper.in("a.auth_tmpl_id", idList);
        return getBaseMapper().selectWithTmplIds(wrapper);
    }

    private List<AdminSmResContrTreeVo> getFuncModTreeVoList() {
        List<AdminSmFuncModEntity> funcModEntityList = adminSmFuncModService.list();

        return funcModEntityList.stream().map(funcModEntity -> {
            AdminSmResContrTreeVo funcModTreeVo = new AdminSmResContrTreeVo();
            funcModTreeVo.setNodeName(funcModEntity.getModName());
            funcModTreeVo.setUpTreeId("0");
            funcModTreeVo.setNodeType("M");
            funcModTreeVo.setNodeId(funcModEntity.getModId());
            return funcModTreeVo;
        }).collect(Collectors.toList());
    }


    private List<AdminSmResContrTreeVo> getBusiFuncTreeVoList(String nodeId) {

        QueryWrapper<AdminSmBusiFuncEntity> queryWrapper = new QueryWrapper<AdminSmBusiFuncEntity>()
                .eq(!StringUtils.isEmpty(nodeId), "mod_id", nodeId);

        List<AdminSmBusiFuncEntity> busiFuncEntityList = adminSmBusiFuncService.list(queryWrapper);

        return busiFuncEntityList.stream()
                .map(busiFuncEntity -> {
                    AdminSmResContrTreeVo busiFuncTreeVo = new AdminSmResContrTreeVo();
                    busiFuncTreeVo.setNodeName(busiFuncEntity.getFuncName());
                    busiFuncTreeVo.setUpTreeId(busiFuncEntity.getModId());
                    busiFuncTreeVo.setNodeType("F");
                    busiFuncTreeVo.setNodeId(busiFuncEntity.getFuncId());
                    return busiFuncTreeVo;
                }).collect(Collectors.toList());
    }


    private List<AdminSmResContrTreeVo> getResContrTreeVoList(List<String> funcIdList) {

        QueryWrapper<AdminSmResContrEntity> queryWrapper = new QueryWrapper<AdminSmResContrEntity>()
                .in(!CollectionUtils.isEmpty(funcIdList), "func_id", funcIdList);

        List<AdminSmResContrEntity> resContrEntityList = adminSmResContrService.list(queryWrapper);

        return resContrEntityList.stream().map(resContrEntity -> {
            AdminSmResContrTreeVo resContrTreeVo = new AdminSmResContrTreeVo();
            resContrTreeVo.setNodeName(resContrEntity.getContrName());
            resContrTreeVo.setUpTreeId(resContrEntity.getFuncId());
            resContrTreeVo.setNodeType("C");
            resContrTreeVo.setNodeId(resContrEntity.getContrId());
            return resContrTreeVo;
        }).collect(Collectors.toList());
    }

    private <T> T getQueryCondition(Map<String, String> params, T t) {
        String json = params.get("condition");

        try {
            if (json != null) {
                return (T) objectMapper.readValue(json, t.getClass());
            }
        } catch (Exception e) {
            throw new YuspException("500", "解析查询参数异常");
        }

        return t;
    }
}
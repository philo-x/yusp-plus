package cn.com.yusys.yusp.file.repository.mapper;

import cn.com.yusys.yusp.commons.mybatisplus.mapper.BaseMapper;
import cn.com.yusys.yusp.commons.mybatisplus.util.MybatisPlusUtils;
import cn.com.yusys.yusp.file.domain.AdminSmRicheditFileInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yusys
 * @version 1.0
 * @date 2021/1/26 10:04
 */
@Mapper
public interface AdminSmRicheditFileInfoMapper extends BaseMapper<AdminSmRicheditFileInfo> {

    /**
     * 插入数据，insertSelective
     * @param adminSmRicheditFileInfo adminSmRicheditFileInfo
     * @return int
     */
    default int insertSelective(AdminSmRicheditFileInfo adminSmRicheditFileInfo) {
        return insert(adminSmRicheditFileInfo);
    }

}
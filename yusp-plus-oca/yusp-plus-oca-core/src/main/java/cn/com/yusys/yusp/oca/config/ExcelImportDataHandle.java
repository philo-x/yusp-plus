package cn.com.yusys.yusp.oca.config;

import cn.com.yusys.yusp.commons.excelcsv.handle.impl.AbstractExcelFieldDataHandle;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yusys
 * @version 1.0
 * @date 2020/7/15 10:07
 */
public class ExcelImportDataHandle extends AbstractExcelFieldDataHandle {

    private static final Logger log = LoggerFactory.getLogger(ExcelImportDataHandle.class);

    private List<String> errorMsg = new ArrayList<>();

    @Override
    public Object doHandle(Object o) {
        AdminSmLogEntity logEntity = (AdminSmLogEntity) o;
        log.debug("excel 导入的数据：{}", logEntity);

        if (StringUtils.isBlank(logEntity.getUserId())) {
            String msg = "错误信息：userId 不能为 null！";
            errorMsg.add(msg);
            log.error("excel 导入的数据：{}", msg);
        }

        return logEntity;
    }
}

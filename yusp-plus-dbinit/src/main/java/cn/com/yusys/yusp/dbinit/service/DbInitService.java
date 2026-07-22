package cn.com.yusys.yusp.dbinit.service;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @description:
 * @author: Zhan YongQiang
 * @date: 2021/9/3 16:18
 */
public interface DbInitService {

    /**
     * 初始化数据库脚本
     * @return
     * @author zhanyq
     * @date 2021-09-03 16:18
     */
    void initDb() throws SQLException, IOException;
}

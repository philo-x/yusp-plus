package cn.com.yusys.yusp.dbinit.service.impl;

import cn.com.yusys.yusp.dbinit.service.DbInitService;
import cn.com.yusys.yusp.dbinit.util.PathUtils;
import cn.com.yusys.yusp.dbinit.util.PropertiesUtils;
import com.mysql.cj.util.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Zhan YongQiang
 * @date: 2021/9/3 16:36
 */
public class DbInitServiceImpl implements DbInitService {
    private static final Logger log = LoggerFactory.getLogger(DbInitServiceImpl.class);


    @Override
    public void initDb() throws SQLException, IOException {

        String dbType = PropertiesUtils.get("db.active");
        if (StringUtils.isNullOrEmpty(dbType)) {
            log.error("未指定要初始化的数据库类型，请确认！");
            return;
        }
        String url = PropertiesUtils.get(dbType + ".jdbc.url");
        String userName = PropertiesUtils.get(dbType + ".jdbc.username");
        String passWord = PropertiesUtils.get(dbType + ".jdbc.password");
        Connection conn = DriverManager.getConnection(url, userName, passWord);

        ScriptRunner scriptRunner = new ScriptRunner(conn);
        try {
            String sqlDir = getSqlDir();
            log.info("sql根目录为：" + sqlDir);
            scriptRunner.setErrorLogWriter(new PrintWriter(sqlDir + "error.log"));
            scriptRunner.setLogWriter(new PrintWriter(sqlDir + "info.log"));

            // 先执行ddl
            runSqlScript(scriptRunner, sqlDir, dbType, "ddl");
            // 在执行 dml
            runSqlScript(scriptRunner, sqlDir, dbType, "dml");

        } catch (Exception e) {
            log.error("sql脚本执行失败：{}", e);
        } finally {
            conn.close();
        }

    }

    /**
     * 获取sql根目录
     *
     * @return sql根目录
     * @author zhanyq
     * @date 2021-09-08 15:53
     */
    private String getSqlDir() throws Exception {
        String sqlDir = getSqlDirOuter();
        if (!StringUtils.isNullOrEmpty(sqlDir)) {
            return sqlDir;
        }
        sqlDir = getSqlDirCurrent();
        if (!StringUtils.isNullOrEmpty(sqlDir)) {
            return sqlDir;
        }
        sqlDir = getSqlDirClassPath();
        if (!StringUtils.isNullOrEmpty(sqlDir)) {
            return sqlDir;
        }
        throw new Exception("没有找到sql文件目录，请确认!");
    }


    /**
     * 从classpath下获取sql文件目录 优先级最低
     *
     * @return sql目录
     * @author zhanyq
     * @date 2021-09-07 10:30
     */
    private String getSqlDirClassPath() throws UnsupportedEncodingException {
        String path = this.getClass().getClassLoader().getResource("").getPath() + File.separator + "sql" + File.separator;
        // 解决路径或者文件名中文乱码
        path = URLDecoder.decode(path, "UTF-8");
        File file = new File(path);
        if (file.exists()) {
            return path;
        }
        return null;
    }

    /**
     * 从外部获取sql目录（jar包同层下的sql目录），优先级第二
     *
     * @return sql目录
     * @author zhanyq
     * @date 2021-09-07 10:32
     */
    private String getSqlDirCurrent() {
        String currentPath = PathUtils.getCurrentPath();
        String sqlDir = currentPath + "sql" + File.separator;
        File file = new File(sqlDir);
        if (file.exists()) {
            return sqlDir;
        }
        return null;
    }

    /**
     * 从外部获取sql目录（jar包上层下的sql目录），优先级最高
     *
     * @return sql 目录
     * @author zhanyq
     * @date 2021-09-07 10:31
     */
    private String getSqlDirOuter() {
        String parent = PathUtils.getParentPath();
        String sqlDir = parent + "sql" + File.separator;
        File file = new File(sqlDir);
        if (file.exists()) {
            return sqlDir;
        }
        return null;
    }


    /**
     * 执行脚本
     *
     * @param initScriptRunner 执行器
     * @param sqlDir           sql根目录
     * @param dbType           数据库类型
     * @param sqlType          sql脚本类型
     * @throws FileNotFoundException
     * @author zhanyq
     * @date 2021-09-07 11:38
     */
    private void runSqlScript(ScriptRunner initScriptRunner, String sqlDir, String dbType, String sqlType) throws FileNotFoundException {
        String sqlFilePath = sqlDir + dbType + File.separator;
        List<String> fileNameLists = getFilterTypeFile(sqlFilePath, sqlType);
        for (String fileName : fileNameLists) {
            Reader initSqlReader = new FileReader(new File(sqlFilePath + fileName));
            initScriptRunner.runScript(initSqlReader);
        }
    }


    /**
     * 根据sql脚本类型筛选相应的脚本
     *
     * @param basicPath sql脚本路径
     * @param type      sql脚本类型 ddl或者dml
     * @return 筛选后的脚本
     * @author zhanyq
     * @date 2021-09-07 11:38
     */
    private List<String> getFilterTypeFile(String basicPath, String type) {

        File file = new File(basicPath);
        if (!file.exists() || !file.isDirectory()) {
            log.error("请确定文件目录{}已经在系统中已经存在", basicPath);
            throw new RuntimeException("请确认脚本目录是否正确".formatted());
        }

        List<String> retList = new ArrayList<>();
        for (String str : file.list()) {
            if (str.length() <= 7) {
                continue;
            }
            String suffixStr = str.substring(str.length() - 7, str.length() - 4);
            if (type.equalsIgnoreCase(suffixStr)) {
                retList.add(str);
            }
        }
        return retList;
    }
}

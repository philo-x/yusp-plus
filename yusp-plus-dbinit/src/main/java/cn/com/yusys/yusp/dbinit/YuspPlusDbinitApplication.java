package cn.com.yusys.yusp.dbinit;

import cn.com.yusys.yusp.dbinit.service.DbInitService;
import cn.com.yusys.yusp.dbinit.service.impl.DbInitServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
/**
 * 入口类
 * @author zhanyq
 * @date 2021-09-08 10:34
 */
public class YuspPlusDbinitApplication {

  private static final Logger log = LoggerFactory.getLogger(YuspPlusDbinitApplication.class);

    public static void main(String[] args) {
        log.info("数据库初始化开始");

        DbInitService service = new DbInitServiceImpl();
        try {
            service.initDb();
        } catch (SQLException throwables) {
            log.error("database init error,", throwables);
        } catch (IOException e) {
            log.error("database init error,", e);
        }
        log.info("数据库初始化结束");
    }

}

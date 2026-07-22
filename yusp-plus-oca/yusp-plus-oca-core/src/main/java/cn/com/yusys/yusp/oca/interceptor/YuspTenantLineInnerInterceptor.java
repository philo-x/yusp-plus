package cn.com.yusys.yusp.oca.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.io.Serial;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 租户拦截器
 *
 * @author chenjing
 * @date 2021-09-20 15:31:54
 */
public class YuspTenantLineInnerInterceptor extends TenantLineInnerInterceptor {


    private static final String USER_ID = "USER_ID";


    /**
     * 不拦截的sql的Map集合（key：sql的id，value是sql的where中包含字段，基本是获取用户信息中的查询语句）
     */
    private static final Map<String, String> NO_INTERCEPTOR_SQL_MAP = new HashMap<String, String>() {
        @Serial
        private static final long serialVersionUID = -4486764051023413809L;

        {
            // MyBatis Plus 底层对 SelectOne 进行了改动，原来会调用 baseMapper 的 selectOne，现在会调用 selectList，然后再取第一个元素，因此这里都要对 selectList 加入白名单
            put("cn.com.yusys.yusp.oca.dao.AdminSmLogicSysDao.selectOne", "SYS_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmLogicSysDao.selectList", "SYS_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmUserDutyRelDao.selectList", USER_ID);
            put("cn.com.yusys.yusp.oca.dao.AdminSmUserDao.selectOne", USER_ID);
            put("cn.com.yusys.yusp.oca.dao.AdminSmUserDao.selectList", USER_ID);
            put("cn.com.yusys.yusp.oca.dao.AdminSmTenantDao.selectById", "tenant_id");
            put("cn.com.yusys.yusp.oca.dao.AdminSmDptDao.selectOne", "DPT_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmDptDao.selectList", "DPT_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmUserRoleRelDao.selectList", USER_ID);
            put("cn.com.yusys.yusp.oca.dao.AdminSmRoleDao.selectOne", "ROLE_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmRoleDao.selectList", "ROLE_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmOrgDao.selectOne", "ORG_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmOrgDao.selectList", "ORG_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmInstuDao.selectOne", "INSTU_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmInstuDao.selectList", "INSTU_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmDutyDao.selectOne", "DUTY_ID");
            put("cn.com.yusys.yusp.oca.dao.AdminSmDutyDao.selectList", "DUTY_ID");
        }
    };

    public YuspTenantLineInnerInterceptor(final TenantLineHandler tenantLineHandler) {
        super(tenantLineHandler);
    }

    /**
     * 如果查询的id属于map中的key，并且where条件后有map的value值，就不会拦截
     * （不拦截获取tanentId时查询的sql语句，否则会形成死循环）;如果查询的id属于不拦截的List，则直接放行
     *
     * @param executor      executor
     * @param ms            ms
     * @param parameter     参数
     * @param rowBounds     rowBounds
     * @param resultHandler 结果处理器
     * @param boundSql      boundSql
     * @throws SQLException sql异常
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        String sqlId = ms.getId();
        String value = NO_INTERCEPTOR_SQL_MAP.get(sqlId);
        if (StringUtils.isNotBlank(value)) {
            String sql = boundSql.getSql();
            /**
             * 由于mybatisPlus更换了底层实现，为了兼容，将 selectList 也加入到 NO_INTERCEPTOR_SQL_MAP 中，
             * 但部分 selectList 不需要被拦截，因此没有 where 条件，从而引发空指针问题
             * 这里进行索引判断，只有存在 where 才往下走
             */
            int indexOf = sql.indexOf("WHERE");
            if (indexOf == -1) {
                super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
                return;
            }
            String whereString = sql.substring(indexOf);
            if (whereString.contains(value) || whereString.contains(value.toLowerCase())) {
                return;
            }

        }
        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override
    protected String processParser(Statement statement, int index, String sql, Object obj) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("SQL to parse, SQL: " + sql);
        }

        // 如果租户ID为空，则不进行解析
        if (this.getTenantLineHandler().getTenantId() == null) {
            return statement.toString();
        }

        if (statement instanceof Insert) {
            this.processInsert((Insert)statement, index, sql, obj);
        } else if (statement instanceof Select) {
            this.processSelect((Select)statement, index, sql, obj);
        } else if (statement instanceof Update) {
            this.processUpdate((Update)statement, index, sql, obj);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete)statement, index, sql, obj);
        }

        sql = statement.toString();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("parse the finished SQL: " + sql);
        }

        return sql;
    }
}

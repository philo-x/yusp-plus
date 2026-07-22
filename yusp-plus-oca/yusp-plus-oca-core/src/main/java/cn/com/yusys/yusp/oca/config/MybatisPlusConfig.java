package cn.com.yusys.yusp.oca.config;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.user.impl.UserInformation;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.oca.constants.OcaCommonConstants;
import cn.com.yusys.yusp.oca.interceptor.YuspTenantLineInnerInterceptor;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.*;

/**
 * 注册oca用到的mybatis-plus过滤器
 *
 * @author tanrui1
 * @date 2021/6/28 11:27:00
 */
@Configuration
@MapperScan(basePackages = {"cn.com.yusys.yusp.oca.dao",
        "cn.com.yusys.yusp.notice.dao",
        "cn.com.yusys.yusp.job.core.dao",
        "cn.com.yusys.yusp.utrace.core.dao"})
public class MybatisPlusConfig implements MetaObjectHandler {

    @Value("${yusp.interceptor.tenant:true}")
    private boolean tenant;

    /**
     * 目前只做了数据字典表的国际化，若需实现其他表的国际化,可在map中追加
     */
    private static final Set<String> INTERNATIONAL_TABLE_SET = new HashSet<>(16);


    /**
     * 忽略多租户的表名单：在这个 Set 中的表将不会添加受到 data_tenant_id 字段影响
     */
    private static final Set<String> TENANT_TABLE_IGNORE_SET = new HashSet<>(16);


    static {
        // 配置多语言表
        INTERNATIONAL_TABLE_SET.add("admin_sm_lookup_dict");
        INTERNATIONAL_TABLE_SET.add("admin_sm_menu");
        INTERNATIONAL_TABLE_SET.add("admin_sm_message");
        INTERNATIONAL_TABLE_SET.add("admin_sm_crel_stra");
        INTERNATIONAL_TABLE_SET.add("admin_sm_res_contr");
        INTERNATIONAL_TABLE_SET.add("admin_sm_user");
        INTERNATIONAL_TABLE_SET.add("admin_sm_role");
        INTERNATIONAL_TABLE_SET.add("admin_sm_tenant");

        // 忽略多租户的表
        TENANT_TABLE_IGNORE_SET.add("schedule_job");
        TENANT_TABLE_IGNORE_SET.add("admin_sm_message");
        TENANT_TABLE_IGNORE_SET.add("sequence_config");
        TENANT_TABLE_IGNORE_SET.add("schedule_job_log");
        TENANT_TABLE_IGNORE_SET.add("admin_sm_tenant");
    }



    /**
     * 根据当前语言环境选择操作对应的数据字典表
     *
     * @return 返回语言环境拦截器
     */
    @Bean
    public MybatisPlusInterceptor i18nMPInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // FIXME 默认不添加国际化拦截，如有需要请自行开启
//        this.addInternationInterceptor(interceptor);
        //添加多租户拦截
        if (tenant) {
            this.addTenantLineInterceptor(interceptor);
        }
        return interceptor;
    }

    private void addTenantLineInterceptor(MybatisPlusInterceptor interceptor) {
        interceptor.addInnerInterceptor(
                new YuspTenantLineInnerInterceptor
                        (new TenantLineHandler() {
                            @Override
                            public Expression getTenantId() {
                                UserInformation userInformation;
                                try {
                                    userInformation = (UserInformation) SessionUtils.getUserInformation();
                                } catch (BizException e) {
                                    return null;
                                }
                                if (Objects.nonNull(userInformation) && Objects.nonNull(userInformation.getAdditionals()) && Objects.nonNull(userInformation.getAdditional(OcaCommonConstants.SESSION_DATATENANT_KEY))) {
                                    return new StringValue(String.valueOf(userInformation.getAdditional(OcaCommonConstants.SESSION_DATATENANT_KEY)));
                }
                return null;
            }
            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            @Override
            public boolean ignoreTable(String tableName) {
                return TENANT_TABLE_IGNORE_SET.contains(tableName);
            }
            @Override
            public String getTenantIdColumn() {
                return "data_tenant_id";
            }
        }));
    }

    private void addInternationInterceptor(MybatisPlusInterceptor interceptor) {
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            return getTableNameExt(tableName);
        });
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
    }


    @Override
    public void insertFill(MetaObject metaObject) {
        // 保存时间
        this.setFieldValByName("lastChgDt", DateUtils.getCurrDate(), metaObject);
        this.setFieldValByName("createTime", DateUtils.getCurrDate(), metaObject);
        this.setFieldValByName("updateTime", DateUtils.getCurrDate(), metaObject);
        this.setFieldValByName("lastChgUsr", Optional.ofNullable(SessionUtils.getUserId()).orElse("sys"), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 修改时间
        this.setFieldValByName("updateTime", DateUtils.getCurrDate(), metaObject);
        this.setFieldValByName("lastChgDt", DateUtils.getCurrDate(), metaObject);
        this.setFieldValByName("lastChgUsr", Optional.ofNullable(SessionUtils.getUserId()).orElse("sys"), metaObject);
    }

    private String getTableNameExt(String tableName) {
        if (!INTERNATIONAL_TABLE_SET.contains(tableName)) {
            return tableName;
        }
        Locale locale = LocaleContextHolder.getLocale();
        //现在只有中文和英文环境，中文请求头中Accept-Language: zh-CN，英文是Accept-Language: en-US，但是有些环境或者容器在中文环境下不是传的Accept-Language: zh-CN，就会导致表加后缀
        if(Locale.US.equals(locale)){
            tableName += "_" + locale.toString().toLowerCase();
        }
        return tableName;
    }

}
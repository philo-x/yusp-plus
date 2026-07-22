package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 数据权限模板表
 * 
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
@TableName("admin_sm_data_auth_tmpl")
public class AdminSmDataAuthTmplEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private String authTmplId;
	/**
	 * 数据权限模板名
	 */
	private String authTmplName;
	/**
	 * 数据权限SQL条件
	 */
	private String sqlString;
	/**
	 * SQL占位符名称
	 */
	private String sqlName;
	/**
	 * 用于表示该数据模板有没有被控制点关联，0未关联，1关联
	 */
	private int status;
	/**
	 * 优先级,值越小优先级越高
	 */
	private String priority;

    public AdminSmDataAuthTmplEntity() {
    }

    public String getAuthTmplId() {
        return this.authTmplId;
    }

    public String getAuthTmplName() {
        return this.authTmplName;
    }

    public String getSqlString() {
        return this.sqlString;
    }

    public String getSqlName() {
        return this.sqlName;
    }

    public int getStatus() {
        return this.status;
    }


    public String getPriority() {
        return this.priority;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }

    public void setAuthTmplName(String authTmplName) {
        this.authTmplName = authTmplName;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}

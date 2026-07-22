package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 数据权限表同控制点关联表
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
@TableName("admin_sm_data_auth")
public class AdminSmDataAuthEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private String authId;
	/**
	 * 控制点记录编号(为*时表示默认过滤器)
	 */
	private String contrId;
	/**
	 * 权限模板编号
	 */
	private String authTmplId;

    public AdminSmDataAuthEntity() {
    }

    public String getAuthId() {
        return this.authId;
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getAuthTmplId() {
        return this.authTmplId;
    }


    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminSmDataAuthEntity that = (AdminSmDataAuthEntity) o;
        return Objects.equals(authId, that.authId) && Objects.equals(contrId, that.contrId) && Objects.equals(authTmplId, that.authTmplId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authId, contrId, authTmplId);
    }
}

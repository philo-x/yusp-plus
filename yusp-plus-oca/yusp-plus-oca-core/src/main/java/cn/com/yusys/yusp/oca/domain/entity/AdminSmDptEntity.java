package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 系统部门表
 * 
 * @author danyb1
 * @date 2020-11-12 10:47:26
 */
@TableName("admin_sm_dpt")
public class AdminSmDptEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

	public static final String  DEFAULT_UP_DPT_ID="0";

	/**
	 * 记录编号
	 */
	@TableId(type =  IdType.ASSIGN_UUID)
	private String dptId;
	/**
	 * 部门代码
	 */
	private String dptCode;
	/**
	 * 部门名称
	 */
	private String dptName;
	/**
	 * 所属机构编号
	 */
	private String orgId;
	/**
	 * 上级部门记录编号
	 */
	private String upDptId;
	/**
	 * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
	 */
	private AvailableStateEnum dptSts;

    /**
     * 子节点
     */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private List<AdminSmDptEntity> children;

	public AdminSmDptEntity() {
		this.setUpDptId(DEFAULT_UP_DPT_ID);
	}

    public String getDptId() {
        return this.dptId;
    }

    public String getDptCode() {
        return this.dptCode;
    }

    public String getDptName() {
        return this.dptName;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getUpDptId() {
        return this.upDptId;
    }

    public AvailableStateEnum getDptSts() {
        return this.dptSts;
    }


    public List<AdminSmDptEntity> getChildren() {
        return this.children;
    }

    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public void setDptCode(String dptCode) {
        this.dptCode = dptCode;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setUpDptId(String upDptId) {
        this.upDptId = upDptId;
    }

    public void setDptSts(AvailableStateEnum dptSts) {
        this.dptSts = dptSts;
    }


    public void setChildren(List<AdminSmDptEntity> children) {
        this.children = children;
    }
}

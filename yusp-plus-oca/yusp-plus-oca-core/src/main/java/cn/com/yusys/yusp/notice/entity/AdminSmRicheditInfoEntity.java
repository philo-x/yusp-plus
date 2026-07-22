package cn.com.yusys.yusp.notice.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 富文本信息表对应实体类
 * @author zhangyt12
 * @date 2021/6/24 15:44
 */
@TableName("admin_sm_richedit_info")
public class AdminSmRicheditInfoEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
	/**
	 * 富文本编号
	 */
	@TableId
	private String richeditId;
	/**
	 * 关联业务模块（NOTICE-公告；）
	 */
	private String relMod;
	/**
	 * 关联业务主表编号
	 */
	private String relId;
	/**
	 * 文本内容
	 */
	private String content;

    public AdminSmRicheditInfoEntity() {
    }

    public String getRicheditId() {
        return this.richeditId;
    }

    public String getRelMod() {
        return this.relMod;
    }

    public String getRelId() {
        return this.relId;
    }

    public String getContent() {
        return this.content;
    }

    public void setRicheditId(String richeditId) {
        this.richeditId = richeditId;
    }

    public void setRelMod(String relMod) {
        this.relMod = relMod;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

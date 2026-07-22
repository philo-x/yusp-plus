package cn.com.yusys.yusp.oca.domain.vo;
/**
 * 数据权限模板授权展示对象
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public class AdminSmResContrAuthVo {
    /**
     * 授权的功能点
     */
    private String resContrId;
    /**
     * 上级菜单id
     */
    private String upMenuId;
    /**
     * 控制点名称
     */
    private String menuPath;
    /**
     * 已被授权的数据权限模板
     */
    private AdminTmplAndRecoVo tmplAndRecoVo;
    /**
     * 控制点名称
     */
    private String cornName;

    public AdminSmResContrAuthVo() {
    }

    public String getResContrId() {
        return this.resContrId;
    }

    public String getUpMenuId() {
        return this.upMenuId;
    }

    public String getMenuPath() {
        return this.menuPath;
    }

    public AdminTmplAndRecoVo getTmplAndRecoVo() {
        return this.tmplAndRecoVo;
    }

    public void setResContrId(String resContrId) {
        this.resContrId = resContrId;
    }

    public void setUpMenuId(String upMenuId) {
        this.upMenuId = upMenuId;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public void setTmplAndRecoVo(AdminTmplAndRecoVo tmplAndRecoVo) {
        this.tmplAndRecoVo = tmplAndRecoVo;
    }

    public String getCornName() {
        return cornName;
    }

    public void setCornName(String cornName) {
        this.cornName = cornName;
    }
}

package cn.com.yusys.yusp.oca.domain.bo;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthRecoVo;

import java.util.List;

/**
 * @author yusy
 * @date 2019-01-07
 */
public class MenuAndControlAuthRecoSaveBo {

    private List<AdminSmAuthRecoVo> menuData;

    private List<AdminSmAuthRecoVo> ctrData;

    public List<AdminSmAuthRecoVo> getMenuData() {
        return this.menuData;
    }

    public List<AdminSmAuthRecoVo> getCtrData() {
        return this.ctrData;
    }

    public void setMenuData(List<AdminSmAuthRecoVo> menuData) {
        this.menuData = menuData;
    }

    public void setCtrData(List<AdminSmAuthRecoVo> ctrData) {
        this.ctrData = ctrData;
    }
}

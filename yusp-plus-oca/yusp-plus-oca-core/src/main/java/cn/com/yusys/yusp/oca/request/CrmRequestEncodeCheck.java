package cn.com.yusys.yusp.oca.request;

import cn.com.yusys.yusp.common.utils.Constant;
import cn.com.yusys.yusp.commons.req.crypto.RequestEncodeCheck;
import cn.com.yusys.yusp.oca.service.AdminSmAuthInfoService;
import cn.com.yusys.yusp.oca.service.AdminSmResContrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author yusys
 */
@Service
public class CrmRequestEncodeCheck implements RequestEncodeCheck {

    @Autowired
    private AdminSmResContrService contrService;

    @Override
    public boolean check(HttpServletRequest httpServletRequest) {
        String path = httpServletRequest.getRequestURI();
        Set<String> urls = contrService.getRequestCheckList(Constant.REQUEST_ENCODE);
        if(urls != null && urls.contains(path)){
            return true;
        }else {
            return false;
        }
    }
}

package cn.com.yusys.yusp.uaa.service;

import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.uaa.grant.oca.OcaLoginUserInfo;
import cn.com.yusys.yusp.uaa.pojo.LoginUserInfo;
import cn.com.yusys.yusp.uaa.pojo.TokenParamDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * oca 模式获取用户信息
 * @author zhangyt12
 * @date 2021/9/7 11:16
 */
@Service("ocaUserDetailsServiceImpl")
public class OcaUserDetailsServiceImpl implements UserDetailsService {

    private final OcaLoginUserInfo ocaLoginUserInfo;

    public OcaUserDetailsServiceImpl(OcaLoginUserInfo ocaLoginUserInfo) {
        this.ocaLoginUserInfo = ocaLoginUserInfo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 2)获取LoginUserInfo信息
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        String password = httpServletRequest.getParameter("password");
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        LoginUserInfo loginUserInfo = new LoginUserInfo(username, password, simpleGrantedAuthorities);
        TokenParamDto tokenParamDto = ocaLoginUserInfo.getLoginUserInfo(username, password);
        BeanUtils.beanCopy(tokenParamDto, loginUserInfo);

        return loginUserInfo;
    }
}

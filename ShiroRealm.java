package com.linghong.dongdong.shiro;

import com.linghong.dongdong.pojo.User;
import com.linghong.dongdong.respotroy.UserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 根据项目需求更改此认证方式
 */
public class ShiroRealm extends AuthorizingRealm {
    @Resource
    private UserRepository userRepository;

    /**
     * 获取授权信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 身份认证
     * 根据用户登录凭证及密码进行认证
     * 加密进行MD5及加盐
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String mobilePhone = token.getUsername();
        User user = userRepository.findByMobilePhone(mobilePhone);
        if (user == null){
            throw new UnknownAccountException();
        }else {
            ByteSource byteSource = ByteSource.Util.bytes(user.getMobilePhone());
            //用户名  密码 盐 当前的Realm
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(mobilePhone,user.getPassword(),byteSource,getName());
            return info;
        }
    }
}

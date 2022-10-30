package com.example.shiro_springboot.realm;

import com.example.shiro_springboot.pojo.User;
import com.example.shiro_springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义realm类
 *
 * @author xukai
 * @create 2022-10-15 10:33
 */
@Slf4j
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    //自定义授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("进入到了自定义授权方法");
        //1.获取用户身份信息，并根据用户身份信息查询 角色信息 和 角色权限
        String principal = principals.getPrimaryPrincipal().toString();
        List<String> userRoles = userService.getUserRolesInfoByPrincipal(principal);
        List<String> permissionList = userService.getUserPermissionInfoByRname(userRoles);
        log.info("当前用户所对应的角色的权限为={}", permissionList);
        log.info("当前用户所对应的角色为={}", userRoles);

        //2.创建对象，封装当前登录用户的角色、权限信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //3.存储角色
        info.addRoles(userRoles);
        info.addStringPermissions(permissionList);
        //4.返回信息
        return info;
    }

    //自定义认证方法，该方法只是获取进行对比的信息(前端传过来的数据信息)
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.获取用户身份信息
        String name = token.getPrincipal().toString();
        //2.调用业务层的方法获取用户信息（数据库）
        User user = userService.getUserInfoByUsername(name);
        //3.非空判断，将数据封装返回
        if (user != null) {
            AuthenticationInfo info = new SimpleAuthenticationInfo(
                    token.getPrincipal(),
                    user.getPwd(),
                    ByteSource.Util.bytes("salt"),
                    name        //token.getPrincipal().toString()
            );
            return info;
        }
        return null;
    }
}

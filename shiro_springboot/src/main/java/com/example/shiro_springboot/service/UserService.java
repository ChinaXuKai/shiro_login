package com.example.shiro_springboot.service;

import com.example.shiro_springboot.pojo.User;

import java.util.List;

/**
 * @author xukai
 * @create 2022-10-15 10:17
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param name
     * @return
     */
    User getUserInfoByUsername(String name);

    /**
     * 根据用户查询角色信息
     *
     * @param principal
     * @return
     */
    List<String> getUserRolesInfoByPrincipal(String principal);

    /**
     * 获取用户的角色权限信息
     *
     * @param roles
     * @return
     */
    List<String> getUserPermissionInfoByRname(List<String> roles);

}

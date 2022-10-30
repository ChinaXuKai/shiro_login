package com.example.shiro_springboot.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.shiro_springboot.mapper.UserMapper;
import com.example.shiro_springboot.pojo.User;
import com.example.shiro_springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xukai
 * @create 2022-10-15 10:18
 */
@Slf4j
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     *
     * @param name
     * @return
     */
    @Override
    public User getUserInfoByUsername(String name) {
//        QueryWrapper wrapper = new QueryWrapper(User.class, name);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    /**
     * 根据用户查询角色信息
     *
     * @param principal
     * @return
     */
    @Override
    public List<String> getUserRolesInfoByPrincipal(String principal) {
        log.info("当前用户的身份信息={}", principal);
        return userMapper.selectRnameByUname(principal);
    }

    /**
     * 获取用户的角色权限信息
     *
     * @param roles
     * @return
     */
    @Override
    public List<String> getUserPermissionInfoByRname(List<String> roles) {
        return userMapper.getUserPermissionInfoByRname(roles);
    }
}

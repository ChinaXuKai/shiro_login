package com.example.shiro_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shiro_springboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xukai
 * @create 2022-10-15 10:12
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名 查询对应的 所有角色名
     *
     * @param principal 用户名
     * @return 用户名对应的所有角色名
     */
    List<String> selectRnameByUname(@Param("principal") String principal);


    /**
     * 根据角色名查询用户权限
     *
     * @param roles 角色名
     * @return
     */
    List<String> getUserPermissionInfoByRname(@Param("roles") List<String> roles);

}

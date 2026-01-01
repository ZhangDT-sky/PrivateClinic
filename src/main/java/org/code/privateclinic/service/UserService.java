package org.code.privateclinic.service;

import org.code.privateclinic.bean.User;

import java.util.List;

public interface UserService {
    /**
     * 获取所有用户列表
     */
    List<User> getUserList();

    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);

    /**
     * 根据用户名获取用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据角色获取用户列表
     */
    List<User> getUserByRole(String role);

    /**
     * 添加用户
     */
    int addUser(User user);

    /**
     * 更新用户信息
     */
    int updateUser(User user);

    /**
     * 删除用户（逻辑删除）
     */
    int deleteUser(Long id);

    /**
     * 物理删除用户
     */
    int deleteUserPhysical(Long id);
}


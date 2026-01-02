package org.code.privateclinic.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.code.privateclinic.annotation.Loggable;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.mapper.UserMapper;
import org.code.privateclinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CACHE_KEY = "userList";
    private static final long CACHE_EXPIRE_HOURS = 24;

    @Override
    @Loggable("查询用户列表")
    public List<User> getUserList() {
        if (redisTemplate.hasKey(CACHE_KEY)) {
            try {
                String cache = redisTemplate.opsForValue().get(CACHE_KEY);
                if (cache != null && !cache.isEmpty()) {
                    return objectMapper.readValue(cache, new TypeReference<List<User>>() {});
                }
            } catch (Exception e) {
                log.warn("缓存数据反序列化失败，删除缓存键 {}，错误信息: {}", CACHE_KEY, e.getMessage());
                redisTemplate.delete(CACHE_KEY);
            }
        }

        List<User> userList = userMapper.selectUser();
        if (userList != null && !userList.isEmpty()) {
            try {
                String jsonValue = objectMapper.writeValueAsString(userList);
                redisTemplate.opsForValue().set(CACHE_KEY, jsonValue, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            } catch (Exception e) {
                log.error("用户列表数据序列化失败，无法存入缓存，错误信息: {}", e.getMessage());
            }
        }
        return userList;
    }

    @Override
    @Loggable("根据ID查询用户信息")
    public User getUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    @Override
    @Loggable("根据用户名查询用户信息")
    public User getUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    @Override
    @Loggable("根据角色查询用户列表")
    public List<User> getUserByRole(String role) {
        return userMapper.selectUserByRole(role);
    }

    @Override
    @Loggable("添加用户")
    public int addUser(User user) {
        User existingUser = userMapper.selectUserByUserName(user.getUserName());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getRole() == null ||
            (!"ADMIN".equalsIgnoreCase(user.getRole()) && !"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            throw new RuntimeException("角色必须是 ADMIN 或 DOCTOR");
        }
        user.setRole(user.getRole().toUpperCase());

        int result = userMapper.insertUser(user);
        if (result > 0) {
            clearUserListCache();
        }
        return result;
    }

    @Override
    @Loggable("更新用户信息")
    public int updateUser(User user) {
        if (user.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            User existingUser = userMapper.selectUserByUserName(user.getUserName());
            if (existingUser != null && !existingUser.getUserId().equals(user.getUserId())) {
                throw new RuntimeException("用户名已被使用");
            }
        }

        int result = userMapper.updateUser(user);
        if (result > 0) {
            clearUserListCache();
        }
        return result;
    }

    @Override
    @Loggable("逻辑删除用户")
    public int deleteUser(Long id) {
        User user = userMapper.selectUserById(id);
        if (user == null) {
            return 0;
        }
        User updateUser = new User();
        updateUser.setUserId(id);
        updateUser.setRole(user.getRole());
        updateUser.setStatus(0);
        int result = userMapper.updateUser(updateUser);
        if (result > 0) {
            clearUserListCache();
        }
        return result;
    }

    @Override
    @Loggable("物理删除用户")
    public int deleteUserPhysical(Long id) {
        User user = userMapper.selectUserById(id);
        if (user == null) {
            return 0;
        }
        int result = userMapper.deleteUserByIdPhysical(id);
        if (result > 0) {
            clearUserListCache();
        }
        return result;
    }

    private void clearUserListCache() {
        try {
            redisTemplate.delete(CACHE_KEY);
        } catch (Exception e) {
            log.warn("清除用户列表缓存失败，错误信息: {}", e.getMessage());
        }
    }
}

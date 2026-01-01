package org.code.privateclinic.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
    public List<User> getUserList(){
        log.info("开始查询");
        long startTime = System.currentTimeMillis();

        try {
            if(redisTemplate.hasKey(CACHE_KEY)){
                log.debug("缓存键 {} 存在，尝试从缓存获取数据", CACHE_KEY);
                try{
                    String cache = redisTemplate.opsForValue().get(CACHE_KEY);
                    if(cache != null && !cache.isEmpty()){
                        List<User> userList = objectMapper.readValue(
                                cache, new TypeReference<List<User>>(){}
                        );
                        long endTime = System.currentTimeMillis();
                        log.info("从缓存获取用户列表成功，共 {} 条记录，耗时 {} ms",
                                userList.size(), (endTime - startTime));
                        return userList;
                    }
                } catch (Exception e){
                    log.warn("缓存数据反序列化失败，删除缓存键 {}，错误信息: {}",
                            CACHE_KEY, e.getMessage(), e);
                    redisTemplate.delete(CACHE_KEY);
                }
            } else {
                log.debug("缓存键 {} 不存在，将从数据库查询", CACHE_KEY);
            }

            log.info("开始从数据库查询用户列表");
            List<User> userList = userMapper.selectUser();

            if (userList == null || userList.isEmpty()) {
                log.warn("数据库查询结果为空，返回空列表");
                return userList;
            }

            log.info("数据库查询成功，共查询到 {} 条用户记录", userList.size());

            try {
                String jsonValue = objectMapper.writeValueAsString(userList);
                redisTemplate.opsForValue().set(
                        CACHE_KEY,
                        jsonValue,
                        CACHE_EXPIRE_HOURS,
                        TimeUnit.HOURS
                );
                log.info("用户列表数据已存入缓存，缓存键: {}，过期时间: {} 小时",
                        CACHE_KEY, CACHE_EXPIRE_HOURS);
            } catch (Exception e) {
                log.error("用户列表数据序列化失败，无法存入缓存，错误信息: {}",
                        e.getMessage(), e);
            }

            long endTime = System.currentTimeMillis();
            log.info("查询用户列表完成，共 {} 条记录，总耗时 {} ms",
                    userList.size(), (endTime - startTime));

            return userList;

        } catch (Exception e) {
            log.error("查询用户列表发生异常，错误信息: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public User getUserById(Long id) {
        log.info("开始根据ID查询用户信息，用户ID: {}", id);
        long startTime = System.currentTimeMillis();
        try {
            User user = userMapper.selectUserById(id);
            long endTime = System.currentTimeMillis();

            if (user == null) {
                log.warn("未找到ID为 {} 的用户信息，耗时 {} ms", id, (endTime - startTime));
            } else {
                log.info("成功查询到ID为 {} 的用户信息，耗时 {} ms", id, (endTime - startTime));
            }
            return user;
        } catch (Exception e) {
            log.error("根据ID查询用户信息发生异常，用户ID: {}，错误信息: {}",
                    id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("开始根据用户名查询用户信息，用户名: {}", username);
        long startTime = System.currentTimeMillis();

        try {
            User user = userMapper.selectUserByUsername(username);
            long endTime = System.currentTimeMillis();

            if (user == null) {
                log.warn("未找到用户名为 {} 的用户信息，耗时 {} ms", username, (endTime - startTime));
            } else {
                log.info("成功查询到用户名为 {} 的用户信息，耗时 {} ms", username, (endTime - startTime));
            }

            return user;
        } catch (Exception e) {
            log.error("根据用户名查询用户信息发生异常，用户名: {}，错误信息: {}",
                    username, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<User> getUserByRole(String role) {
        log.info("开始根据角色查询用户列表，角色: {}", role);
        long startTime = System.currentTimeMillis();

        try {
            List<User> userList = userMapper.selectUserByRole(role);
            long endTime = System.currentTimeMillis();

            log.info("成功查询到角色为 {} 的用户列表，共 {} 条记录，耗时 {} ms",
                    role, userList != null ? userList.size() : 0, (endTime - startTime));

            return userList;
        } catch (Exception e) {
            log.error("根据角色查询用户列表发生异常，角色: {}，错误信息: {}",
                    role, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int addUser(User user) {
        log.info("开始添加用户，用户名: {}，角色: {}", user.getUsername(), user.getRole());
        long startTime = System.currentTimeMillis();

        try {
            // 检查用户名是否已存在
            User existingUser = userMapper.selectUserByUsername(user.getUsername());
            if (existingUser != null) {
                log.warn("用户名 {} 已存在，无法添加", user.getUsername());
                throw new RuntimeException("用户名已存在");
            }
            // 设置默认状态为启用
            if (user.getStatus() == null) {
                user.setStatus(1);
            }
            // 验证角色（支持大小写，但统一转换为大写存储）
            if (user.getRole() == null || 
                (!"ADMIN".equalsIgnoreCase(user.getRole()) && !"DOCTOR".equalsIgnoreCase(user.getRole()))) {
                log.warn("添加用户失败，角色 {} 无效", user.getRole());
                throw new RuntimeException("角色必须是 ADMIN 或 DOCTOR");
            }
            // 统一转换为大写
            user.setRole(user.getRole().toUpperCase());

            int result = userMapper.insertUser(user);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功添加用户，用户ID: {}，用户名: {}，角色: {}，耗时 {} ms",
                        user.getUserId(), user.getUsername(), user.getRole(), (endTime - startTime));
                // 清除缓存
                clearUserListCache();
            } else {
                log.warn("添加用户失败，用户名: {}，耗时 {} ms", user.getUsername(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("添加用户发生异常，用户名: {}，错误信息: {}",
                    user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int updateUser(User user) {
        log.info("开始更新用户信息，用户ID: {}", user.getUserId());
        long startTime = System.currentTimeMillis();

        try {
            if (user.getUserId() == null) {
                log.error("更新用户信息失败，用户ID为空");
                throw new RuntimeException("用户ID不能为空");
            }

            // 如果更新了用户名，检查新用户名是否已存在
            if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                User existingUser = userMapper.selectUserByUsername(user.getUsername());
                if (existingUser != null && !existingUser.getUserId().equals(user.getUserId())) {
                    log.warn("用户名 {} 已被其他用户使用，无法更新", user.getUsername());
                    throw new RuntimeException("用户名已被使用");
                }
            }

            int result = userMapper.updateUser(user);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功更新用户信息，用户ID: {}，耗时 {} ms",
                        user.getUserId(), (endTime - startTime));
                // 清除缓存
                clearUserListCache();
            } else {
                log.warn("更新用户信息失败，用户ID: {}，耗时 {} ms",
                        user.getUserId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("更新用户信息发生异常，用户ID: {}，错误信息: {}",
                    user.getUserId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deleteUser(Long id) {
        log.info("开始逻辑删除用户，用户ID: {}", id);
        long startTime = System.currentTimeMillis();

        try {
            // 先查询用户，确定角色
            User user = userMapper.selectUserById(id);
            if (user == null) {
                log.warn("逻辑删除用户失败，用户ID: {} 不存在", id);
                return 0;
            }

            // 根据角色更新对应用户信息
            User updateUser = new User();
            updateUser.setUserId(id);
            updateUser.setRole(user.getRole());
            updateUser.setStatus(0);
            
            int result = userMapper.updateUser(updateUser);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功逻辑删除用户，用户ID: {}，角色: {}，耗时 {} ms", id, user.getRole(), (endTime - startTime));
                // 清除缓存
                clearUserListCache();
            } else {
                log.warn("逻辑删除用户失败，用户ID: {}，耗时 {} ms", id, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("逻辑删除用户发生异常，用户ID: {}，错误信息: {}",
                    id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deleteUserPhysical(Long id) {
        log.info("开始物理删除用户，用户ID: {}", id);
        long startTime = System.currentTimeMillis();

        try {
            // 先查询用户，确定角色
            User user = userMapper.selectUserById(id);
            if (user == null) {
                log.warn("物理删除用户失败，用户ID: {} 不存在", id);
                return 0;
            }

            int result = userMapper.deleteUserByIdPhysical(id);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功物理删除用户，用户ID: {}，角色: {}，耗时 {} ms", id, user.getRole(), (endTime - startTime));
                // 清除缓存
                clearUserListCache();
            } else {
                log.warn("物理删除用户失败，用户ID: {}，耗时 {} ms", id, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("物理删除用户发生异常，用户ID: {}，错误信息: {}",
                    id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 清除用户列表缓存
     */
    private void clearUserListCache() {
        try {
            redisTemplate.delete(CACHE_KEY);
            log.debug("已清除用户列表缓存，缓存键: {}", CACHE_KEY);
        } catch (Exception e) {
            log.warn("清除用户列表缓存失败，错误信息: {}", e.getMessage());
        }
    }
}


package org.code.privateclinic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.code.privateclinic.bean.LoginRequestDTO;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.service.AuthService;
import org.code.privateclinic.service.UserService;
import org.code.privateclinic.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(LoginRequestDTO loginRequestDTO) {
        log.info("开始登录验证，用户名: {}", loginRequestDTO.getUserId());
        long startTime = System.currentTimeMillis();

        try {
            if (loginRequestDTO.getUserId() == null || loginRequestDTO.getUserId().isEmpty()) {
                log.warn("登录失败：用户名为空");
                throw new RuntimeException("用户名不能为空");
            }
            if (loginRequestDTO.getPassword() == null || loginRequestDTO.getPassword().isEmpty()) {
                log.warn("登录失败：密码为空");
                throw new RuntimeException("密码不能为空");
            }
            
            String username = loginRequestDTO.getUserId();
            String password = loginRequestDTO.getPassword();
            
            // 查询用户（自动从 doctor 或 admin 表中查找）
            User user = userService.getUserByUsername(username);
            if (user == null) {
                throw new RuntimeException("用户名或密码错误");
            }
            
            // 检查状态
            if (user.getStatus() == null || user.getStatus() != 1) {
                throw new RuntimeException("账户已被禁用");
            }
            
            // 验证密码
            if (!password.equals(user.getPassword())) {
                throw new RuntimeException("用户名或密码错误");
            }
            
            // 生成 JWT Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId().toString());
            claims.put("username", user.getUsername());
            claims.put("userName", user.getUsername());
            claims.put("userType", user.getRole());
            claims.put("userRole", user.getRole());
            
            String token = jwtTokenUtil.generateToken(user.getUserId().toString(), claims);
            
            long endTime = System.currentTimeMillis();
            log.info("登录成功，用户名: {}，角色: {}，用户ID: {}，耗时 {} ms",
                    username, user.getRole(), user.getUserId(), (endTime - startTime));
            
            return token;
            
        } catch (RuntimeException e) {
            log.error("登录失败，用户名: {}，错误信息: {}",
                    loginRequestDTO.getUserId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("登录发生异常，用户名: {}，错误信息: {}",
                    loginRequestDTO.getUserId(), e.getMessage(), e);
            throw new RuntimeException("登录失败：" + e.getMessage());
        }
    }
}

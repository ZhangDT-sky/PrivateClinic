package org.code.privateclinic.service.impl;

import org.code.privateclinic.annotation.Loggable;
import org.code.privateclinic.bean.LoginRequestDTO;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.service.AuthService;
import org.code.privateclinic.service.UserService;
import org.code.privateclinic.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Loggable("用户登录")
    public String login(LoginRequestDTO loginRequestDTO) {
        if (loginRequestDTO.getUserId() == null || loginRequestDTO.getUserId().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (loginRequestDTO.getPassword() == null || loginRequestDTO.getPassword().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        
        String userName = loginRequestDTO.getUserId();
        String password = loginRequestDTO.getPassword();
        
        // 查询用户（自动从 doctor 或 admin 表中查找）
        User user = userService.getUserByUserName(userName);
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
        claims.put("userName", user.getUserName());
        claims.put("userType", user.getRole());
        claims.put("userRole", user.getRole());
        
        return jwtTokenUtil.generateToken(user.getUserId().toString(), claims);
    }
}

package org.code.privateclinic.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.UserService;
import org.code.privateclinic.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 获取所有用户列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<User>> getUserList(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能查看用户列表
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权查看用户列表");
        }
        return ResponseMessage.success(userService.getUserList());
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{userId}")
    public ResponseMessage<User> getUserById(HttpServletRequest request, @PathVariable Long userId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User currentUser = userService.getUserByUserName(username);
        // 只有管理员能查看用户信息
        if (currentUser == null || (!"ADMIN".equalsIgnoreCase(currentUser.getRole()))) {
            return ResponseMessage.failed("只有管理员有权查看用户信息");
        }
        User user = userService.getUserById(userId);
        if(user == null){
            return ResponseMessage.failed("未查询到相关用户信息");
        }
        return ResponseMessage.success(user);
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/userName/{userName}")
    public ResponseMessage<User> getUserByUserName(HttpServletRequest request, @PathVariable String userName){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User currentUser = userService.getUserByUserName(username);
        // 只有管理员能查看用户信息
        if (currentUser == null || (!"ADMIN".equalsIgnoreCase(currentUser.getRole()))) {
            return ResponseMessage.failed("只有管理员有权查看用户信息");
        }
        User user = userService.getUserByUserName(userName);
        if(user == null){
            return ResponseMessage.failed("未查询到相关用户信息");
        }
        return ResponseMessage.success(user);
    }

    /**
     * 根据角色获取用户列表
     */
    @GetMapping("/role/{role}")
    public ResponseMessage<List<User>> getUserByRole(HttpServletRequest request, @PathVariable String role){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能查看用户列表
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权查看用户列表");
        }
        return ResponseMessage.success(userService.getUserByRole(role));
    }

    /**
     * 添加用户
     */
    @PostMapping
    public ResponseMessage<User> addUser(HttpServletRequest request, @RequestBody User user){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User currentUser = userService.getUserByUserName(username);
        // 只有管理员能添加用户
        if (currentUser == null || (!"ADMIN".equalsIgnoreCase(currentUser.getRole()))) {
            return ResponseMessage.failed("只有管理员有权添加用户");
        }
        try {
            int result = userService.addUser(user);
            if(result > 0){
                return ResponseMessage.success(user, "添加用户成功");
            } else {
                return ResponseMessage.failed("添加用户失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    public ResponseMessage<String> updateUser(HttpServletRequest request, @RequestBody User user){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User currentUser = userService.getUserByUserName(username);
        // 只有管理员能更新用户信息
        if (currentUser == null || (!"ADMIN".equalsIgnoreCase(currentUser.getRole()))) {
            return ResponseMessage.failed("只有管理员有权更新用户信息");
        }
        try {
            int result = userService.updateUser(user);
            if(result > 0){
                return ResponseMessage.success("更新用户信息成功");
            } else {
                return ResponseMessage.failed("更新用户信息失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 逻辑删除用户（更新状态为0）
     */
    @DeleteMapping("/{userId}")
    public ResponseMessage<String> deleteUser(HttpServletRequest request, @PathVariable Long userId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User currentUser = userService.getUserByUserName(username);
        // 只有管理员能删除用户
        if (currentUser == null || (!"ADMIN".equalsIgnoreCase(currentUser.getRole()))) {
            return ResponseMessage.failed("只有管理员有权删除用户");
        }
        try {
            int result = userService.deleteUser(userId);
            if(result > 0){
                return ResponseMessage.success("删除用户成功");
            } else {
                return ResponseMessage.failed("删除用户失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 物理删除用户
     */
    @DeleteMapping("/physical/{userId}")
    public ResponseMessage<String> deleteUserPhysical(HttpServletRequest request, @PathVariable Long userId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User currentUser = userService.getUserByUserName(username);
        // 只有管理员能物理删除用户
        if (currentUser == null || (!"ADMIN".equalsIgnoreCase(currentUser.getRole()))) {
            return ResponseMessage.failed("只有管理员有权物理删除用户");
        }
        try {
            int result = userService.deleteUserPhysical(userId);
            if(result > 0){
                return ResponseMessage.success("物理删除用户成功");
            } else {
                return ResponseMessage.failed("物理删除用户失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }
}


package org.code.privateclinic.controller;

import org.code.privateclinic.bean.User;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<User>> getUserList(){
        return ResponseMessage.success(userService.getUserList());
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{userId}")
    public ResponseMessage<User> getUserById(@PathVariable Long userId){
        User user = userService.getUserById(userId);
        if(user == null){
            return ResponseMessage.failed("未查询到相关用户信息");
        }
        return ResponseMessage.success(user);
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/username/{username}")
    public ResponseMessage<User> getUserByUsername(@PathVariable String username){
        User user = userService.getUserByUsername(username);
        if(user == null){
            return ResponseMessage.failed("未查询到相关用户信息");
        }
        return ResponseMessage.success(user);
    }

    /**
     * 根据角色获取用户列表
     */
    @GetMapping("/role/{role}")
    public ResponseMessage<List<User>> getUserByRole(@PathVariable String role){
        return ResponseMessage.success(userService.getUserByRole(role));
    }

    /**
     * 添加用户
     */
    @PostMapping
    public ResponseMessage<User> addUser(@RequestBody User user){
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
    public ResponseMessage<String> updateUser(@RequestBody User user){
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
    public ResponseMessage<String> deleteUser(@PathVariable Long userId){
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
    public ResponseMessage<String> deleteUserPhysical(@PathVariable Long userId){
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


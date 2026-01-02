package org.code.privateclinic.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.code.privateclinic.bean.Drug;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.DrugService;
import org.code.privateclinic.service.UserService;
import org.code.privateclinic.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    /**
     * 获取所有药品列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<Drug>> getDrugList(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能查看药品列表
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权查看药品列表");
        }
        return ResponseMessage.success(drugService.getDrugList());
    }

    /**
     * 根据ID获取药品信息
     */
    @GetMapping("/{drugId}")
    public ResponseMessage<Drug> getDrugById(HttpServletRequest request, @PathVariable Long drugId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能查看药品信息
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权查看药品信息");
        }
        Drug drug = drugService.getDrugById(drugId);
        if(drug == null){
            return ResponseMessage.failed("未查询到相关药品信息");
        }
        return ResponseMessage.success(drug);
    }

    /**
     * 根据药品名称获取药品信息
     */
    @GetMapping("/name/{drugName}")
    public ResponseMessage<Drug> getDrugByName(HttpServletRequest request, @PathVariable String drugName){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能查看药品信息
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权查看药品信息");
        }
        Drug drug = drugService.getDrugByName(drugName);
        if(drug == null){
            return ResponseMessage.failed("未查询到相关药品信息");
        }
        return ResponseMessage.success(drug);
    }

    /**
     * 添加药品
     */
    @PostMapping
    public ResponseMessage<Drug> addDrug(HttpServletRequest request, @RequestBody Drug drug){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能添加药品
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权添加药品");
        }
        try {
            int result = drugService.addDrug(drug);
            if(result > 0){
                return ResponseMessage.success(drug, "添加药品成功");
            } else {
                return ResponseMessage.failed("添加药品失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新药品信息
     */
    @PutMapping
    public ResponseMessage<String> updateDrug(HttpServletRequest request, @RequestBody Drug drug){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能更新药品信息
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权更新药品信息");
        }
        try {
            int result = drugService.updateDrug(drug);
            if(result > 0){
                return ResponseMessage.success("更新药品信息成功");
            } else {
                return ResponseMessage.failed("更新药品信息失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 逻辑删除药品（更新状态为0）
     */
    @DeleteMapping("/{drugId}")
    public ResponseMessage<String> deleteDrug(HttpServletRequest request, @PathVariable Long drugId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能删除药品
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权删除药品");
        }
        try {
            int result = drugService.deleteDrug(drugId);
            if(result > 0){
                return ResponseMessage.success("删除药品成功");
            } else {
                return ResponseMessage.failed("删除药品失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 物理删除药品
     */
    @DeleteMapping("/physical/{drugId}")
    public ResponseMessage<String> deleteDrugPhysical(HttpServletRequest request, @PathVariable Long drugId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能物理删除药品
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权物理删除药品");
        }
        try {
            int result = drugService.deleteDrugPhysical(drugId);
            if(result > 0){
                return ResponseMessage.success("物理删除药品成功");
            } else {
                return ResponseMessage.failed("物理删除药品失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新药品库存
     */
    @PutMapping("/stock/{drugId}")
    public ResponseMessage<String> updateStock(HttpServletRequest request, @PathVariable Long drugId, @RequestParam Integer stock){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有管理员能更新药品库存
        if (user == null || (!"ADMIN".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有管理员有权更新药品库存");
        }
        try {
            int result = drugService.updateStock(drugId, stock);
            if(result > 0){
                return ResponseMessage.success("更新药品库存成功");
            } else {
                return ResponseMessage.failed("更新药品库存失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }
}


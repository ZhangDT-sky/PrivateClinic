package org.code.privateclinic.controller;

import org.code.privateclinic.bean.LoginRequestDTO;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseMessage<String> login(@RequestBody @Validated LoginRequestDTO loginRequestDTO) {
        try {
            String token = authService.login(loginRequestDTO);
            return ResponseMessage.success(token, "登录成功");
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }
}

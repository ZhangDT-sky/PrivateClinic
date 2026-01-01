package org.code.privateclinic.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long userId;
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(max = 100, message = "密码长度不能超过100个字符")
    private String password;
    

    @Size(max = 50, message = "用户姓名长度不能超过50个字符")
    private String userName;
    

    @NotBlank(message = "角色不能为空")
    @Size(max = 20, message = "角色长度不能超过20个字符")
    @Pattern(regexp = "^(ADMIN|DOCTOR|admin|doctor)$", message = "角色必须是 ADMIN 或 DOCTOR")
    private String role;

    @Size(max = 20, message = "手机号长度不能超过20个字符")
    private String phone;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}


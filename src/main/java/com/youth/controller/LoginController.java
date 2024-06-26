package com.youth.controller;

import com.youth.common.Token;
import com.youth.common.UserDetailsImpl;
import com.youth.entity.User;
import com.youth.response.ResponseResult;
import com.youth.service.LoginService;
import com.youth.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
@Tag(name = "用户登录", description = "用户登录、刷新令牌、注销")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录")
    @Parameter(name = "user", description = "用户登录", required = true)
    @PostMapping("/userLogin")
    public ResponseResult<Token> userLogin(@RequestBody User user) {
        return loginService.userLogin(user);
    }


    /**
     * 登出/注销
     */
    @Operation(summary = "注销", description = "注销")
    @PostMapping("/logout")
    public ResponseResult logout(HttpServletRequest request) {
        return loginService.logout(request);
    }

    /**
     * 刷新令牌
     */
    @Operation(summary = "刷新令牌", description = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    @PostMapping("/refreshToken/{refreshToken}")
    public ResponseResult<Token> refreshToken(@PathVariable("refreshToken") String refreshToken) {
        return loginService.refreshToken(refreshToken);
    }



//    @Operation(summary = "注销", description = "注销")
//    @Parameter(name = "token", description = "访问令牌", required = true)
//    @PostMapping("/logOut/{token}")
//    public ResponseResult<String> logOut(@PathVariable("token") String token) {
//        // 放入黑名单
//        jwtUtil.addBlacklist(token, jwtUtil.getExpirationDate(token));
//        return ResponseResult.success("注销成功");
//    }

}

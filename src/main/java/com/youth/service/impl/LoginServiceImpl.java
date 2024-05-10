package com.youth.service.impl;

import com.youth.common.Token;
import com.youth.common.UserDetailsImpl;
import com.youth.dao.LoginDao;
import com.youth.entity.User;
import com.youth.enums.RedisKeyEnum;
import com.youth.response.ResponseResult;
import com.youth.service.LoginService;
import com.youth.utils.JwtUtil;
import com.youth.utils.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    LoginDao loginDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResponseResult<Token> userLogin(User user) {
        // 获取认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 认证没通过给出提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登陆失败");
        }

        //认证通过后，使用userId生成一个jwt，存入ResponseResult返回
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        String id = userDetails.getUser().getId().toString();

        Map<String, Object> payload = new HashMap<>();
        payload.put("user", userDetails);
        Token token = jwtUtil.createTokens(payload);
        jwtUtil.tokenAssociation(token);
        redisUtil.set(RedisKeyEnum.LOGIN_INFO.getKey() + id, userDetails, jwtUtil.getRefreshTokenExpire());
        return ResponseResult.success(token, "登录成功");
    }

    @Override
    public ResponseResult logout(HttpServletRequest request) {
        String token = request.getHeader(jwtUtil.getHeader());
        // 将token放入黑名单
        jwtUtil.addBlacklist(token, jwtUtil.getExpirationDate(token));
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        Long id = userDetailsImpl.getUser().getId();
        // 删除redis中的值
        redisUtil.remove(RedisKeyEnum.LOGIN_INFO.getKey() + id);
        return ResponseResult.success(null, "注销成功");
    }

    @Override
    public ResponseResult<Token> refreshToken(String refreshToken) {
        if (StringUtils.isEmpty(refreshToken)) {
            return ResponseResult.failed("refreshToken不可为空");
        }

        // 判断token是否超时
        if (jwtUtil.isTokenExpired(refreshToken)) {
            return ResponseResult.failed("refreshToken已超时");
        }
        // 判断refreshToken是否在黑名单
        if (jwtUtil.checkBlacklist(refreshToken)) {
            return ResponseResult.failed("refreshToken已失效");
        }
        // 刷新令牌 放入黑名单
        jwtUtil.addBlacklist(refreshToken, jwtUtil.getExpirationDate(refreshToken));
        // 访问令牌 放入黑名单
        String accessToken = jwtUtil.getAccessTokenByRefresh(refreshToken);
        if (!StringUtils.isEmpty(accessToken)) {
            jwtUtil.addBlacklist(accessToken, jwtUtil.getExpirationDate(accessToken));
        }

        // 生成新的 访问令牌 和 刷新令牌
        UserDetailsImpl userDetailsImpl = jwtUtil.getMyUserDetails(refreshToken);
        String id = userDetailsImpl.getUser().getId().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("user", userDetailsImpl);
        // 生成Token
        Token token = jwtUtil.createTokens(map);
        // 关联token存入redis
        jwtUtil.tokenAssociation(token);
        //将userDetails存入redis
        redisUtil.set(RedisKeyEnum.LOGIN_INFO.getKey() + id, userDetailsImpl, jwtUtil.getRefreshTokenExpire());
        return ResponseResult.success(token, "刷新成功");
    }
}

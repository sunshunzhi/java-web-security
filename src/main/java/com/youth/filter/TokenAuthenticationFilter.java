package com.youth.filter;

import com.alibaba.fastjson.JSON;
import com.youth.common.UserDetailsImpl;
import com.youth.enums.RedisKeyEnum;
import com.youth.utils.JwtUtil;
import com.youth.utils.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader(jwtUtil.getHeader());

        if (!StringUtils.hasText(token)) {
            // 如果没有token则进行放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        String userId;
        boolean rightToken = jwtUtil.isRightToken(token);
        if (!rightToken) {
            throw new RuntimeException(jwtUtil.getHeader() + "非法");
        } else {
            UserDetailsImpl userDetailsImpl = jwtUtil.getMyUserDetails(token);
            userId = userDetailsImpl.getUser().getId().toString();
        }
        // 从redis中获取用户信息
        Object o = redisUtil.get(RedisKeyEnum.LOGIN_INFO.getKey() + userId);
        if (Objects.isNull(o)) {
            throw new RuntimeException("用户未登录");
        }
        UserDetailsImpl userDetails = JSON.parseObject(o.toString(), UserDetailsImpl.class);
        // 存入SecurityContextHolder
        //  获取权限信息，封装到Authentication
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        // 放行
        filterChain.doFilter(request, response);
    }
}

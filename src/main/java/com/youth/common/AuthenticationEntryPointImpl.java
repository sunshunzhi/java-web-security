package com.youth.common;

import com.alibaba.fastjson.JSON;
import com.youth.enums.ResultEnum;
import com.youth.response.ResponseResult;
import com.youth.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        ResponseResult result = ResponseResult.failed(ResultEnum.UNAUTHORIZED);
        String s = JSON.toJSONString(result);
        // 处理异常
        WebUtil.renderString(response, s);
    }
}



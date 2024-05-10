package com.youth.common;

import com.alibaba.fastjson.JSON;
import com.youth.enums.ResultEnum;
import com.youth.response.ResponseResult;
import com.youth.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedHandlerImpl implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        ResponseResult result = ResponseResult.failed(ResultEnum.FORBIDDEN);
        String s = JSON.toJSONString(result);
        WebUtil.renderString(response, s);
    }
}


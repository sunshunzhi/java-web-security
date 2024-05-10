package com.youth.service;

import com.youth.common.Token;
import com.youth.entity.User;
import com.youth.response.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {

    ResponseResult<Token> userLogin(User user);

    ResponseResult logout(HttpServletRequest request);

    ResponseResult<Token> refreshToken(String refreshToken);
}

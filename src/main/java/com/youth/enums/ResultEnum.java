package com.youth.enums;

import com.youth.common.IErrorCode;
import lombok.Getter;

/**
 * API返回码封装类
 */
@Getter
public enum ResultEnum implements IErrorCode {

    SUCCESS(200, "操作成功"),

    FAILED(500, "操作失败"),

    VALIDATE_FAILED(404, "参数检验失败"),

    UNAUTHORIZED(401, "暂未登录或密钥已经过期"),

    FORBIDDEN(403, "没有相关权限");



    private long code;

    private String message;

    ResultEnum(long code, String message) {
        this.code = code;
        this.message = message;
    }

}

package com.sjz.mock.model;

public enum ResponseCodeEnum {

    REQUEST_SUCCESS("1000","请求成功"),
    REQUEST_FAIL("1001","请求响应失败通用码"),
    DATA_CHECK_FAIL("1002","数据校验失败"),
    DATA_NOT_EXIST("1003","数据不存在"),

    OAUTH_NOT_LOGIN("2000", "用户未登录"),
    OAUTH_NOT_AUTHORITY("2001", "用户无权限"),
    OAUTH_ACCOUNT_UNKNOWN("2002", "用户名/密码错误"),
    OAUTH_ACCOUNT_LOCKED("2003", "用户被锁定"),
    OAUTH_LOGIN_EXCESSIVE("2004", "用户登录次数超过系统限制"),
    OAUTH_SESSION_EXPIRED("2005", "用户SESSION过期"),
    OAUTH_VERIFY_CODE_NOT_CORRECT("2006", "验证码不正确")

    ;

    private String code;
    private String msg;

    ResponseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

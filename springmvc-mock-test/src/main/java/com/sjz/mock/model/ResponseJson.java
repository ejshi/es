package com.sjz.mock.model;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 返回值
 * @param <T>
 */
public class ResponseJson<T> implements java.io.Serializable {
    /**
     * 结果，true:请求成功,false:请求失败.
     */
    private boolean result;

    /**
     * 返回结果码.
     */
    private String code;

    /**
     * 相应描述.
     */
    private String msg;

    /**
     * 具体每个输入错误的消息.
     */
    private Map<String, String> errors = Maps.newHashMap();

    /**
     * 返回的数据.
     */
    private T data;

    /**
     * 返回的数据条数
     */
    private Long total;

    public ResponseJson(boolean result, String code, String msg) {
        super();
        this.result = result;
        this.code = code;
        this.msg = msg;
    }

    public ResponseJson(boolean result, ResponseCodeEnum responseCode) {
        super();
        this.result = result;
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public ResponseJson(boolean result, ResponseCodeEnum responseCode, String msg) {
        super();
        this.result = result;
        this.code = responseCode.getCode();
        this.msg = msg;
    }

    public ResponseJson(boolean result, ResponseCodeEnum responseCode, T data) {
        super();
        this.result = result;
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.data = data;
    }

    public ResponseJson(boolean result, ResponseCodeEnum responseCode, T data, Long total) {
        super();
        this.result = result;
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.data = data;
        this.total = total;
    }

    public ResponseJson(boolean result, ResponseCodeEnum responseCode, Map<String, String> errors) {
        super();
        this.result = result;
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.errors = errors;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
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

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}

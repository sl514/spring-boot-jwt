/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.common.utils;

import io.renren.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@Schema(description = "响应")
public class Result<T> implements Serializable {

    /**
     * 编码：0表示成功，其他值表示失败
     */
    @Schema(description = "编码：0表示成功，其他值表示失败")
    private int code = 0;
    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String msg = "success";

    private String status;

    private Object attach;
    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    public Result<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public Result<T> ok(T data,String status) {
        this.setData(data);
        this.setStatus(status);
        return this;
    }


    public boolean success() {
        return code == 0;
    }

    public Result<T> error() {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public Result<T> error(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Result<T> error(String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }

}
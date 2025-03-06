package murraco.core;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 编码：0表示成功，其他值表示失败
     */
    private int code = 0;
    /**
     * 消息内容
     */
    private String msg = "success";

    private String status;

    private Object attach;
    /**
     * 响应数据
     */
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


    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Result<T> error(String msg) {
        this.code = -1;
        this.msg = msg;
        return this;
    }
}
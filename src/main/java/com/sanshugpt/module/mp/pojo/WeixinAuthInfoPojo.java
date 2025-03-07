package com.sanshugpt.module.mp.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Author liang.sha
 * @Date 9/7/2024 下午 3:51
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class WeixinAuthInfoPojo {
    private String openid;
    private String unionid;
    private String nickname;
    private String gender;
    private String avatar;

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 密码
     */
    //
    private String headimgurl;
    private String token;
    private int sex;
    /**
     * 创建时间
     */
    private Date createDate;
}

package com.sanshugpt.module.mp.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName mp_account
 */
@TableName(value ="mp_account")
@Data
public class MpAccount {
    private Long id;

    private String name;

    private String appId;

    private String appSecret;

    private String token;

    private String aesKey;

    private Long creator;

    private Date createDate;

    private Long updater;

    private Date updateDate;
}
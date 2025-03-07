package com.sanshugpt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@TableName(value ="app_user_roles")
public class AppUserRoles implements GrantedAuthority {
    public static final String ROLE_CLIENT = "ROLE_CLIENT";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    private Long appUserId;
    private Long appUserRoles;
    @TableField(exist = false)
    private String roleName;
    public String getAuthority() {
    return roleName;
  }

}

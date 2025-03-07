package com.sanshugpt.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRolesVO implements GrantedAuthority {
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

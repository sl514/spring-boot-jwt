package com.sanshugpt.vo;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class AppUserVO {
  private Integer id;

  @Schema(description = "用户名", example = "zhangsan", required = true)
  private String username;

  @Schema(description = "用户邮箱", example = "zhangsan@example.com")
  private String email;

  @Schema(description = "密码")
  @NotNull(message = "密码不能为空")
  @JSONField(serialize = false)
  @JsonIgnore
  private String password;
  private String openid;

  @Schema(description = "用户角色列表")
  private List<AppUserRolesVO> appUserRoles;
}

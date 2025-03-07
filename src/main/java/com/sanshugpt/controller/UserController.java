package com.sanshugpt.controller;

import com.sanshugpt.module.common.utils.ConvertUtils;
import com.sanshugpt.module.common.utils.Result;
import com.sanshugpt.service.AppUserService;
import com.sanshugpt.vo.AppUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "用户管理", description = "用户相关操作")  // 替换 @Api
@RequiredArgsConstructor
public class UserController {

    @Resource
    private AppUserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录")
    public Result<String> login(
            @Parameter(description = "用户名", example = "zhangsan") @RequestParam String username,
            @Parameter(description = "密码", example = "123456") @RequestParam String password) {
        return userService.login(username, password);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册")
    public String signup(
            @Parameter(description = "用户注册信息") @RequestBody AppUserVO user) {
        return userService.register(user);
    }


    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "查询用户", description = "管理员根据用户名查询用户信息")
    public AppUserVO search(
            @Parameter(description = "用户名") @PathVariable String username) {
        return ConvertUtils.sourceToTarget (userService.getByUsername(username), AppUserVO.class);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @Operation(summary = "获取当前用户信息", description = "管理员和客户端用户都可以查看自己的信息")
    public Result<AppUserVO> whoami(HttpServletRequest req) {
        return new Result<AppUserVO>().ok(ConvertUtils.sourceToTarget(userService.whoami(req), AppUserVO.class));
    }


}
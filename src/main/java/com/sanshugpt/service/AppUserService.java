package com.sanshugpt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sanshugpt.entity.AppUser;
import com.sanshugpt.module.common.utils.Result;
import com.sanshugpt.vo.AppUserVO;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author sanshu
* @description 针对表【app_user】的数据库操作Service
* @createDate 2025-03-06 22:20:46
*/
public interface AppUserService extends IService<AppUser> {
    public Result<String> login(String username, String password);
    public String register(AppUserVO appUser);
    public AppUserVO whoami(HttpServletRequest req);
    public String refresh(String username);

    public AppUserVO getByUsername(String username);
}

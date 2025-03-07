package com.sanshugpt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sanshugpt.entity.AppUserRoles;
import com.sanshugpt.vo.AppUserRolesVO;

import java.util.List;

/**
* @author sanshu
* @description 针对表【app_user_roles】的数据库操作Service
* @createDate 2025-03-06 21:40:17
*/
public interface AppUserRolesService extends IService<AppUserRoles> {
    public List<AppUserRolesVO> getAppUserRoles(Integer appUserId);
    void insert(AppUserRoles appUserRole);
}

package com.sanshugpt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sanshugpt.entity.AppUserRoles;
import com.sanshugpt.vo.AppUserRolesVO;
import com.sanshugpt.module.common.utils.ConvertUtils;
import com.sanshugpt.service.AppUserRolesService;
import com.sanshugpt.dao.AppUserRolesDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author sanshu
* @description 针对表【app_user_roles】的数据库操作Service实现
* @createDate 2025-03-06 21:40:17
*/
@Service
public class AppUserRolesServiceImpl extends ServiceImpl<AppUserRolesDao, AppUserRoles>
    implements AppUserRolesService{
    @Override
    public List<AppUserRolesVO> getAppUserRoles(Integer appUserId) {
        LambdaQueryWrapper<AppUserRoles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppUserRoles::getAppUserId,appUserId);
        List<AppUserRoles> appUserRoles = this.list(queryWrapper);
        return ConvertUtils.sourceToTarget(appUserRoles, AppUserRolesVO.class);
    }

    @Override
    public void insert(AppUserRoles appUserRole) {
        this.save(appUserRole);
    }
}





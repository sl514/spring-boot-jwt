package com.sanshugpt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sanshugpt.entity.AppUser;
import com.sanshugpt.entity.AppUserRoles;
import com.sanshugpt.enums.EnumRole;
import com.sanshugpt.exception.CustomException;
import com.sanshugpt.dao.AppUserDao;
import com.sanshugpt.vo.AppUserRolesVO;
import com.sanshugpt.module.common.utils.ConvertUtils;
import com.sanshugpt.module.common.utils.Result;
import com.sanshugpt.security.JwtTokenProvider;
import com.sanshugpt.service.AppUserRolesService;
import com.sanshugpt.service.AppUserService;
import com.sanshugpt.vo.AppUserVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author sanshu
* @description 针对表【app_user】的数据库操作Service实现
* @createDate 2025-03-06 22:20:46
*/
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserDao, AppUser>
    implements AppUserService{

    @Resource
    private AppUserDao userMapper;
    @Resource
    private  PasswordEncoder passwordEncoder;
    @Resource
    private  JwtTokenProvider jwtTokenProvider;
    @Resource
    private  AuthenticationManager authenticationManager;

    @Resource
    AppUserRolesService appUserRolesService;

    @Override
    public AppUserVO getByUsername(String username) {
        AppUser appUser = userMapper.findByUsername(username);
        if (appUser == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return ConvertUtils.sourceToTarget(appUser, AppUserVO.class);
    }


    public Result<String> login(String username, String password) {
        try {
            Result<String> result = new Result<>();
            AppUserVO userDTO =  getByUsername(username);
            if(userDTO == null){
                return result.error("账号或密码错误");
            }
            if(!passwordEncoder.matches(password, userDTO.getPassword())){
                return result.error("账号或密码错误");
            }

            List<AppUserRolesVO> appUserRoles = appUserRolesService.getAppUserRoles(userDTO.getId());
            String token = jwtTokenProvider.createToken(username, appUserRoles);
            return new Result<String>().ok(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new Result<String>().error("账号密码错误");
        }
    }



    @Transactional
    public String register(AppUserVO appUser) {
        AppUser user = userMapper.findByUsername(appUser.getUsername());

        if (user == null) {
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            user = ConvertUtils.sourceToTarget(appUser, AppUser.class);
            userMapper.insert(user);
            AppUserRoles appUserRole=  AppUserRoles.builder().appUserId(user.getId()).appUserRoles(EnumRole.ROLE_CLIENT.getId()).build();
            appUserRolesService.insert(appUserRole);
            appUserRole.setRoleName(EnumRole.ROLE_CLIENT.getRoleName());
            return jwtTokenProvider.createToken(appUser.getUsername(), List.of(ConvertUtils.sourceToTarget(appUserRole, AppUserRolesVO.class)));
        } else {
            throw new CustomException("用户已存在", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }



    public AppUserVO whoami(HttpServletRequest req) {
        AppUser appUser =  userMapper.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
        if (appUser == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return ConvertUtils.sourceToTarget(appUser, AppUserVO.class);
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, ConvertUtils.sourceToTarget(userMapper.findByUsername(username).getAppUserRoles(), AppUserRolesVO.class) );
    }

}





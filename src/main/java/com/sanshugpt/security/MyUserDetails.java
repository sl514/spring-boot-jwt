package com.sanshugpt.security;

import com.sanshugpt.dao.AppUserRolesDao;
import com.sanshugpt.entity.AppUser;
import com.sanshugpt.dao.AppUserDao;
import com.sanshugpt.entity.AppUserRoles;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

  @Resource
  private AppUserDao userMapper;

  @Resource
  AppUserRolesDao appUserRolesDao;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     AppUser appUser = userMapper.findByUsername(username);

    if (appUser == null) {
      throw new UsernameNotFoundException("User '" + username + "' not found");
    }

    List<AppUserRoles> roles =appUserRolesDao.findRolesByUserId(appUser.getId());

    return org.springframework.security.core.userdetails.User//
        .withUsername(username)//
        .password(appUser.getPassword())//
        .authorities(roles)//
        .accountExpired(false)//
        .accountLocked(false)//
        .credentialsExpired(false)//
        .disabled(false)//
        .build();
  }

}

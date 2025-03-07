package com.sanshugpt.dao;

import com.sanshugpt.entity.AppUser;
import com.sanshugpt.entity.AppUserRoles;
import com.sanshugpt.module.common.dao.BaseDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author sanshu
* @description 针对表【app_user】的数据库操作Mapper
* @createDate 2025-03-06 22:20:46
* @Entity com.sanshugpt.domain.AppUser
*/
@Mapper
public interface AppUserDao extends BaseDao<AppUser> {
    @Select("SELECT u.* " +
            "FROM app_user u " +
            "WHERE u.username = #{username}")
    AppUser findByUsername(@Param("username") String username);

}





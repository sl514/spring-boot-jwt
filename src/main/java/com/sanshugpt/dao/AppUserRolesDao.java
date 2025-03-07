package com.sanshugpt.dao;

import com.sanshugpt.entity.AppUserRoles;
import com.sanshugpt.module.common.dao.BaseDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author sanshu
* @description 针对表【app_user_roles】的数据库操作Mapper
* @createDate 2025-03-06 21:40:17
* @Entity com.sanshugpt.domain.AppUserRoles
*/
@Mapper
public interface AppUserRolesDao extends BaseDao<AppUserRoles> {

    @Select("SELECT r.*,mr.role_name FROM app_user_roles r " +
            "INNER JOIN m_roles mr ON r.app_user_roles = mr.id " +
            "WHERE r.app_user_id = #{userId}")
    @Results({
            @Result(property = "roleName", column = "role_name", javaType = String.class) // 映射 roleName 为 String
    })
    List<AppUserRoles> findRolesByUserId(@Param("userId") Long userId);
}





package com.sanshugpt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName m_roles
 */
@TableName(value ="m_roles")
@Data
public class MRoles {
    private Long id;

    private String roleName;
}
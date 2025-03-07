package com.sanshugpt.enums;

import lombok.Data;

public enum EnumRole {
    ROLE_ADMIN(0L, "ROLE_ADMIN"),
    ROLE_CLIENT(1L, "ROLE_CLIENT");

    private final Long id;
    private final String roleName;

    EnumRole(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public static EnumRole fromId(Long id) {
        for (EnumRole role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found for id: " + id);
    }

    public static EnumRole fromRoleName(String roleName) {
        for (EnumRole role : values()) {
            if (role.roleName.equals(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found for name: " + roleName);
    }
}
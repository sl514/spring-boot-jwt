package com.sanshugpt.qo;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AppUserQO {

    @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    private String username;

    private String email;

    @Size(min = 8, message = "Minimum password length: 8 characters")
    private String password;
}

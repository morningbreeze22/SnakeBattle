package com.snakebattle.backend.service.impl.utils;

import com.snakebattle.backend.pojo.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    public static User getUser() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authentication.getPrincipal();
        return loginUser.getUser();
    }
}


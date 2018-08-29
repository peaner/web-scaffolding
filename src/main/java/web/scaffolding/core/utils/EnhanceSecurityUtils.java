package web.scaffolding.core.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import web.scaffolding.service.user.CustomUserDetail;

public class EnhanceSecurityUtils {

    public static CustomUserDetail retrieveEnhanceUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            return new CustomUserDetail();
        }

        Object principal = authentication.getPrincipal();

        if (principal == null || principal instanceof String) {
            return new CustomUserDetail();
        }

        return (CustomUserDetail) principal;
    }
}

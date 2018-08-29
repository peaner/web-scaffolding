package web.scaffolding.service.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetail implements UserDetails {

    private static final long serialVersionUID = -139774883890085697L;

    private Long userId;

    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 用户账户是否是可用的
     */
    private boolean enabled = true;

    /**
     * 用户账户是否过期
     */
    private boolean accountNonExpired;

    /**
     * 用户账户是否锁定
     */
    private boolean accountNonLocked;

    /**
     * 证书是否过期
     */
    private boolean credentialsNonExpired;

}

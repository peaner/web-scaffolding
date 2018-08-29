package web.scaffolding.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 处理登录后的跳转，如果用户登录成功后，跳转到访问被拦截的页面
 */
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private String loginUri = "/login";

    private String logoutUri = "/logout";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String path = request.getContextPath() ;
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

        if (savedRequest == null) {
            response.sendRedirect("/***");
            return;
        }

        if (roles.contains("ROLE_ADMIN")){
            response.sendRedirect(basePath + savedRequest.getRequestURI());
            return;
        }
        if (roles.contains("ROLE_USER")) {
            response.sendRedirect(basePath + savedRequest.getRequestURI());
            return;
        }

        String redirectUri = savedRequest.getRequestURI().toLowerCase();
        if (redirectUri.startsWith(loginUri)
                || redirectUri.startsWith(logoutUri)) {
            redirectUri = "/";
        }
        response.sendRedirect(redirectUri);
    }
}

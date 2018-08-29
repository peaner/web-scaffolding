package web.scaffolding.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()

                //todo Refused to display 'http://......' in a frame because it set 'X-Frame-Options' to 'DENY'
                .headers()
                .frameOptions()
                .disable()

                .and()
                .authorizeRequests()
                .antMatchers("/","/css/**", "/js/**", "/fonts/**","/users").permitAll()
                .antMatchers( "/login").permitAll()
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .antMatchers("/templates/**").hasAnyRole("ADMIN","USER")
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(customSuccessHandler())

                .and()
                .logout()
                .logoutUrl("/logout")

                .and()
                .sessionManagement()
                .maximumSessions(5)
                .and()

                .and()
                .rememberMe()
                .tokenValiditySeconds(604800)

                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("***");
        serializer.setCookiePath("/");
        serializer.setCookieMaxAge(60 * 60 * 24 * 1);
        return serializer;
    }

    // TODO 不能直接注入 FindByIndexNameSessionRepository
    @Bean
    public SessionRegistry sessionRegistry(FindByIndexNameSessionRepository sessionRepository) {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }

    @Bean
    public CustomSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler();
    }
}

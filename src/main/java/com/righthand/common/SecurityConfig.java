package com.righthand.common;

import com.righthand.membership.config.ConfigMembership;
import com.righthand.membership.customHandler.CustomAccessDeniedHandler;
import com.righthand.membership.customHandler.CustomLoginFailureHandler;
import com.righthand.membership.customHandler.CustomLoginSuccessHandler;
import com.righthand.membership.customHandler.CustomLogoutHandler;
import com.righthand.membership.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MembershipService logInService;

    @Autowired
    ConfigMembership configMembership;

    @Autowired
    CustomLogoutHandler customLogoutHandler;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationFailureHandler loginFailuredHandler() {
        return new CustomLoginFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        if (configMembership.isUseMemberShip()) {
            web.ignoring().antMatchers("/css/**", "/script/**", "/images/**", "/fonts/**", "lib/**", "/static/**", "/resources/**");
            // 파일 스토리지
        } else {
            web.ignoring().antMatchers("/**");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (configMembership.isUseMemberShip()) {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/board/writer", "/user/**")
                    .authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/?error=true")
                    .usernameParameter("userId")
                    .passwordParameter("userPwd")

                    // Post 로그인 처리 url
                    .loginProcessingUrl("/api/login")
                    .successHandler(successHandler())
                    .failureHandler(loginFailuredHandler())
                    .permitAll()

                    .and()
                    .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(customLogoutHandler)
                    .invalidateHttpSession(true)
                    .permitAll()
                    .and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                    .and()
                    .rememberMe().key("uniqueAndSecret")
                    .rememberMeParameter("rememberMe")
            ;
        } else {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
            ;
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(logInService)
                .passwordEncoder(logInService.passwordEncoder());
    }
}

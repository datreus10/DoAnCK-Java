package com.example.demo.config;

import com.example.demo.component.MyAuthenticationSuccessHandler;
import com.example.demo.component.Oauth2SuccessHandler;
import com.example.demo.service.CustomOAuth2UserService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserService userService;

    @Autowired
    private Oauth2SuccessHandler successHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/**","/js/**","/images/**", "/register","/register/verify","/oauth2/**","/forgot_password","/reset_password").permitAll()
            .anyRequest().authenticated()
            .and().formLogin().permitAll().loginPage("/login").failureUrl("/login-error").successHandler(myAuthenticationSuccessHandler)
            .and().oauth2Login().loginPage("/login").userInfoEndpoint().userService(customOAuth2UserService)
            .and().successHandler(successHandler)
            .and().logout()
                .invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
                .permitAll()
            .and().rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400).userDetailsService(userService);  //2 weeks
       //http.formLogin().defaultSuccessUrl("/login-success", true);
        //http.csrf().ignoringAntMatchers("/login");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(this.userService);
        auth.setPasswordEncoder(passwordEncoder());
        auth.setHideUserNotFoundExceptions(false);
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}

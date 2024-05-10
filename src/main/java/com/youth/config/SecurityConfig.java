package com.youth.config;

import com.youth.common.AuthenticationEntryPointImpl;
import com.youth.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.youth.common.AccessDeniedHandlerImpl;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//新版本不需要继承WebSecurityConfigurerAdapter
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;


    // 加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //配置AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        ProviderManager providerManager = new ProviderManager(provider);
        return providerManager;
    }

    // 身份认证提供程序（加密方式，用户查询）
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }


    //配置SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .formLogin(form -> form.successHandler())
                //禁用了CSRF（跨站请求伪造）保护
                .csrf(AbstractHttpConfigurer::disable)
                //不通过Session获取SecurityContext
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //添加自定义的过滤器
                .authorizeHttpRequests(authorize -> {
                    //允许所有用户访问特定URL路径
                    authorize.requestMatchers("/login/userLogin", "/login/refreshToken/**",
                            "/doc.html", "/webjars/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**").permitAll();
                    //其他URL路径都需要认证
                    authorize.anyRequest().authenticated();
                })
                //添加自定义的过滤器
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //最大会话数为1，maxSessionsPreventsLogin为true表示不允许新的登录
//                .sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(true))
                //异常处理
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                );
        return http.build();
    }

}


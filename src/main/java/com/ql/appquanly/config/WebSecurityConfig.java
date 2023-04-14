package com.ql.appquanly.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.ql.appquanly.service.UserService;

@Configuration
public class WebSecurityConfig {

        @Autowired
        UserService userService;

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

                authProvider.setUserDetailsService(userService);
                authProvider.setPasswordEncoder(passwordEncoder());

                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/login", "logout", "/").permitAll()
                                                .requestMatchers("/employee/**", "/addDepartment", "/editDepartment",
                                                                "/deleteDepartment")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/addvehicle").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .defaultSuccessUrl("/")

                                // .loginPage("/login")
                                // .loginProcessingUrl("/j_spring_security_check")

                                ).logout()
                                .and().httpBasic();
                // .formLogin(form -> form
                // .loginPage("/login")
                // .loginProcessingUrl("/j_spring_security_check")
                // .failureUrl("/login?error")
                // .usernameParameter("username")
                // .passwordParameter("password")
                // .permitAll()
                // .defaultSuccessUrl("/hello"))
                // .logout(logout -> logout
                // .logoutUrl("/logout")
                // .logoutSuccessUrl("/logoutSuccessful"))
                // .exceptionHandling().accessDeniedPage("/403");

                // http.httpBasic();
                // http.authenticationProvider(authenticationProvider());

                // http.authorizeRequests()
                // .requestMatchers("/", "/home").permitAll() // Cho phép tất cả mọi người truy
                // cập vào 2
                // // địa chỉ này
                // .anyRequest().authenticated() // Tất cả các request khác đều cần phải xác
                // thực mới được
                // // truy cập
                // .and()
                // .formLogin() // Cho phép người dùng xác thực bằng form login
                // .defaultSuccessUrl("/hello")
                // .permitAll() // Tất cả đều được truy cập vào địa chỉ này
                // .and()
                // .logout() // Cho phép logout
                // .permitAll();
                return http.build();
        }

}

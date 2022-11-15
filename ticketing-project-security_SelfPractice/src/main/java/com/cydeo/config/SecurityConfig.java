package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean  //manually , no have connect to DB yet, here we override my User to Spring Security User
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        //PasswordEncoder encoder to create encoder password from my password which is coming from login page

        //UserDetails user1 = new User();//Spring Security' User

        List<UserDetails> userList = new ArrayList<>();
        userList.add(new User("mike",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))  ) );//ROLE_ADMIN : spring understands so that way.
        userList.add(new User("ozzy",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))  ) );

        return new InMemoryUserDetailsManager(userList); // it says where to save this userList

        //we passed Spring Security's User to UI-Part with my user information manually
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests() //we need to authorize pages, who will see, who should not see
                .antMatchers("/user/**").hasRole("ADMIN") // under /user end point everything should be accessible by admin
                .antMatchers("/project/**").hasRole("MANAGER")
                .antMatchers("/task/employee/**").hasRole("EMPLOYEE")
                .antMatchers("/task/**").hasRole("MANAGER")
                .antMatchers(
                        "/",
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"

                ).permitAll() // those above pages are permitted for all
                .anyRequest().authenticated() // other pages are needed to be authenticated
                .and()
                .httpBasic() //popup box
                .and().build();

    }

}

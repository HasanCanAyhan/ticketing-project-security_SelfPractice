package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean  //manually , no have connect to DB yet
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        //PasswordEncoder encoder to create encoder password from my password which is coming from login page

        //UserDetails user1 = new User();//Spring Security' User

        List<UserDetails> userList = new ArrayList<>();
        userList.add(new User("mike",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))  ) );//ROLE_ADMIN : spring understands so that way.
        userList.add(new User("ozzy",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))  ) );

        return new InMemoryUserDetailsManager(userList); // it says where to save this userList


    }

}

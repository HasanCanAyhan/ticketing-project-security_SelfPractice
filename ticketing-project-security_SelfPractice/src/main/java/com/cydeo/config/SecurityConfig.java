package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final SecurityService securityService;

    private final AutSuccessHandler autSuccessHandler;

    public SecurityConfig(SecurityService securityService, AutSuccessHandler autSuccessHandler) {
        this.securityService = securityService;
        this.autSuccessHandler = autSuccessHandler;
    }

    /*
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

     */


    @Bean // for defining our validation form
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
//                .antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAuthority("Admin") // it should be matched with database role
                .antMatchers("/project/**").hasAuthority("Manager")
                .antMatchers("/task/employee/**").hasAuthority("Employee")
                .antMatchers("/task/**").hasAuthority("Manager")
//                .antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN")
//                .antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE")
                .antMatchers( // it can be folder or end point
                        "/",
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
//                .httpBasic()
                .formLogin()
                    .loginPage("/login")
                    //.defaultSuccessUrl("/welcome")
                    .successHandler(autSuccessHandler) // it will be landed ->> admin : userCreate page, manager : projectCreate page
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // looking for where is logout button in the html page
                    .logoutSuccessUrl("/login") // after logout, go to begin which is login page
                .and()
                .rememberMe()
                    .tokenValiditySeconds(120)
                    .key("cydeo")
                    .userDetailsService(securityService)// to capture who log in
                .and()
                .build();
    }

    /*
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests() //we need to authorize pages, who will see, who should not see
                //.antMatchers("/user/**").hasRole("ADMIN") // under /user end point everything should be accessible by admin
                .antMatchers("/user/**").hasAuthority("Admin")
                //.antMatchers("/project/**").hasRole("MANAGER")
                //.antMatchers("/task/employee/**").hasRole("EMPLOYEE")
                //.antMatchers("/task/**").hasRole("MANAGER")
                //.antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN")
                //.antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE")// hasAuthority -->> ROLE_ .....
                .antMatchers( // related to pages
                        "/", //localhost:8080/
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"

                ).permitAll() // those above pages are permitted for all
                .anyRequest().authenticated() // other pages are needed to be authenticated
                .and()
                //.httpBasic() //popup box, spring provides it
                .formLogin()
                    .loginPage("/login") // we changed in the login.htm -->>    <form th:action="@{/login}" method="post" >
                    .defaultSuccessUrl("/welcome")//after successful login go to welcome
                    .failureUrl("/login?error=true") // if user put wrong info
                    .permitAll() // form should be accessible from all
                .and().build();

    }

     */

}

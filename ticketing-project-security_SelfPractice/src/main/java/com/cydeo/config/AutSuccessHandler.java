package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class AutSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities()); // to capture user's role
        /*
        Set doesn't include duplicates.
        Also, why we use some collection in here is, Spring can not know if we are using one role for each user or multiple role for each user.
        So we are going with the guaranteed way.
         */

        if (roles.contains("Admin")){ // if admin log in , go to user create page
            response.sendRedirect("/user/create");
        }

        if (roles.contains("Manager")){
            response.sendRedirect("/task/create");
        }

        if (roles.contains("Employee")){
            response.sendRedirect("/task/employee/pending-tasks");
        }



    }
}

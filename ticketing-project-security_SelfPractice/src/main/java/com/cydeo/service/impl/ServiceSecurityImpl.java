package com.cydeo.service.impl;

import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ServiceSecurityImpl implements SecurityService {

    private final UserRepository userRepository;

    public ServiceSecurityImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //get the user from DB, and convert to user springs understands by using UserPrincipal class

        User user = userRepository.findByUserNameAndIsDeleted(username, false); // entity

        if (user == null){
            throw  new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }
}

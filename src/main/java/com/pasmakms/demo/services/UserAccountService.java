package com.pasmakms.demo.services;

import com.pasmakms.demo.domain.UserAccount;
import com.pasmakms.demo.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService implements UserDetailsService {

    private UserAccountRepository userRepo;

    @Autowired
    public UserAccountService(UserAccountRepository userRepo){
        this.userRepo = userRepo;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userRepo.findByUserName(username);
        if(user != null){
            return user;
        }
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }

    public UserAccount getUserAccount(String name){
        return userRepo.findByUserName(name);
    }
}

package com.pasmakms.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailRepository {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}

package com.pasmakms.demo.repositories;

import com.pasmakms.demo.domain.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount,Long> {
            UserAccount findByUserName(String username);
}

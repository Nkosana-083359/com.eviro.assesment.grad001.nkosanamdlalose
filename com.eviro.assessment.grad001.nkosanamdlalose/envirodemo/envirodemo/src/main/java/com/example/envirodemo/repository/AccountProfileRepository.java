package com.example.envirodemo.repository;

import com.example.envirodemo.entity.AccountProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountProfileRepository extends JpaRepository<AccountProfile, Long> {
    AccountProfile getByNameAndSurname(String name, String surname);
}

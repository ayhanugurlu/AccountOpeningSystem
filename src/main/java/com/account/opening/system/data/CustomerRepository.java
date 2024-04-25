package com.account.opening.system.data;

import com.account.opening.system.data.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);

    boolean existsByUsernameAndPassword(String username,String password);
}

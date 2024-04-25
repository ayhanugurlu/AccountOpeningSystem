package com.account.opening.system.service.impl;


import com.account.opening.system.data.CustomerRepository;
import com.account.opening.system.data.model.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        List<String> roles = new ArrayList<>();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        bCryptPasswordEncoder.encode(customer.getPassword());
        roles.add("USER");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(customer.getUsername())
                        .password(bCryptPasswordEncoder.encode(customer.getPassword()))
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;
    }
}

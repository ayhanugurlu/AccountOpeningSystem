package com.account.opening.system.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    String username;

    String name;

    String password;

    Date dateOfBirth;

    String documentId;

    @OneToOne(cascade = CascadeType.ALL)
    Address address;

    @OneToMany(cascade = CascadeType.ALL)
    List<BankAccount> bankAccounts;

}

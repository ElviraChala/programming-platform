package com.elvira.programming_platform.model;

import com.elvira.programming_platform.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "username", nullable = false, unique = true)
    protected String username;

    @Column(name = "name")
    protected String name;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "email", unique = true)
    protected String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    protected Role role = Role.STUDENT;
}

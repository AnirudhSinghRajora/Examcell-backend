package com.examcell.resultgen.model;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Or SINGLE_TABLE / TABLE_PER_CLASS
@Table(name = "users") // Explicit table name
@Data
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue
    @UuidGenerator // Use UUIDs as primary keys
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    // Password field omitted as authentication is handled elsewhere

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    protected User(String username, Role role) {
        this.username = username;
        this.role = role;
    }
} 
package com.examcell.resultgen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {
    
    public Admin() {
        super();
    }
    
    public Admin(String username, String password, String firstName, String lastName, Role role) {
        super(username, password, firstName, lastName, role);
    }
} 
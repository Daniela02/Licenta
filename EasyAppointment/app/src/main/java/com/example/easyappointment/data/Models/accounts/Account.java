package com.example.easyappointment.data.Models.accounts;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Account {
    @Id public Long account_id;
    public String email;
    public String name;
    public String type;// client or provider

    public Account() {
    }

    public Account(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Account{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

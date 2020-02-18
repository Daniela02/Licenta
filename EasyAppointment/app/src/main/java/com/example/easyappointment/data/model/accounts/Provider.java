package com.example.easyappointment.data.model.accounts;

import com.example.easyappointment.data.model.provider.Category;
import com.example.easyappointment.data.model.provider.Provider_Service;
import com.example.easyappointment.data.model.provider.Schedules;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Provider {
    @Id public Long provider_id;
    public ToOne<Account> account;
    public String address;
    public String telephone;
    public ToMany<Provider_Service> services;
    public ToMany<Schedules> schedules;
    public ToOne<Category> category;


    public Provider(String address, String telephone) {
        this.address = address;
        this.telephone = telephone;
    }

    public ToOne<Account> getAccount() {
        return account;
    }

    public void setAccount(ToOne<Account> account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ToMany<Provider_Service> getServices() {
        return services;
    }

    public void setServices(ToMany<Provider_Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "account=" + account +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", services=" + services +
                ", schedules=" + schedules +
                ", category=" + category +
                '}';
    }
}

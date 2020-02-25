package com.example.easyappointment.data.Models.accounts;

import com.example.easyappointment.data.Models.providerSpecifics.Category;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;
import com.example.easyappointment.data.Models.providerSpecifics.Schedules;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import java.util.ArrayList;
import java.util.List;

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
    public ToMany<Provider_Service> provider_services;
    public ToMany<Schedules> schedules;
    public ToOne<Category> category;

    public Provider() {
    }

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

    public List<Service> getServices() {
        List<Service> services = new ArrayList();
        for (int i = 0; i < provider_services.size(); i++) {
            services.add(provider_services.get(i).service.getTarget());
        }

        return services;
    }

    public void setServices(ToMany<Provider_Service> services) {
        this.provider_services = services;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "account=" + account +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", provider_services=" + provider_services +
                ", schedules=" + schedules +
                ", category=" + category +
                '}';
    }
}

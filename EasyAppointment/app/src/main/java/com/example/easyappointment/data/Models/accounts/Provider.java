package com.example.easyappointment.data.Models.accounts;

import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.providerSpecifics.Category;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;
import com.example.easyappointment.data.Models.providerSpecifics.Schedules;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public List<Appointments> getAppointments() {
        List<Appointments> appointments = new ArrayList<>();
        for (int i = 0; i < provider_services.size(); i++) {
            for (int j = 0; j < provider_services.get(i).appointments.size(); j++) {
                appointments.add(provider_services.get(i).appointments.get(j));
            }
        }

        appointments.sort((a1, a2) -> {
            Date startTime1 = new Date(a1.start_time);
            Date startTime2 = new Date(a2.start_time);
            if (startTime1.after(startTime2)) {
                return 1;
            }
            if (startTime1.equals(startTime2)) {
                return 0;
            }
            return -1;
        });

        return appointments;
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

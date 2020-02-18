package com.example.easyappointment.data.model.accounts;

import com.example.easyappointment.data.model.Appointments;
import com.example.easyappointment.data.model.client.Living_Area;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Client {
    @Id public Long client_id;
    public ToOne<Account> account;
    public ToOne<Living_Area> living_area;
    public ToMany<Appointments> appointments;

    public Client() {}

    public ToOne<Account> getAccount() {
        return account;
    }

    public void setAccount(ToOne<Account> account) {
        this.account = account;
    }

    public ToOne<Living_Area> getLiving_area() {
        return living_area;
    }

    public void setLiving_area(ToOne<Living_Area> living_area) {
        this.living_area = living_area;
    }

    public ToMany<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(ToMany<Appointments> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "Client{" +
                "account=" + account +
                ", living_area=" + living_area +
                ", appointments=" + appointments +
                '}';
    }
}

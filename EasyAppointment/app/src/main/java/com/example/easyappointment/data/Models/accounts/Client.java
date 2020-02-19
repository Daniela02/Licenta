package com.example.easyappointment.data.Models.accounts;

import com.example.easyappointment.data.Models.Appointments;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Client {
    @Id public Long client_id;
    public ToOne<Account> account;
    public ToMany<Appointments> appointments;

    public Client() {}

    public ToOne<Account> getAccount() {
        return account;
    }

    public void setAccount(ToOne<Account> account) {
        this.account = account;
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
                ", appointments=" + appointments +
                '}';
    }
}

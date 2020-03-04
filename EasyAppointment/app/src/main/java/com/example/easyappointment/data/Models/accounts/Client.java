package com.example.easyappointment.data.Models.accounts;

import com.example.easyappointment.data.Models.Appointments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public List<Appointments> getAppointments() {
        List<Appointments> appointmentsList = new ArrayList<>();
        for (int i = 0; i < appointments.size(); i++) {
            appointmentsList.add(appointments.get(i));
        }

        appointmentsList.sort((a1, a2) -> {
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

        return appointmentsList;
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

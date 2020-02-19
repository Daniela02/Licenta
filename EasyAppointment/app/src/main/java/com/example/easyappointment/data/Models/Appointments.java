package com.example.easyappointment.data.Models;

import com.example.easyappointment.data.Models.accounts.Client;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Appointments {
    @Id public Long appointments_id;
    public ToOne<Client> client;
    public ToOne<Provider_Service> provider_service;
    public String status; //pending, booked, happened, canceled
    public String start_time;
    public String end_time;

    public Appointments(String status, String start_time, String end_time) {
        this.status = status;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public ToOne<Client> getClient() {
        return client;
    }

    public void setClient(ToOne<Client> client) {
        this.client = client;
    }

    public ToOne<Provider_Service> getProvider_service() {
        return provider_service;
    }

    public void setProvider_service(ToOne<Provider_Service> provider_service) {
        this.provider_service = provider_service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "Appointments{" +
                "client=" + client +
                ", provider_service=" + provider_service +
                ", status='" + status + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
    }
}

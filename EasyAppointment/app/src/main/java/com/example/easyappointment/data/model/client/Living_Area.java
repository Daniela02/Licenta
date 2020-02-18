package com.example.easyappointment.data.model.client;

import com.example.easyappointment.data.model.accounts.Client;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
//TODO add living_Areas to the DB
@Entity
public class Living_Area {
    @Id public Long living_area_id;
    public String living_area;
    public ToMany<Client> clients;

    public Living_Area(String living_area) {
        this.living_area = living_area;
    }

    public String getLiving_area() {
        return living_area;
    }

    public void setLiving_area(String living_area) {
        this.living_area = living_area;
    }

    public ToMany<Client> getClients() {
        return clients;
    }

    public void setClients(ToMany<Client> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        return "Living_Area{" +
                "living_area='" + living_area + '\'' +
                ", clients=" + clients +
                '}';
    }
}

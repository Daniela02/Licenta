package com.example.easyappointment.data.Models.providerSpecifics;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;
import io.objectbox.relation.ToOne;

@Entity
public class Service {
    @Id public Long id;
    public ToOne<Provider_Service> provider_service;
    public String name;
    public Double price;
    @Uid(6059980457444666489L)
    public Integer duration; //minutes

    public Service() {
    }

    public Service(String name, Double price, Integer duration) {
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    public ToOne<Provider_Service> getProvider_service() {
        return provider_service;
    }

    public void setProvider_service(ToOne<Provider_Service> provider_service) {
        this.provider_service = provider_service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Service{" +
                "provider_service=" + provider_service +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", duration=" + duration +
                '}';
    }
}

package com.example.easyappointment.data.model.provider;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Service {
    @Id public Long id;
    public ToOne<Provider_Service> provider_service;
    public String name;
    public String description;
    public Integer duration; //milliseconds

    public Service(String name, String description, Integer duration) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", client_name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                '}';
    }
}

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
    public String description;
    @Uid(3971746293054693494L)
    public String duration; //minutes

    public Service(String name, String description, String duration) {
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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

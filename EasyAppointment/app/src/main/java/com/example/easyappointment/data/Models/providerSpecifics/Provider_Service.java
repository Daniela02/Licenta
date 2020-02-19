package com.example.easyappointment.data.Models.providerSpecifics;

import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.accounts.Provider;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Provider_Service {
    @Id public Long provider_service_id;
    public ToOne<Service> service;
    public ToOne<Provider> provider;
    public ToMany<Appointments> appointments;

    public Provider_Service() {}

    public ToOne<Service> getService() {
        return service;
    }

    public void setService(ToOne<Service> service) {
        this.service = service;
    }

    public ToOne<Provider> getProvider() {
        return provider;
    }

    public void setProvider(ToOne<Provider> provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "Provider_Service{" +
                "service=" + service +
                ", provider=" + provider +
                '}';
    }
}

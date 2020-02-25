package com.example.easyappointment.data.Models.providerSpecifics;

import com.example.easyappointment.data.Models.accounts.Provider;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Category {
    @Id public Long category_id;
    public ToMany<Provider> providers;
    public String category_name;

    public Category() {
    }

    public Category(String category_name) {
        this.category_name = category_name;
    }

    public ToMany<Provider> getProviders() {
        return providers;
    }

    public void setProviders(ToMany<Provider> providers) {
        this.providers = providers;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "providers=" + providers +
                ", category_name='" + category_name + '\'' +
                '}';
    }
}

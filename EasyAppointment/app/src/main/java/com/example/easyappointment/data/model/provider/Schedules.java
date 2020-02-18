package com.example.easyappointment.data.model.provider;

import com.example.easyappointment.data.model.accounts.Provider;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Schedules {
    @Id public Long schedule_id;
    public ToOne<Provider> provider;
    public String weekDay;
    public String start_time;
    public String end_time;

    public Schedules(String weekDay, String start_time, String end_time) {
        this.weekDay = weekDay;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public ToOne<Provider> getProvider() {
        return provider;
    }

    public void setProvider(ToOne<Provider> provider) {
        this.provider = provider;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
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
        return "Schedules{" +
                "provider=" + provider +
                ", weekDay='" + weekDay + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
    }
}

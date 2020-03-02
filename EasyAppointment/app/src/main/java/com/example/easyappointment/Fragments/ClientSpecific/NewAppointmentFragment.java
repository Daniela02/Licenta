package com.example.easyappointment.Fragments.ClientSpecific;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.providerSpecifics.Schedules;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.objectbox.Box;

public class NewAppointmentFragment extends Fragment {

    public NewAppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_appointment, container, false);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Spinner dropdownTimeTable = view.findViewById(R.id.timeTableSpinner);

        String service_id = getArguments().getString("service_id");

        Box<Service> serviceBox = ObjectBox.get().boxFor(Service.class);
        Service service = serviceBox.get(Long.parseLong(service_id));
        Provider provider = service.provider_service.getTarget().provider.getTarget();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                String dayOfWeek = getDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK));
                Optional<Schedules> schedules = provider.schedules
                        .stream()
                        .filter(s -> s.weekDay.contains(dayOfWeek))
                        .limit(1)
                        .findAny();
                if (schedules.isPresent()) {
                    Date chosenDay = calendar.getTime();
                    List<Appointments> appointmentsList = provider.getAppointments()
                            .stream()
                            .filter(a -> a.start_time.contains(chosenDay.toString()))
                            .filter(a -> a.status.contains("accepted"))
                            .collect(Collectors.toList());

                    Long duration = service.duration * 60000L;
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(schedules.get().getStart_time()));
                    Long start_time = calendar.getTimeInMillis();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(schedules.get().getEnd_time()));
                    Long end_time = calendar.getTimeInMillis();


                }


            }
        });

        return view;
    }

    private String getDayOfTheWeek(int day) {
        String s;
        switch (day) {
            case Calendar.MONDAY:
                s = new String("Monday");
                break;
            case Calendar.TUESDAY:
                s = new String("Monday");
                break;
            case Calendar.WEDNESDAY:
                s = new String("Monday");
                break;
            case Calendar.THURSDAY:
                s = new String("Monday");
                break;
            case Calendar.FRIDAY:
                s = new String("Monday");
                break;
            default:
                s = new String("nu exista");
                break;
        }
        return s;
    }
}

package com.example.easyappointment.Fragments.ClientSpecific;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Client;
import com.example.easyappointment.data.Models.accounts.Client_;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;
import com.example.easyappointment.data.Models.providerSpecifics.Schedules;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import java.util.ArrayList;
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
        Long duration = service.duration * 60000L;
        Provider provider = service.provider_service.getTarget().provider.getTarget();
        Calendar calendar = Calendar.getInstance();

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {

            calendar.set(year, month, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if (calendar.getTime().after(new Date())) {
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
                            .filter(a -> a.start_time.contains(chosenDay.toString().substring(0, 9)))//month and day
                            .filter(a -> a.start_time.contains(chosenDay.toString().substring(30, 33))) //year
                            .filter(a -> a.status.contains(getString(R.string.accepted)))
                            .collect(Collectors.toList()); //sorted

                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(schedules.get().getStart_time()));
                    Long start_time = calendar.getTimeInMillis();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(schedules.get().getEnd_time()));
                    Long end_time = calendar.getTimeInMillis();
                    List<String> availableTimeTables = new ArrayList<>();

                    Long i = start_time;
                    int poz = 0;

                    while (i <= end_time - duration) {
                        if (poz == appointmentsList.size()) {
                            calendar.setTimeInMillis(i);
                            String minute = String.valueOf(calendar.get(Calendar.MINUTE));
                            String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                            if (minute.length() == 1) {
                                minute = '0' + minute;
                            }
                            availableTimeTables.add(hour + ":" + minute);
                            i += duration;
                            continue;
                        }

                        calendar.setTime(new Date(appointmentsList.get(poz).start_time));
                        Long appointment_start_time = calendar.getTimeInMillis();
                        calendar.setTime(new Date(appointmentsList.get(poz).end_time));
                        Long appointment_end_time = calendar.getTimeInMillis();

                        while (i < appointment_start_time) {

                            if (i + duration <= appointment_start_time) {
                                calendar.setTimeInMillis(i);
                                String minute = String.valueOf(calendar.get(Calendar.MINUTE));
                                String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                                if (minute.length() == 1) {
                                    minute = '0' + minute;
                                }
                                availableTimeTables.add(hour + ":" + minute);
                            }
                            i += duration;
                        }
                        i = appointment_end_time;
                        poz++;
                    }
                    if (availableTimeTables.size() != 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, availableTimeTables);
                        dropdownTimeTable.setAdapter(adapter);
                        return;
                    }
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"nothing"});
            dropdownTimeTable.setAdapter(adapter);
        });

        Button bookButton = view.findViewById(R.id.book_appointment_button);

        bookButton.setOnClickListener(v -> {
            String hour = dropdownTimeTable.getSelectedItem().toString().split(":")[0];
            String minute = dropdownTimeTable.getSelectedItem().toString().split(":")[1];
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
            Date start_time = calendar.getTime();
            calendar.setTimeInMillis(calendar.getTimeInMillis() + duration);
            Date end_time = calendar.getTime();
            Box<Appointments> appointmentsBox = ObjectBox.get().boxFor(Appointments.class);
            Box<Client> clientBox = ObjectBox.get().boxFor(Client.class);

            HomePageActivity host = (HomePageActivity) getActivity();

            Client client = clientBox.query().equal(Client_.accountId, host.account.account_id).build().findFirst();
            Provider_Service provider_service = service.provider_service.getTarget();

            Appointments appointments = new Appointments();
            appointmentsBox.attach(appointments);
            appointments.setStart_time(start_time.toString());
            appointments.setEnd_time(end_time.toString());
            appointments.setStatus(getString(R.string.pending));
            appointments.client.setTarget(client);
            appointments.provider_service.setTarget(provider_service);
            client.appointments.add(appointments);
            provider_service.appointments.add(appointments);

            appointmentsBox.put(appointments);
            clientBox.put(client);
            ObjectBox.get().boxFor(Provider_Service.class).put(provider_service);

            Toast.makeText(this.getActivity(), "Booked Appointment", Toast.LENGTH_SHORT).show();

            NavController navController = Navigation.findNavController(host, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_future_appointments);
        });

        return view;
    }

    private String getDayOfTheWeek(int day) {
        String weekday;
        switch (day) {
            case Calendar.MONDAY:
                weekday = getString(R.string.monday);
                break;
            case Calendar.TUESDAY:
                weekday = getString(R.string.tuesday);
                break;
            case Calendar.WEDNESDAY:
                weekday = getString(R.string.wednesday);
                break;
            case Calendar.THURSDAY:
                weekday = getString(R.string.thursday);
                break;
            case Calendar.FRIDAY:
                weekday = getString(R.string.friday);
                break;
            default:
                weekday = "no schedule";
                break;
        }
        return weekday;
    }
}

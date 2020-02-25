package com.example.easyappointment.Fragments.ProviderSpecific;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.accounts.Provider_;
import com.example.easyappointment.data.Models.providerSpecifics.Schedules;

import java.util.List;

import io.objectbox.Box;

public class ProviderSetScheduleFragment extends Fragment {

    public ProviderSetScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_set_schedule,
                container, false);
        if (getActivity().getClass().getSimpleName().contains("HomePageActivity")) {
            view.findViewById(R.id.scheduleTextView).setVisibility(View.GONE);
            Button submitButton = view.findViewById(R.id.submit_schedule);
            submitButton.setVisibility(View.VISIBLE);

            HomePageActivity host = (HomePageActivity) getActivity();

            Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
            Provider provider = providerBox.query().equal(Provider_.accountId, host.account.account_id).build().findFirst();


            Schedules monday = findWeekDayId(provider.schedules, "Monday");
            Schedules tuesday = findWeekDayId(provider.schedules, "Tuesday");
            Schedules wednesday = findWeekDayId(provider.schedules, "Wednesay");
            Schedules thursday = findWeekDayId(provider.schedules, "Thursday");
            Schedules friday = findWeekDayId(provider.schedules, "Friday");

            //DISPLAYING OLD INFO
            ((EditText) view.findViewById(R.id.monStartTime)).setText(monday.getStart_time());
            ((EditText) view.findViewById(R.id.monEndTime)).setText(monday.getEnd_time());

            ((EditText) view.findViewById(R.id.tuesStartTime)).setText(tuesday.getStart_time());
            ((EditText) view.findViewById(R.id.tuesEndTime)).setText(tuesday.getEnd_time());

            ((EditText) view.findViewById(R.id.wedStartTime)).setText(wednesday.getStart_time());
            ((EditText) view.findViewById(R.id.wedEndTime)).setText(wednesday.getEnd_time());

            ((EditText) view.findViewById(R.id.thursStartTime)).setText(thursday.getStart_time());
            ((EditText) view.findViewById(R.id.thursEndTime)).setText(thursday.getEnd_time());

            ((EditText) view.findViewById(R.id.friStartTime)).setText(friday.getStart_time());
            ((EditText) view.findViewById(R.id.friEndTime)).setText(friday.getEnd_time());

            submitButton.setOnClickListener(v -> {

                if (host.account.type.equals("Provider")) {
                    //PROVIDER
                    //GETTING NEW INFO
                    String startTime = ((EditText) view.findViewById(R.id.monStartTime)).getText().toString();
                    String endTime = ((EditText) view.findViewById(R.id.monEndTime)).getText().toString();

                    monday.setStart_time(startTime);
                    monday.setEnd_time(endTime);

                    startTime = ((EditText) view.findViewById(R.id.tuesStartTime)).getText().toString();
                    endTime = ((EditText) view.findViewById(R.id.tuesEndTime)).getText().toString();

                    tuesday.setStart_time(startTime);
                    tuesday.setEnd_time(endTime);

                    startTime = ((EditText) view.findViewById(R.id.wedStartTime)).getText().toString();
                    endTime = ((EditText) view.findViewById(R.id.wedEndTime)).getText().toString();

                    wednesday.setStart_time(startTime);
                    wednesday.setEnd_time(endTime);

                    startTime = ((EditText) view.findViewById(R.id.thursStartTime)).getText().toString();
                    endTime = ((EditText) view.findViewById(R.id.thursEndTime)).getText().toString();

                    thursday.setStart_time(startTime);
                    thursday.setEnd_time(endTime);

                    startTime = ((EditText) view.findViewById(R.id.friStartTime)).getText().toString();
                    endTime = ((EditText) view.findViewById(R.id.friEndTime)).getText().toString();

                    friday.setStart_time(startTime);
                    friday.setEnd_time(endTime);

                    //UPDATING DATABASE
                    Box<Schedules> schedulesBox = ObjectBox.get().boxFor(Schedules.class);
                    schedulesBox.put(monday);
                    schedulesBox.put(tuesday);
                    schedulesBox.put(wednesday);
                    schedulesBox.put(thursday);
                    schedulesBox.put(friday);
                    provider.schedules.add(monday);
                    provider.schedules.add(tuesday);
                    provider.schedules.add(wednesday);
                    provider.schedules.add(thursday);
                    provider.schedules.add(friday);

                    providerBox.put(provider);

                    Toast.makeText(this.getContext(), "Information submitted", Toast.LENGTH_SHORT).show();

                }
            });
        }

        return view;
    }

    private Schedules findWeekDayId(List<Schedules> schedules, String weekDay) {
        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).weekDay.contains(weekDay)) {
                return schedules.get(i);
            }
        }
        return new Schedules(weekDay, null, null);
    }
}

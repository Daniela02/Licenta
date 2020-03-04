package com.example.easyappointment.Fragments.ClientSpecific;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Adapters.ListAppointmentsAdapter;
import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Client;
import com.example.easyappointment.data.Models.accounts.Client_;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.objectbox.Box;

public class FutureAppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public FutureAppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_future_appointments, container, false);
        Date now = new Date();

        HomePageActivity host = (HomePageActivity) getActivity();
        recyclerView = view.findViewById(R.id.futureAppointmentsRecycleView);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        if (host.account.type.contains(host.getString(R.string.client))) {
            Box<Client> clientBox = ObjectBox.get().boxFor(Client.class);

            Client client = clientBox
                    .query()
                    .equal(Client_.accountId, host.account.account_id)
                    .build()
                    .findFirst();

            List<Appointments> appointmentsList = client.getAppointments()
                    .stream()
                    .filter(a -> (new Date(a.start_time).after(now)))
                    .collect(Collectors.toList());
            mAdapter = new ListAppointmentsAdapter(appointmentsList, false, false, host);
        }

        recyclerView.setAdapter(mAdapter);

        return view;
    }

}

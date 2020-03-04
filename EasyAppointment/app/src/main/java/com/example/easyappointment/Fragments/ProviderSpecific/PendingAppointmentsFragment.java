package com.example.easyappointment.Fragments.ProviderSpecific;

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
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.accounts.Provider_;

import java.util.List;
import java.util.stream.Collectors;

import io.objectbox.Box;

public class PendingAppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public PendingAppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_appointments, container, false);

        HomePageActivity host = (HomePageActivity) getActivity();
        recyclerView = view.findViewById(R.id.pendingAppointmentsRecycleView);

        if (host.account.type.contains(host.getString(R.string.provider))) {
            Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
            Provider provider = providerBox.query().equal(Provider_.accountId, host.account.account_id).build().findFirst();

            layoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(layoutManager);
            List<Appointments> appointmentsList = provider.getAppointments()
                    .stream()
                    .filter(a -> a.status.contains(host.getString(R.string.pending)))
                    .collect(Collectors.toList());
            mAdapter = new ListAppointmentsAdapter(appointmentsList, true, false, host);
            recyclerView.setAdapter(mAdapter);
        }
        return view;

    }

}

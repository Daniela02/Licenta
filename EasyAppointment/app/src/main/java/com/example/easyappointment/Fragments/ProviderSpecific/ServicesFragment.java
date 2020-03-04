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
import com.example.easyappointment.data.Adapters.ListServiceAdapter;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.accounts.Provider_;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import java.util.List;

import io.objectbox.Box;

public class ServicesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        recyclerView = view.findViewById(R.id.servicesRecycleView);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        HomePageActivity host = (HomePageActivity) getActivity();

        Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);

        Provider provider = providerBox
                .query().equal(Provider_.accountId, host.account.account_id)
                .build()
                .findFirst();

        List<Service> servicesList = provider.getServices();
        mAdapter = new ListServiceAdapter(servicesList, false, host);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

}

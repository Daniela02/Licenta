package com.example.easyappointment.Fragments.ClientSpecific;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Adapters.ListServiceAdapter;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.providerSpecifics.Service;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.objectbox.Box;


public class ProviderProfileFragment extends Fragment {

    private Provider provider;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ProviderProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_profile, container, false);

        Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
        provider = providerBox.get(Long.parseLong(getArguments().getString("profile_id")));

        String imageURL = provider.account.getTarget().imageURL;
        ImageView profilePicture = view.findViewById(R.id.provider_photo_profile);

        if (imageURL != null) {
            Picasso.get().load(Uri.parse(imageURL)).into(profilePicture);
        }
        ((TextView) view.findViewById(R.id.provider_name_profile)).setText(provider.account.getTarget().name);

        TextView showDetails = view.findViewById(R.id.show_details_profile);
        showDetails.setOnClickListener(v -> {

            ((TextView) view.findViewById(R.id.provider_address_profile)).setText("Address: " + provider.address);
            ((TextView) view.findViewById(R.id.provider_telephone_profile)).setText("Tel: " + provider.telephone);
            ((TextView) view.findViewById(R.id.provider_email_profile)).setText("Email: " + provider.account.getTarget().email);

            view.findViewById(R.id.provider_address_profile).setVisibility(View.VISIBLE);
            view.findViewById(R.id.provider_telephone_profile).setVisibility(View.VISIBLE);
            view.findViewById(R.id.provider_email_profile).setVisibility(View.VISIBLE);
            showDetails.setVisibility(View.GONE);

        });

        view.findViewById(R.id.details_layout_profile).setOnClickListener(v -> {
            view.findViewById(R.id.provider_address_profile).setVisibility(View.GONE);
            view.findViewById(R.id.provider_telephone_profile).setVisibility(View.GONE);
            view.findViewById(R.id.provider_email_profile).setVisibility(View.GONE);
            showDetails.setVisibility(View.VISIBLE);
        });

        recyclerView = view.findViewById(R.id.servicesRecycleView_profile);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        HomePageActivity host = (HomePageActivity) getActivity();

        List<Service> servicesList = provider.getServices();
        mAdapter = new ListServiceAdapter(servicesList, "profile", host);
        recyclerView.setAdapter(mAdapter);
        return view;
    }
    
}

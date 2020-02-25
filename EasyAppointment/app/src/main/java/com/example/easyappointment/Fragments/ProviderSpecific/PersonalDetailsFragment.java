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

import io.objectbox.Box;

public class PersonalDetailsFragment extends Fragment {

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_details,
                container, false);
        if (getActivity().getClass().getSimpleName().contains("HomePageActivity")) {
            (view.findViewById(R.id.personalDetailsTextView)).setVisibility(View.GONE);
            Button submitButton = view.findViewById(R.id.submit_personal_details);
            submitButton.setVisibility(View.VISIBLE);

            HomePageActivity host = (HomePageActivity) getActivity();

            Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);

            Provider provider = providerBox.query().equal(Provider_.accountId, host.account.account_id).build().findFirst();
            //DISPLAYING OLD INFO
            ((EditText) view.findViewById(R.id.editPhone)).setText(provider.getTelephone());
            ((EditText) view.findViewById(R.id.editAddress)).setText(provider.getAddress());

            submitButton.setOnClickListener(v -> {

                if (host.account.type.equals("Provider")) {
                    //PROVIDER
                    //GETTING NEW INFO
                    String phone = ((EditText) view.findViewById(R.id.editPhone)).getText().toString();
                    String address = ((EditText) view.findViewById(R.id.editAddress)).getText().toString();

                    provider.setAddress(address);
                    provider.setTelephone(phone);

                    //UPDATING DATABASE
                    providerBox.put(provider);

                    Toast.makeText(this.getContext(), "Information submitted", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

}

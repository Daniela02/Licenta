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
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import io.objectbox.Box;

public class AddServiceFragment extends Fragment {
    public AddServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_service, container, false);
        if (getActivity().getClass().getSimpleName().contains("HomePageActivity")) {
            view.findViewById(R.id.addServiceTextView).setVisibility(View.GONE);
            Button submitButton = view.findViewById(R.id.submit_new_service);
            submitButton.setVisibility(View.VISIBLE);

            HomePageActivity host = (HomePageActivity) getActivity();

            Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);

            Provider provider = providerBox.query().equal(Provider_.accountId, host.account.account_id).build().findFirst();

            submitButton.setOnClickListener(v -> {
                String serviceName = ((EditText) view.findViewById(R.id.addServiceName)).getText().toString();
                String serviceDescription = ((EditText) view.findViewById(R.id.addServiceDescription)).getText().toString();
                Integer serviceDuration = Integer.parseInt(((EditText) view.findViewById(R.id.addServiceDuration)).getText().toString());
                Box<Service> serviceBox = ObjectBox.get().boxFor(Service.class);

                Box<Provider_Service> provider_serviceBox = ObjectBox.get().boxFor(Provider_Service.class);
                Service service = new Service(serviceName, serviceDescription, serviceDuration);
                serviceBox.attach(service);
                Provider_Service provider_service = new Provider_Service();
                provider_serviceBox.attach(provider_service);
                service.provider_service.setTarget(provider_service);
                provider_service.provider.setTarget(provider);
                provider_service.service.setTarget(service);
                provider.provider_services.add(provider_service);

                serviceBox.put(service);

                provider_serviceBox.put(provider_service);

                providerBox.put(provider);

                Toast.makeText(this.getContext(), "Information submitted", Toast.LENGTH_SHORT).show();
            });
        }
        return view;
    }

}

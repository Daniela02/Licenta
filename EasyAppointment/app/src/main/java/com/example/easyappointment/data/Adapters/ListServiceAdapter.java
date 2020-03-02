package com.example.easyappointment.data.Adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import java.util.List;

import io.objectbox.Box;

public class ListServiceAdapter extends RecyclerView.Adapter {

    private List<Service> serviceList;
    private Boolean isClient;
    private HomePageActivity host;

    public ListServiceAdapter(List<Service> serviceList, Boolean isClient, HomePageActivity host) {
        this.serviceList = serviceList;
        this.isClient = isClient;
        this.host = host;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_service, parent, false);
        return new ListViewHolder(view, serviceList, isClient, host);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView serviceName;
        private TextView serviceDescription;
        private TextView serviceDuration;
        private EditText editServiceName;
        private EditText editServiceDescription;
        private EditText editServiceDuration;
        private Button editButton;
        private Button deleteButton;
        private Button submitButton;
        private LinearLayout serviceLayout;
        private List<Service> servicesList;
        private Boolean isClient;
        private HomePageActivity host;

        public ListViewHolder(@NonNull View itemView, List<Service> servicesList, Boolean isClient, HomePageActivity host) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.show_service_name);
            serviceDescription = itemView.findViewById(R.id.show_service_description);
            serviceDuration = itemView.findViewById(R.id.show_service_duration);
            editServiceName = itemView.findViewById(R.id.edit_service_name);
            editServiceDescription = itemView.findViewById(R.id.edit_service_description);
            editServiceDuration = itemView.findViewById(R.id.edit_service_duration);
            editButton = itemView.findViewById(R.id.edit_service_button);
            deleteButton = itemView.findViewById(R.id.delete_service_button);
            submitButton = itemView.findViewById(R.id.submit_service_button);
            serviceLayout = itemView.findViewById(R.id.list_service);
            this.isClient = isClient;
            this.servicesList = servicesList;
            this.host = host;

            itemView.setOnClickListener(this::onClick);

            Log.d("Adapter ", servicesList.toString());
        }


        @Override
        public void onClick(View v) {

        }

        public void bindView(int poz) {
            //set the text for one service

            Service service = servicesList.get(poz);
            serviceName.setText(service.name);
            serviceDescription.setText(service.description);
            serviceDuration.setText(service.duration + " min");
            if (isClient) {
                submitButton.setText("New");

                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);

                submitButton.setOnClickListener(v -> {
                    NavController navController = Navigation.findNavController(host, R.id.nav_host_fragment);
                    Bundle bundle = new Bundle();
                    bundle.putString("service_id", service.id.toString());
                    navController.navigate(R.id.new_appointment, bundle);
                });
            } else {
                deleteButton.setOnClickListener(v -> {
                    serviceLayout.setVisibility(View.GONE);
                    Provider_Service provider_service = service.provider_service.getTarget();
                    Provider provider = provider_service.provider.getTarget();

                    serviceDuration.setVisibility(View.GONE);
                    serviceName.setVisibility(View.GONE);
                    serviceDescription.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.GONE);

                    Box<Service> serviceBox = ObjectBox.get().boxFor(Service.class);
                    Box<Provider_Service> provider_serviceBox = ObjectBox.get().boxFor(Provider_Service.class);


                    provider.provider_services.remove(provider_service);
                    serviceBox.remove(service);
                    provider_serviceBox.remove(provider_service);

                });

                editButton.setOnClickListener(v -> {
                    serviceDuration.setVisibility(View.GONE);
                    serviceName.setVisibility(View.GONE);
                    serviceDescription.setVisibility(View.GONE);
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);

                    editServiceName.setVisibility(View.VISIBLE);
                    editServiceDescription.setVisibility(View.VISIBLE);
                    editServiceDuration.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.VISIBLE);

                    editServiceName.setText(service.name);
                    editServiceDescription.setText(service.description);
                    editServiceDuration.setText(service.duration);
                });

                submitButton.setOnClickListener(v -> {
                    String name = editServiceName.getText().toString();
                    String description = editServiceDescription.getText().toString();
                    Integer duration = Integer.parseInt(editServiceDuration.getText().toString());
                    service.setName(name);
                    service.setDescription(description);
                    service.setDuration(duration);
                    Box<Service> serviceBox = ObjectBox.get().boxFor(Service.class);
                    serviceBox.put(service);

                    serviceDuration.setVisibility(View.VISIBLE);
                    serviceName.setVisibility(View.VISIBLE);
                    serviceDescription.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    editServiceName.setVisibility(View.GONE);
                    editServiceDescription.setVisibility(View.GONE);
                    editServiceDuration.setVisibility(View.GONE);
                    submitButton.setVisibility(View.GONE);

                    serviceName.setText(service.name);
                    serviceDescription.setText(service.description);
                    serviceDuration.setText(service.duration + " h");
                });
            }

        }
    }
}
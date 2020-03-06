package com.example.easyappointment.data.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.GoogleApi.GoogleCalendar;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;

import java.util.List;

import io.objectbox.Box;

public class ListAppointmentsAdapter extends RecyclerView.Adapter {

    private List<Appointments> appointmentsList;
    private Boolean isProvider;
    private Boolean showHistory;
    private HomePageActivity host;

    public ListAppointmentsAdapter(List<Appointments> appointmentsList, Boolean isProvider, Boolean showHistory, HomePageActivity host) {
        this.appointmentsList = appointmentsList;
        this.isProvider = isProvider;
        this.showHistory = showHistory;
        this.host = host;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_show_appointments, parent, false);
        return new ListViewHolder(view, appointmentsList, isProvider, showHistory, host);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private List<Appointments> appointmentsList;
        private Boolean isProvider;
        private Boolean showHistory;
        private Button cancelButton;
        private Button acceptButton;
        private TextView serviceName;
        private TextView providerOrClientName;
        private TextView startTime;
        private TextView status;
        private LinearLayout appointmentLayout;
        private HomePageActivity host;
        private LinearLayout getDirections;
        private Long clientId;

        public ListViewHolder(@NonNull View itemView, List<Appointments> appointmentsList, Boolean isProvider, Boolean showHistory, HomePageActivity host) {
            super(itemView);
            this.appointmentsList = appointmentsList;
            this.isProvider = isProvider;
            this.showHistory = showHistory;
            this.cancelButton = itemView.findViewById(R.id.appointment_cancel_button);
            this.acceptButton = itemView.findViewById(R.id.appointment_accept_button);
            this.serviceName = itemView.findViewById(R.id.appointment_service_name);
            this.providerOrClientName = itemView.findViewById(R.id.appointment_provider);
            this.startTime = itemView.findViewById(R.id.appointment_start_time);
            this.status = itemView.findViewById(R.id.appointment_status);
            this.getDirections = itemView.findViewById(R.id.appointment_direction);
            this.host = host;
            this.appointmentLayout = itemView.findViewById(R.id.layout_appointment);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            if (isProvider) {
                NavController navController = Navigation.findNavController(host, R.id.nav_host_fragment);
                Bundle bundle = new Bundle();
                bundle.putString("client_id", clientId.toString());
                navController.navigate(R.id.client_profile, bundle);
            }
        }

        public void bindView(int poz) {
            Appointments appointment = appointmentsList.get(poz);

            clientId = appointment.client.getTarget().client_id;

            serviceName.setText(appointment.provider_service.getTarget().service.getTarget().name);
            providerOrClientName.setText(appointment.provider_service.getTarget().provider.getTarget().account.getTarget().name);
            startTime.setText(appointment.start_time);
            status.setText(appointment.status);
            if (appointment.status.equals(host.getString(R.string.pending))) {
                status.setTextColor(ContextCompat.getColor(host, R.color.colorPrimary));
            }
            if (isProvider) {
                getDirections.setVisibility(View.GONE);
                Box<Appointments> appointmentsBox = ObjectBox.get().boxFor(Appointments.class);
                appointment.seenByProvider = true;
                appointmentsBox.put(appointment);

                providerOrClientName.setText(appointment.client.getTarget().account.getTarget().name);

                cancelButton.setOnClickListener(v -> {
                    Provider_Service provider_service = appointment.provider_service.getTarget();
                    provider_service.appointments.remove(appointment);

                    appointmentsBox.remove(appointment);

                    serviceName.setVisibility(View.GONE);
                    providerOrClientName.setVisibility(View.GONE);
                    startTime.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    status.setVisibility(View.GONE);
                });

                if (appointment.status.contains(host.getString(R.string.accepted))) {
                    //ACCEPTED APPOINTMENTS
                    acceptButton.setVisibility(View.GONE);
                    status.setVisibility(View.GONE);
                } else {
                    //PENDING
                    status.setVisibility(View.VISIBLE);
                    acceptButton.setOnClickListener(v -> {
                        appointmentLayout.setVisibility(View.GONE);
                        appointment.setStatus(host.getString(R.string.accepted));
                        status.setText(appointment.status);
                        appointment.seenByClient = false;
                        appointmentsBox.put(appointment);

                        if (host.hasPermissionToWriteCalendar) {
                            //NEWLY ACCEPTED APPOINTMENT CREATES EVENT IN GOOGLE CALENDAR
                            GoogleCalendar googleCalendar = new GoogleCalendar(host);
                            googleCalendar.createProviderCalendarEvent(appointment);

                            Toast.makeText(host, "Event created in Google Calendar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } else {
                //CLIENT
                Box<Appointments> appointmentsBox = ObjectBox.get().boxFor(Appointments.class);
                Provider provider = appointment.provider_service.getTarget().provider.getTarget();

                getDirections.setOnClickListener(v -> {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + provider.address);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    host.startActivity(mapIntent);
                });

                if (!showHistory) {//Future
                    status.setVisibility(View.VISIBLE);

                    cancelButton.setOnClickListener(v -> {
                        Provider_Service provider_service = appointment.provider_service.getTarget();
                        provider_service.appointments.remove(appointment);
                        appointmentsBox.remove(appointment);
                        serviceName.setVisibility(View.GONE);
                        providerOrClientName.setVisibility(View.GONE);
                        startTime.setVisibility(View.GONE);
                        cancelButton.setVisibility(View.GONE);
                        status.setVisibility(View.GONE);
                    });

                    acceptButton.setVisibility(View.GONE);

                    if (host.hasPermissionToWriteCalendar && appointment.status.equals(host.getString(R.string.accepted)) && appointment.seenByClient.equals(false)) {
                        //NEWLY ACCEPTED APPOINTMENT CREATES EVENT IN GOOGLE CALENDAR
                        GoogleCalendar googleCalendar = new GoogleCalendar(host);
                        googleCalendar.createClientCalendarEvent(appointment);
                        Toast.makeText(host, "Event created in Google Calendar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    status.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    acceptButton.setVisibility(View.GONE);
                }

                appointment.seenByClient = true;
                appointmentsBox.put(appointment);
            }
        }
    }
}

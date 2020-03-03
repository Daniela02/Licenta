package com.example.easyappointment.data.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.ObjectBox;
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
        private TextView providerName;
        private TextView startTime;
        private TextView status;
        private LinearLayout appointmentLayout;
        private HomePageActivity host;

        public ListViewHolder(@NonNull View itemView, List<Appointments> appointmentsList, Boolean isProvider, Boolean showHistory, HomePageActivity host) {
            super(itemView);
            this.appointmentsList = appointmentsList;
            this.isProvider = isProvider;
            this.showHistory = showHistory;
            this.cancelButton = itemView.findViewById(R.id.appointment_cancel_button);
            this.acceptButton = itemView.findViewById(R.id.appointment_accept_button);
            this.serviceName = itemView.findViewById(R.id.appointment_service_name);
            this.providerName = itemView.findViewById(R.id.appointment_provider);
            this.startTime = itemView.findViewById(R.id.appointment_start_time);
            this.status = itemView.findViewById(R.id.appointment_status);
            this.host = host;
            this.appointmentLayout = itemView.findViewById(R.id.layout_appointment);
            itemView.setOnClickListener(this::onClick);
        }


        @Override
        public void onClick(View v) {

        }

        public void bindView(int poz) {
            Appointments appointment = appointmentsList.get(poz);

            serviceName.setText(appointment.provider_service.getTarget().service.getTarget().name);
            providerName.setText(appointment.provider_service.getTarget().provider.getTarget().account.getTarget().name);
            startTime.setText(appointment.start_time);
            status.setText(appointment.status);
            if (appointment.status.equals("pending")) {
                status.setTextColor(ContextCompat.getColor(host, R.color.colorPrimary));
            }
            if (isProvider) {

                cancelButton.setOnClickListener(v -> {
                    Provider_Service provider_service = appointment.provider_service.getTarget();
                    provider_service.appointments.remove(appointment);

                    Box<Appointments> appointmentsBox = ObjectBox.get().boxFor(Appointments.class);
                    appointmentsBox.remove(appointment);

                    serviceName.setVisibility(View.GONE);
                    providerName.setVisibility(View.GONE);
                    startTime.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    status.setVisibility(View.GONE);
                });

                if (appointment.status.contains("accepted")) {
                    //ACCEPTED APPOINTMENTS
                    acceptButton.setVisibility(View.GONE);
                    status.setVisibility(View.GONE);
                } else {
                    //PENDING
                    status.setVisibility(View.VISIBLE);
                    acceptButton.setOnClickListener(v -> {
                        appointmentLayout.setVisibility(View.GONE);
                        appointment.setStatus("accepted");
                        Box<Appointments> appointmentsBox = ObjectBox.get().boxFor(Appointments.class);
                        status.setText(appointment.status);
                        appointmentsBox.put(appointment);
                    });
                }

            } else {
                //CLIENT

                if (showHistory == false) {//Future
                    status.setVisibility(View.VISIBLE);
                    cancelButton.setOnClickListener(v -> {
                        Provider_Service provider_service = appointment.provider_service.getTarget();
                        provider_service.appointments.remove(appointment);

                        Box<Appointments> appointmentsBox = ObjectBox.get().boxFor(Appointments.class);
                        appointmentsBox.remove(appointment);

                        serviceName.setVisibility(View.GONE);
                        providerName.setVisibility(View.GONE);
                        startTime.setVisibility(View.GONE);
                        cancelButton.setVisibility(View.GONE);
                        status.setVisibility(View.GONE);

                    });
                    acceptButton.setVisibility(View.GONE);
                } else {
                    status.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    acceptButton.setVisibility(View.GONE);
                }

            }
        }
    }
}

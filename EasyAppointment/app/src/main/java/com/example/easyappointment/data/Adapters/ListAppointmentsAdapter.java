package com.example.easyappointment.data.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    public ListAppointmentsAdapter(List<Appointments> appointmentsList, Boolean isProvider, Boolean showHistory) {
        this.appointmentsList = appointmentsList;
        this.isProvider = isProvider;
        this.showHistory = showHistory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_show_appointments, parent, false);
        return new ListViewHolder(view, appointmentsList, isProvider, showHistory);
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
        private Button cancelAcceptButton;
        private TextView serviceName;
        private TextView providerName;
        private TextView startTime;
        private TextView status;

        public ListViewHolder(@NonNull View itemView, List<Appointments> appointmentsList, Boolean isProvider, Boolean showHistory) {
            super(itemView);
            this.appointmentsList = appointmentsList;
            this.isProvider = isProvider;
            this.showHistory = showHistory;
            this.cancelAcceptButton = itemView.findViewById(R.id.appointment_accept_cancel_button);
            this.serviceName = itemView.findViewById(R.id.appointment_service_name);
            this.providerName = itemView.findViewById(R.id.appointment_provider);
            this.startTime = itemView.findViewById(R.id.appointment_start_time);
            this.status = itemView.findViewById(R.id.appointment_status);
            itemView.setOnClickListener(this::onClick);

            Log.d("Adapter ", appointmentsList.toString());
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
            if (isProvider) {
                if (appointment.status.contains("accepted")) {
                    //ACCEPTED APPOINTMENTS
                    cancelAcceptButton.setText("Cancel");
                    status.setVisibility(View.GONE);

                    cancelAcceptButton.setOnClickListener(v -> {
                        Provider_Service provider_service = appointment.provider_service.getTarget();
                        provider_service.appointments.remove(appointment);

                        Box<Appointments> appointmentsBox = ObjectBox.get().boxFor(Appointments.class);
                        appointmentsBox.remove(appointment);

                        serviceName.setVisibility(View.GONE);
                        providerName.setVisibility(View.GONE);
                        startTime.setVisibility(View.GONE);
                    });
                } else {
                    //PENDING
                    status.setVisibility(View.VISIBLE);
                    cancelAcceptButton.setText("Accept");
                    cancelAcceptButton.setOnClickListener(v -> {
                        appointment.setStatus("accepted");
                        status.setText(appointment.status);
                    });
                }

            } else {
                //CLIENT
                status.setVisibility(View.VISIBLE);
                if (showHistory == false && appointment.status.contains("accepted")) {
                    cancelAcceptButton.setText("Cancel");

                    cancelAcceptButton.setOnClickListener(v -> {
                        Provider_Service provider_service = appointment.provider_service.getTarget();
                        provider_service.appointments.remove(appointment);

                        Box<Appointments> appointmentsBox = ObjectBox.get().boxFor(Appointments.class);
                        appointmentsBox.remove(appointment);

                        serviceName.setVisibility(View.GONE);
                        providerName.setVisibility(View.GONE);
                        startTime.setVisibility(View.GONE);
                    });
                } else {
                    cancelAcceptButton.setVisibility(View.GONE);
                }

            }
        }
    }
}

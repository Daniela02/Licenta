package com.example.easyappointment.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.R;

public class ProviderSetScheduleFragment extends Fragment {

    public ProviderSetScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_set_schedule,
                container, false);
        if (getActivity().getClass().getSimpleName().contains("HomePageActivity")) {
            ((TextView) view.findViewById(R.id.scheduleTextView)).setVisibility(View.GONE);
        }

        return view;
    }
}

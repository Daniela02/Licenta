package com.example.easyappointment.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.R;

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
            ((TextView) view.findViewById(R.id.addServiceTextView)).setVisibility(View.GONE);
        }
        return view;
    }

}

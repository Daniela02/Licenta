package com.example.easyappointment.Fragments.ClientSpecific;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.R;


public class ProviderProfileFragment extends Fragment {

    public ProviderProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_provider_profile, container, false);
    }
    
}

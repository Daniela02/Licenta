package com.example.easyappointment.Fragments.ClientSpecific;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.R;

public class SearchForProviderFragment extends Fragment {

    public SearchForProviderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_for_provider, container, false);
    }


}

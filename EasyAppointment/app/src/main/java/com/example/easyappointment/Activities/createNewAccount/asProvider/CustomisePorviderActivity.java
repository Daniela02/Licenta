package com.example.easyappointment.Activities.createNewAccount.asProvider;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.easyappointment.Fragments.CategoryAndServices;
import com.example.easyappointment.R;

public class CustomisePorviderActivity extends FragmentActivity {

    public static final String NAME = "name";
    public static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customise_provider);

    }

}
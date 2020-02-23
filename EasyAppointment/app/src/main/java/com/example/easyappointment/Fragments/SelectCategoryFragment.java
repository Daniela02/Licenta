package com.example.easyappointment.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.providerSpecifics.Category;
import com.example.easyappointment.data.Models.providerSpecifics.Category_;

import io.objectbox.Box;


public class SelectCategoryFragment extends Fragment {

    private Spinner dropDownCategory;

    public SelectCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_category, container, false);
        dropDownCategory = view.findViewById(R.id.categorySpinner);

        if (getActivity().getClass().getSimpleName().contains("HomePageActivity")) {
            ((TextView) view.findViewById(R.id.categoryTextView)).setVisibility(View.GONE);
        }

        Box<Category> categoryBox = ObjectBox.get().boxFor(Category.class);
        String[] items = categoryBox.query().build().property(Category_.category_name).distinct().findStrings();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropDownCategory.setAdapter(adapter);
        return view;
    }


}

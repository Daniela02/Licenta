package com.example.easyappointment.Fragments.ProviderSpecific;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.accounts.Provider_;
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
            Button submitButton = view.findViewById(R.id.submit_select_category);
            submitButton.setVisibility(View.VISIBLE);

            HomePageActivity host = (HomePageActivity) getActivity();

            Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
            Provider provider = providerBox
                    .query()
                    .equal(Provider_.accountId, host.account.account_id)
                    .build()
                    .findFirst();
            ((TextView) view.findViewById(R.id.categoryTextView)).setText("Your current category is: " + provider.category.getTarget().category_name);
            ((TextView) view.findViewById(R.id.categoryTextView)).setTextSize(17);

            submitButton.setOnClickListener(v -> {
                String selectedCategory = ((Spinner) view.findViewById(R.id.categorySpinner))
                        .getSelectedItem()
                        .toString();

                Box<Category> categoryBox = ObjectBox.get().boxFor(Category.class);
                Category category = categoryBox
                        .query()
                        .equal(Category_.category_name, selectedCategory)
                        .build()
                        .findFirst();

                Category oldCategory = categoryBox
                        .query()
                        .equal(Category_.category_name, provider.category.getTarget().category_name)
                        .build()
                        .findFirst();

                category.providers.add(provider);
                provider.category.setTarget(category);
                oldCategory.providers.remove(provider);
                providerBox.put(provider);

                Toast.makeText(this.getContext(), "Information submitted", Toast.LENGTH_SHORT).show();
            });

        }

        Box<Category> categoryBox = ObjectBox.get().boxFor(Category.class);
        String[] items = categoryBox.query().build().property(Category_.category_name).distinct().findStrings();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropDownCategory.setAdapter(adapter);
        return view;
    }


}

package com.example.easyappointment.Fragments.ClientSpecific;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Adapters.ListProvidersAdapter;
import com.example.easyappointment.data.Adapters.ListServiceAdapter;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Account;
import com.example.easyappointment.data.Models.accounts.Account_;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.accounts.Provider_;
import com.example.easyappointment.data.Models.providerSpecifics.Category;
import com.example.easyappointment.data.Models.providerSpecifics.Category_;
import com.example.easyappointment.data.Models.providerSpecifics.Service;
import com.example.easyappointment.data.Models.providerSpecifics.Service_;

import java.util.List;

import io.objectbox.Box;

public class ShowProvidersFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ShowProvidersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.searchResultsRecycleView);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        HomePageActivity host = (HomePageActivity) getActivity();

        if (getArguments() != null && getArguments().containsKey("category_name")) {
            host.getSupportActionBar().setTitle("Providers");
            view.findViewById(R.id.searchBarEditText).setVisibility(View.GONE);
            view.findViewById(R.id.searchButton).setVisibility(View.GONE);
            String categoryName = getArguments().getString("category_name");

            Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
            Box<Category> categoryBox = ObjectBox.get().boxFor(Category.class);

            Category category = categoryBox
                    .query().equal(Category_.category_name, categoryName)
                    .build()
                    .findFirst();
            List<Provider> providerList = providerBox
                    .query()
                    .equal(Provider_.categoryId, category.category_id)
                    .build()
                    .find();
            mAdapter = new ListProvidersAdapter(providerList, host);
            recyclerView.setAdapter(mAdapter);
        } else {
            if (host.account.type.contains(host.getString(R.string.client))) {
                host.getSupportActionBar().setTitle("Search");
                EditText searchedText = view.findViewById(R.id.searchBarEditText);
                Button searchButton = view.findViewById(R.id.searchButton);
                searchButton.setOnClickListener(v -> {

                    String searchedName = searchedText.getText().toString().toLowerCase();
                    Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);

                    long[] accountList = accountBox.query()
                            .equal(Account_.type, host.getString(R.string.provider))
                            .contains(Account_.name, searchedName)
                            .build().findIds();

                    if (accountList.length == 0) {
                        Box<Service> serviceBox = ObjectBox.get().boxFor(Service.class);

                        List<Service> serviceList = serviceBox
                                .query()
                                .contains(Service_.name, searchedName)
                                .build()
                                .find();

                        mAdapter = new ListServiceAdapter(serviceList, "search", host);
                    } else {
                        Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);

                        List<Provider> providerList = providerBox
                                .query()
                                .in(Provider_.accountId, accountList)
                                .build()
                                .find();

                        mAdapter = new ListProvidersAdapter(providerList, host);
                    }
                    recyclerView.setAdapter(mAdapter);
                });
            }
        }
        return view;
    }


}

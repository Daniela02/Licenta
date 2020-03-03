package com.example.easyappointment.Fragments.Common;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Adapters.ListAppointmentsAdapter;
import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.accounts.Provider_;
import com.example.easyappointment.data.Models.providerSpecifics.Category;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.objectbox.Box;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private HashMap<String, Integer> imageMap;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        HomePageActivity host = (HomePageActivity) getActivity();
        recyclerView = view.findViewById(R.id.homePageRecycleView);

        if (host.account.type.contains("Provider")) {
            view.findViewById(R.id.search_by_category_TextView).setVisibility(View.GONE);
            Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
            Provider provider = providerBox.query().equal(Provider_.accountId, host.account.account_id).build().findFirst();

            layoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(layoutManager);
            Date now = new Date();
            List<Appointments> appointmentsList = provider.getAppointments()
                    .stream()
                    .filter(a -> (new Date(a.start_time).after(now)))
                    .filter(a -> a.status.contains("accepted"))
                    .collect(Collectors.toList());
            mAdapter = new ListAppointmentsAdapter(appointmentsList, true, false, host);
            recyclerView.setAdapter(mAdapter);
        }
        if (host.account.type.contains("Client")) {
            view.findViewById(R.id.accepted_appointments_TextView).setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            GridLayout gridLayout = view.findViewById(R.id.category_GridLayout);
            Box<Category> categoryBox = ObjectBox.get().boxFor(Category.class);
            List<Category> categoryList = categoryBox.getAll();
            initImageMap(categoryList);
            gridLayout.removeAllViews();
            int total = categoryList.size();
            int column = 2;
            int row = total / column;
            gridLayout.setColumnCount(column);
            gridLayout.setRowCount(row + 1);
            for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
                if (c == column) {
                    c = 0;
                    r++;
                }
                TextView category = new TextView(this.getContext());
                ImageView photo = new ImageView(this.getContext());
                String category_name = categoryList.get(i).category_name;
                photo.setImageResource(imageMap.get(category_name));
                photo.setOnClickListener(v -> {
                    NavController navController = Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment);
                    Bundle bundle = new Bundle();
                    bundle.putString("category_name", category_name);
                    navController.navigate(R.id.client_search, bundle);
                });
                photo.setPadding(15, 5, 15, 5);
                category.setText(category_name);
                category.setTextSize(17);
                category.setTypeface(null, Typeface.BOLD);
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                param.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                param.leftMargin = 15;
                param.rightMargin = 15;
                param.topMargin = 20;
                param.setGravity(Gravity.CENTER);
                param.columnSpec = GridLayout.spec(c);
                param.rowSpec = GridLayout.spec(r);
                category.setLayoutParams(param);
                param.height = 450;
                param.width = 300;
                photo.setLayoutParams(param);
                gridLayout.addView(category);
                gridLayout.addView(photo);
            }

        }
        return view;
    }

    private void initImageMap(List<Category> categoryList) {
        imageMap = new HashMap<>();
        imageMap.put(categoryList.get(0).category_name, R.drawable.car);
        imageMap.put(categoryList.get(1).category_name, R.drawable.women_beauty);
        imageMap.put(categoryList.get(2).category_name, R.drawable.doctors);
        imageMap.put(categoryList.get(3).category_name, R.drawable.barbers);

    }
}

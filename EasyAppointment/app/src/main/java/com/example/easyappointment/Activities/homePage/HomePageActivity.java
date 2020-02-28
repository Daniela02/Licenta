package com.example.easyappointment.Activities.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Account;
import com.example.easyappointment.data.Models.accounts.Account_;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import io.objectbox.Box;

public class HomePageActivity extends AppCompatActivity {

    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    public Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent = getIntent();
        String email = intent.getStringExtra(EMAIL);
        String name = intent.getStringExtra(NAME);
        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
        account = accountBox.query().equal(Account_.email, email).equal(Account_.name, name).build().findFirst();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton clientSearch = findViewById(R.id.client_search);
        FloatingActionButton providerAddService = findViewById(R.id.provider_add_service);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_future_appointments, R.id.nav_appointments,
                R.id.nav_change_personal_details, R.id.nav_signout, R.id.nav_change_category,
                R.id.nav_change_schedule, R.id.nav_services, R.id.provider_add_service,
                R.id.nav_pending_appointments, R.id.client_search)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        ((TextView) (navigationView.getHeaderView(0).findViewById(R.id.nav_email))).setText(email);
        ((TextView) (navigationView.getHeaderView(0).findViewById(R.id.nav_name))).setText(name);

        if (account.type.equals("Provider")) {
            //PROVIDER
            clientSearch.hide();
            hideClientSpecificMenuItems();
            Navigation.setViewNavController(providerAddService, navController);
            providerAddService.setOnClickListener(v -> {
                navController.navigate(R.id.provider_add_service);
            });

        } else {
            //CLIENT
            providerAddService.hide();

            hideProviderSpecificMenuItems();
            Navigation.setViewNavController(clientSearch, navController);
            clientSearch.setOnClickListener(view -> {
                navController.navigate(R.id.client_search);
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void hideProviderSpecificMenuItems() {
        navigationView = findViewById(R.id.nav_view);
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.nav_change_personal_details).setVisible(false);
        nav_menu.findItem(R.id.nav_services).setVisible(false);
        nav_menu.findItem(R.id.nav_change_category).setVisible(false);
        nav_menu.findItem(R.id.nav_change_schedule).setVisible(false);
        nav_menu.findItem(R.id.nav_pending_appointments).setVisible(false);
    }

    public void hideClientSpecificMenuItems() {
        navigationView = findViewById(R.id.nav_view);
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.nav_appointments).setVisible(false);
    }
}

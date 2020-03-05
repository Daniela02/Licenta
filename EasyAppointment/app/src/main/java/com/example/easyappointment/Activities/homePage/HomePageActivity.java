package com.example.easyappointment.Activities.homePage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.squareup.picasso.Picasso;

import io.objectbox.Box;

import static com.example.easyappointment.GoogleApi.GoogleCalendar.MY_PERMISSION_WRITE_CALENDAR;

public class HomePageActivity extends AppCompatActivity {

    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String NOTIFICATION = "NOTIFICATION";
    private Intent intent;
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    public Account account;
    public boolean hasPermitionToWriteCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            hasPermitionToWriteCalendar = false;
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSION_WRITE_CALENDAR);
            }
        } else {
            hasPermitionToWriteCalendar = true;
        }

        intent = getIntent();
        String email = intent.getStringExtra(EMAIL);
        String name = intent.getStringExtra(NAME);
        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
        account = accountBox
                .query()
                .equal(Account_.email, email)
                .equal(Account_.name, name)
                .build()
                .findFirst();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton clientSearch = findViewById(R.id.client_search);
        FloatingActionButton providerAddService = findViewById(R.id.provider_add_service);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        ((TextView) (navigationView.getHeaderView(0).findViewById(R.id.nav_email))).setText(email);
        ((TextView) (navigationView.getHeaderView(0).findViewById(R.id.nav_name))).setText(name);
        ImageView profilePicture = navigationView.getHeaderView(0).findViewById(R.id.nav_image_profile);

        if (account.imageURL != null) {
            Picasso.get().load(Uri.parse(account.imageURL)).into(profilePicture);
        }

        if (account.type.equals(getString(R.string.provider))) {
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
        //from notification
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (intent.hasExtra(NOTIFICATION) && intent.getStringExtra(NOTIFICATION).equals(getString(R.string.provider).toLowerCase())) {
            navController.navigate(R.id.nav_pending_appointments);
        }
        if (intent.hasExtra(NOTIFICATION) && intent.getStringExtra(NOTIFICATION).equals(getString(R.string.client).toLowerCase())) {
            navController.navigate(R.id.nav_future_appointments);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_WRITE_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermitionToWriteCalendar = true;
                } else {
                    hasPermitionToWriteCalendar = false;
                }
                return;
            }
        }
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
        nav_menu.findItem(R.id.nav_future_appointments).setVisible(false);
    }
}

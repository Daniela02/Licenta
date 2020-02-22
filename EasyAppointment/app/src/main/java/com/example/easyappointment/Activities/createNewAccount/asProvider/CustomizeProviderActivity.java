package com.example.easyappointment.Activities.createNewAccount.asProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyappointment.Activities.profile.ProfileActivity;
import com.example.easyappointment.Fragments.AddServiceFragment;
import com.example.easyappointment.Fragments.PersonalDetailsFragment;
import com.example.easyappointment.Fragments.ProviderSetScheduleFragment;
import com.example.easyappointment.Fragments.SelectCategoryFragment;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Account;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.providerSpecifics.Category;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;
import com.example.easyappointment.data.Models.providerSpecifics.Schedules;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import io.objectbox.Box;

public class CustomizeProviderActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    private Integer count;
    private Button nextButton;
    private Button backButton;

    private Account account;
    private Provider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_provider);
        Intent providerIntent = getIntent();
        nextButton = findViewById(R.id.NextButton);
        backButton = findViewById(R.id.BackButton);
        backButton.setVisibility(View.GONE);
        count = 0;

        String email = providerIntent.getStringExtra(EMAIL);
        String name = providerIntent.getStringExtra(NAME);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
        SelectCategoryFragment selectCategoryFragment = new SelectCategoryFragment();
        ProviderSetScheduleFragment providerSetScheduleFragment = new ProviderSetScheduleFragment();
        AddServiceFragment addServiceFragment = new AddServiceFragment();

        fragmentTransaction.add(R.id.customizeProviderRelativeLayout, personalDetailsFragment);
        fragmentTransaction.add(R.id.customizeProviderRelativeLayout, selectCategoryFragment);
        fragmentTransaction.hide(selectCategoryFragment);
        fragmentTransaction.add(R.id.customizeProviderRelativeLayout, providerSetScheduleFragment);
        fragmentTransaction.hide(providerSetScheduleFragment);
        fragmentTransaction.add(R.id.customizeProviderRelativeLayout, addServiceFragment);
        fragmentTransaction.hide(addServiceFragment);
        fragmentTransaction.commitNow();
        fragmentManager.executePendingTransactions();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count) {
                    case 0:
                        // to category
                        FragmentTransaction fragmentTransactionCat = fragmentManager.beginTransaction();
                        fragmentTransactionCat.hide(personalDetailsFragment);
                        fragmentTransactionCat.show(selectCategoryFragment);
                        fragmentTransactionCat.commitNow();
                        fragmentManager.executePendingTransactions();
                        backButton.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        // to schedule
                        FragmentTransaction fragmentTransactionSche = fragmentManager.beginTransaction();
                        fragmentTransactionSche.hide(selectCategoryFragment);
                        fragmentTransactionSche.show(providerSetScheduleFragment);
                        fragmentTransactionSche.commitNow();
                        fragmentManager.executePendingTransactions();
                        break;
                    case 2:
                        // to new service
                        FragmentTransaction fragmentTransactionSer = fragmentManager.beginTransaction();
                        fragmentTransactionSer.hide(providerSetScheduleFragment);
                        fragmentTransactionSer.show(addServiceFragment);
                        fragmentTransactionSer.commitNow();
                        fragmentManager.executePendingTransactions();

                        nextButton.setText("Submit");
                        break;
                    case 3:
                        //SUBMIT
                        String phone = ((EditText) findViewById(R.id.editPhone)).getText().toString();
                        String address = ((EditText) findViewById(R.id.editAddress)).getText().toString();

                        account = new Account(email, name);
                        account.setType("Provider");
                        provider = new Provider(address, phone);
                        provider.account.setTarget(account);

                        //CATEGORY
                        String selectedCategory = ((Spinner) findViewById(R.id.categorySpinner)).getSelectedItem().toString();
                        Category category = new Category(selectedCategory);
                        provider.category.setTarget(category);

                        //SCHEDULES
                        String startTime = ((EditText) findViewById(R.id.monStartTime)).getText().toString();
                        String endTime = ((EditText) findViewById(R.id.monEndTime)).getText().toString();
                        Schedules monday = new Schedules("Monday", startTime, endTime);
                        provider.schedules.add(monday);

                        startTime = ((EditText) findViewById(R.id.tuesStartTime)).getText().toString();
                        endTime = ((EditText) findViewById(R.id.tuesEndTime)).getText().toString();
                        Schedules tuesday = new Schedules("Tuesday", startTime, endTime);
                        provider.schedules.add(tuesday);

                        startTime = ((EditText) findViewById(R.id.wedStartTime)).getText().toString();
                        endTime = ((EditText) findViewById(R.id.wedEndTime)).getText().toString();
                        Schedules wednesday = new Schedules("Wednesay", startTime, endTime);
                        provider.schedules.add(wednesday);

                        startTime = ((EditText) findViewById(R.id.thursStartTime)).getText().toString();
                        endTime = ((EditText) findViewById(R.id.thursEndTime)).getText().toString();
                        Schedules thursday = new Schedules("Thursday", startTime, endTime);
                        provider.schedules.add(thursday);

                        startTime = ((EditText) findViewById(R.id.friStartTime)).getText().toString();
                        endTime = ((EditText) findViewById(R.id.friEndTime)).getText().toString();
                        Schedules friday = new Schedules("Friday", startTime, endTime);
                        provider.schedules.add(friday);

                        //SERVICE
                        String serviceName = ((EditText) findViewById(R.id.addServiceName)).getText().toString();
                        String serviceDescription = ((EditText) findViewById(R.id.addServiceDescription)).getText().toString();
                        String serviceDuration = ((EditText) findViewById(R.id.addServiceDuration)).getText().toString();

                        Service service = new Service(serviceName, serviceDescription, serviceDuration);
                        Provider_Service provider_service = new Provider_Service();
                        provider_service.provider.setTarget(provider);
                        provider_service.service.setTarget(service);
                        provider.services.add(provider_service);


                        //CREATE ACCOUNT, PROVIDER AND DEPENDENCIES

                        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
                        accountBox.put(account);

                        Box<Schedules> schedulesBox = ObjectBox.get().boxFor(Schedules.class);
                        schedulesBox.put(monday);
                        schedulesBox.put(tuesday);
                        schedulesBox.put(wednesday);
                        schedulesBox.put(thursday);
                        schedulesBox.put(friday);

                        Box<Service> serviceBox = ObjectBox.get().boxFor(Service.class);
                        serviceBox.put(service);

                        Box<Provider_Service> provider_serviceBox = ObjectBox.get().boxFor(Provider_Service.class);
                        provider_serviceBox.put(provider_service);

                        Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
                        providerBox.put(provider);

                        Intent homeIntent = new Intent(CustomizeProviderActivity.this, ProfileActivity.class);
                        homeIntent.putExtra(ProfileActivity.EMAIL, email);
                        homeIntent.putExtra(ProfileActivity.NAME, name);
                        startActivity(homeIntent);
                        finish();
                        break;
                    default:
                        break;
                }
                count++;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count) {
                    case 1:
                        // to personal details
                        FragmentTransaction fragmentTransactionPD = fragmentManager.beginTransaction();
                        fragmentTransactionPD.show(personalDetailsFragment);
                        fragmentTransactionPD.hide(selectCategoryFragment);
                        fragmentTransactionPD.commitNow();
                        fragmentManager.executePendingTransactions();
                        backButton.setVisibility(View.GONE);
                        break;
                    case 2:
                        //to category
                        FragmentTransaction fragmentTransactionCat = fragmentManager.beginTransaction();
                        fragmentTransactionCat.show(selectCategoryFragment);
                        fragmentTransactionCat.hide(providerSetScheduleFragment);
                        fragmentTransactionCat.commitNow();
                        fragmentManager.executePendingTransactions();
                        break;
                    case 3:
                        //to daily schedule
                        FragmentTransaction fragmentTransactionDS = fragmentManager.beginTransaction();
                        fragmentTransactionDS.show(providerSetScheduleFragment);
                        fragmentTransactionDS.hide(addServiceFragment);
                        fragmentTransactionDS.commitNow();
                        fragmentManager.executePendingTransactions();
                        nextButton.setText("Next");
                        break;
                    default:
                        break;
                }
                count--;
            }
        });


    }

}
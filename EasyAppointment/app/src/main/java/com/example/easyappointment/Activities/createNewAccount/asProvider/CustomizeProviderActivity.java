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

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.Fragments.ProviderSpecific.AddServiceFragment;
import com.example.easyappointment.Fragments.ProviderSpecific.PersonalDetailsFragment;
import com.example.easyappointment.Fragments.ProviderSpecific.ProviderSetScheduleFragment;
import com.example.easyappointment.Fragments.ProviderSpecific.SelectCategoryFragment;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Account;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.providerSpecifics.Category;
import com.example.easyappointment.data.Models.providerSpecifics.Category_;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;
import com.example.easyappointment.data.Models.providerSpecifics.Schedules;
import com.example.easyappointment.data.Models.providerSpecifics.Service;

import io.objectbox.Box;

public class CustomizeProviderActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String ACCOUNT_IMAGE = "image";
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
        String image = providerIntent.getStringExtra(ACCOUNT_IMAGE);

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

        nextButton.setOnClickListener(v -> {
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

                    nextButton.setText(getString(R.string.submit));
                    break;
                case 3:
                    //SUBMIT
                    String phone = ((EditText) findViewById(R.id.editPhone)).getText().toString();
                    String address = ((EditText) findViewById(R.id.editAddress)).getText().toString();

                    account = new Account(email, name);
                    account.setImageURL(image);
                    account.setType("Provider");
                    provider = new Provider(address, phone);
                    Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
                    providerBox.attach(provider);

                    //CATEGORY
                    String selectedCategory = ((Spinner) findViewById(R.id.categorySpinner)).getSelectedItem().toString();

                    //SCHEDULES
                    Box<Schedules> schedulesBox = ObjectBox.get().boxFor(Schedules.class);
                    String startTime = ((EditText) findViewById(R.id.monStartTime)).getText().toString();
                    String endTime = ((EditText) findViewById(R.id.monEndTime)).getText().toString();
                    Schedules monday = new Schedules(getString(R.string.monday), startTime, endTime);
                    schedulesBox.attach(monday);
                    provider.schedules.add(monday);

                    startTime = ((EditText) findViewById(R.id.tuesStartTime)).getText().toString();
                    endTime = ((EditText) findViewById(R.id.tuesEndTime)).getText().toString();
                    Schedules tuesday = new Schedules(getString(R.string.tuesday), startTime, endTime);
                    schedulesBox.attach(tuesday);
                    provider.schedules.add(tuesday);

                    startTime = ((EditText) findViewById(R.id.wedStartTime)).getText().toString();
                    endTime = ((EditText) findViewById(R.id.wedEndTime)).getText().toString();
                    Schedules wednesday = new Schedules(getString(R.string.wednesday), startTime, endTime);
                    schedulesBox.attach(wednesday);
                    provider.schedules.add(wednesday);

                    startTime = ((EditText) findViewById(R.id.thursStartTime)).getText().toString();
                    endTime = ((EditText) findViewById(R.id.thursEndTime)).getText().toString();
                    Schedules thursday = new Schedules(getString(R.string.thursday), startTime, endTime);
                    schedulesBox.attach(thursday);
                    provider.schedules.add(thursday);

                    startTime = ((EditText) findViewById(R.id.friStartTime)).getText().toString();
                    endTime = ((EditText) findViewById(R.id.friEndTime)).getText().toString();
                    Schedules friday = new Schedules(getString(R.string.friday), startTime, endTime);
                    schedulesBox.attach(friday);
                    provider.schedules.add(friday);

                    //SERVICE
                    String serviceName = ((EditText) findViewById(R.id.addServiceName)).getText().toString();
                    Double servicePrice = Double.parseDouble(((EditText) findViewById(R.id.addServicePrice)).getText().toString());
                    Integer serviceDuration = Integer.parseInt(((EditText) findViewById(R.id.addServiceDuration)).getText().toString());

                    if (servicePrice == null) {
                        servicePrice = 0D;
                    }

                    Box<Provider_Service> provider_serviceBox = ObjectBox.get().boxFor(Provider_Service.class);
                    Box<Service> serviceBox = ObjectBox.get().boxFor(Service.class);

                    Service service = new Service(serviceName, servicePrice, serviceDuration);
                    serviceBox.attach(service);
                    Provider_Service provider_service = new Provider_Service();
                    provider_serviceBox.attach(provider_service);
                    service.provider_service.setTarget(provider_service);
                    provider_service.provider.setTarget(provider);
                    provider_service.service.setTarget(service);
                    provider.provider_services.add(provider_service);


                    //CREATE ACCOUNT, PROVIDER AND DEPENDENCIES

                    Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
                    accountBox.put(account);

                    schedulesBox.put(monday);
                    schedulesBox.put(tuesday);
                    schedulesBox.put(wednesday);
                    schedulesBox.put(thursday);
                    schedulesBox.put(friday);


                    serviceBox.put(service);


                    provider_serviceBox.put(provider_service);

                    Box<Category> categoryBox = ObjectBox.get().boxFor(Category.class);
                    Category category = categoryBox
                            .query().equal(Category_.category_name, selectedCategory)
                            .build()
                            .findFirst();
                    category.providers.add(provider);
                    provider.account.setTarget(account);
                    provider.category.setTarget(category);
                    providerBox.put(provider);

                    Intent homeIntent = new Intent(CustomizeProviderActivity.this, HomePageActivity.class);
                    homeIntent.putExtra(HomePageActivity.EMAIL, email);
                    startActivity(homeIntent);
                    finish();
                    break;
                default:
                    break;
            }
            count++;
        });

        backButton.setOnClickListener(v -> {
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
        });


    }

}
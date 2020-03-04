package com.example.easyappointment.Activities.createNewAccount;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyappointment.Activities.createNewAccount.asProvider.CustomizeProviderActivity;
import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Account;
import com.example.easyappointment.data.Models.accounts.Client;

import io.objectbox.Box;

public class ChooseTypeActivity extends AppCompatActivity {
    public static final String ACCOUNT_NAME = "name";
    public static final String ACCOUNT_EMAIL = "email";
    public static final String ACCOUNT_IMAGE = "image";
    private Button clientButton;
    private Button providerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);

        clientButton = findViewById(R.id.ClientButton);
        providerButton = findViewById(R.id.ProviderButton);

        Intent chooseIntent = getIntent();
        final String name = chooseIntent.getStringExtra(ACCOUNT_NAME);
        final String email = chooseIntent.getStringExtra(ACCOUNT_EMAIL);
        final String image = chooseIntent.getStringExtra(ACCOUNT_IMAGE);


        clientButton.setOnClickListener(v -> {
            //CREATING THE NEW ACCOUNT
            Account account = new Account(email, name);
            account.setType(getString(R.string.client));
            account.setImageURL(image);

            Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);

            accountBox.put(account);
            //CREATING THE NEW CLIENT

            Box<Client> clientBox = ObjectBox.get().boxFor(Client.class);
            Client client = new Client();
            clientBox.attach(client);
            client.account.setTarget(account);

            clientBox.put(client);
            //home page
            Intent homeIntent = new Intent(ChooseTypeActivity.this, HomePageActivity.class);
            homeIntent.putExtra(HomePageActivity.EMAIL, email);
            homeIntent.putExtra(HomePageActivity.NAME, name);
            startActivity(homeIntent);
            finish();
        });

        providerButton.setOnClickListener(v -> {

            Intent providerIntent = new Intent(ChooseTypeActivity.this, CustomizeProviderActivity.class);
            providerIntent.putExtra(CustomizeProviderActivity.EMAIL, email);
            providerIntent.putExtra(CustomizeProviderActivity.NAME, name);
            startActivity(providerIntent);
            finish();
        });

    }

}

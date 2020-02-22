package com.example.easyappointment.Activities.createNewAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyappointment.Activities.createNewAccount.asProvider.CustomizeProviderActivity;
import com.example.easyappointment.Activities.profile.ProfileActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Account;
import com.example.easyappointment.data.Models.accounts.Client;

import io.objectbox.Box;

public class ChooseTypeActivity extends AppCompatActivity {
    public static final String ACCOUNT_NAME = "name";
    public static final String ACCOUNT_EMAIL = "email";
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

        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CREATING THE NEW ACCOUNT
                Account account = new Account(email, name);
                account.setType("Client");

                Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
                accountBox.removeAll();
                accountBox.put(account);
                //CREATING THE NEW CLIENT

                Client client = new Client();
                client.account.setTarget(account);
                Box<Client> clientBox = ObjectBox.get().boxFor(Client.class);
                clientBox.put(client);

                //profile activity
                Intent homeIntent = new Intent(ChooseTypeActivity.this, ProfileActivity.class);
                homeIntent.putExtra(ProfileActivity.EMAIL, email);
                homeIntent.putExtra(ProfileActivity.NAME, name);
                startActivity(homeIntent);
                finish();
            }
        });

        providerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent providerIntent = new Intent(ChooseTypeActivity.this, CustomizeProviderActivity.class);
                providerIntent.putExtra(CustomizeProviderActivity.EMAIL, email);
                providerIntent.putExtra(CustomizeProviderActivity.NAME, name);
                startActivity(providerIntent);
                finish();
            }
        });

    }

}

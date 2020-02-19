package com.example.easyappointment.Activities.createNewAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyappointment.Activities.createNewAccount.asProvider.CustomisePorviderActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Account;
import com.example.easyappointment.data.Models.accounts.Client;
import com.example.easyappointment.Activities.profile.ProfileActivity;

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
                accountBox.put(account);
                //CREATING THE NEW CLIENT

                Client client = new Client();
                client.account.setTarget(account);
                Box<Client> clientBox = ObjectBox.get().boxFor(Client.class);
                clientBox.put(client);

                //profile activity
                Intent clientIntent = new Intent(ChooseTypeActivity.this, ProfileActivity.class);
                clientIntent.putExtra(ProfileActivity.EMAIL, email);
                clientIntent.putExtra(ProfileActivity.NAME, name);
                startActivity(clientIntent);
                //finish();
            }
        });
    //TODO on click listener for create provider & CreateProvider Activity

        providerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CREATING THE NEW ACCOUNT
                Account account = new Account(email, name);
                account.setType("Provider");

                Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
                accountBox.put(account);

                Intent providerIntent = new Intent(ChooseTypeActivity.this, CustomisePorviderActivity.class);
                providerIntent.putExtra(CustomisePorviderActivity.EMAIL, email);
                providerIntent.putExtra(CustomisePorviderActivity.NAME, name);
                startActivity(providerIntent);
            }
        });

    }
}

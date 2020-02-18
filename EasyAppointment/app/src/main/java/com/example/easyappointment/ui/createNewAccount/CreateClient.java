package com.example.easyappointment.ui.createNewAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.easyappointment.R;
import com.example.easyappointment.data.model.ObjectBox;
import com.example.easyappointment.data.model.accounts.Account;
import com.example.easyappointment.data.model.accounts.Client;

import io.objectbox.Box;
import io.objectbox.relation.ToOne;

public class CreateClient extends AppCompatActivity {
    public static final String ACCOUNT_NAME = "name";
    public static final String ACCOUNT_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client);

        Intent chooseIntent = getIntent();
        //find account by id
        final String name = chooseIntent.getStringExtra(ACCOUNT_NAME);
        final String email = chooseIntent.getStringExtra(ACCOUNT_EMAIL);
        Account account = new Account(email, name);
        account.setType("Client");

        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
        accountBox.put(account);
        ObjectBox.get().boxFor(Client.class).removeAll();

        //create new client and the relation between the account and the client
        Client client = new Client();
        client.account.setTarget(account);
        //TODO get the info from the activity and then create client
        long clientId = ObjectBox.get().boxFor(Client.class).put(client);
    }
}

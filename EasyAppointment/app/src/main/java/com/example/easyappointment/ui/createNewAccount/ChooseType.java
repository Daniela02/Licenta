package com.example.easyappointment.ui.createNewAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyappointment.R;

public class ChooseType extends AppCompatActivity {
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
                Intent clientIntent = new Intent(ChooseType.this, CreateClient.class);
                clientIntent.putExtra(CreateClient.ACCOUNT_EMAIL, email);
                clientIntent.putExtra(CreateClient.ACCOUNT_NAME, name);
                startActivity(clientIntent);
                finish();
            }
        });
    /*TODO on click listener for create provider & CreateProvider Activity

        providerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent providerIntent = new Intent(this, CreateProvider.class);


                providerIntent.putExtra(ChooseType.ACCOUNT, (Parcelable) account);
                startActivity(providerIntent);
            }
        });
     */
    }
}

package com.example.easyappointment.Activities.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyappointment.Activities.createNewAccount.ChooseTypeActivity;
import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Account;
import com.example.easyappointment.data.Models.accounts.Account_;
import com.example.easyappointment.data.Models.accounts.Client;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.providerSpecifics.Category;
import com.example.easyappointment.data.Models.providerSpecifics.Provider_Service;
import com.example.easyappointment.data.Models.providerSpecifics.Schedules;
import com.example.easyappointment.data.Models.providerSpecifics.Service;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.List;

import io.objectbox.Box;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "AndroidClarified";
    private static final String  SIGN_OUT = "sign_out";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent signOutIntent = getIntent();
        String isSignOut = signOutIntent.getStringExtra(SIGN_OUT);
        if(isSignOut == null) {
            ObjectBox.init(this);
        }
        setContentView(R.layout.activity_login);

        final GoogleSignInClient googleSignInClient;
        final SignInButton googleSignInButton;

        //TODO DELETE
//        ObjectBox.get().boxFor(Account.class).removeAll();
//        ObjectBox.get().boxFor(Provider.class).removeAll();
//        ObjectBox.get().boxFor(Client.class).removeAll();
//        ObjectBox.get().boxFor(Schedules.class).removeAll();
//        ObjectBox.get().boxFor(Category.class).removeAll();
//        ObjectBox.get().boxFor(Provider_Service.class).removeAll();
//        ObjectBox.get().boxFor(Service.class).removeAll();

        Log.d("Verificare bd", ObjectBox.get().boxFor(Account.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Provider.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Client.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Schedules.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Category.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Provider_Service.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Service.class).getAll().toString());


        if (ObjectBox.get().boxFor(Category.class).isEmpty()) {
            categoriesInit();
        }

        //Google Sign in Button

        googleSignInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
        }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        //check if mail is in account table
        String email =  googleSignInAccount.getEmail();
        String name =  googleSignInAccount.getDisplayName();
        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
        List<Account> findEmail = accountBox.query().equal(Account_.email, email).build().find();
        //TODO delete
        Box<Provider> providerBox = ObjectBox.get().boxFor(Provider.class);
        Log.d("Conturi", accountBox.getAll().toString());
        Log.d("Providers no:", String.valueOf(providerBox.count()));

        if(findEmail.isEmpty()){
            Intent clientOrProviderIntent = new Intent(this, ChooseTypeActivity.class);
            clientOrProviderIntent.putExtra(ChooseTypeActivity.ACCOUNT_EMAIL, email);
            clientOrProviderIntent.putExtra(ChooseTypeActivity.ACCOUNT_NAME, name);
            startActivity(clientOrProviderIntent);
            finish();
        }
        else {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(HomePageActivity.NAME, name);
            intent.putExtra(HomePageActivity.EMAIL, email);
            startActivity(intent);
            finish();
        }

    }

    public void onStart() {
        super.onStart();

        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }

    public void categoriesInit() {
        Box<Category> categoryBox = ObjectBox.get().boxFor(Category.class);
        categoryBox.put(new Category("Auto Service"));
        categoryBox.put(new Category("Women Beauty Salons"));
        categoryBox.put(new Category("Doctors"));
        categoryBox.put(new Category("Barber Shops"));
    }

}

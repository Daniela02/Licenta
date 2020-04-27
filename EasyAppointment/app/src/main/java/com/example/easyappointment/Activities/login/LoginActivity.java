package com.example.easyappointment.Activities.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyappointment.Activities.createNewAccount.ChooseTypeActivity;
import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.BackgroundServices.SendClientNotificationService;
import com.example.easyappointment.BackgroundServices.SendProviderNotificationService;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.Appointments;
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
    private static final String ACCOUNT_ID = "account_id";
    private static Account account;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ObjectBox.get() == null) {
            ObjectBox.init(this);
        }

        setContentView(R.layout.activity_login);

        final GoogleSignInClient googleSignInClient;
        final SignInButton googleSignInButton;

        Log.d("Verificare bd", ObjectBox.get().boxFor(Account.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Provider.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Client.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Schedules.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Category.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Provider_Service.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Service.class).getAll().toString());
        Log.d("Verificare bd", ObjectBox.get().boxFor(Appointments.class).getAll().toString());

        if (ObjectBox.get().boxFor(Category.class).isEmpty()) {
            categoriesInit();
        }

        //Google Sign in Button

        googleSignInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 101);
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == 101) {
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
            }
    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        //check if mail is in account table
        String email =  googleSignInAccount.getEmail();
        String name =  googleSignInAccount.getDisplayName();
        String image = null;
        if (googleSignInAccount.getPhotoUrl() != null) {
            image = googleSignInAccount.getPhotoUrl().toString();
        }

        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
        List<Account> findEmail = accountBox.query().equal(Account_.email, email).build().find();
        if(findEmail.isEmpty()){
            Intent clientOrProviderIntent = new Intent(this, ChooseTypeActivity.class);
            clientOrProviderIntent.putExtra(ChooseTypeActivity.ACCOUNT_EMAIL, email);
            clientOrProviderIntent.putExtra(ChooseTypeActivity.ACCOUNT_NAME, name);
            clientOrProviderIntent.putExtra(ChooseTypeActivity.ACCOUNT_IMAGE, image);
            startActivity(clientOrProviderIntent);
            finish();
        }
        else {
            account = accountBox.query().equal(Account_.email, email).build().findFirst();
            account.setImageURL(image);
            accountBox.put(account);
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(HomePageActivity.EMAIL, email);
            startActivity(intent);
            finish();
        }

    }

    public void onStart() {
        super.onStart();

        GoogleSignInAccount alreadyLoggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyLoggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyLoggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent providerNotificationServiceIntent = new Intent(this, SendProviderNotificationService.class);
        Intent clientNotificationServiceIntent = new Intent(this, SendClientNotificationService.class);
        if (account != null && account.type.contains(getString(R.string.provider))) {
            providerNotificationServiceIntent.putExtra(ACCOUNT_ID, account.account_id);
            startService(providerNotificationServiceIntent);
            stopService(clientNotificationServiceIntent);
        }

        if (account != null && account.type.contains(getString(R.string.client))) {
            clientNotificationServiceIntent.putExtra(ACCOUNT_ID, account.account_id);
            startService(clientNotificationServiceIntent);
            stopService(providerNotificationServiceIntent);
        }
    }

    public void categoriesInit() {
        Box<Category> categoryBox = ObjectBox.get().boxFor(Category.class);
        categoryBox.put(new Category("Car Service"));
        categoryBox.put(new Category("Women's Beauty Salon"));
        categoryBox.put(new Category("Doctor"));
        categoryBox.put(new Category("Barber Shop"));

        //CREATE SHARED PREFERENCES FOR ASSOCIATING EACH CATEGORY WITH A PHOTO
        SharedPreferences pref = getSharedPreferences("Pref", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("Car Service", R.drawable.car);
        editor.putInt("Women's Beauty Salon", R.drawable.women_beauty);
        editor.putInt("Doctor", R.drawable.doctors);
        editor.putInt("Barber Shop", R.drawable.barbers);

        editor.commit();

    }

}

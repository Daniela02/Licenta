package com.example.easyappointment.Fragments.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.Activities.login.LoginActivity;
import com.example.easyappointment.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SignOutFragment extends Fragment {
    private static final String SIGN_OUT = "sign_out";
    private Button signOut;

    public SignOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_out,
                container, false);
        signOut = view.findViewById(R.id.sign_out);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);

        signOut.setOnClickListener(v -> googleSignInClient.signOut().addOnCompleteListener(task -> {
            signOut();
        }));
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    private void signOut() {
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        intent.putExtra(SIGN_OUT, "sign out");
        startActivity(intent);
        getActivity().finish();
    }
}

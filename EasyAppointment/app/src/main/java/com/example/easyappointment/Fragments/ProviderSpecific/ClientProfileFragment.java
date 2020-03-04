package com.example.easyappointment.Fragments.ProviderSpecific;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Client;
import com.squareup.picasso.Picasso;

import io.objectbox.Box;

public class ClientProfileFragment extends Fragment {

    private Client client;

    public ClientProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);
        Box<Client> clientBox = ObjectBox.get().boxFor(Client.class);
        client = clientBox.get(Long.parseLong(getArguments().getString("client_id")));
        String imageURL = client.account.getTarget().imageURL;

        ImageView profilePicture = view.findViewById(R.id.client_photo_profile);
        if (imageURL != null) {
            Picasso.get().load(Uri.parse(imageURL)).into(profilePicture);
        }

        ((TextView) view.findViewById(R.id.client_name_profile)).setText(client.account.getTarget().name);
        ((TextView) view.findViewById(R.id.client_email_profile)).setText(client.account.getTarget().email);

        return view;
    }
}

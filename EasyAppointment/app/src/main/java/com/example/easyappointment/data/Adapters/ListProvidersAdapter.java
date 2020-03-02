package com.example.easyappointment.data.Adapters;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListProvidersAdapter extends RecyclerView.Adapter {
    private List<Provider> providerList;
    private HomePageActivity host;

    public ListProvidersAdapter(List<Provider> providerList, HomePageActivity host) {
        this.providerList = providerList;
        this.host = host;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_show_provider, parent, false);
        return new ListViewHolder(view, providerList, host);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListProvidersAdapter.ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private List<Provider> providerList;
        private Long providerId;
        private HomePageActivity host;
        private TextView providerName;
        private TextView providerCategory;
        private ImageView profilePicture;


        public ListViewHolder(@NonNull View itemView, List<Provider> providerList, HomePageActivity host) {
            super(itemView);
            this.providerList = providerList;
            this.providerName = itemView.findViewById(R.id.show_provider_name);
            this.providerCategory = itemView.findViewById(R.id.show_provider_category);
            this.host = host;
            this.profilePicture = ((ImageView) (itemView.findViewById(R.id.provider_photo_search)));

            itemView.setOnClickListener(this::onClick);
        }


        @Override
        public void onClick(View v) {
            NavController navController = Navigation.findNavController(host, R.id.nav_host_fragment);
            Bundle bundle = new Bundle();
            bundle.putString("provider_id", providerId.toString());
            navController.navigate(R.id.provider_profile, bundle);
        }

        public void bindView(int poz) {
            Provider provider = providerList.get(poz);
            providerId = provider.provider_id;
            providerName.setText(provider.account.getTarget().name);
            providerCategory.setText(provider.category.getTarget().category_name);
            String imageURL = provider.account.getTarget().imageURL;
            if (imageURL != null) {
                Picasso.get().load(Uri.parse(imageURL)).into(profilePicture);
            }

        }
    }
}

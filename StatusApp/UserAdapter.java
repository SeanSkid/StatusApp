package com.example.mobilestatusapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserHolder> {

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {
        String availableImage = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/Available.png?alt=media&token=e7d83e68-321c-442d-bde9-5e238c1c5f48";
        String unavailableImage = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/Unavailable.png?alt=media&token=c3cfb8b9-1365-480e-a0be-79b394edbc43";
        String busyImage = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/Busy.png?alt=media&token=855d0109-ca2a-429e-888d-60933f765e50";

        holder.NameText.setText(model.getName());
        holder.StatusText.setText(model.getStatus());
        //holder.LocationText.setText(model.getLocation());
        Picasso.with(holder.ImageView.getContext()).load(model.getImage()).fit().centerCrop().into(holder.ImageView);
        if (model.getStatus().equals("Available")) {
            Picasso.with(holder.StatusView.getContext()).load(availableImage).into(holder.StatusView);
            holder.LocationText.setText(model.getLocation());
        } else if (model.getStatus().equals("Busy")) {
            Picasso.with(holder.StatusView.getContext()).load(busyImage).into(holder.StatusView);
            holder.LocationText.setText(model.getLocation());
        } else {
            Picasso.with(holder.StatusView.getContext()).load(unavailableImage).into(holder.StatusView);
            holder.LocationText.setText("");
        }
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new UserHolder(v);
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView NameText, StatusText, LocationText;
        ImageView ImageView, StatusView;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            NameText = itemView.findViewById(R.id.NameText);
            StatusText = itemView.findViewById(R.id.StatusText);
            LocationText = itemView.findViewById(R.id.LocationText);
            ImageView = itemView.findViewById(R.id.ImageView);
            StatusView = itemView.findViewById(R.id.StatusView);
        }
    }

}

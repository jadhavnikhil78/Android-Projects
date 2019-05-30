package com.example.usercontact;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>  {
    ArrayList<ContactDetails> myData = new ArrayList<ContactDetails>();
    String uid = new String();

    public ContactAdapter(ArrayList<ContactDetails> myData, String uid) {
        this.myData = myData;
        this.uid = uid;
        Log.d("demo2", uid);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_contact_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, uid);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ContactDetails contactDetails = myData.get(i);

        viewHolder.name.setText(contactDetails.getName());
        viewHolder.phone.setText(contactDetails.getPhoneNumber());
        viewHolder.email.setText(contactDetails.getEmail());

        viewHolder.contactDetails = contactDetails;
        viewHolder.uid = uid;
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, phone, email;
        ImageView photo;
        ContactDetails contactDetails;
        String uid;

        public ViewHolder(@NonNull View itemView, String uid) {
            super(itemView);
            this.contactDetails = contactDetails;
            this.uid = uid;

            name = itemView.findViewById(R.id.textView_Name);
            phone = itemView.findViewById(R.id.textView_Phone);
            email = itemView.findViewById(R.id.textView_Email);

            photo = itemView.findViewById(R.id.imageView2);
            Log.d("demo2",uid);
            final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference(uid);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mDatabase2.child(contactDetails.getId()).removeValue();
                    return false;
                }
            });

        }
    }
}

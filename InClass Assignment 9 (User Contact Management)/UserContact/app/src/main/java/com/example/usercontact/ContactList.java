package com.example.usercontact;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ContactList extends AppCompatActivity {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static ArrayList<ContactDetails> mDescribable = new ArrayList<ContactDetails>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        setTitle("Contacts");

        recyclerView = findViewById(R.id.rv_contactlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null && getIntent().getExtras() != null){
            final String trackDetails = (String) getIntent().getExtras().getSerializable(MainActivity.MUSIC_TRACK_KEY);
            DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference(trackDetails);

            mDatabase2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ContactList.mDescribable.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ContactDetails myexpense = ds.getValue(ContactDetails.class);
                        ContactList.mDescribable.add(myexpense);
                    }

                    if (ContactList.mDescribable.size() > 0) {
                        //Log.d("demo",trackDetails);
                        mAdapter = new ContactAdapter(ContactList.mDescribable, trackDetails);
                        recyclerView.setAdapter(mAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Button button = findViewById(R.id.button_CreateNewContact);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), NewContact.class);
                    intent.putExtra(MainActivity.MUSIC_TRACK_KEY, trackDetails);
                    getBaseContext().startActivity(intent);
                }
            });

            ImageButton imageButton = findViewById(R.id.imageButton);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(ContactList.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}

package com.example.usercontact;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class NewContact extends AppCompatActivity {

    ContactDetails contactDetails = new ContactDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        setTitle("Create New Contact");

        if (getIntent() != null && getIntent().getExtras() != null) {
            final String trackDetails = (String) getIntent().getExtras().getSerializable(MainActivity.MUSIC_TRACK_KEY);
            final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference(trackDetails);

            Button submit = findViewById(R.id.button_Submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText nameContact = findViewById(R.id.editText_NameContact);
                    EditText emailContact = findViewById(R.id.editText_EmailContact);
                    EditText phoneContact = findViewById(R.id.editText_Phone);
                    ImageView imageView = findViewById(R.id.imageView);

                    if(!nameContact.getText().toString().isEmpty() && !emailContact.getText().toString().isEmpty()
                            && !phoneContact.getText().toString().isEmpty()){
                        contactDetails.setName(nameContact.getText().toString());
                        contactDetails.setEmail(emailContact.getText().toString());
                        contactDetails.setPhoneNumber(phoneContact.getText().toString());
                        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        contactDetails.setImage(encodeBitmapAndSaveToFirebase(bitmap));

                        String key = mDatabase2.push().getKey();
                        contactDetails.setId(key);
                        mDatabase2.child(key).setValue(contactDetails);

                        finish();
                    }
                    else
                        Toast.makeText(v.getContext(), "Some Fields are empty", Toast.LENGTH_LONG).show();

                }
            });

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSelection();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.imageView);
        if (requestCode == 0) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String imageString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        Log.d("demo", imageString);

        return imageString;
    }

    public void imageSelection() {
        PackageManager pm = getPackageManager();
        final CharSequence[] options = {"Take Photo", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}

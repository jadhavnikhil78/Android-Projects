package com.example.usercontact;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signUp extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        String userid = "";
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            userid = currentUser.getUid();
            Intent intent = new Intent(getBaseContext(), ContactList.class);
            intent.putExtra(MainActivity.MUSIC_TRACK_KEY, userid);
            getBaseContext().startActivity(intent);
        }
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        mAuth = FirebaseAuth.getInstance();

        Button signup = findViewById(R.id.button_SignUp2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fname = findViewById(R.id.editText_FirstName);
                EditText lname = findViewById(R.id.editText_LastName);
                EditText email = findViewById(R.id.editText_Email);
                EditText password = findViewById(R.id.editText_PasswordSign);
                EditText confirmpassword = findViewById(R.id.editText_RePassword);

                if(!fname.getText().toString().isEmpty() && !lname.getText().toString().isEmpty() && !email.getText().toString().isEmpty()
                && !password.getText().toString().isEmpty() && !confirmpassword.getText().toString().isEmpty()){
                    if(password.getText().toString().compareTo(confirmpassword.getText().toString()) == 0){

                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("Login", "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String userid = user.getUid();
                                            Intent intent = new Intent(getBaseContext(), ContactList.class);
                                            intent.putExtra(MainActivity.MUSIC_TRACK_KEY, userid);
                                            getBaseContext().startActivity(intent);
                                            //updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });

                    }
                    else {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(signUp.this);
                        dlgAlert.setMessage("Passwords do not match");
                        dlgAlert.setTitle("Error Message...");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();

                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                    }
                }
                else
                    Toast.makeText(v.getContext(), "Some Fields are empty", Toast.LENGTH_LONG).show();

            }
        });

        Button cancel = findViewById(R.id.button_Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

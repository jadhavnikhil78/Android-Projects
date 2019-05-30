package com.example.usercontact;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static String MUSIC_TRACK_KEY = "TRACK";

    @Override
    public void onStart() {
        super.onStart();
        String userid = "";
        // Check if user is signed in (non-null) and update UI accordingly.
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
        setContentView(R.layout.activity_main);

        setTitle("Login");

        Button signup = findViewById(R.id.button_SignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signUp.class);
                startActivity(intent);
            }
        });

        Button login = findViewById(R.id.button_Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userName = findViewById(R.id.editText_UserName);
                EditText password = findViewById(R.id.editText2);

                if(!userName.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    mAuth.signInWithEmailAndPassword(userName.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Login", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userid = user.getUid();
                                        //updateUI(user);
                                        Intent intent = new Intent(getBaseContext(), ContactList.class);
                                        intent.putExtra(MainActivity.MUSIC_TRACK_KEY, userid);
                                        getBaseContext().startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Login", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
                else
                    Toast.makeText(v.getContext(), "Some Fields are empty", Toast.LENGTH_LONG).show();
            }
        });
    }
}

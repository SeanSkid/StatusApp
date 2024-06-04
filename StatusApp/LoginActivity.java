package com.example.mobilestatusapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {


    private EditText userEmail, userPassword;
    private ImageView ntuImage;
    private Button signInButton;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        // Set Things
        userEmail = (EditText)findViewById(R.id.EmailInput);
        userPassword = (EditText)findViewById(R.id.PasswordInput);
        ntuImage = (ImageView)findViewById(R.id.NTULogo);
        Picasso.with(getBaseContext()).load("https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/NTU_Logo.png?alt=media&token=7b31d671-1e86-414f-8bc0-047c3558c211").into(ntuImage);
        signInButton = (Button)findViewById(R.id.SignInButton);

        // Button Listener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signIn() {
        // Take text from input and pass through validate
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        if (!validate(email, password)) {
            return;
        }
        FirebaseLogin(email, password);
    }

    public boolean validate(String email, String password) {
        // Check if user inputted data into the fields
        if(email.isEmpty()) {
            userEmail.setError("Enter Email Address");
        }
        else if(password.isEmpty()) {
            userPassword.setError("Enter Password");
        }
        else {
            return true;
        }
        return false;
    }

    public void FirebaseLogin(String email, String password) {
        // Code edited from Firebase Assistant (Tools > Firebase > Authentication)
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(LoginActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
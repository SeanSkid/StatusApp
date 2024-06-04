package com.example.mobilestatusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StatusActivity extends AppCompatActivity {

    private Button saveButton;
    private Spinner statusInput, locationInput1;
    private EditText locationInput2;
    private FirebaseAuth mAuth;
    private CollectionReference usersRef;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);

        mAuth = FirebaseAuth.getInstance();

        // Set Things
        statusInput = (Spinner)findViewById(R.id.StatusInput);
        locationInput1 = (Spinner)findViewById(R.id.LocationInput1);
        locationInput2 = (EditText)findViewById(R.id.LocationInput2);
        saveButton = (Button)findViewById(R.id.SaveButton);
        usersRef = db.collection("users");

        // Set up Spinners content
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.StatusChoice, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.LocationChoice, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusInput.setAdapter(adapter1);
        locationInput1.setAdapter(adapter2);

        // Button Listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatus();
            }
        });
    }

    public void UpdateStatus() {
        if (!validate(locationInput2.getText().toString())) {
            return;
        }
        String Status = statusInput.getSelectedItem().toString();
        String Location = locationInput1.getSelectedItem().toString() + " " + locationInput2.getText().toString();
        updateDatabase("Status", Status);
        updateDatabase("Location", Location);
        finish();
    }

    public boolean validate(String number) {
        // Check if user inputted a number
        if(number.isEmpty()) {
            locationInput2.setError("Enter Room No.");
        }
        else {
            return true;
        }
        return false;
    }

    public void updateDatabase(String field, String content) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = usersRef.document(currentUser.getUid());
        // Code edited from Firebase Assistant (Tools > Firebase > Firestore)
        docRef.update(field, content).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(StatusActivity.this, "Update Successful", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StatusActivity.this, "Failed to Update", Toast.LENGTH_LONG).show();
            }
        });
    }
}

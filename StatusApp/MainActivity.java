package com.example.mobilestatusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView userImage, statusImage, backgroundBit2;
    private TextView userName, userRole, userStatus, userLocation, statusText, NotificationText, NotificationText2;
    private Button lecturerButton, statusButton, mapButton, signOutButton, setupButton;
    private String availableImage, unavailableImage, busyImage;
    private Spinner notificationInput;
    private FirebaseAuth mAuth;
    private CollectionReference usersRef;
    private NotificationManagerCompat notificationManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance(); // Get Firebase Auth Instance

        // Views
        userImage = (ImageView)findViewById(R.id.ImageOutput);
        statusImage = (ImageView)findViewById(R.id.StatusImage);
        backgroundBit2 = (ImageView)findViewById(R.id.backgroundBit2);
        userName = (TextView)findViewById(R.id.NameOutput);
        userRole = (TextView)findViewById(R.id.RoleOutput);
        userStatus = (TextView)findViewById(R.id.StatusOutput);
        userLocation = (TextView)findViewById(R.id.LocationOutput);
        statusText = (TextView)findViewById(R.id.statusText);
        NotificationText = (TextView)findViewById(R.id.NotificationText);
        NotificationText2 = (TextView)findViewById(R.id.NotificationText2);
        notificationInput = (Spinner)findViewById(R.id.NotificationInput);

        // Buttons
        lecturerButton = (Button)findViewById(R.id.LecturerButton);
        statusButton = (Button)findViewById(R.id.StatusButton);
        mapButton = (Button)findViewById(R.id.MapButton);
        signOutButton = (Button)findViewById(R.id.SignOutButton);
        setupButton = (Button)findViewById(R.id.SetupButton);

        // ImageURL's
        availableImage = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/Available.png?alt=media&token=e7d83e68-321c-442d-bde9-5e238c1c5f48";
        unavailableImage = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/Unavailable.png?alt=media&token=c3cfb8b9-1365-480e-a0be-79b394edbc43";
        busyImage = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/Busy.png?alt=media&token=855d0109-ca2a-429e-888d-60933f765e50";

        // Database Reference
        usersRef = db.collection("users");

        // Notification
        notificationManager = NotificationManagerCompat.from(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in, if not bring up Login Activity.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // If no user data, send to Login Activity
        if(currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        // If user data present, get data.
        if(currentUser != null) {
            SetupProfile(currentUser.getUid());
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.LecturerChoice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationInput.setAdapter(adapter);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out, launch LoginActivity
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch StatusActivity
                startActivity(new Intent(MainActivity.this, StatusActivity.class));
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch MapActivity
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        lecturerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch ListActivity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });

        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Notification Listener
                CreateListener(notificationInput.getSelectedItem().toString());
            }
        });
    }

    public void SetupProfile(String userID) {
        // Search up user on Firestore database
        DocumentReference docRef = usersRef.document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Set Details
                        userName.setText(document.getString("Name"));
                        userRole.setText(document.getString("Role"));
                        Picasso.with(getBaseContext()).load(document.getString("Image")).fit().centerCrop().into(userImage);
                        // Checks user, applies profile content based on Role
                        if (document.getString("Role").equals("Lecturer")) {
                            userStatus.setText(document.getString("Status"));
                            if (document.getString("Status").equals("Available")) {
                                Picasso.with(getBaseContext()).load(availableImage).into(statusImage);
                            } else if (document.getString("Status").equals("Busy")) {
                                Picasso.with(getBaseContext()).load(busyImage).into(statusImage);
                            } else {
                                Picasso.with(getBaseContext()).load(unavailableImage).into(statusImage);
                            }
                            // If Lecturer, gives Change Status content
                            userLocation.setText(document.getString("Location"));
                            userStatus.setVisibility(View.VISIBLE);
                            userLocation.setVisibility(View.VISIBLE);
                            statusButton.setVisibility(View.VISIBLE);
                            statusImage.setVisibility(View.VISIBLE);
                            statusText.setVisibility(View.VISIBLE);
                            backgroundBit2.setVisibility(View.INVISIBLE);
                            notificationInput.setVisibility(View.INVISIBLE);
                            NotificationText.setVisibility(View.INVISIBLE);
                            NotificationText2.setVisibility(View.INVISIBLE);
                            setupButton.setVisibility(View.GONE);
                        } else {
                            // If Student, gives Create Notification content
                            userStatus.setVisibility(View.INVISIBLE);
                            userLocation.setVisibility(View.INVISIBLE);
                            statusButton.setVisibility(View.GONE);
                            statusImage.setVisibility(View.INVISIBLE);
                            statusText.setVisibility(View.INVISIBLE);
                            backgroundBit2.setVisibility(View.VISIBLE);
                            notificationInput.setVisibility(View.VISIBLE);
                            NotificationText.setVisibility(View.VISIBLE);
                            NotificationText2.setVisibility(View.VISIBLE);
                            setupButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // Failed to get document
                        Toast.makeText(MainActivity.this, "No Document Found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Failed to get collection
                    Toast.makeText(MainActivity.this, "No collection Found", Toast.LENGTH_LONG).show();
                }
            }
        });
        return;
    }

    public void CreateListener(String Lecturer) {
        // Alternate way of setting userID's for lecturers
        String selectedID = "";
        if (Lecturer.equals("Brian Cox")) {
            selectedID = "Q0lYooQlF1ZI8WSXOR9fPFfCqOZ2";
        } else if (Lecturer.equals("Bill Nye")) {
            selectedID = "lKdohuDSeaOQXrO4MekYK66jBZH3";
        } else if (Lecturer.equals("Stephen Fry")) {
            selectedID = "u6WMJc26LgWqt96Y9EOn3360Eez1";
        } else if (Lecturer.equals("Tim Berners-Lee")) {
            selectedID = "mYtEiuGkyMP07n7Z2EyZ9UZP8cf1";
        }
        // Creates a listen event on the document based on ID above
        DocumentReference docRef = usersRef.document(selectedID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "No documentFound", Toast.LENGTH_LONG).show();
                    return;
                }
                if (documentSnapshot.exists()) {
                    if ( documentSnapshot.getString("Status").equals("Available")) {
                        CreateNotification(documentSnapshot.getString("Name"));
                        return;
                    }
                }
            }
        });
    }

    public void CreateNotification(String Lecturer) {
        // Creates a notification for the user when lecturer availability changes.
        Notification notification = new NotificationCompat.Builder(this, NotificationActivity.Channel1ID)
                .setSmallIcon(R.drawable.ntu_logo)
                .setContentTitle("Updated Availability")
                .setContentText(Lecturer + " became Available.")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
        return;
    }
}
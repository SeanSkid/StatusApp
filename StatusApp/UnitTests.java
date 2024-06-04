package com.example.mobilestatusapp;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UnitTests extends Application {

    public void Tests() {
        // Test Firestore
        String expected1 = "Sean Skidmore";
        String result1 = TestFirestore("zdZZxdN8laZgtUocko7dNeEK53w1");
        EqualsCheck("Test 1", expected1, result1);

        // Test Storage
        String expected2 = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/NTU_Logo.png?alt=media&token=7b31d671-1e86-414f-8bc0-047c3558c211";
        String result2 = TestStorage("NTU_Logo.png");
        EqualsCheck("Test 2", expected2, result2);

        // Test valdiate function within LoginActivity for true
        LoginActivity validate = new LoginActivity();
        String expected3 = "true";
        boolean validateBool1 = validate.validate("n0749370@ntu.ac.uk", "jimbob123");
        String result3 = String.valueOf(validateBool1);
        EqualsCheck("Test 3", expected3, result3);

        // Test valdiate function within LoginActivity for false
        String expected4 = "true";
        boolean validateBool2 = validate.validate("n0749371@ntu.ac.uk", "");
        String result4 = String.valueOf(validateBool2);
        EqualsCheck("Test 4", expected4, result4);

        // Test valdiate function within StatusActivity for true
        StatusActivity validate2 = new StatusActivity();
        String expected5 = "true";
        boolean validateBool3 = validate2.validate("123");
        String result5 = String.valueOf(validateBool3);
        EqualsCheck("Test 5", expected5, result5);

        // Test valdiate function within StatusActivity for false
        String expected6 = "false";
        boolean validateBool4 = validate2.validate("");
        String result6 = String.valueOf(validateBool4);
        EqualsCheck("Test 6", expected6, result6);

    }

    public void EqualsCheck(String test, String expected, String result) {
        // Checks if two Strings are equal
        if (expected.equals(result)) {
            Log.d("UnitTest",test + ": Passed");
        } else {
            Log.d("UnitTest",  test + ": Failed");
        }
    }

    public String TestFirestore(String testID) {
        final String[] result = new String[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        DocumentReference docRef = usersRef.document(testID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    result[0] = document.getString("Name");
                }
            }
        });
        return result[0];
    }

    public String TestStorage(String testName) {
        String url = "";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        url = storageRef.child(testName).getDownloadUrl().getResult().toString();
        return url;
    }

}

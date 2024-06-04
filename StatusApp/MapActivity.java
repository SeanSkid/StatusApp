package com.example.mobilestatusapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MapActivity extends AppCompatActivity {

    private Button backButton;
    private String mapURL, map2URL;
    private TextView titleText, extraText;
    private ImageView mapImage1, mapImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Set Things
        backButton = (Button)findViewById(R.id.backButton);
        mapImage1 = (ImageView)findViewById(R.id.imageView);
        mapImage2 = (ImageView)findViewById(R.id.imageView2);
        titleText = (TextView)findViewById(R.id.TitleText);
        extraText = (TextView)findViewById(R.id.ExtraText);
        mapURL = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/map.png?alt=media&token=ddb19642-3011-4e09-abda-0e77b6063447";
        map2URL = "https://firebasestorage.googleapis.com/v0/b/statusapp-d8613.appspot.com/o/map2.png?alt=media&token=793fd6c3-a016-4ed5-818f-4595edaeaf7c";
        Picasso.with(getBaseContext()).load(mapURL).into(mapImage1);
        Picasso.with(getBaseContext()).load(map2URL).into(mapImage2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revert back to MainActivity
                finish();
            }
        });

        mapImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup for Map Image
                mapImage1.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.GONE);
                titleText.setVisibility(View.INVISIBLE);
                extraText.setVisibility(View.INVISIBLE);
                mapImage2.setVisibility(View.VISIBLE);
            }
        });

        mapImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup for Fullscreen Image
                mapImage1.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
                titleText.setVisibility(View.VISIBLE);
                extraText.setVisibility(View.VISIBLE);
                mapImage2.setVisibility(View.GONE);
            }
        });
    }
}

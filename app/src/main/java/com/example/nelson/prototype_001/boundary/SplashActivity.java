package com.example.nelson.prototype_001.boundary;

/**
 * Created by Nelson on 22/10/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LiveabilityUI.class);
        startActivity(intent);
        finish();
    }
}
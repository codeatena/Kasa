package com.general.mediaplayer.kasa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.general.mediaplayer.kasa.R;

public class LoopingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_looping);
    }

    public void onHome(View view)
    {
        Intent mainIntent = new Intent(this ,HomeActivity.class);
        startActivity(mainIntent);
        finish();
    }
}

package com.general.mediaplayer.kasa.activity;

import android.os.Bundle;
import android.view.View;

import com.general.mediaplayer.kasa.R;
import com.general.mediaplayer.kasa.utility.AlertUtility;

public class FanActivity extends UsbSerialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);
    }

    public void onBack(View view)
    {
        finish();
    }

    public void onSchedule(View view)
    {
        AlertUtility.showAlert(this , "Allows you to set schedules, timers and countdowns.");

    }

    public void onAway(View view)
    {
        AlertUtility.showAlert(this , "Automatically turns your devices on and off at different times to make it look like you’re home.");

    }

    public void onTimer(View view)
    {
        AlertUtility.showAlert(this , "Allows you to track real-time energy usage.");

    }

    public void onPower(View view)
    {
        sendCommand("HS105\n");

    }
}

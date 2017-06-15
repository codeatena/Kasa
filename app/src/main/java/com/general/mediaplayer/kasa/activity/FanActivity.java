package com.general.mediaplayer.kasa.activity;

import android.os.Bundle;
import android.view.View;

import com.general.mediaplayer.kasa.R;
import com.general.mediaplayer.kasa.utility.AlertUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageButton;

public class FanActivity extends UsbSerialActivity {

    @BindView(R.id.power_imgbutton)
    GifImageButton powerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);

        ButterKnife.bind(this);

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
        stopFlash(powerBtn ,R.drawable.power_enable_icon);

        sendCommand("HS105\n");

    }
}

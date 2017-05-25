package com.general.mediaplayer.kasa.activity;

import android.os.Bundle;
import android.view.View;

import com.general.mediaplayer.kasa.R;
import com.general.mediaplayer.kasa.utility.AlertUtility;

public class BedroomActivity extends UsbSerialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom);
    }

    public void onBack(View view)
    {
        finish();
    }

    public void onWhite(View view)
    {
        AlertUtility.showAlert(this , "Allows you to fine-tune white temperature from soft candlelight to bright daylight.");
    }

    public void onClock(View view)
    {
        AlertUtility.showAlert(this , "Circadian mode automatically adjusts color and brightness depending on the time of day.");

    }

    public void onSchedule(View view)
    {
        AlertUtility.showAlert(this , "Allows you to set schedules, timers and countdowns.");

    }

    public void onUsage(View view)
    {
        AlertUtility.showAlert(this , "Allows you to track real-time energy usage.");

    }

    public void onPower(View view)
    {
        sendCommand("LB130\n");
    }
}

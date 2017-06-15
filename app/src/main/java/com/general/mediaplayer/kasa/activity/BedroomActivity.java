package com.general.mediaplayer.kasa.activity;

import android.os.Bundle;
import android.view.View;

import com.general.mediaplayer.kasa.R;
import com.general.mediaplayer.kasa.utility.AlertUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageButton;

public class BedroomActivity extends UsbSerialActivity {

    @BindView(R.id.power_imgbutton)
    GifImageButton powerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom);

        ButterKnife.bind(this);

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
        stopFlash(powerBtn ,R.drawable.power_disable_icon);

        sendCommand("LB130\n");
    }
}

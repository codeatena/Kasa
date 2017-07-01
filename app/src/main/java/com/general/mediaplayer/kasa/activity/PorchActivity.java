package com.general.mediaplayer.kasa.activity;

import android.os.Bundle;
import android.view.View;

import com.general.mediaplayer.kasa.R;
import com.general.mediaplayer.kasa.model.Constants;
import com.general.mediaplayer.kasa.model.MessageEvent;
import com.general.mediaplayer.kasa.utility.AlertUtility;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageButton;

public class PorchActivity extends BaseActivity {

    @BindView(R.id.power_imgbutton)
    GifImageButton powerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porch);

        ButterKnife.bind(this);

        if (getIntent().getBooleanExtra(Constants.INIT_STATUS ,false))
        {
            stopFlash(powerBtn ,R.drawable.power_enable_icon);
        }
    }

    public void onBack(View view)
    {
        finish();
    }

    public void onSchedule(View view)
    {
        AlertUtility.showAlert(this , "Allows you to set schedules.");

    }

    public void onAway(View view)
    {
        AlertUtility.showAlert(this , "Away allows you turn your devices on and off at different times to give the appearance that someone is home.");

    }

    public void onTimer(View view)
    {
        AlertUtility.showAlert(this , "Allows you to set timers and countdowns.");

    }

    public void onPower(View view)
    {
        stopFlash(powerBtn ,R.drawable.power_enable_icon);
        EventBus.getDefault().post(new MessageEvent(Constants.PORCH_SERIAL));
    }
}

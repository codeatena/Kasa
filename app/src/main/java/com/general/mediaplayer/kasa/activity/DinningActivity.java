package com.general.mediaplayer.kasa.activity;

import android.os.Bundle;
import android.view.View;

import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.general.mediaplayer.kasa.R;
import com.general.mediaplayer.kasa.model.Constants;
import com.general.mediaplayer.kasa.model.MessageEvent;
import com.general.mediaplayer.kasa.utility.AlertUtility;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageButton;

public class DinningActivity extends BaseActivity {

    @BindView(R.id.power_imgbutton)
    GifImageButton powerBtn;

    @BindView(R.id.rangeSeekbar1)
    CrystalSeekbar crystalSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinning);

        ButterKnife.bind(this);

        crystalSeekbar.setEnabled(false);

        if (getIntent().getBooleanExtra(Constants.INIT_STATUS ,false))
        {
            stopFlash(powerBtn ,R.drawable.power_disable_icon);
        }
    }

    public void onBack(View view)
    {
        finish();
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
        EventBus.getDefault().post(new MessageEvent(Constants.DINNINGROOM_SERIAL));
    }
}

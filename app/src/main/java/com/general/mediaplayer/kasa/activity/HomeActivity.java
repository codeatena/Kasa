package com.general.mediaplayer.kasa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.general.mediaplayer.kasa.R;
import com.general.mediaplayer.kasa.model.Constants;
import com.general.mediaplayer.kasa.model.MessageEvent;
import com.general.mediaplayer.kasa.utility.AlertUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;

public class HomeActivity extends UsbSerialActivity implements View.OnClickListener{

//    @BindView(R.id.bottomBar)
//    BottomBar bottomBar;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.add_button)
    Button addBtn;

    @BindView(R.id.leftbottom_button)
    Button leftbottomBtn;

    @BindView(R.id.rightbottom_button)
    Button rightbottomBtn;

    SectionedRecyclerViewAdapter sectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        setAdaptor();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertUtility.showAlert(HomeActivity.this , "This is where you would go to add a new smart home device.");

            }
        });

        EventBus.getDefault().register(this);

        leftbottomBtn.setOnClickListener(this);
        rightbottomBtn.setOnClickListener(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event)
    {
        /* Do something */
        Log.d("command" ,event.command);
        sendCommand(event.command);
    }

    private long mLastClickTIme = 0;

    @Override
    public void onClick(View v) {

        if (SystemClock.elapsedRealtime() - mLastClickTIme < 500)
        {
            // show CSR app
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.general.mediaplayer.csr");
            if (launchIntent != null) {
                startActivity(launchIntent);//null pointer check in case package name was not found
            }

            return;
        }

        mLastClickTIme = SystemClock.elapsedRealtime();
    }

    private class HomeSection extends StatelessSection {

        List<String> strList;
        List<Integer> imgList;
        String title;

        public HomeAdapterListener onClickListener;

        public HomeSection(String title, List<String> list , List<Integer> imgList ,HomeAdapterListener listender) {
            super(R.layout.section_header_home, R.layout.section_item_home);

            this.title = title;
            this.strList = list;
            this.imgList = imgList;
            onClickListener = listender;
        }

        @Override
        public int getContentItemsTotal() {
            return imgList.size(); // number of items of this section
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            // return a custom instance of ViewHolder for the items of this section
            return new HomeItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final HomeItemViewHolder itemHolder = (HomeItemViewHolder) holder;

            itemHolder.titleText.setText(strList.get(2 * position));
            itemHolder.percentText.setText(strList.get(2 * position + 1));
            itemHolder.leftImageView.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, this.imgList.get(position)));

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onClickListener.itemViewOnClick(itemHolder ,position);
                }
            });

            if (position == 1)
            {
                itemHolder.powerButton.setImageResource(R.drawable.green_flash);
            }

            itemHolder.powerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onClickListener.iconTextViewOnClick(itemHolder ,position);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HomeSectionViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HomeSectionViewHolder headerHolder = (HomeSectionViewHolder) holder;
            headerHolder.sectionText.setText(title);

        }

    }

    public interface HomeAdapterListener {

        void iconTextViewOnClick(RecyclerView.ViewHolder v, int position);
        void itemViewOnClick(RecyclerView.ViewHolder v, int position);

    }

    private class HomeItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView percentText;
        ImageView leftImageView;
        GifImageButton powerButton;
        RelativeLayout rootView;

        public HomeItemViewHolder(View itemView) {
            super(itemView);

            rootView = (RelativeLayout)itemView.findViewById(R.id.rootview);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            percentText = (TextView) itemView.findViewById(R.id.percent_text);
            leftImageView = (ImageView) itemView.findViewById(R.id.left_imageview);
            powerButton = (GifImageButton)itemView.findViewById(R.id.power_imgbutton);
        }
    }

    private class HomeSectionViewHolder extends RecyclerView.ViewHolder {

        private final TextView sectionText;

        public HomeSectionViewHolder(View itemView) {
            super(itemView);

            sectionText = (TextView) itemView.findViewById(R.id.section_text);
        }
    }

    void stopAnimation(RecyclerView.ViewHolder v ,int resId)
    {
        stopFlash(((HomeItemViewHolder)v).powerButton ,resId);
    }

    private void setAdaptor()
    {
        HomeAdapterListener myAdapterListener1 = new HomeAdapterListener() {

            @Override
            public void iconTextViewOnClick(RecyclerView.ViewHolder v, int position) {

                if (position == 0)
                {
                    stopAnimation(v ,R.drawable.power_enable_icon);
                    sendCommand(Constants.BEDROOM_SERIAL);

                }
                else{

                    stopAnimation(v ,R.drawable.power_disable_icon);
                    sendCommand(Constants.DINNINGROOM_SERIAL);
                }

            }

            @Override
            public void itemViewOnClick(RecyclerView.ViewHolder v, int position) {

                if (position == 0)
                {
                    Intent intent = new Intent(HomeActivity.this ,BedroomActivity.class);
                    GifImageButton button = ((HomeItemViewHolder)v).powerButton;
                    if (!(button.getDrawable() instanceof GifDrawable))
                    {
                        intent.putExtra(Constants.INIT_STATUS ,true);
                    }

                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(HomeActivity.this ,DinningActivity.class);
                    GifImageButton button = ((HomeItemViewHolder)v).powerButton;
                    if (!(button.getDrawable() instanceof GifDrawable))
                    {
                        intent.putExtra(Constants.INIT_STATUS ,true);
                    }
                    startActivity(intent);
                }
            }
        };

        HomeAdapterListener myAdapterListener2 = new HomeAdapterListener() {

            @Override
            public void iconTextViewOnClick(RecyclerView.ViewHolder v, int position) {

                stopAnimation(v ,R.drawable.power_enable_icon);
                sendCommand(Constants.FAN_SERIAL);
            }

            @Override
            public void itemViewOnClick(RecyclerView.ViewHolder v, int position) {

                Intent intent = new Intent(HomeActivity.this ,FanActivity.class);
                GifImageButton button = ((HomeItemViewHolder)v).powerButton;
                if (!(button.getDrawable() instanceof GifDrawable))
                {
                    intent.putExtra(Constants.INIT_STATUS ,true);
                }
                startActivity(intent);
            }
        };

        HomeAdapterListener myAdapterListener3 = new HomeAdapterListener() {

            @Override
            public void iconTextViewOnClick(RecyclerView.ViewHolder v, int position) {

                stopAnimation(v ,R.drawable.power_enable_icon);
                sendCommand(Constants.PORCH_SERIAL);

            }

            @Override
            public void itemViewOnClick(RecyclerView.ViewHolder v, int position) {

                Intent intent = new Intent(HomeActivity.this ,PorchActivity.class);
                GifImageButton button = ((HomeItemViewHolder)v).powerButton;
                if (!(button.getDrawable() instanceof GifDrawable))
                {
                    intent.putExtra(Constants.INIT_STATUS ,true);
                }
                startActivity(intent);
            }
        };

        sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new HomeSection("SMART BULBS" , Arrays.asList("Bedroom Lights", "60%" ,"Dinning Room", "100%") ,Arrays.asList(R.drawable.bedroom_icon ,R.drawable.dinning_icon) ,myAdapterListener1));
        sectionAdapter.addSection(new HomeSection("SMART PLUG MINIS" , Arrays.asList("Fan", "") ,Arrays.asList(R.drawable.fan_icon) ,myAdapterListener2));
        sectionAdapter.addSection(new HomeSection("SMART SWITCHES" , Arrays.asList("Porch Lights", "") ,Arrays.asList(R.drawable.porch_icon) ,myAdapterListener3));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sectionAdapter);
    }
}

package com.general.mediaplayer.kasa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.general.mediaplayer.kasa.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HomeActivity extends UsbSerialActivity {

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    SectionedRecyclerViewAdapter sectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        bottomBar.getTabWithId(R.id.tab_devices).findViewById(R.id.bb_bottom_bar_icon).setScaleX(1.5f);
        bottomBar.getTabWithId(R.id.tab_scenes).findViewById(R.id.bb_bottom_bar_icon).setScaleX(1.5f);
        bottomBar.getTabWithId(R.id.tab_actions).findViewById(R.id.bb_bottom_bar_icon).setScaleX(1.5f);
        bottomBar.getTabWithId(R.id.tab_devices).findViewById(R.id.bb_bottom_bar_icon).setScaleY(1.5f);
        bottomBar.getTabWithId(R.id.tab_scenes).findViewById(R.id.bb_bottom_bar_icon).setScaleY(1.5f);
        bottomBar.getTabWithId(R.id.tab_actions).findViewById(R.id.bb_bottom_bar_icon).setScaleY(1.5f);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

            }
        });

        setAdaptor();

    }

    private class HomeSection extends StatelessSection {

        List<String> strList;
        List<Integer> imgList;
        String title;

        public HomeAdapterListener onClickListener;

        public HomeSection() {
            // call constructor with layout resources for this Section header and items
            super(R.layout.section_header_home, R.layout.section_item_home);
        }

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
        ToggleImageButton powerButton;
        RelativeLayout rootView;

        public HomeItemViewHolder(View itemView) {
            super(itemView);

            rootView = (RelativeLayout)itemView.findViewById(R.id.rootview);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            percentText = (TextView) itemView.findViewById(R.id.percent_text);
            leftImageView = (ImageView) itemView.findViewById(R.id.left_imageview);
            powerButton = (ToggleImageButton)itemView.findViewById(R.id.power_imgbutton);
        }
    }

    private class HomeSectionViewHolder extends RecyclerView.ViewHolder {

        private final TextView sectionText;

        public HomeSectionViewHolder(View itemView) {
            super(itemView);

            sectionText = (TextView) itemView.findViewById(R.id.section_text);
        }
    }

    private void setAdaptor()
    {
        HomeAdapterListener myAdapterListener1 = new HomeAdapterListener() {

            @Override
            public void iconTextViewOnClick(RecyclerView.ViewHolder v, int position) {

                if (position == 0)
                    sendCommand("LB130\n");
                else
                    sendCommand("LB110\n");

            }

            @Override
            public void itemViewOnClick(RecyclerView.ViewHolder v, int position) {

                if (position == 0)
                {
                    Intent intent = new Intent(HomeActivity.this ,BedroomActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(HomeActivity.this ,DinningActivity.class);
                    startActivity(intent);
                }
            }
        };

        HomeAdapterListener myAdapterListener2 = new HomeAdapterListener() {

            @Override
            public void iconTextViewOnClick(RecyclerView.ViewHolder v, int position) {

                sendCommand("HS105\n");
            }

            @Override
            public void itemViewOnClick(RecyclerView.ViewHolder v, int position) {

                Intent intent = new Intent(HomeActivity.this ,FanActivity.class);
                startActivity(intent);
            }
        };

        HomeAdapterListener myAdapterListener3 = new HomeAdapterListener() {

            @Override
            public void iconTextViewOnClick(RecyclerView.ViewHolder v, int position) {

                sendCommand("HS200\n");

            }

            @Override
            public void itemViewOnClick(RecyclerView.ViewHolder v, int position) {

                Intent intent = new Intent(HomeActivity.this ,PorchActivity.class);
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

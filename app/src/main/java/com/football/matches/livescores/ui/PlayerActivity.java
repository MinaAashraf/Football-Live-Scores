package com.football.matches.livescores.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.PlayerData;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity extends AppCompatActivity {
    PlayerData playerData = null;
    @BindView(R.id.player_image)
    CircleImageView playerImage;
    @BindView(R.id.player_name)
    TextView playerName;
    @BindView(R.id.player_position)
    TextView playerPosition;
    @BindView(R.id.tabs_layout)
    TabLayout tabsLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.textview_title)
    TextView textviewTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title_linearLaout)
    LinearLayout titleLinearLaout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.star_icon)
    ImageView starIcon;

    public void handleActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        CollapsingSettings collapsingSettings = new CollapsingSettings(textviewTitle, titleLinearLaout);
        collapsingSettings.startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                collapsingSettings.onOffsetChangeHander(appBarLayout, i);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        handleActionBar();
        Gson gson = new Gson();
        String json = getIntent().getStringExtra("player");

        CheckFavourite checkFavourite = new CheckFavourite(this, json, getResources().getString(R.string.followingPlayerObjs), starIcon);
        checkFavourite.check();
        starIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFavourite.onClickStar(playerName.getText().toString());
            }
        });

        playerData = gson.fromJson(json, PlayerData.class);
        Picasso.get().load(playerData.getStrThumb()).placeholder(R.drawable.player_tshirt).into(playerImage);
        playerName.setText(playerData.getStrPlayer());
        textviewTitle.setText(playerData.getStrPlayer());
        playerPosition.setText(playerData.getStrPosition());

        tabsLayout.setTabTextColors(Color.parseColor("#FF9E9E9E"), Color.parseColor("#000000"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new TabsAdapter(fragmentManager);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(2);
        tabsLayout.setupWithViewPager(viewpager);
        if (getIntent().hasExtra("seeAllNews"))
        {
            tabsLayout.getTabAt(1).select();
        }
    }

    public class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new PlayerInfoFragment();
                    break;
                case 1:
                    fragment = new PlayerNewsFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Info";
                case 1:
                    return "News";
                default:
                    return null;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
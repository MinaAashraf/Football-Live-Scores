package com.football.matches.livescores.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.Team;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchActivity extends AppCompatActivity {

    @BindView(R.id.leagueName)
    TextView leagueName;
    @BindView(R.id.fr_teamImage)
    ImageView frTeamImage;
    @BindView(R.id.fr_teamName)
    TextView frTeamName;
    @BindView(R.id.goals_value)
    TextView goalsValue;
    @BindView(R.id.second_teamImage)
    ImageView secondTeamImage;
    @BindView(R.id.second_teamName)
    TextView secondTeamName;
    @BindView(R.id.matchDate)
    TextView matchDate;
    @BindView(R.id.tabs_layout)
    TabLayout tabsLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    FixtureResposeObj fixture;
    @BindView(R.id.textview_title)
    TextView textviewTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    public void handleActionBar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        CollapsingSettings collapsingSettings = new CollapsingSettings(textviewTitle, constraintLayout);
        collapsingSettings.startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                collapsingSettings.onOffsetChangeHander(appBarLayout,i);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        ButterKnife.bind(this);
        handleActionBar();

        String json = getIntent().getStringExtra("fixture");
        fixture = new Gson().fromJson(json, FixtureResposeObj.class);
        setFixtureVales();
        tabsLayout.setTabTextColors(Color.parseColor("#F4F4F4"), Color.parseColor("#ffffff"));
        FragmentManager fragmentManager = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new TabsAdapter(fragmentManager);
        viewpager.setAdapter(pagerAdapter);
        tabsLayout.setupWithViewPager(viewpager);
    }

    Date date = null;

    public String[] dateFormat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat clockFormat = new SimpleDateFormat("hh:mm a");
        try {
            date = inputFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        String formattedClock = clockFormat.format(date);
        return new String[]{formattedDate, formattedClock};
    }

    private void setFixtureVales() {
        Team homeTeam = fixture.getTeams().getHome();
        Team awayTeam = fixture.getTeams().getAway();
        Picasso.get().load(homeTeam.getLogo()).into(frTeamImage);
        Picasso.get().load(awayTeam.getLogo()).into(secondTeamImage);
        frTeamName.setText(homeTeam.getName());
        secondTeamName.setText(awayTeam.getName());
        leagueName.setText(fixture.getLeague().getName());
        textviewTitle.setText(homeTeam.getName() + " - "+awayTeam.getName());
        String status = fixture.getFixture().getStatus().getShortt();
        String dateArr[] = dateFormat(fixture.getFixture().getDate());

        switch (status) {
            case "FT":
                goalsValue.setText(fixture.getGoals().getHome() + " - " + fixture.getGoals().getAway());
                matchDate.setText(dateArr[0]);
                break;
            case "NS":
                goalsValue.setTextSize(matchDate.getTextSize());
                goalsValue.setText(dateArr[0]);
                matchDate.setText(dateArr[1]);
                break;
            default:
                goalsValue.setText(status);
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
                    fragment = new MatchOverviewFragment();
                    break;
                case 1:
                    fragment = new LiveEventsFragment();
                    break;
                case 2:
                    fragment = new FixtureLineupsFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Live Events";
                case 2:
                    return "Lineups";
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
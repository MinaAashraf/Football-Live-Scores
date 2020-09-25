package com.football.matches.livescores.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.League;
import com.football.matches.livescores.pojo.LeagueResponceObject;
import com.football.matches.livescores.pojo.Season;
import com.football.matches.livescores.pojo.StandingObj;
import com.football.matches.livescores.pojo.Team;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeagueActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.league_logo)
    ImageView leagueLogo;
    @BindView(R.id.leagueName)
    TextView leagueName;
    @BindView(R.id.league_season)
    TextView leagueSeason;
    @BindView(R.id.tabs_layout)
    TabLayout tabsLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    int leagueId = -1;
    PagerAdapter pagerAdapter;
    MyViewModel myViewModel;
    List<StandingObj> standingObjs = new ArrayList<>();
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.title_frame)
    FrameLayout titleFrame;
    @BindView(R.id.textview_title)
    TextView textviewTitle;
    @BindView(R.id.title_linearLaout)
    LinearLayout titleLinearLaout;
    @BindView(R.id.star_icon)
    ImageView starIcon;

    public void handleActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        tabsLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
        setContentView(R.layout.activity_league);
        ButterKnife.bind(this);
        handleActionBar();

        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        tabsLayout.setTabTextColors(Color.parseColor("#FF9E9E9E"), Color.parseColor("#000000"));
        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerAdapter = new TabsAdapter(fragmentManager);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(4);
        tabsLayout.setupWithViewPager(viewpager);

        String json = getIntent().getStringExtra("league");
        CheckFavourite checkFavourite = new CheckFavourite(this, json, getResources().getString(R.string.followingLeagueObjs), starIcon);
        checkFavourite.check();
        starIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFavourite.onClickStar(leagueName.getText().toString());
            }
        });
        LeagueResponceObject leagueResponceObject = objFromJson(json);
        setLeagueContent(leagueResponceObject);

        if (getIntent().hasExtra("seeAll")) {
            getStandingsListIfExist();
            tabsLayout.getTabAt(2).select();
        }

        if (getIntent().hasExtra("seeAllNews"))
        {
            tabsLayout.getTabAt(4).select();
        }

    }


    public LeagueResponceObject objFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, LeagueResponceObject.class);
    }
    public void setLeagueContent(LeagueResponceObject leagueResponceObject) {
        League league = leagueResponceObject.getLeague();
        leagueId = league.getId();
        Season season = leagueResponceObject.getSeason();
        Picasso.get().load(league.getLogo()).into(leagueLogo);
        leagueName.setText(league.getName());
        textviewTitle.setText(league.getName());
        leagueSeason.setText(season.getStart() + " / " + season.getEnd());
        if (!getIntent().hasExtra("seeAll"))
            getTeams(leagueId);
    }

    public void getTeams(int leagueId) {
        myViewModel.getLeagueTeams(leagueId).observe(LeagueActivity.this, new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teamList) {
                TeamsFragment.teamAdapter.setData(teamList);
            }
        });
    }

    public void getStandingsListIfExist() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<StandingObj>>() {
        }.getType();
        String json = getIntent().getStringExtra("seeAll");
        standingObjs.addAll((Collection<? extends StandingObj>) gson.fromJson(json, type));
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
                    fragment = new LeagueMatches();
                    break;
                case 1:
                    fragment = new TeamsFragment();
                    break;
                case 2:
                    fragment = new StandingsFragment();
                    break;
                case 3:
                    fragment = new StatisticsFragment();
                    break;
                case 4:
                    fragment = new LeagueNewsFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Games";
                case 1:
                    return "Teams";
                case 2:
                    return "Standings";
                case 3:
                    return "Statistics";
                case 4:
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
package com.football.matches.livescores.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.LeagueResponceObject;
import com.football.matches.livescores.pojo.Team;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamActivity extends AppCompatActivity {

    @BindView(R.id.team_image)
    ImageView teamImage;
    @BindView(R.id.team_name)
    TextView teamName;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    static Team team;
    Spinner leaguesSpinner;
    @BindView(R.id.tabs_layout)
    TabLayout tabsLayout;

    PagerAdapter pagerAdapter;
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
        setContentView(R.layout.activity_team);
        ButterKnife.bind(this);
        handleActionBar();
        tabsLayout.setTabTextColors(Color.parseColor("#FF9E9E9E"), Color.parseColor("#000000"));

        leaguesSpinner = findViewById(R.id.leagues_spinner);
        String teamJson = getIntent().getStringExtra("team");
        CheckFavourite checkFavourite = new CheckFavourite(this, teamJson, getResources().getString(R.string.followingTeamsObjs), starIcon);
        checkFavourite.check();
        starIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFavourite.onClickStar(teamName.getText().toString());
            }
        });

        team = objFromJson(teamJson);
        //Set Name And Image For Team:
        if (team.getLogo() != null)
            Picasso.get().load(team.getLogo()).into(teamImage);
        teamName.setText(team.getName());
        textviewTitle.setText(team.getName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerAdapter = new TabsAdapter(fragmentManager);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(4);
        tabsLayout.setupWithViewPager(viewpager);
        getTeamLeagues();
        if (getIntent().hasExtra("seeAllNews")) {
            tabsLayout.getTabAt(2).select();
        }
    }


    Team objFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Team.class);
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
                    fragment = new TeamMatchesFragment();
                    break;
                case 1:
                    fragment = new TeamStandingsFragment();
                    break;
                case 2:
                    fragment = new TeamNewsFragment();
                    break;
                case 3:
                    fragment = new TransfersFragment();
                    break;
                case 4:
                    fragment = new TeamSquadFragment();
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
                    return "Matches";
                case 1:
                    return "Standings";
                case 2:
                    return "News";
                case 3:
                    return "Transfers";
                case 4:
                    return "Squad";
                default:
                    return null;
            }
        }
    }

    List<LeagueResponceObject> leaguesObjects = new ArrayList<>();
    List<String> leaguesNames = new ArrayList<>();

    public void getTeamLeagues() {
        MyViewModel myViewModel = ViewModelProviders.of(TeamActivity.this).get(MyViewModel.class);
        myViewModel.getTeamLeagues(team.getId()).observe(TeamActivity.this, new Observer<List<LeagueResponceObject>>() {
            @Override
            public void onChanged(List<LeagueResponceObject> leagueResponceObjects) {
                leaguesObjects.addAll(leagueResponceObjects);
                for (LeagueResponceObject object : leagueResponceObjects) {
                    leaguesNames.add(object.getLeague().getName());
                }
             //   spinnerAdapter.notifyDataSetChanged();
                connectWithFragment();
            }
        });
    }

    public void connectWithFragment() {
        Fragment fragment = (Fragment) pagerAdapter.instantiateItem(viewpager, 0);
        Fragment fragment2 = (Fragment) pagerAdapter.instantiateItem(viewpager,1);

        if (fragment != null && fragment instanceof TeamMatchesFragment) {
            final TeamMatchesFragment teamMatchesFragment = (TeamMatchesFragment) fragment;
            teamMatchesFragment.spinnerAdapter.notifyDataSetChanged();
            teamMatchesFragment.myViewModel.getTeamFixures(leaguesObjects.get(0).getLeague().getId(), team.getId()).observe(this, new Observer<List<FixtureResposeObj>>() {
                @Override
                public void onChanged(List<FixtureResposeObj> fixtureResposeObjs) {
                    teamMatchesFragment.fixtures.addAll(fixtureResposeObjs);
                    teamMatchesFragment.matchesAdapter.notifyDataSetChanged();
                }
            });
        }

        if (fragment2 != null && fragment2 instanceof TeamStandingsFragment)
        {
            TeamStandingsFragment teamStandingsFragment = (TeamStandingsFragment) fragment2;
            teamStandingsFragment.spinnerAdapter.notifyDataSetChanged();
         //    teamStandingsFragment.observeData(leaguesObjects.get(0).getLeague().getId());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
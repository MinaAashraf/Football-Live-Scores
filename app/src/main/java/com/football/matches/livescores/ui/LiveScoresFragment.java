package com.football.matches.livescores.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.Goals;
import com.football.matches.livescores.pojo.League;
import com.football.matches.livescores.pojo.Team;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveScoresFragment extends Fragment {
    @BindView(R.id.nofollowing_layout)
    LinearLayout nofollowingLayout;
    HashMap<String, List<FixtureResposeObj>> datedKeyMap = new HashMap<>();
    HashMap<Integer, List<FixtureResposeObj>> leaguesFixturesMap = new HashMap<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    MyViewModel myViewModel;
    Set<String> teamsSet, compSet;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.livescores_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        MainActivity mainActivity = ((MainActivity) getActivity());
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.setActionBarTitleStyle("Games");
      /*  MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });*/
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("liveScores", Context.MODE_PRIVATE);
        teamsSet = sharedPreferences.getStringSet(getResources().getString(R.string.followingTeamsObjs), new HashSet<String>());
        compSet = sharedPreferences.getStringSet(getResources().getString(R.string.followingLeagueObjs), new HashSet<String>());


        if (teamsSet.isEmpty() && compSet.isEmpty()) {
            linearlayout.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            nofollowingLayout.setVisibility(View.VISIBLE);
        }

        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        if (!compSet.isEmpty()) {
            for (League league : getLeagueObjsFromJsons(compSet))
                leagueIds.add(league.getId());
            observeAllLeagueMatches(leagueIds);
        }

        if (!teamsSet.isEmpty()) {
            for (Team team : getTeamObjsFromJsons(teamsSet))
                teamIds.add(team.getId());
            observeAllTeamMatches(teamIds);
        }

        return view;
    }

    List<Integer> teamIds = new ArrayList<>();
    List<Integer> leagueIds = new ArrayList<>();

    public List<League> getLeagueObjsFromJsons(Set<String> set) {
        List<League> leagues = new ArrayList<>();
        Gson gson = new Gson();
        for (String json : set)
            leagues.add(gson.fromJson(json, League.class));
        return leagues;
    }

    public List<Team> getTeamObjsFromJsons(Set<String> set) {
        List<Team> teams = new ArrayList<>();
        Gson gson = new Gson();
        for (String json : set)
            teams.add(gson.fromJson(json, Team.class));
        return teams;
    }

    int count = 0;

    public void extractMapItems(List<FixtureResposeObj> fixtureResposeObjs) {
        /* if (count++!=0)
             return;*/
        for (FixtureResposeObj fixtureObj : fixtureResposeObjs) {
            String date = fixtureObj.getFixture().getDate();
            if (datedKeyMap.containsKey(date)) {
                List<FixtureResposeObj> fixtures = datedKeyMap.get(date);
                boolean exist = false;
                for (int i = 0; i < fixtures.size(); i++) {
                    if (fixtures.get(i).getFixture().getId() == fixtureObj.getFixture().getId()) {
                        exist = true;
                        break;
                    }
                }
                if (exist)
                    continue;
            }
            if (datedKeyMap.containsKey(date)) {
                List<FixtureResposeObj> fixtureResposeObjList = datedKeyMap.get(date);
                fixtureResposeObjList.add(fixtureObj);
                datedKeyMap.put(date, fixtureResposeObjList);

            } else {
                List<FixtureResposeObj> newList = new ArrayList<>();
                newList.add(fixtureObj);
                datedKeyMap.put(date, newList);
            }


        }
        createViews();

    }

    private void createViews() {
        List<String> keyList = new ArrayList<>(datedKeyMap.keySet());
        sortByDate(keyList);
        for (String key : keyList) {
            LinearLayout myLayout = new LinearLayout(getActivity());
            myLayout.removeAllViews();
            myLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            );

            params.setMargins(0, (int) (10 * getContext().getResources().getDisplayMetrics().density), 0, 0);
            myLayout.setLayoutParams(params);
            View dateView = getLayoutInflater().inflate(R.layout.dateview, null);
            TextView date = dateView.findViewById(R.id.date);
            date.setText(TimeFormat.formatToYesterdayOrToday(TimeFormat.dateFormat(key)[0]));
            myLayout.addView(dateView);
            leaguesFixturesMap.clear();
            extractleagueMapItems(datedKeyMap.get(key));
            List<List<FixtureResposeObj>> leaguesMatcheslistofList = new ArrayList<>(leaguesFixturesMap.values());
            for (List<FixtureResposeObj> fixtureList : leaguesMatcheslistofList) {
                View view = getLayoutInflater().inflate(R.layout.league_item, null);
                ImageView leaegueImage = view.findViewById(R.id.league_logo);
                TextView leagueName = view.findViewById(R.id.league_name);
                League league = fixtureList.get(0).getLeague();
                Picasso.get().load(league.getLogo()).placeholder(R.drawable.addcomp_icon).into(leaegueImage);
                leagueName.setText(league.getName());
                myLayout.addView(view);
                for (FixtureResposeObj fixtureResposeObj : fixtureList) {
                    View view1 = setFixtureView(fixtureResposeObj);
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Gson gson = new Gson();
                            String myFixture = gson.toJson(fixtureResposeObj);
                            getActivity().startActivity(new Intent(getActivity(), MatchActivity.class).putExtra("fixture", myFixture));
                        }
                    });
                    myLayout.addView(view1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        myLayout.setElevation(2);
                    }
                }
            }
            if (progressbar.getVisibility() == View.VISIBLE)
                progressbar.setVisibility(View.GONE);

            linearlayout.addView(myLayout);

        }

    }

    public View setFixtureView(FixtureResposeObj fixture) {
        View view1 = getLayoutInflater().inflate(R.layout.match_item2, null);
        TextView frTeamName, secondTeamName, golasValue;
        ImageView frTeamImage, secondTeamImage;
        golasValue = view1.findViewById(R.id.goals_value);
        frTeamName = view1.findViewById(R.id.fr_teamName);
        frTeamImage = view1.findViewById(R.id.fr_teamImage);
        secondTeamName = view1.findViewById(R.id.second_teamName);
        secondTeamImage = view1.findViewById(R.id.second_teamImage);

        String dateArr[] = TimeFormat.dateFormat(fixture.getFixture().getDate());
        Picasso.get().load(fixture.getTeams().getHome().getLogo()).into(frTeamImage);
        Picasso.get().load(fixture.getTeams().getAway().getLogo()).into(secondTeamImage);
        frTeamName.setText(fixture.getTeams().getHome().getName());
        secondTeamName.setText(fixture.getTeams().getAway().getName());
        Goals goals = fixture.getGoals();
        String status = fixture.getFixture().getStatus().getShortt();
        switch (status) {
            case "FT":
            case "AET":
                golasValue.setText(goals.getHome() + " - " + goals.getAway());
                break;
            case "NS":
                golasValue.setText(dateArr[1]);
                break;
            default:
                golasValue.setText(status);

        }
        return view1;
    }


    public void extractleagueMapItems(List<FixtureResposeObj> fixtureResposeObjs) {
        for (FixtureResposeObj fixtureObj : fixtureResposeObjs) {
            int leagueId = fixtureObj.getLeague().getId();
            if (leaguesFixturesMap.containsKey(leagueId)) {
                List<FixtureResposeObj> fixtures = leaguesFixturesMap.get(leagueId);
                boolean exist = false;
                for (int i = 0; i < fixtures.size(); i++) {
                    if (fixtures.get(i).getLeague().getId() == fixtureObj.getLeague().getId()) {
                        exist = true;
                        break;
                    }
                }
                if (exist)
                    continue;
            }
            if (leaguesFixturesMap.containsKey(leagueId)) {
                List<FixtureResposeObj> list = new ArrayList<>(leaguesFixturesMap.get(leagueId));
                list.add(fixtureObj);
                leaguesFixturesMap.put(leagueId, list);
            } else {
                List<FixtureResposeObj> newList = new ArrayList<>();
                newList.add(fixtureObj);
                leaguesFixturesMap.put(leagueId, newList);
            }
        }

    }


    public void observeAllTeamMatches(List<Integer> teamIds) {
        for (int id : teamIds) {
            Map<String, String> map = new HashMap<>();
            map.put("team", String.valueOf(id));
            observeGeneralFixtures(map);
        }
    }

    public void observeAllLeagueMatches(List<Integer> leagueIds) {
        for (int id : leagueIds) {
            Map<String, String> map = new HashMap<>();
            map.put("league", String.valueOf(id));
            observeGeneralFixtures(map);
        }
    }

    public void observeGeneralFixtures(Map<String, String> map) {
        myViewModel.getGeneralFixtures(map).observe(getActivity(), new Observer<List<List<FixtureResposeObj>>>() {
            @Override
            public void onChanged(List<List<FixtureResposeObj>> lists) {
                if (lists.size() == teamsSet.size() + compSet.size()) {
                    for (List<FixtureResposeObj> fixtireObjList : lists) {
                        fixtureResposeObjects.addAll(fixtireObjList);
                    }
                    extractMapItems(fixtureResposeObjects);
                }
            }
        });
    }

    List<FixtureResposeObj> fixtureResposeObjects = new ArrayList<>();


    public void sortByDate(List<String> datestring) {
        Collections.sort(datestring, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }

    public void loadNativeAd(LinearLayout linearLayout) {
        AdLoader adLoader = new AdLoader.Builder(getActivity(), getResources().getString(R.string.ad_unit_id)).
                forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        UnifiedNativeAdView templateView = new UnifiedNativeAdView(getActivity());
                        templateView.setNativeAd(unifiedNativeAd);
                        linearLayout.addView(templateView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }
}

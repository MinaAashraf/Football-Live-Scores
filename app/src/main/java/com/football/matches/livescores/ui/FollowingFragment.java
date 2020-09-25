package com.football.matches.livescores.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.LeagueAdapter;
import com.football.matches.livescores.Adapters.PlayerAdapter;
import com.football.matches.livescores.Adapters.TeamAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.LeagueResponceObject;
import com.football.matches.livescores.pojo.PlayerData;
import com.football.matches.livescores.pojo.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.football.matches.livescores.ui.SharedPreferenceFactory.context;

public class FollowingFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.add_fab)
    FloatingActionButton addFab;
    @BindView(R.id.addteam_btn)
    Button addteamBtn;
    @BindView(R.id.addteam_lay)
    LinearLayout addteamLay;
    @BindView(R.id.addcomp_btn)
    Button addcompBtn;
    @BindView(R.id.addcomp_lay)
    LinearLayout addcompLay;
    @BindView(R.id.addplayer_btn)
    Button addplayerBtn;
    @BindView(R.id.addplayer_lay)
    LinearLayout addplayerLay;
    @BindView(R.id.star)
    ImageView star;
    @BindView(R.id.nofollowing_txt)
    TextView nofollowingTxt;
    @BindView(R.id.teamstitle)
    TextView teamstitle;
    @BindView(R.id.teams_recycler)
    RecyclerView teamsRecycler;
    @BindView(R.id.competitionstitle)
    TextView competitionstitle;
    @BindView(R.id.competitions_recycler)
    RecyclerView competitionsRecycler;
    @BindView(R.id.playerstitle)
    TextView playerstitle;
    @BindView(R.id.players_recycler)
    RecyclerView playersRecycler;
    Set<String> teamsSet;
    Set<String> compSet;
    Set<String> playerSet;
    List<Team> folowingteamsList = new ArrayList<>();
    List<LeagueResponceObject> folowingcompList = new ArrayList<>();
    List<PlayerData> folowingPlayersList = new ArrayList<>();
    LeagueAdapter leagueAdapter;
    PlayerAdapter playerAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.teams)
    ConstraintLayout teams;
    @BindView(R.id.comps)
    ConstraintLayout comps;
    @BindView(R.id.players)
    ConstraintLayout players;

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("liveScores", Context.MODE_PRIVATE);
        teamsSet = sharedPreferences.getStringSet(getResources().getString(R.string.followingTeamsObjs), new HashSet<String>());
        compSet = sharedPreferences.getStringSet(getResources().getString(R.string.followingLeagueObjs), new HashSet<String>());
        playerSet = sharedPreferences.getStringSet(getResources().getString(R.string.followingPlayerObjs), new HashSet<String>());

        if (teamsSet.isEmpty() && compSet.isEmpty() && playerSet.isEmpty()) {
            star.setVisibility(View.VISIBLE);
            nofollowingTxt.setVisibility(View.VISIBLE);
            teams.setVisibility(View.GONE);
            comps.setVisibility(View.GONE);
            players.setVisibility(View.GONE);
        } else {
            star.setVisibility(View.GONE);
            nofollowingTxt.setVisibility(View.GONE);
            if (!teamsSet.isEmpty()) {
                teams.setVisibility(View.VISIBLE);
                getObjListFromJsonSet("team");
                TeamAdapter teamAdapter = new TeamAdapter(getActivity(), R.layout.followingteam_item);
                teamAdapter.setData(folowingteamsList);
                teamsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                teamsRecycler.setAdapter(teamAdapter);
            } else {
                teams.setVisibility(View.GONE);
            }

            if (!compSet.isEmpty()) {
                comps.setVisibility(View.VISIBLE);
                leagueAdapter = new LeagueAdapter(getActivity(), R.layout.followingteam_item);
                getObjListFromJsonSet("comp");
                competitionsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                competitionsRecycler.setAdapter(leagueAdapter);
            } else {
                comps.setVisibility(View.GONE);
            }
            if (!playerSet.isEmpty()) {
                players.setVisibility(View.VISIBLE);
                playerAdapter = new PlayerAdapter(getActivity(), R.layout.followingplayer_item);
                getObjListFromJsonSet("player");
                playersRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                playersRecycler.setAdapter(playerAdapter);
            } else {
                players.setVisibility(View.GONE);
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.followingfragment, container, false);
        ButterKnife.bind(this, view);

        MainActivity mainActivity = ((MainActivity) getActivity());
        new CheckConnectivity(mainActivity, view.findViewById(R.id.parent));
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.setActionBarTitleStyle("Following");
        addFab.setOnClickListener(this);
        addteamLay.setOnClickListener(this);
        addteamBtn.setOnClickListener(this);
        addcompLay.setOnClickListener(this);
        addcompBtn.setOnClickListener(this);
        addplayerLay.setOnClickListener(this);
        addplayerBtn.setOnClickListener(this);
        return view;
    }

    public void getObjListFromJsonSet(String objType) {
        Gson gson = new Gson();
        switch (objType) {
            case "team":
                folowingteamsList.clear();
                for (String j : teamsSet)
                    folowingteamsList.add(gson.fromJson(j, Team.class));
                break;
            case "comp":
                folowingcompList.clear();
                for (String j : compSet)
                    folowingcompList.add(gson.fromJson(j, LeagueResponceObject.class));
                leagueAdapter.setData(folowingcompList);
                break;
            case "player":
                folowingPlayersList.clear();
                for (String j : playerSet)
                    folowingPlayersList.add(gson.fromJson(j, PlayerData.class));
                playerAdapter.setData(folowingPlayersList);
                break;
        }
    }


    public void animate(int visibility) {
        int dur = 150;
        if (visibility == View.GONE) {
            addFab.animate().rotation(-45).setDuration(dur - 100);
            addteamLay.setVisibility(View.VISIBLE);
            addcompLay.setVisibility(View.VISIBLE);
            addplayerLay.setVisibility(View.VISIBLE);
            addteamLay.animate().translationY(-240 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(dur);

            addcompLay.animate().translationY(-160 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(dur);

            addplayerLay.animate().translationY(-80 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(dur);
        } else {
            addFab.animate().rotation(0).setDuration(dur - 100);
            addteamLay.animate().translationY(0).setDuration(dur);
            addcompLay.animate().translationY(0).setDuration(dur);
            addplayerLay.animate().translationY(0).setDuration(dur);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    addteamLay.setVisibility(View.GONE);
                    addcompLay.setVisibility(View.GONE);
                    addplayerLay.setVisibility(View.GONE);
                }
            }, 101);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fab:
                animate(addteamLay.getVisibility());
                break;
            case R.id.addteam_lay:
            case R.id.addteam_btn:
                startActivity(new Intent(getActivity(), ChooseTeam.class).putExtra("from", "team"));
                animate(View.VISIBLE);
                break;
            case R.id.addcomp_lay:
            case R.id.addcomp_btn:
                startActivity(new Intent(getActivity(), ChooseTeam.class).putExtra("from", "league"));
                animate(View.VISIBLE);
                break;
            case R.id.addplayer_lay:
            case R.id.addplayer_btn:
                startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("from", "player"));
                animate(View.VISIBLE);
                break;
        }
    }

}


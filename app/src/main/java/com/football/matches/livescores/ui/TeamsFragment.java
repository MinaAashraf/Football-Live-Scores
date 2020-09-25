package com.football.matches.livescores.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.TeamAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.Team;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamsFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
   public static TeamAdapter teamAdapter;
   MyViewModel myViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teams_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(),view.findViewById(R.id.parent));
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        teamAdapter = new TeamAdapter(getActivity(), R.layout.team_item2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(teamAdapter);
        int leagueId = ((LeagueActivity) getActivity()).leagueId;
        if (leagueId != -1 && teamAdapter.dataList.isEmpty())
            getTeams(leagueId, getActivity());

        return view;
    }

    public void getTeams(int leagueId, Context context) {

        myViewModel.getLeagueTeams(leagueId).observe((LifecycleOwner) context, new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teamList) {
                teamAdapter.setData(teamList);
            }
        });
    }
}

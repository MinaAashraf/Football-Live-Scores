package com.football.matches.livescores.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.LineupsAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.LineupResponseObj;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FixtureLineupsFragment extends Fragment {
    @BindView(R.id.home_team)
    Button homeTeam;

    @BindView(R.id.formation)
    TextView formation;
    @BindView(R.id.lineups_recycler)
    RecyclerView lineupsRecycler;
    @BindView(R.id.substitutesRecycler)
    RecyclerView substitutesRecycler;
    @BindView(R.id.coach_name)
    TextView coachName;
    List<LineupResponseObj> lineupObjs = new ArrayList<>();
    @BindView(R.id.away_team)
    Button awayTeam;

    LineupsAdapter lineupsAdapter, substitutesAdapter;
    @BindView(R.id.too_early_layout)
    LinearLayout tooEarlyLayout;
    @BindView(R.id.lineups_layout)
    ConstraintLayout lineupsLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fixture_lineups_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        FixtureResposeObj fixture = ((MatchActivity) getActivity()).fixture;
        lineupsAdapter = new LineupsAdapter();
        substitutesAdapter = new LineupsAdapter();
        lineupsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        lineupsRecycler.setAdapter(lineupsAdapter);
        substitutesRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        substitutesRecycler.setAdapter(substitutesAdapter);


        homeTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataRecordingToTeam(0);
                homeTeam.setBackgroundColor(Color.parseColor("#000000"));
                homeTeam.setTextColor(Color.parseColor("#ffffff"));
                awayTeam.setBackgroundColor(Color.parseColor("#ffffff"));
                awayTeam.setTextColor(Color.parseColor("#000000"));
            }
        });
        awayTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataRecordingToTeam(1);
                awayTeam.setBackgroundColor(Color.parseColor("#000000"));
                awayTeam.setTextColor(Color.parseColor("#ffffff"));
                homeTeam.setBackgroundColor(Color.parseColor("#ffffff"));
                homeTeam.setTextColor(Color.parseColor("#000000"));
            }
        });


        MyViewModel myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getLineUps(fixture.getFixture().getId()).observe(getActivity(), new Observer<List<LineupResponseObj>>() {
            @Override
            public void onChanged(List<LineupResponseObj> lineupResponseObjs) {
                if (lineupResponseObjs.size() != 0) {
                    lineupObjs.addAll(lineupResponseObjs);
                    setDataRecordingToTeam(0);
                    homeTeam.setText(lineupResponseObjs.get(0).getTeam().getName());
                    awayTeam.setText(lineupResponseObjs.get(1).getTeam().getName());
                    tooEarlyLayout.setVisibility(View.GONE);
                    lineupsLayout.setVisibility(View.VISIBLE);
                } else {
                    lineupsLayout.setVisibility(View.GONE);
                    tooEarlyLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    private void setDataRecordingToTeam(int position) {
        LineupResponseObj lineupResponseObj = lineupObjs.get(position);
        formation.setText("Formation : " + lineupResponseObj.getFormation());
        lineupsAdapter.setData(lineupResponseObj.getStartXI());
        substitutesAdapter.setData(lineupResponseObj.getSubstitutes());
        coachName.setText(lineupResponseObj.getCoach().getName());


    }
}

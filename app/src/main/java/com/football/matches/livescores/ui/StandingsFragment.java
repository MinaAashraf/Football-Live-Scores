package com.football.matches.livescores.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.StandingAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.StandingObj;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StandingsFragment extends Fragment {

    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.standings_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        getData();
        return view;
    }

  /*  private class ListViewAdapter extends ArrayAdapter {
         List <StandingObj> data;
        public ListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            data = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.standing_item, parent, false);

            TextView rank = view.findViewById(R.id.rank);
            ImageView teamImage = view.findViewById(R.id.team_image);
            TextView teamName = view.findViewById(R.id.team_name);
            TextView points = view.findViewById(R.id.points);
            TextView goalDiff = view.findViewById(R.id.goals_dif);
            TextView g_for = view.findViewById(R.id.g_for);
            TextView g_against = view.findViewById(R.id.g_against);
            TextView playedMatches = view.findViewById(R.id.played);
            StandingObj standingObj = data.get(position);

            rank.setText("" + standingObj.getRank());
            Picasso.get().load(standingObj.getTeam().getLogo()).into(teamImage);
            teamName.setText(standingObj.getTeam().getName());
            points.setText("" + standingObj.getPoints());
            goalDiff.setText("" + standingObj.getGoalsDiff());
            g_for.setText("" + standingObj.getAll().getGoals().getForTeam());
            g_against.setText("" + standingObj.getAll().getGoals().getAgainst());
            playedMatches.setText("" + standingObj.getAll().getPlayed());
            return view;
        }
    }*/

    public void getData() {
        MyViewModel myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getLeagueStandings(((LeagueActivity) getActivity()).leagueId).observe(getActivity(), new Observer<List<List<StandingObj>>>() {
            @Override
            public void onChanged(List<List<StandingObj>> standingObjsList) {
              /*  data.addAll(standingObjsList.get(0));
                adapter.notifyDataSetChanged();*/
                createAllStandingsViews(standingObjsList);
            }
        });

    }

    int count = 0;

    private void createAllStandingsViews(List<List<StandingObj>> totalList) {
        if (count++ > 0)
            return;
        for (List<StandingObj> list : totalList)
            createSingleView(list);
    }

    private void createSingleView(List<StandingObj> data) {
        View view = getLayoutInflater().inflate(R.layout.standings_item, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
       /* ArrayAdapter adapter = new ListViewAdapter(getActivity(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);*/
        StandingAdapter standingAdapter = new StandingAdapter();
        standingAdapter.setData(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(standingAdapter);
        linearlayout.addView(view);
    }
}

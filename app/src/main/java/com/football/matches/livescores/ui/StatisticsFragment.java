package com.football.matches.livescores.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.StatisticsObject;
import com.football.matches.livescores.pojo.PlayerStatisticsObj;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsFragment extends Fragment {


    @BindView(R.id.listview)
    ListView listview;
    List<PlayerStatisticsObj> topScorers = new ArrayList<>();

    ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(),view.findViewById(R.id.parent));
        adapter = new ListAdapter(getActivity(), android.R.layout.simple_list_item_1, topScorers);
        getTopScorers();
        listview.setAdapter(adapter);
        return view;
    }

    public class ListAdapter extends ArrayAdapter {

        public ListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.topscorer_item, parent, false);
            TextView rank = view.findViewById(R.id.rank);
            TextView playerName = view.findViewById(R.id.player_name);
            TextView matches_played = view.findViewById(R.id.matches_played);
            TextView minutes_played = view.findViewById(R.id.minutes);
            TextView goals_scored = view.findViewById(R.id.goal_scored);
            TextView assist = view.findViewById(R.id.assist);
            ImageView teamLogo = view.findViewById(R.id.team_image);

            PlayerStatisticsObj object = topScorers.get(position);
            StatisticsObject statisticsObject = object.getStatistics().get(0);
            Picasso.get().load(statisticsObject.getTeam().getLogo()).into(teamLogo);
            rank.setText("" + (position+1));
            playerName.setText(object.getPlayer().getName());
            matches_played.setText("" + statisticsObject.getGames().getAppearences());
            minutes_played.setText("" + statisticsObject.getGames().getMinutes());
            goals_scored.setText("" + statisticsObject.getGoals().getTotal());
            assist.setText("" + statisticsObject.getGoals().getAssists());


            return view;
        }
    }

    public void getTopScorers() {
        MyViewModel myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getTopScorerStatistics(((LeagueActivity) getActivity()).leagueId).observe(getActivity(), new Observer<List<PlayerStatisticsObj>>() {
            @Override
            public void onChanged(List<PlayerStatisticsObj> playerStatisticsObjs) {
                topScorers.addAll(playerStatisticsObjs);
                adapter.notifyDataSetChanged();
            }
        });
    }

}

package com.football.matches.livescores.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.League;
import com.football.matches.livescores.pojo.StandingObj;
import com.football.matches.livescores.pojo.Team;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamStandingsFragment extends Fragment {
    List<StandingObj> data = new ArrayList<>();
    List<String> dataNames = new ArrayList<>();
    ListViewAdapter myAdapter;
    MyViewModel myViewModel;
    ArrayAdapter spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_standings_fragment, container, false);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        final TeamActivity myActivity = ((TeamActivity) getActivity());
        final Spinner leagueSpinner = view.findViewById(R.id.leagues_spinner);
        TextView seeAll = view.findViewById(R.id.seeAll_table);
        myAdapter = new ListViewAdapter(getActivity(), android.R.layout.simple_list_item_1, dataNames);
        ListView listView = view.findViewById(R.id.listview);
        listView.setAdapter(myAdapter);
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        spinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, myActivity.leaguesNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagueSpinner.setAdapter(spinnerAdapter);


        leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                observeData(myActivity.leaguesObjects.get(position).getLeague().getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String json = gson.toJson(standings);
                String json2 = gson.toJson(myActivity.leaguesObjects.get(leagueSpinner.getSelectedItemPosition()));
                getActivity().startActivity(new Intent(getActivity(), LeagueActivity.class).putExtra("seeAll", json).putExtra("league", json2));
            }
        });
        return view;
    }

    List<StandingObj> standings = new ArrayList<>();

    private class ListViewAdapter extends ArrayAdapter {

        public ListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
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
            try {
                StandingObj standingObj = data.get(position);
                rank.setText("" + standingObj.getRank());
                Picasso.get().load(standingObj.getTeam().getLogo()).into(teamImage);
                teamName.setText(standingObj.getTeam().getName());
                points.setText("" + standingObj.getPoints());
                goalDiff.setText("" + standingObj.getGoalsDiff());
                g_for.setText("" + standingObj.getAll().getGoals().getForTeam());
                g_against.setText("" + standingObj.getAll().getGoals().getAgainst());
                playedMatches.setText("" + standingObj.getAll().getPlayed());
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
    private void getPremierLeagueStandings(List<StandingObj> standingObjs) {
        standings.addAll(standingObjs);
        for (int i = 0; i < standings.size(); i++) {
            if (standings.get(i).getTeam().getId() == ((TeamActivity) getActivity()).team.getId()) {
                if (i == 0) {
                    data.add(standings.get(i));
                    data.add(standings.get(i + 1));
                    data.add(standings.get(i + 2));
                    data.add(standings.get(i + 3));
                    data.add(standings.get(i + 4));
                } else if (i == standings.size() - 1) {
                    data.add(standings.get(i - 4));
                    data.add(standings.get(i - 3));
                    data.add(standings.get(i - 2));
                    data.add(standings.get(i - 1));
                    data.add(standings.get(i));
                } else {
                    data.add(standings.get(i - 1));
                    data.add(standings.get(i));
                    data.add(standings.get(i + 1));
                    try {
                        data.add(standings.get(i - 2));
                        data.add(standings.get(i + 2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private void getDifferentCompetitionStandings(List<List<StandingObj>> standingObjsList) {
        for (List<StandingObj> list : standingObjsList) {
            boolean flag = false;
            for (StandingObj standingObj : list) {
                if (standingObj.getTeam().getId() == TeamActivity.team.getId()) {
                    standings.addAll(list);
                    data.addAll(list);
                    flag = true;
                    break;
                }
            }
            if (flag)
                break;
        }
    }

    public void observeData(int leagueId) {
        standings.clear();
        myViewModel.getLeagueStandings(leagueId).observe(getActivity(), new Observer<List<List<StandingObj>>>() {
            @Override
            public void onChanged(List<List<StandingObj>> standingObjsList) {
                if (standingObjsList.size() == 1) {
                    getPremierLeagueStandings(standingObjsList.get(0));

                } else {
                    getDifferentCompetitionStandings(standingObjsList);
                }
                for (StandingObj standing : data)
                    dataNames.add(standing.getTeam().getName());
                myAdapter.notifyDataSetChanged();
            }
        });
    }

}

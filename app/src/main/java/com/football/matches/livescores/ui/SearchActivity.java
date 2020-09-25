package com.football.matches.livescores.ui;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.LeagueAdapter;
import com.football.matches.livescores.Adapters.PlayerAdapter;
import com.football.matches.livescores.Adapters.TeamAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.LeagueResponceObject;
import com.football.matches.livescores.pojo.PlayerData;
import com.football.matches.livescores.pojo.Team;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String type = "";
    @BindView(R.id.search_view)
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        type = getIntent().getStringExtra("from");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (type) {
                    case "local":
                    case "team":
                        if (newText.length() >= 3)
                            getSearchedTeams(newText);
                        break;
                    case "league":
                        if (newText.length() >= 3)
                            getSearchedLeagues(newText);
                        break;
                    case "player":
                        getSearchedPlayers(newText);
                        break;
                }
                return true;
            }
        });
    }

    MyViewModel myViewModel;

    private void getSearchedPlayers(String newText) {
        myViewModel.getSearchedPlayers(newText).observe(this, new Observer<List<PlayerData>>() {
            @Override
            public void onChanged(List<PlayerData> playerData) {
                adapter = new PlayerAdapter(SearchActivity.this, R.layout.player_iteam);
                List<PlayerData> players = new ArrayList<>();
                if (playerData != null && !playerData.isEmpty())
                    for (PlayerData playerData1 : playerData) {
                        if (playerData1.getStrSport().equals("Soccer"))
                            players.add(playerData1);
                    }
                ((PlayerAdapter) adapter).setData(players);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void getSearchedTeams(String search) {
        myViewModel.getSearchedTeams(search).observe(this, new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teamsList) {
                adapter = new TeamAdapter(SearchActivity.this, R.layout.team_item);
                ((TeamAdapter) adapter).setData(teamsList);
                recyclerView.setAdapter(adapter);
            }
        });
    }


    public void getSearchedLeagues(String search) {
        myViewModel.getSearchedLeagues(search).observe(this, new Observer<List<LeagueResponceObject>>() {
            @Override
            public void onChanged(List<LeagueResponceObject> leagueResponceObjects) {
                adapter = new LeagueAdapter(SearchActivity.this, R.layout.team_item);
                ((LeagueAdapter) adapter).setData(leagueResponceObjects);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

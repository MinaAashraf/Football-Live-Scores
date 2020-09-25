package com.football.matches.livescores.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.football.matches.livescores.Adapters.CountryAdapter;
import com.football.matches.livescores.Adapters.LeagueAdapter;
import com.football.matches.livescores.Adapters.TeamAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.Country;
import com.football.matches.livescores.pojo.League;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChooseTeam extends AppCompatActivity {
    CountryAdapter adapter;
    public static Button next_btn;
    public static RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    public static MyViewModel myViewModel;
    public static ProgressBar progressBar;
    String type = "local";
    TextView searchView;
    // VrPanoramaView vrPanoramaView = findViewById(R.id.vr_view);
    // VrPanoramaView.Options options = new VrPanoramaView.Options();
    //  options.inputType = VrPanoramaView.Options.TYPE_MONO;
    // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seramic);
    // vrPanoramaView.loadImageFromBitmap(bitmap, options);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        new CheckConnectivity(this, findViewById(R.id.parent));

        next_btn = findViewById(R.id.skip_btn);
        searchView = findViewById(R.id.searchview);
        if (getIntent().hasExtra("from")) {
            next_btn.setVisibility(View.GONE);
            type = getIntent().getStringExtra("from");
            searchView.setText("Search by " + type + " name..");
            getSupportActionBar().setTitle("Choose " + type);
        }
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseTeam.this, SearchActivity.class).putExtra("from", type));
            }
        });
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recyclerView);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        adapter = new CountryAdapter(this, datalist, this, type);
        teamAdapter = new TeamAdapter(this, R.layout.team_item);
        leagueAdapter = new LeagueAdapter(this, R.layout.team_item);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
         sharedPreferences = getSharedPreferences("liveScores", Context.MODE_PRIVATE);


        myViewModel.getCountries().observe(this, new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                datalist.addAll(countries);
                datalistAll.addAll(countries);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getIntent().hasExtra("from")) {
                    startActivity(new Intent(ChooseTeam.this, MainActivity.class));
                    dontStartAgain();
                }
                finish();
            }
        });
    }

    public void dontStartAgain() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dontStartAgain", true).apply();
    }

    TeamAdapter teamAdapter;
    List<League> leagues = new ArrayList<>();
    LeagueAdapter leagueAdapter;
    List<Country> datalist = new ArrayList<>();
    List<Country> datalistAll = new ArrayList<>();

    @Override
    public void onBackPressed() {
        if (recyclerView.getAdapter() instanceof CountryAdapter)
            super.onBackPressed();
        else if (recyclerView.getAdapter() instanceof TeamAdapter || recyclerView.getAdapter() instanceof LeagueAdapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

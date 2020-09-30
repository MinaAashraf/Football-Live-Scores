package com.football.matches.livescores.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.Country;
import com.football.matches.livescores.pojo.League;
import com.football.matches.livescores.pojo.LeagueResponceObject;
import com.football.matches.livescores.pojo.Team;
import com.football.matches.livescores.ui.ChooseTeam;
import com.football.matches.livescores.ui.SharedPreferenceFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    Context context;
    List<Country> dataList;
    Activity activity;
    TeamAdapter teamAdapter;
    LeagueAdapter leagueAdapter;
    Set<String> set;
    Button next_btn = ChooseTeam.next_btn;
    static public String type;
    ProgressBar progressBar = ChooseTeam.progressBar;
    RecyclerView recyclerView = ChooseTeam.recyclerView;
    String countryName = "";

    public CountryAdapter(Context context, List<Country> dataList, Activity activity, String type) {
        this.context = context;
        this.dataList = dataList;
        this.activity = activity;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.type = type;
        SharedPreferences sharedPreferences = context.getSharedPreferences("liveScores", Context.MODE_PRIVATE);
        if (type.equals("local")) {
            set = sharedPreferences.getStringSet(context.getResources().getString(R.string.followingTeamsObjs), new HashSet<String>());
            if (!set.isEmpty()) {
                next_btn.setText("Next");
                next_btn.setBackgroundColor(Color.parseColor("#6200EE"));
                next_btn.setTextColor(Color.WHITE);
            }
        }
        if (!sharedPreferences.getBoolean("dontStartAgain", false))
            observepremiereLeague();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.country_name.setText(dataList.get(position).getName());
        String url = dataList.get(position).getFlag();
        if (url != null)
            SvgLoader.pluck().with(activity).load(url, holder.country_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryName = dataList.get(position).getName();
                progressBar.setVisibility(View.VISIBLE);
                if (type.equals("team") || type.equals("local")) {
                    teamAdapter = new TeamAdapter(context, R.layout.team_item);
                    observeTeams(countryName);
                    recyclerView.setAdapter(teamAdapter);
                } else if (type.equals("league")) {
                    leagueAdapter = new LeagueAdapter(context, R.layout.team_item);
                    observeLeagues(countryName);
                    recyclerView.setAdapter(leagueAdapter);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

  /*  @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Country> filteredList = new ArrayList();
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(dataListAll);
                notifyDataSetChanged();
            } else {
                for (Country country : dataListAll) {
                    if (country.getName().toLowerCase().contains(charSequence.toString().toLowerCase()))
                        filteredList.add(country);
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dataList.clear();
            dataList.addAll((Collection<? extends Country>) filterResults.values);
            notifyDataSetChanged();
        }
    };*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView country_name;
        CircleImageView country_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            country_name = itemView.findViewById(R.id.country_name);
            country_image = itemView.findViewById(R.id.country_image);
        }
    }

    private void observeTeams(String countryName) {
        ChooseTeam.myViewModel.getTeams(countryName).observe((LifecycleOwner) context, new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teamsList) {
                progressBar.setVisibility(View.GONE);
                teamAdapter.setData(teamsList);
            }
        });
    }


    List<League> leagues = new ArrayList<>();

    private void observeLeagues(String countryName) {
        leagues.clear();
        ChooseTeam.myViewModel.getCountryLeagues(countryName).observe((LifecycleOwner) context, new Observer<List<LeagueResponceObject>>() {
            @Override
            public void onChanged(List<LeagueResponceObject> leagueResponceObjects) {
                progressBar.setVisibility(View.GONE);
                leagueAdapter.setData(leagueResponceObjects);
            }
        });
    }

    private void observepremiereLeague() {
        ChooseTeam.myViewModel.getCountryLeagues("England").observe((LifecycleOwner) context, new Observer<List<LeagueResponceObject>>() {
            @Override
            public void onChanged(List<LeagueResponceObject> leagueResponceObjects) {
                for (LeagueResponceObject leagueResponceObject : leagueResponceObjects) {
                    if (leagueResponceObject.getLeague().getName().equals("Premier League")) {
                        String json = new Gson().toJson(leagueResponceObject);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("liveScores", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Set<String> leagueSet = sharedPreferences.getStringSet(context.getResources().getString(R.string.followingLeagueObjs), new HashSet<String>());
                        Set<String> newSet = new HashSet<String>();
                        if (leagueSet.size() != 0) {
                            for (String json1 : leagueSet)
                                newSet.add(json1);
                        }
                        newSet.add(json);
                        editor.putStringSet(context.getResources().getString(R.string.followingLeagueObjs), newSet).apply();
                    }
                }
            }
        });
    }

}

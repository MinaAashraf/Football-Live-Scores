package com.football.matches.livescores.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.League;
import com.football.matches.livescores.pojo.LeagueResponceObject;
import com.football.matches.livescores.ui.ChooseTeam;
import com.football.matches.livescores.ui.LeagueActivity;
import com.football.matches.livescores.ui.SharedPreferenceFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    List<LeagueResponceObject> leaguesList = new ArrayList<>();
    Context context;
    static public Set<String> leagueSet;
    public Set <String> newSet = new HashSet<String>();
    SharedPreferences.Editor editor;
    Button doneBtn = ChooseTeam.next_btn;
    int item_view;

    public LeagueAdapter(Context context, int item_view) {
        this.context = context;
        this.item_view = item_view;
        if (item_view == R.layout.team_item) {
            sharedPreferences = context.getSharedPreferences("liveScores", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            leagueSet = sharedPreferences.getStringSet(context.getResources().getString(R.string.followingLeagueObjs), new HashSet<String>());
            if (leagueSet.size()!=0)
            {
                for (String json : leagueSet)
                    newSet.add(json);
            }
        }
    }

    public void setData(List<LeagueResponceObject> leagues) {
        this.leaguesList = leagues;
        notifyDataSetChanged();
    }

    public void clear() {
        this.leaguesList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        String json = objToJson(leaguesList.get(position));
        final League league = leaguesList.get(position).getLeague();
        holder.legue_name.setText(league.getName());
        Picasso.get().load(league.getLogo()).placeholder(R.drawable.addcomp_icon).into(holder.league_image);

        if (item_view == R.layout.team_item && !newSet.isEmpty()) {
            if (newSet.contains(json))
                holder.star_image.setImageResource(R.drawable.starfill);
            else
                holder.star_image.setImageResource(R.drawable.starstroke);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_view == R.layout.followingteam_item) {
                    context.startActivity(new Intent(context, LeagueActivity.class).putExtra("league", json));
                } else {
                    if (newSet.contains(json)) {
                        holder.star_image.setImageResource(R.drawable.starstroke);
                        newSet.remove(json);
                        editor.putStringSet(context.getResources().getString(R.string.followingLeagueObjs), newSet).apply();
                        Toast.makeText(context, "You no longer follow " + league.getName() + ".", Toast.LENGTH_SHORT).show();

                    } else {
                        holder.star_image.setImageResource(R.drawable.starfill);
                        newSet.add(json);
                        editor.putStringSet(context.getResources().getString(R.string.followingLeagueObjs),newSet).apply();
                        Toast.makeText(context, "You now follow " + league.getName() + ".", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaguesList.size() == 0 ? 0 : leaguesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView league_image;
        TextView legue_name;
        ImageView star_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            league_image = itemView.findViewById(R.id.team_image);
            legue_name = itemView.findViewById(R.id.team_name);
            star_image = itemView.findViewById(R.id.star_icon);
        }
    }

    public String objToJson(LeagueResponceObject leagueResponceObject) {
        Gson gson = new Gson();
        return gson.toJson(leagueResponceObject);
    }

}

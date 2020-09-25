package com.football.matches.livescores.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.football.matches.livescores.pojo.Team;
import com.football.matches.livescores.ui.ChooseTeam;
import com.football.matches.livescores.ui.SharedPreferenceFactory;
import com.football.matches.livescores.ui.TeamActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {
    Context context;
    public List<Team> dataList = new ArrayList<>();
    int itemLayout;
    static public Set<String> teamsSet;
    Set <String> newSet = new HashSet<String>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TeamAdapter(Context context, int itemLayout) {
        this.context = context;
        this.itemLayout = itemLayout;
        if (itemLayout == R.layout.team_item) {
            sharedPreferences = context.getSharedPreferences("liveScores", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            teamsSet = sharedPreferences.getStringSet(context.getResources().getString(R.string.followingTeamsObjs), new HashSet<String>());
            if (teamsSet.size()!=0)
            {
                for (String json : teamsSet)
                    newSet.add(json);
            }
        }

    }

    public void setData(List<Team> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.itemLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Team team = dataList.get(position);
        String json = objToJson(team);
        holder.teamName.setText(team.getName());
        if (team.getLogo() != null)
            Picasso.get().load(team.getLogo()).placeholder(R.drawable.team_icon).into(holder.teamImge);

        if (itemLayout == R.layout.team_item && !newSet.isEmpty()) {
            if (CountryAdapter.type.equals("local"))
                activeBtn();
            if (newSet.contains(json))
                holder.star.setImageResource(R.drawable.starfill);
            else
                holder.star.setImageResource(R.drawable.starstroke);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemLayout == R.layout.followingteam_item || itemLayout == R.layout.team_item2)
                    context.startActivity(new Intent(context, TeamActivity.class).putExtra("team", json));

                else {
                    if (newSet.contains(json)) {
                        holder.star.setImageResource(R.drawable.starstroke);
                        if (CountryAdapter.type.equals("local") && newSet.isEmpty())
                            deactive();
                        newSet.remove(json);
                        editor.putStringSet(context.getResources().getString(R.string.followingTeamsObjs), newSet).apply();
                        Toast.makeText(context, "You no longer follow " + team.getName() + ".", Toast.LENGTH_SHORT).show();

                    } else {
                        holder.star.setImageResource(R.drawable.starfill);
                        if (CountryAdapter.type.equals("local"))
                            activeBtn();
                        newSet.add(json);
                        editor.putStringSet(context.getResources().getString(R.string.followingTeamsObjs), newSet).apply();
                        Toast.makeText(context, "You now follow " + team.getName() + ".", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamName;
        ImageView teamImge;
        ImageView star;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemLayout == R.layout.team_item) {
                star = itemView.findViewById(R.id.star_icon);
            }
            teamName = itemView.findViewById(R.id.team_name);
            teamImge = itemView.findViewById(R.id.team_image);
        }
    }

    public String objToJson(Team team) {
        Gson gson = new Gson();
        return gson.toJson(team);
    }

    Button doneBtn = ChooseTeam.next_btn;

    public void activeBtn() {
        doneBtn.setText("Next");
        doneBtn.setBackgroundColor(Color.BLACK);
        doneBtn.setTextColor(Color.WHITE);
    }

    public void deactive() {
        doneBtn.setText("Skip this step");
        doneBtn.setBackgroundColor(Color.WHITE);
        doneBtn.setTextColor(Color.BLACK);

    }
}

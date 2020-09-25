package com.football.matches.livescores.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.StandingObj;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StandingAdapter extends RecyclerView.Adapter<StandingAdapter.MyViewHolder> {
    List<StandingObj> data = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.standing_item, parent, false);
        return new MyViewHolder(view);
    }

    public void setData(List<StandingObj> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StandingObj standingObj = data.get(position);
        holder.rank.setText("" + standingObj.getRank());
        Picasso.get().load(standingObj.getTeam().getLogo()).into(holder.teamImage);
        holder.teamName.setText(standingObj.getTeam().getName());
        holder.points.setText("" + standingObj.getPoints());
        holder.goalDiff.setText("" + standingObj.getGoalsDiff());
        holder.g_for.setText("" + standingObj.getAll().getGoals().getForTeam());
        holder.g_against.setText("" + standingObj.getAll().getGoals().getAgainst());
        holder.playedMatches.setText("" + standingObj.getAll().getPlayed());
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rank;
        ImageView teamImage;
        TextView teamName;
        TextView points;
        TextView goalDiff;
        TextView g_for;
        TextView g_against;
        TextView playedMatches;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            teamImage = itemView.findViewById(R.id.team_image);
            teamName = itemView.findViewById(R.id.team_name);
            points = itemView.findViewById(R.id.points);
            goalDiff = itemView.findViewById(R.id.goals_dif);
            g_for = itemView.findViewById(R.id.g_for);
            g_against = itemView.findViewById(R.id.g_against);
            playedMatches = itemView.findViewById(R.id.played);
        }
    }
}

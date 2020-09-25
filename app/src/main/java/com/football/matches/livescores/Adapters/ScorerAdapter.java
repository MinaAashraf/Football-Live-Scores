package com.football.matches.livescores.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.EventResponceObj;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ScorerAdapter extends RecyclerView.Adapter<ScorerAdapter.MyViewHolder> {

    List<EventResponceObj> events = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scorer_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventResponceObj event = events.get(position);
        Picasso.get().load(event.getTeam().getLogo()).into(holder.teamLogo);
        holder.minute.setText(""+event.getTime().getElapsed());
        holder.scorerName.setText(event.getPlayer().getName());
    }

    public void setData(List<EventResponceObj> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return events.size() == 0 ? 0 : events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView minute, scorerName;
        ImageView teamLogo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            minute = itemView.findViewById(R.id.minite);
            scorerName = itemView.findViewById(R.id.scorer_name);
            teamLogo = itemView.findViewById(R.id.team_logo);
        }
    }
}

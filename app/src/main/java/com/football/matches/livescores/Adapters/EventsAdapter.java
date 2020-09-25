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
import com.football.matches.livescores.ui.MatchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    List<EventResponceObj> events = new ArrayList<>();
    MatchActivity activity;
    public EventsAdapter (MatchActivity activity)
    {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventResponceObj event = events.get(position);
        Picasso.get().load(event.getTeam().getLogo()).into(holder.teamLogo);
        holder.firstPlayer.setText(event.getPlayer().getName());
        holder.minute.setText(""+event.getTime().getElapsed());
        String assistName = event.getAssist().getName();
        if (assistName != null)
            holder.secondPlayer.setText(assistName);
        switch (event.getType()) {
            case "Goal":
                holder.eventIcon.setImageResource(R.drawable.soccer_ball);
                holder.eventType.setText("Goal");
                holder.downarrow.setVisibility(View.GONE);
                holder.uparrow.setVisibility(View.GONE);
                break;
            case "Card":
                switch (event.getDetail()) {
                    case "Yellow Card":
                        holder.eventIcon.setImageResource(R.drawable.yellowcard);
                        break;
                    case "Red Card":
                        holder.eventIcon.setImageResource(R.drawable.redcard);
                        break;
                }
                holder.eventType.setText(event.getDetail());
                holder.downarrow.setVisibility(View.GONE);
                holder.uparrow.setVisibility(View.GONE);
                break;
            case "subst":
                holder.eventIcon.setImageResource(R.drawable.substitite_icon);
                holder.eventType.setText("Substitutes");
                holder.downarrow.setVisibility(View.VISIBLE);
                holder.uparrow.setVisibility(View.VISIBLE);
                break;
        }


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
        ImageView teamLogo, eventIcon, downarrow, uparrow;
        TextView firstPlayer, secondPlayer, eventType, minute;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            teamLogo = itemView.findViewById(R.id.team_logo);
            eventIcon = itemView.findViewById(R.id.event_icon);
            eventType = itemView.findViewById(R.id.event_type);
            firstPlayer = itemView.findViewById(R.id.first_player);
            secondPlayer = itemView.findViewById(R.id.second_player);
            downarrow = itemView.findViewById(R.id.downarrow);
            uparrow = itemView.findViewById(R.id.uparrow);
            minute = itemView.findViewById(R.id.minite);
        }
    }
}

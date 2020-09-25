package com.football.matches.livescores.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.LineUp;
import com.football.matches.livescores.pojo.Player;

import java.util.ArrayList;
import java.util.List;

public class LineupsAdapter extends RecyclerView.Adapter<LineupsAdapter.MyViewHolder> {

    List<LineUp> lineUps = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lineup_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Player player = lineUps.get(position).getPlayer();
        holder.playerNumber.setText(player.getNumber());
        holder.playerPos.setText(player.getPos());
        String playerName = player.getName();
        holder.playerName.setText(playerName);
    }

    @Override
    public int getItemCount() {
        return lineUps.size() == 0 ? 0 : lineUps.size();
    }

    public void setData(List<LineUp> lineUps) {
        this.lineUps = lineUps;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playerNumber, playerPos , playerName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNumber = itemView.findViewById(R.id.player_number);
            playerPos = itemView.findViewById(R.id.player_pos);
            playerName = itemView.findViewById(R.id.player_name);
        }
    }
}

package com.football.matches.livescores.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.PlayerData;
import com.football.matches.livescores.ui.PlayerActivity;
import com.football.matches.livescores.ui.SharedPreferenceFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {

    List<PlayerData> players = new ArrayList<>();
    int item_layout;
    Context context;
    Set<String> playersSet , newSet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    public PlayerAdapter(Context context, int item_layout) {
        this.item_layout = item_layout;
        this.context = context;
        if (item_layout == R.layout.player_iteam) {
            sharedPreferences = context.getSharedPreferences("liveScores", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            playersSet = sharedPreferences.getStringSet(context.getResources().getString(R.string.followingPlayerObjs), new HashSet<String>());
            newSet = new HashSet<String>();
            if (playersSet.size()!=0)
            {
                for (String json : playersSet)
                    newSet.add(json);
            }
        }
    }

    public void setData(List<PlayerData> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PlayerData player = players.get(position);
        String json = gson.toJson(player);
        if (player.getStrThumb() != null)
            Picasso.get().load(player.getStrThumb()).placeholder(R.drawable.player_icon).into(holder.playerImage);
        else
            holder.playerImage.setImageResource(R.drawable.player_icon);

        holder.playerName.setText(player.getStrPlayer());

        if (item_layout == R.layout.player_iteam && !newSet.isEmpty()) {
            if (newSet.contains(json))
                holder.starIcon.setImageResource(R.drawable.starfill);
            else
                holder.starIcon.setImageResource(R.drawable.starstroke);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item_layout) {
                    case R.layout.followingplayer_item:
                        context.startActivity(new Intent(context, PlayerActivity.class).putExtra("player", json));
                        break;
                    case R.layout.player_iteam:
                        if (newSet.contains(json)) {
                            holder.starIcon.setImageResource(R.drawable.starstroke);
                            newSet.remove(json);
                            editor.putStringSet(context.getResources().getString(R.string.followingPlayerObjs),newSet).apply();
                            Toast.makeText(context, "You no longer follow " + player.getStrPlayer(), Toast.LENGTH_SHORT).show();
                        } else {
                            holder.starIcon.setImageResource(R.drawable.starfill);
                            newSet.add(json);
                            editor.putStringSet(context.getResources().getString(R.string.followingPlayerObjs), newSet).apply();
                            Toast.makeText(context, "You now follow " + player.getStrPlayer(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return players.size() == 0 ? 0 : players.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView playerImage, starIcon;
        TextView playerName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImage = itemView.findViewById(R.id.player_image);
            starIcon = itemView.findViewById(R.id.star_icon);
            playerName = itemView.findViewById(R.id.player_name);
        }
    }

}

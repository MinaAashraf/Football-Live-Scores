package com.football.matches.livescores.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.TransferResponceObj;
import com.football.matches.livescores.pojo.TransferTeams;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.MyViewHolder> {


    List<TransferResponceObj> transferResponceObjs = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transfer_item, parent, false);
        return new MyViewHolder(view);
    }

    public void setData(List<TransferResponceObj> transferResponceObjs) {
        Collections.reverse(transferResponceObjs);
        this.transferResponceObjs = transferResponceObjs;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TransferResponceObj transferResponceObj = transferResponceObjs.get(position);
        TransferTeams teams = transferResponceObj.getTransfer().getTeams();
        Picasso.get().load(teams.getOut().getLogo()).placeholder(R.drawable.team_icon).into(holder.out_team_logo);
        Picasso.get().load(teams.getIn().getLogo()).placeholder(R.drawable.team_icon).into(holder.in_team_logo);
        holder.out_team_name.setText(teams.getOut().getName());
        holder.in_team_name.setText(teams.getIn().getName());

        holder.playerName.setText(transferResponceObj.getPlayer().getName());
        holder.type.setText(transferResponceObj.getTransfer().getType());
        holder.date.setText(dateFormat(transferResponceObj.getUpdate())[0]);
    }

    @Override
    public int getItemCount() {
        return transferResponceObjs.size() == 0 ? 0 : transferResponceObjs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView out_team_logo , in_team_logo , player_image ;
        TextView playerName , type , date , out_team_name , in_team_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            player_image = itemView.findViewById(R.id.player_image);
            out_team_logo = itemView.findViewById(R.id.outteam_logo);
            in_team_logo = itemView.findViewById(R.id.inteam_logo);
            out_team_name = itemView.findViewById(R.id.outteam_name);
            in_team_name = itemView.findViewById(R.id.inteam_name);
            playerName = itemView.findViewById(R.id.player_name);
            type = itemView.findViewById(R.id.type);
            date = itemView.findViewById(R.id.date);
        }
    }

    Date date = null;

    public String[] dateFormat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat clockFormat = new SimpleDateFormat("hh:mm a");
        try {
            date = inputFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        String formattedClock = clockFormat.format(date);
        return new String[]{formattedDate, formattedClock};
    }
}

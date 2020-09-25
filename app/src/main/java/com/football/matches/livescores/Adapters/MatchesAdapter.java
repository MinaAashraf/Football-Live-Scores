package com.football.matches.livescores.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.Goals;
import com.football.matches.livescores.ui.MatchActivity;
import com.football.matches.livescores.ui.TimeFormat;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Object> fixtures;
    Context context;

    public MatchesAdapter(List<Object> fixtures, Context context) {
        this.fixtures = fixtures;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MyViewHolder(view);
       /* switch (viewType) {
            case R.layout.match_item:
                return new MyViewHolder(view);
            case R.layout.native_ad_item:

            default:
                return new NativeAdHolder(view);
        }*/

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case R.layout.match_item:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                FixtureResposeObj fixture = (FixtureResposeObj) fixtures.get(position);
                String dateArr[] = TimeFormat.dateFormat(fixture.getFixture().getDate());
                myViewHolder.matchDate.setText(TimeFormat.formatToYesterdayOrToday(dateArr[0]));
                Picasso.get().load(fixture.getTeams().getHome().getLogo()).into(myViewHolder.frTeamImage);
                Picasso.get().load(fixture.getTeams().getAway().getLogo()).into(myViewHolder.secondTeamImage);
                myViewHolder.frTeamName.setText(fixture.getTeams().getHome().getName());
                myViewHolder.secondTeamName.setText(fixture.getTeams().getAway().getName());
                Goals goals = fixture.getGoals();
                String status = fixture.getFixture().getStatus().getShortt();
                switch (status) {
                    case "FT": case "AET":
                        myViewHolder.golasValue.setText(goals.getHome() + " - " + goals.getAway());
                        break;
                    case "NS":
                        myViewHolder.golasValue.setText(dateArr[1]);
                        break;
                    default:
                        myViewHolder.golasValue.setText(status);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson = new Gson();
                        String myFixture = gson.toJson(fixture);
                        context.startActivity(new Intent(context, MatchActivity.class).putExtra("fixture", myFixture));
                    }
                });
                break;
            case R.layout.native_ad_item:
            default:
              /*  NativeAdHolder nativeAdHolder = (NativeAdHolder) holder;
                TemplateView templateView = nativeAdHolder.templateView;
                templateView = (TemplateView) fixtures.get(position);
                ViewGroup viewGroup = (ViewGroup) holder.itemView;
                if (viewGroup.getChildCount() > 0)
                    viewGroup.removeAllViews();
                if (templateView.getParent() != null)
                    ((ViewGroup) templateView.getParent()).removeView(templateView);

               */

        }
    }

    @Override
    public int getItemViewType(int position) {
        return (fixtures.get(position) instanceof TemplateView) ? R.layout.native_ad_item : R.layout.match_item;
    }



    @Override
    public int getItemCount() {
        return fixtures.size() == 0 ? 0 : fixtures.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView matchDate, frTeamName, secondTeamName, golasValue;
        ImageView frTeamImage, secondTeamImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            matchDate = itemView.findViewById(R.id.matchDate);
            golasValue = itemView.findViewById(R.id.goals_value);
            frTeamName = itemView.findViewById(R.id.fr_teamName);
            frTeamImage = itemView.findViewById(R.id.fr_teamImage);
            secondTeamName = itemView.findViewById(R.id.second_teamName);
            secondTeamImage = itemView.findViewById(R.id.second_teamImage);
        }
    }

    public class NativeAdHolder extends RecyclerView.ViewHolder {
        TemplateView templateView;

        public NativeAdHolder(@NonNull View itemView) {
            super(itemView);
            templateView = itemView.findViewById(R.id.my_template);
        }
    }

}

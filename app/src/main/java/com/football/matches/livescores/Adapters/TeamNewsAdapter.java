package com.football.matches.livescores.Adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.Article;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.GenralTeamNews;
import com.football.matches.livescores.pojo.PredictionPercent;
import com.football.matches.livescores.pojo.Team;
import com.football.matches.livescores.ui.ArticleActivity;
import com.football.matches.livescores.ui.NextMatchWithPredict;
import com.football.matches.livescores.ui.TimeFormat;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class TeamNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<GenralTeamNews> list = new ArrayList<>();
    Context context;

    public TeamNewsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(viewType, parent, false);
        if (viewType == R.layout.new_item)
            return new BodyViewHolder(view);
        else
            return new HeaderViewHolder(view);
    }

    public void setData(List<GenralTeamNews> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getSize ()
    {
        return this.list.size();
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (list.get(position) instanceof NextMatchWithPredict) {
            NextMatchWithPredict nextMatchWithPredict = (NextMatchWithPredict) list.get(position);
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            setNextMatch(nextMatchWithPredict.getFixtureResposeObj(), headerViewHolder);
            setPredictions(nextMatchWithPredict.getPercent(), headerViewHolder);
        } else {
            BodyViewHolder bodyViewHolder = (BodyViewHolder) holder;
            Article article = (Article) list.get(position);
            setArticles(article, bodyViewHolder);
        }
    }


    private void setArticles(Article article, BodyViewHolder holder) {
        if (article.getUrlToImage() != null) {
            Picasso.get().load(article.getUrlToImage()).fit().centerCrop().into(holder.article_Image);
        } else
            holder.article_Image.setImageResource(R.drawable.no_image);

        holder.article_title.setText(article.getTitle());
        holder.article_source.setText(article.getSource().getName());

        String[] arr = TimeFormat.dateFormat(article.getPublishedAt());
        String publishedDate = TimeFormat.formatToYesterdayOrToday(arr[0]);
        holder.article_date.setText(publishedDate + ", " + arr[1]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String json = gson.toJson(article);
                context.startActivity(new Intent(context, ArticleActivity.class).putExtra("article", json));
            }
        });
    }

    private void setNextMatch(FixtureResposeObj fixtureResposeObj, HeaderViewHolder holder) {
        String[] dateArr = TimeFormat.dateFormat(fixtureResposeObj.getFixture().getDate());
        Team fTeam = fixtureResposeObj.getTeams().getHome();
        Team sTeam = fixtureResposeObj.getTeams().getAway();
        Picasso.get().load(fTeam.getLogo()).into(holder.fteam_image);
        Picasso.get().load(sTeam.getLogo()).into(holder.secteam_image);
        holder.leagueName.setText(fixtureResposeObj.getLeague().getName());
        holder.fr_teamName.setText(fTeam.getName());
        holder.second_teamName.setText(sTeam.getName());
        holder.matchDate.setText(TimeFormat.formatToYesterdayOrToday(dateArr[0]));
        holder.matchClock.setText(dateArr[1]);
    }

    private void setPredictions(PredictionPercent percent, HeaderViewHolder holder) {
        String percentHome = percent.getHome();
        String percentDraw = percent.getDraw();
        String percentAway = percent.getAway();

        holder.fteam_predic_percent.setText(percentHome);
        holder.equality_predic_percent.setText(percentDraw);
        holder.secteam_predic_percent.setText(percentAway);
        float fPercent = Float.parseFloat(percentHome.substring(0, percentHome.length() - 1));
        float drawPercent = Float.parseFloat(percentDraw.substring(0, percentDraw.length() - 1));
        float sPercent = Float.parseFloat(percentAway.substring(0, percentAway.length() - 1));
        animateProgress(holder.fteam_seekbar, fPercent);
        animateProgress(holder.equlity_seekbar, drawPercent);
        animateProgress(holder.secteam_seekbar, sPercent);
    }

    public void animateProgress(final CircularSeekBar seekBar, float progressValue) {
        ValueAnimator anim = ValueAnimator.ofInt(0, (int) progressValue);
        anim.setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animProgress = (Integer) animation.getAnimatedValue();
                seekBar.setProgress(animProgress);
            }
        });
        anim.start();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof NextMatchWithPredict)
            return R.layout.next_match_item;
        else
            return R.layout.new_item;
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size();
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {
        ImageView article_Image;
        TextView article_title, article_date, article_source;

        public BodyViewHolder(@NonNull View itemView) {
            super(itemView);
            article_Image = itemView.findViewById(R.id.article_image);
            article_title = itemView.findViewById(R.id.article_title);
            article_date = itemView.findViewById(R.id.article_date);
            article_source = itemView.findViewById(R.id.article_source);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView leagueName, matchDate, matchClock, fr_teamName, second_teamName, fteam_predic_percent, equality_predic_percent, secteam_predic_percent;
        ImageView fteam_image, secteam_image;
        CircularSeekBar fteam_seekbar, equlity_seekbar, secteam_seekbar;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueName = itemView.findViewById(R.id.leagueName);
            matchDate = itemView.findViewById(R.id.matchDate);
            matchClock = itemView.findViewById(R.id.matchClock);
            fr_teamName = itemView.findViewById(R.id.fr_teamName);
            second_teamName = itemView.findViewById(R.id.second_teamName);
            fteam_predic_percent = itemView.findViewById(R.id.fteam_predic_percent);
            equality_predic_percent = itemView.findViewById(R.id.equality_predic_percent);
            secteam_predic_percent = itemView.findViewById(R.id.secteam_predic_percent);
            fteam_image = itemView.findViewById(R.id.fteam_image);
            secteam_image = itemView.findViewById(R.id.secteam_image);
            fteam_seekbar = itemView.findViewById(R.id.fteam_seekbar);
            equlity_seekbar = itemView.findViewById(R.id.equlity_seekbar);
            secteam_seekbar = itemView.findViewById(R.id.secteam_seekbar);
            fteam_seekbar.setEnabled(false);
            equlity_seekbar.setEnabled(false);
            secteam_seekbar.setEnabled(false);
        }
    }

}

package com.football.matches.livescores.Adapters;

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
import com.football.matches.livescores.ui.ArticleActivity;
import com.football.matches.livescores.ui.TimeFormat;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeArticlesAdapter extends RecyclerView.Adapter<HomeArticlesAdapter.MyViewHolder> {
    List<Article> articles = new ArrayList<>();
    Context context;

    public HomeArticlesAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return R.layout.new_item;
        else
            return R.layout.new_item2;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Article article = articles.get(position);
        if (article.getUrlToImage() != null) {
            Picasso.get().load(article.getUrlToImage()).fit().centerCrop().into(holder.article_Image);
        } else
            holder.article_Image.setImageResource(R.drawable.no_image);

        holder.article_title.setText(article.getTitle());
        holder.article_source.setText(article.getSource().getName());
        String arr[] = TimeFormat.dateFormat(article.getPublishedAt());
        String publishedDate = TimeFormat.formatToYesterdayOrToday(arr[0]) + ", " + arr[1];
        holder.article_date.setText(publishedDate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String json = gson.toJson(article);
                context.startActivity(new Intent(context, ArticleActivity.class).putExtra("article", json));
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size() == 0 ? 0 : articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView article_Image;
        TextView article_title, article_date, article_source;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            article_Image = itemView.findViewById(R.id.article_image);
            article_title = itemView.findViewById(R.id.article_title);
            article_date = itemView.findViewById(R.id.article_date);
            article_source = itemView.findViewById(R.id.article_source);
        }
    }


}

package com.football.matches.livescores.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.Video;
import com.football.matches.livescores.pojo.VideoResponceObj;
import com.football.matches.livescores.pojo.VideoTitle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    List<VideoResponceObj> videoResponceObjs = new ArrayList<>();
    List<String> htmlEmbeds = new ArrayList<>();
    List<VideoTitle> titles = new ArrayList<>();
    Context context ;


    public VideoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(view);
    }

    public void setData(List<VideoResponceObj> videoResponceObjs) {
        this.videoResponceObjs = videoResponceObjs;
        for (VideoResponceObj videoResponceObj : videoResponceObjs) {
            htmlEmbeds.add(videoResponceObj.getEmbed());
            String videoDate = videoResponceObj.getDate();
            titles.add(new VideoTitle(videoResponceObj.getTitle(),videoDate));
            for (Video video : videoResponceObj.getVideos()) {
                htmlEmbeds.add(video.getEmbed());
                titles.add(new VideoTitle(video.getTitle(),videoDate));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoTitle title = titles.get(position);
        String embed = htmlEmbeds.get(position);
        holder.title.setText(title.getTitle());

        holder.webView.setWebChromeClient(new WebChromeClient());
        holder.webView.setWebViewClient(new WebViewClient());
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        holder.webView.loadData(embed, "text/html", null);
        //holder.webView.setScrollbarFadingEnabled(true);
        holder.webView.setVerticalScrollBarEnabled(false);
        holder.webView.getSettings().setDomStorageEnabled(true);
        holder.webView.getSettings().setAppCacheEnabled(true);
        holder.webView.getSettings().setAppCachePath(context.getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        holder.webView.getSettings().setDatabaseEnabled(true);
        holder.webView.getSettings().setDatabasePath(context.getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
        if (title.getDate()!=null)
        holder.date.setText(dateFormat(title.getDate())[0]);
    }


    @Override
    public int getItemCount() {
        return videoResponceObjs.size() == 0 ? 0 : htmlEmbeds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        TextView title, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.video_view);
            title = itemView.findViewById(R.id.video_title);
            date = itemView.findViewById(R.id.video_date);
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

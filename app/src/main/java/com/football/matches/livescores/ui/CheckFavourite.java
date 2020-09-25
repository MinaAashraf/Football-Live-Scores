package com.football.matches.livescores.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.widget.ImageView;
import android.widget.Toast;

import com.football.matches.livescores.R;

import java.util.HashSet;
import java.util.Set;

public class CheckFavourite {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Set<String> set;
    Set<String> newSet = new HashSet<String>();
    boolean flag = false;
    Context context;
    String json;
    String key;
    ImageView star;

    public CheckFavourite(Context context, String json, String key, ImageView star) {
        sharedPreferences = context.getSharedPreferences("liveScores", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.context = context;
        this.json = json;
        this.key = key;
        this.star = star;
    }

    public void check() {

        set = sharedPreferences.getStringSet(key, new HashSet<>());
        if (set.size()!=0)
        {
            for (String json : set)
                newSet.add(json);
        }
        if (!newSet.isEmpty() && newSet.contains(json)) {
            star.setImageResource(R.drawable.starfill);
            flag = true;
        } else {
            star.setImageResource(R.drawable.starstroke);
            flag = false;
        }
    }

    public void onClickStar(String name) {
        if (flag) {
            star.setImageResource(R.drawable.starstroke);
            newSet.remove(json);
            Toast.makeText(context, "You no longer follow " + name, Toast.LENGTH_SHORT).show();

        } else {
            star.setImageResource(R.drawable.starfill);
            newSet.add(json);
            Toast.makeText(context, "You now follow " + name, Toast.LENGTH_SHORT).show();
        }
        editor.putStringSet(key, newSet).apply();
    }


}

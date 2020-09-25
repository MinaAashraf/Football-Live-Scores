package com.football.matches.livescores.ui;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceFactory {

    public  SharedPreferences sharedPreferences;
    public static Context context;

    private SharedPreferenceFactory() {
    }

    public  SharedPreferences create(Context myContext) {
        context = myContext;
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("liveScores", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

}

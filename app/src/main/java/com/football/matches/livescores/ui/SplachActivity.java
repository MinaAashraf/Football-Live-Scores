package com.football.matches.livescores.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.football.matches.livescores.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplachActivity extends AppCompatActivity {
    @BindView(R.id.logo_icon)
    ImageView logoIcon;
    @BindView(R.id.logo_txt)
    TextView logoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
        ButterKnife.bind(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkStartPossobilityAndMove();
            }
        }, 4000);
    }

    public void checkStartPossobilityAndMove() {
        SharedPreferences sharedPreferences = getSharedPreferences("liveScores", Context.MODE_PRIVATE);
        boolean dontStartAgain = sharedPreferences.getBoolean("dontStartAgain", false);
        Intent intent;
        if (dontStartAgain) {
            intent = new Intent(SplachActivity.this, MainActivity.class);
            startMainActivity(intent);
        } else {
            intent = new Intent(SplachActivity.this, ChooseTeam.class);
            startActivity(intent);
            finish();
        }
    }

    private void startMainActivity(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplachActivity.this, logoIcon, ViewCompat.getTransitionName(logoIcon));
            startActivity(intent, options.toBundle());
            finishAfterTransition();
        } else {
            startActivity(intent);
            finish();
        }
    }


}
package com.football.matches.livescores.ui;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.football.matches.livescores.R;

public class CheckConnectivity {

   private Activity activity;
   private View originalView, noConnectView;
   private TextView retry;

    public CheckConnectivity(Activity activity, View originalView) {
        this.activity = activity;
        this.originalView = originalView;
        noConnectView = activity.getLayoutInflater().inflate(R.layout.noconnect_layout, null);
        this.retry = noConnectView.findViewById(R.id.retry);
        check();
        retryOnClick();
    }

   private boolean check() {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        setLaoutsVisiblity(connected);
        return connected;
    }

   private void setLaoutsVisiblity(boolean connected) {
        if (connected) {
            originalView.setVisibility(View.VISIBLE);
            noConnectView.setVisibility(View.GONE);
        } else {
            originalView.setVisibility(View.GONE);
            noConnectView.setVisibility(View.VISIBLE);
        }
    }


   private void retryOnClick() {
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLaoutsVisiblity(check());
            }
        });
    }
}

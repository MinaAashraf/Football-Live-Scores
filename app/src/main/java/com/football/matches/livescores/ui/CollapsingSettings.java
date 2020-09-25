package com.football.matches.livescores.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.matches.livescores.R;
import com.google.android.material.appbar.AppBarLayout;

import org.w3c.dom.Text;

public class CollapsingSettings {

    TextView textviewTitle ;
    View titleLinearLaout;

    public CollapsingSettings(TextView textviewTitle,View titleLinearLaout) {
        this.textviewTitle = textviewTitle;
        this.titleLinearLaout = titleLinearLaout;
    }



    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.5f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.5f;
    private static final int ALPHA_ANIMATIONS_DURATION = 100;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    public void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }



    public void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleLinearLaout, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleLinearLaout, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
    public void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

public void onOffsetChangeHander (AppBarLayout appBarLayout , int offset)
{
    int maxScroll = appBarLayout.getTotalScrollRange();
    float percentage = (float) Math.abs(offset) / (float) maxScroll;

    handleAlphaOnTitle(percentage);
    handleToolbarTitleVisibility(percentage);
}



}

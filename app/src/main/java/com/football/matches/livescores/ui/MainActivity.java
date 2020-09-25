package com.football.matches.livescores.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.football.matches.livescores.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    Fragment homeFragment = new HomeFragment();
    Fragment gamesFragment = new LiveScoresFragment();
    Fragment followingFragment = new FollowingFragment();
    FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNav;

    TextView title;
    View view;
    private static final String CURRENT_ITEM_KEY = "current_item";
    int current_item = R.id.home_icon;
    @BindView(R.id.parent)
    ConstraintLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNav.setItemIconTintList(null);

      /*  FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();*/

        if (savedInstanceState == null) {
            fm.beginTransaction().add(R.id.fragment, followingFragment, "following").hide(followingFragment).commit();
            fm.beginTransaction().add(R.id.fragment, gamesFragment, "games").hide(gamesFragment).commit();
            fm.beginTransaction().add(R.id.fragment, homeFragment, "home").commit();
        }

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                String current = "home";
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (id) {
                    case R.id.home_icon:
                        current = "home";
                        //  fragmentManager.beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        return true;
                    case R.id.livescores_icon:
                        current = "Games";
                        // fragmentManager.beginTransaction().replace(R.id.fragment, new LiveScoresFragment()).commit();
                        fm.beginTransaction().hide(active).show(gamesFragment).commit();
                        active = gamesFragment;
                        return true;
                    case R.id.following:
                        current = "Following";
                        //fragmentManager.beginTransaction().replace(R.id.fragment, new FollowingFragment()).commit();
                        fm.beginTransaction().hide(active).show(followingFragment).commit();
                        active = followingFragment;
                        return true;
                }

                return false;
            }
        });

        bottomNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {

            }
        });

    }

    public void setActionBarTitleStyle(String txt) {
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        view = inflator.inflate(R.layout.actionbar_title_style, null);
        title = view.findViewById(R.id.title);
        title.setText(txt);
        getSupportActionBar().setCustomView(view);
    }


}
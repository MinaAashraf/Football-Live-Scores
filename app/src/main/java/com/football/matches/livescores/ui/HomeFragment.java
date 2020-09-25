package com.football.matches.livescores.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.football.matches.livescores.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    @BindView(R.id.tabs_layout)
    TabLayout tabsLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);

        MainActivity mainActivity = ((MainActivity) getActivity());
        mainActivity.setSupportActionBar(toolbar);
        tabsLayout.setTabTextColors(Color.parseColor("#FF9E9E9E"), Color.parseColor("#000000"));
        PagerAdapter pagerAdapter = new TabsAdapter(getChildFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        tabsLayout.setupWithViewPager(viewpager);

        return view;
    }

    public class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HomeNewsFragment();
                    break;
                case 1:
                    fragment = new VideosFragment();
                    break;
                case 2:
                    fragment = new TransfersFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "News";
                case 1:
                    return "Videos";
                case 2:
                    return "Transfers";
                default:
                    return null;
            }
        }
    }

}

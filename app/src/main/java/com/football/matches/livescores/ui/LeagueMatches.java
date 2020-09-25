package com.football.matches.livescores.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.Goals;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeagueMatches extends Fragment {
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    MyViewModel myViewModel;
    HashMap<String, List<FixtureResposeObj>> datedKeyMap = new HashMap<>();
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.league_matches, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        int leagueId = ((LeagueActivity) getActivity()).leagueId;
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        observeLeagueFixtures(leagueId);

        return view;
    }

    private void observeLeagueFixtures(int leagueId) {
        myViewModel.getLeagueFixtures(leagueId).observe(getActivity(), new Observer<List<FixtureResposeObj>>() {
            @Override
            public void onChanged(List<FixtureResposeObj> fixtureResposeObjs) {
                extractMapItems(fixtureResposeObjs);
            }
        });
    }


    public void extractMapItems(List<FixtureResposeObj> fixtureResposeObjs) {
        for (FixtureResposeObj fixtureObj : fixtureResposeObjs) {
            String date = fixtureObj.getFixture().getDate();
            if (datedKeyMap.containsKey(date)) {
                List<FixtureResposeObj> fixtures = datedKeyMap.get(date);
                boolean exist = false;
                for (int i = 0; i < fixtures.size(); i++) {
                    if (fixtures.get(i).getFixture().getId() == fixtureObj.getFixture().getId()) {
                        exist = true;
                        break;
                    }
                }
                if (exist)
                    continue;
            }
            if (datedKeyMap.containsKey(date)) {
                List<FixtureResposeObj> fixtureResposeObjList = datedKeyMap.get(date);
                fixtureResposeObjList.add(fixtureObj);
                datedKeyMap.put(date, fixtureResposeObjList);

            } else {
                List<FixtureResposeObj> newList = new ArrayList<>();
                newList.add(fixtureObj);
                datedKeyMap.put(date, newList);
            }


        }
        createViews();
    }

    private void createViews() {
        List<String> keyList = new ArrayList<>(datedKeyMap.keySet());
        sortByDate(keyList);
        for (String key : keyList) {
            LinearLayout myLayout = new LinearLayout(getActivity());
            myLayout.removeAllViews();
            myLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            myLayout.setBackgroundColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myLayout.setElevation(1f);
            }
            params.setMargins(0, (int) (10 * getContext().getResources().getDisplayMetrics().density), 0, (int) (10 * getContext().getResources().getDisplayMetrics().density));
            myLayout.setLayoutParams(params);
            View dateView = getLayoutInflater().inflate(R.layout.dateview, null);
            TextView myDate = dateView.findViewById(R.id.date);
            String finalDate = TimeFormat.dateFormat(key)[0];
            myDate.setText(TimeFormat.formatToYesterdayOrToday(finalDate));
            myLayout.addView(dateView);
            for (FixtureResposeObj fixtureResposeObj : datedKeyMap.get(key)) {
                View view1 = setFixtureView(fixtureResposeObj);
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson = new Gson();
                        String myFixture = gson.toJson(fixtureResposeObj);
                        getActivity().startActivity(new Intent(getActivity(), MatchActivity.class).putExtra("fixture", myFixture));
                    }
                });
                myLayout.addView(view1);
            }
            linearlayout.addView(myLayout);
            if (progressbar.getVisibility() == View.VISIBLE) {
                progressbar.setVisibility(View.GONE);
                linearlayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public View setFixtureView(FixtureResposeObj fixture) {
        View view1 = getLayoutInflater().inflate(R.layout.match_item2, null);
        TextView frTeamName, secondTeamName, golasValue;
        ImageView frTeamImage, secondTeamImage;
        golasValue = view1.findViewById(R.id.goals_value);
        frTeamName = view1.findViewById(R.id.fr_teamName);
        frTeamImage = view1.findViewById(R.id.fr_teamImage);
        secondTeamName = view1.findViewById(R.id.second_teamName);
        secondTeamImage = view1.findViewById(R.id.second_teamImage);

        String dateArr[] = TimeFormat.dateFormat(fixture.getFixture().getDate());
        Picasso.get().load(fixture.getTeams().getHome().getLogo()).into(frTeamImage);
        Picasso.get().load(fixture.getTeams().getAway().getLogo()).into(secondTeamImage);
        frTeamName.setText(fixture.getTeams().getHome().getName());
        secondTeamName.setText(fixture.getTeams().getAway().getName());
        Goals goals = fixture.getGoals();
        String status = fixture.getFixture().getStatus().getShortt();
        switch (status) {
            case "FT":
            case "AET":
                golasValue.setText(goals.getHome() + " - " + goals.getAway());
                break;
            case "NS":
                golasValue.setText(dateArr[1]);
                break;
            default:
                golasValue.setText(status);

        }
        return view1;
    }

    public void sortByDate(List<String> datestring) {
        Collections.sort(datestring, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }


}

package com.football.matches.livescores.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.ArticlesAdapter;
import com.football.matches.livescores.Adapters.TeamNewsAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.Article;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.GenralTeamNews;
import com.football.matches.livescores.pojo.PredictionPercent;
import com.football.matches.livescores.pojo.PredictionsResponceObj;
import com.football.matches.livescores.pojo.Team;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamNewsFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    TeamNewsAdapter teamNewsAdapter;
    MyViewModel myViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_news_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        Team team = TeamActivity.team;

        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        teamNewsAdapter = new TeamNewsAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(teamNewsAdapter);

        myViewModel.getTeamNextFixture(team.getId()).observe(getActivity(), new Observer<List<FixtureResposeObj>>() {
            @Override
            public void onChanged(List<FixtureResposeObj> fixtureResposeObjs) {
                FixtureResposeObj fixtureResposeObj = fixtureResposeObjs.get(0);
                int fixtureId = fixtureResposeObj.getFixture().getId();
                getPredictions(fixtureId, fixtureResposeObj);
            }
        });
        getArticles(team.getName());
        return view;
    }

    private void getPredictions(int fixtureNumber, FixtureResposeObj fixtureResposeObj) {
        myViewModel.getPredictions(fixtureNumber).observe(getActivity(), new Observer<List<PredictionsResponceObj>>() {
            @Override
            public void onChanged(List<PredictionsResponceObj> predictionsResponceObjs) {
                PredictionPercent percent = predictionsResponceObjs.get(0).getPredictions().getPercent();
                genralTeamNewsList.add(0, new NextMatchWithPredict(fixtureResposeObj, percent));
                teamNewsAdapter.setData(genralTeamNewsList);
            }
        });
    }

    List<GenralTeamNews> genralTeamNewsList = new ArrayList<>();

    private void getArticles(String searchTitle) {
        myViewModel.getArticlesByTitle(searchTitle, 100).observe(getActivity(), new Observer<List<List<Article>>>() {
            @Override
            public void onChanged(List<List<Article>> lists) {
                genralTeamNewsList.addAll(lists.get(0));
                teamNewsAdapter.setData(genralTeamNewsList);
            }
        });

    }
}
package com.football.matches.livescores.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.ArticlesAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeagueNewsFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArticlesAdapter articlesAdapter;
    MyViewModel myViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_news_fragment, container, false);
        ButterKnife.bind(this,view);
        new CheckConnectivity(getActivity(),view.findViewById(R.id.parent));
        articlesAdapter = new ArticlesAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(articlesAdapter);
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        getArticles(((LeagueActivity) getActivity()).leagueName.getText().toString());

        return view;
    }

    private void getArticles(String searchTitle) {
        myViewModel.getArticlesByTitle(searchTitle, 100).observe(getActivity(), new Observer<List<List<Article>>>() {
            @Override
            public void onChanged(List<List<Article>> lists) {
                articlesAdapter.setData(lists.get(0));
            }
        });
    }
}
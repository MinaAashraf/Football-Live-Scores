package com.football.matches.livescores.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.HomeArticlesAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.AdapterWithSearchedTitle;
import com.football.matches.livescores.pojo.Article;
import com.football.matches.livescores.pojo.LeagueResponceObject;
import com.football.matches.livescores.pojo.PlayerData;
import com.football.matches.livescores.pojo.Team;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeNewsFragment extends Fragment {
    @BindView(R.id.topnews_recycler)
    RecyclerView topnewsRecycler;
    HomeArticlesAdapter topNews_adapter;
    MyViewModel myViewModel;
    Set<String> teamsSet, compSet, playerSet;
    @BindView(R.id.parentLayout)
    LinearLayout parentLayout;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_news_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
       /* MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });*/

        topNews_adapter = new HomeArticlesAdapter(getActivity());
        topnewsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        topnewsRecycler.setAdapter(topNews_adapter);
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        String[] arr = {"Premier League", "la liga"};
        getArticles(arr, topNews_adapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("liveScores", Context.MODE_PRIVATE);
        teamsSet = sharedPreferences.getStringSet(getResources().getString(R.string.followingTeamsObjs), new HashSet<>());
        compSet = sharedPreferences.getStringSet(getResources().getString(R.string.followingLeagueObjs), new HashSet<>());
        playerSet = sharedPreferences.getStringSet(getResources().getString(R.string.followingPlayerObjs), new HashSet<>());
        myViewModel2 = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        getObjListFromJsonSet("team");
        getObjListFromJsonSet("comp");
        getObjListFromJsonSet("player");
        return view;
    }

    private void getArticles(String[] searchTopic, HomeArticlesAdapter adapter) {
        myViewModel.getArticles(searchTopic, 10).observe(getActivity(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                adapter.setData(articles);
                progressbar.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    static List<Team> folowingteamsList = new ArrayList<>();
    List<LeagueResponceObject> folowingcompList = new ArrayList<>();
    List<PlayerData> folowingPlayersList = new ArrayList<>();

    public void getObjListFromJsonSet(String objType) {
        Gson gson = new Gson();
        switch (objType) {
            case "team":
                folowingteamsList.clear();
                for (String j : teamsSet)
                    folowingteamsList.add(gson.fromJson(j, Team.class));
                getTeamsNews();
                break;
            case "comp":
                folowingcompList.clear();
                for (String j : compSet)
                    folowingcompList.add(gson.fromJson(j, LeagueResponceObject.class));
                getLeaguesNews();
                break;
            case "player":
                folowingPlayersList.clear();
                for (String j : playerSet)
                    folowingPlayersList.add(gson.fromJson(j, PlayerData.class));
                getPlayerNews();
                break;
        }
    }

    public void getTeamsNews() {
        for (Team team : folowingteamsList) {
            createView(team);
        }
    }

    public void getLeaguesNews() {
        for (LeagueResponceObject leagueResponceObject : folowingcompList)
            createView(leagueResponceObject);
    }

    public void getPlayerNews() {
        for (PlayerData playerData : folowingPlayersList)
            createView(playerData);
    }

    MyViewModel myViewModel2;

    private void getArticlesByTitle(String searchTitles) {
        myViewModel.getArticlesByTitle(searchTitles, 10).observe(getActivity(), new Observer<List<List<Article>>>() {
            @Override
            public void onChanged(List<List<Article>> lists) {
               if (lists.size() == folowingteamsList.size() + folowingcompList.size() + folowingPlayersList.size()) {
                    for (AdapterWithSearchedTitle adapterWithSearchedTitle : adaptersWithSearchedTiltes) {
                        String title = adapterWithSearchedTitle.getTitle().toLowerCase();
                        for (List<Article> list : lists) {
                            boolean titleMatching = true;
                            for (Article article : list) {
                                if (!article.getTitle().toLowerCase().contains(title)) {
                                    titleMatching = false;
                                    break;
                                }
                            }
                            if (titleMatching) {
                                adapterWithSearchedTitle.getAdapter().setData(list);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
    List<AdapterWithSearchedTitle> adaptersWithSearchedTiltes = new ArrayList<>();

    private void createView(Object object) {
        ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.news_item_runtime, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, (int) (10 * getContext().getResources().getDisplayMetrics().density), 0, (int) (10 * getContext().getResources().getDisplayMetrics().density));
        view.setLayoutParams(params);
        ImageView logo = view.findViewById(R.id.logo);
        TextView name = view.findViewById(R.id.name);
        RecyclerView recycler = view.findViewById(R.id.news_recycler);
        TextView seeAll = view.findViewById(R.id.seeAll);
        // TemplateView templateView = view.findViewById(R.id.my_template);
        // loadNativeAd(templateView);

        if (object instanceof Team) {
            Team team = (Team) object;
            Picasso.get().load(team.getLogo()).into(logo);
            name.setText(team.getName());
            HomeArticlesAdapter adapter = new HomeArticlesAdapter(getActivity());
            adaptersWithSearchedTiltes.add(new AdapterWithSearchedTitle(adapter, team.getName()));
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler.setAdapter(adapter);
            getArticlesByTitle(team.getName());
            seeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    startActivity(new Intent(getActivity(), TeamActivity.class).putExtra("team", gson.toJson(team)).putExtra("seeAllNews", true));
                }
            });
        }
        if (object instanceof LeagueResponceObject) {
            LeagueResponceObject leagueResponceObject = (LeagueResponceObject) object;
            Picasso.get().load(leagueResponceObject.getLeague().getLogo()).into(logo);
            name.setText(leagueResponceObject.getLeague().getName());
            HomeArticlesAdapter adapter = new HomeArticlesAdapter(getActivity());
            adaptersWithSearchedTiltes.add(new AdapterWithSearchedTitle(adapter, leagueResponceObject.getLeague().getName()));
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler.setAdapter(adapter);
           getArticlesByTitle(leagueResponceObject.getLeague().getName());
            seeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    startActivity(new Intent(getActivity(), LeagueActivity.class).putExtra("league", gson.toJson(leagueResponceObject)).putExtra("seeAllNews", true));
                }
            });
        }
        if (object instanceof PlayerData) {
            PlayerData playerData = (PlayerData) object;
            Picasso.get().load(playerData.getStrThumb()).into(logo);
            name.setText(playerData.getStrPlayer());
            HomeArticlesAdapter adapter = new HomeArticlesAdapter(getActivity());
            adaptersWithSearchedTiltes.add(new AdapterWithSearchedTitle(adapter, playerData.getStrPlayer()));
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler.setAdapter(adapter);
            getArticlesByTitle(playerData.getStrPlayer());
            seeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    startActivity(new Intent(getActivity(), PlayerActivity.class).putExtra("player", gson.toJson(playerData)).putExtra("seeAllNews", true));
                }
            });
        }
        parentLayout.addView(view);

    }

    public void loadNativeAd(TemplateView templateView) {
        AdLoader adLoader = new AdLoader.Builder(getActivity(), getResources().getString(R.string.ad_unit_id)).
                forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        //  templateView.setVisibility(View.VISIBLE);
                        //templateView.setNativeAd(unifiedNativeAd);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }

}

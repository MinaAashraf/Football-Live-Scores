package com.football.matches.livescores.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.MatchesAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.League;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class TeamMatchesFragment extends Fragment {
    List<Object> fixtures = new ArrayList<>();
    MatchesAdapter matchesAdapter;
    RecyclerView recyclerView;
    static MyViewModel myViewModel;
    Spinner leagueSpinner;
    ArrayAdapter spinnerAdapter;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_matches_fragment, container, false);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        leagueSpinner = view.findViewById(R.id.leagues_spinner);
        matchesAdapter = new MatchesAdapter(fixtures, getActivity());
        recyclerView = view.findViewById(R.id.matches_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(matchesAdapter);

        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        spinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ((TeamActivity) getActivity()).leaguesNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagueSpinner.setAdapter(spinnerAdapter);

        leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                fixtures.clear();
                League league = ((TeamActivity) getActivity()).leaguesObjects.get(position).getLeague();
                myViewModel.getTeamFixures(league.getId(), TeamActivity.team.getId()).observe(getActivity(), new Observer<List<FixtureResposeObj>>() {
                    @Override
                    public void onChanged(List<FixtureResposeObj> fixtureResposeObjs) {
                        fixtures.addAll(fixtureResposeObjs);
                        matchesAdapter.notifyDataSetChanged();
                       /* for (int i = ITEMS_PER_AD - 1; i < fixtures.size(); i += ITEMS_PER_AD) {
                            TemplateView templateView = new TemplateView(getActivity());
                            fixtures.add(i, templateView);
                        }
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                loadNativeAd(ITEMS_PER_AD - 1);
                            }
                        });*/
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    public void loadNativeAd(int index) {
        if (index >= fixtures.size())
            return;

        AdLoader adLoader = new AdLoader.Builder(getActivity(), getResources().getString(R.string.ad_unit_id)).
                forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        ((TemplateView) fixtures.get(index)).setNativeAd(unifiedNativeAd);
                        loadNativeAd(index + ITEMS_PER_AD);
                        matchesAdapter.notifyDataSetChanged();
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        loadNativeAd(index + ITEMS_PER_AD);
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }

    public final static int ITEMS_PER_AD = 8;
}

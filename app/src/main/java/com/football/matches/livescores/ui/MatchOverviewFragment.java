package com.football.matches.livescores.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.ScorerAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.EventResponceObj;
import com.football.matches.livescores.pojo.FixtureResposeObj;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchOverviewFragment extends Fragment {
    @BindView(R.id.staduim_name)
    TextView staduimName;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.referee_name)
    TextView refereeName;
    @BindView(R.id.scorerRecycle)
    RecyclerView scorerRecycle;
    @BindView(R.id.scorer_title)
    TextView scorerTitle;
    @BindView(R.id.referee_title)
    TextView refereeTitle;
    @BindView(R.id.referee_layout)
    ConstraintLayout refereeLayout;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.match_overiew_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        final ScorerAdapter scorerAdapter = new ScorerAdapter();
        scorerRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        scorerRecycle.setAdapter(scorerAdapter);
        FixtureResposeObj fixture = ((MatchActivity) getActivity()).fixture;
        MyViewModel myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getScorersEvents(fixture.getFixture().getId()).observe(getActivity(), new Observer<List<EventResponceObj>>() {
            @Override
            public void onChanged(List<EventResponceObj> eventResponceObjs) {
                if (eventResponceObjs.size() != 0) {
                    scorerAdapter.setData(eventResponceObjs);
                    scorerTitle.setVisibility(View.VISIBLE);
                    scorerRecycle.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                    linearlayout.setVisibility(View.VISIBLE);

                } else {
                    scorerTitle.setVisibility(View.GONE);
                    scorerRecycle.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    linearlayout.setVisibility(View.VISIBLE);
                }
            }
        });


        staduimName.setText(fixture.getFixture().getVenue().getName());
        city.setText(fixture.getFixture().getVenue().getCity());
        String referee = fixture.getFixture().getReferee();

        if (referee == null) {
            refereeTitle.setVisibility(View.GONE);
            refereeLayout.setVisibility(View.GONE);
        } else
            refereeName.setText(referee);

        return view;
    }
}

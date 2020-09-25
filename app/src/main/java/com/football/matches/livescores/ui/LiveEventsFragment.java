package com.football.matches.livescores.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.football.matches.livescores.Adapters.EventsAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.EventResponceObj;
import com.football.matches.livescores.pojo.FixtureResposeObj;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveEventsFragment extends Fragment {
    @BindView(R.id.event_Recycler)
    RecyclerView eventRecycler;
    @BindView(R.id.too_early_layout)
    LinearLayout tooEarlyLayout;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_events_fragment, container, false);
        ButterKnife.bind(this, view);
        MatchActivity myActivity = ((MatchActivity) getActivity());
        new CheckConnectivity(myActivity, view.findViewById(R.id.parent));
        FixtureResposeObj fixture = myActivity.fixture;
        final EventsAdapter eventsAdapter = new EventsAdapter(myActivity);
        eventRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventRecycler.setAdapter(eventsAdapter);
        MyViewModel myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getEvents(fixture.getFixture().getId()).observe(getActivity(), new Observer<List<EventResponceObj>>() {
            @Override
            public void onChanged(List<EventResponceObj> eventResponceObjs) {
                if (eventResponceObjs.size() != 0) {
                    eventsAdapter.setData(eventResponceObjs);
                    tooEarlyLayout.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    eventRecycler.setVisibility(View.VISIBLE);
                } else {
                    eventRecycler.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    tooEarlyLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        return view;
    }
}

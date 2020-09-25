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

import com.football.matches.livescores.Adapters.VideoAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.VideoResponceObj;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videos_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(),view.findViewById(R.id.parent));
        VideoAdapter videoAdapter = new VideoAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(videoAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(5);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        MyViewModel myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getVideos().observe(getActivity(), new Observer<List<VideoResponceObj>>() {
            @Override
            public void onChanged(List<VideoResponceObj> videoResponceObjs) {
                videoAdapter.setData(videoResponceObjs);
            }
        });
        return view;
    }
}

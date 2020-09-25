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

import com.football.matches.livescores.Adapters.TransferAdapter;
import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.TransferResponceObj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamTransfers extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transfers_fragment, container, false);
        ButterKnife.bind(this, view);
        int id = TeamActivity.team.getId();
        TransferAdapter transferAdapter = new TransferAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(transferAdapter);

        MyViewModel myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getTranfers(id).observe(getActivity(), new Observer<List<List<TransferResponceObj>>>() {
            @Override
            public void onChanged(List<List<TransferResponceObj>> transferResponceObjs) {
                transferAdapter.setData(transferResponceObjs.get(0));
            }
        });
        return view;
    }
}

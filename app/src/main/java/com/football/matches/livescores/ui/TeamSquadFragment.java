package com.football.matches.livescores.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.football.matches.livescores.pojo.PlayerStatisticsObj;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamSquadFragment extends Fragment {

    MyViewModel myViewModel;
    @BindView(R.id.goalKeeper_layout)
    LinearLayout goalKeeperLayout;
    @BindView(R.id.defence_layout)
    LinearLayout defenceLayout;
    @BindView(R.id.midField_layout)
    LinearLayout midFieldLayout;
    @BindView(R.id.attack_layout)
    LinearLayout attackLayout;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_squad_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        getSquadPlayers(TeamActivity.team.getId());
        return view;
    }

    List<PlayerStatisticsObj> goalKeepers_statObjs = new ArrayList<>();
    List<PlayerStatisticsObj> defense_statObjs = new ArrayList<>();
    List<PlayerStatisticsObj> mid_statObjs = new ArrayList<>();
    List<PlayerStatisticsObj> attack_statObjs = new ArrayList<>();

    public void getSquadPlayers(int teamId) {
        myViewModel.getSquadPlayers(teamId).observe(getActivity(), new Observer<List<PlayerStatisticsObj>>() {
            @Override
            public void onChanged(List<PlayerStatisticsObj> playerStatisticsObjs) {
                if (playerStatisticsObjs != null) {
                    for (PlayerStatisticsObj obj : playerStatisticsObjs) {
                        switch (obj.getStatistics().get(0).getGames().getPosition()) {
                            case "Goalkeeper":
                                goalKeepers_statObjs.add(obj);
                                break;
                            case "Defender":
                                defense_statObjs.add(obj);
                                break;
                            case "Midfielder":
                                mid_statObjs.add(obj);
                                break;
                            case "Attacker":
                                attack_statObjs.add(obj);
                                break;
                        }
                    }
                    setplayersLayouts(goalKeeperLayout, goalKeepers_statObjs);
                    setplayersLayouts(defenceLayout, defense_statObjs);
                    setplayersLayouts(midFieldLayout, mid_statObjs);
                    setplayersLayouts(attackLayout, attack_statObjs);
                }
            }

            private void setplayersLayouts(LinearLayout parentLayout, List<PlayerStatisticsObj> playerStatisticsObj) {

                for (PlayerStatisticsObj obj : playerStatisticsObj) {
                    View view = getLayoutInflater().inflate(R.layout.squadplayer_item, null);
                    ImageView playerImage = view.findViewById(R.id.player_image);
                    TextView playerName = view.findViewById(R.id.player_name);
                    TextView playerCountry = view.findViewById(R.id.player_country);
                    Picasso.get().load(obj.getPlayer().getPhoto()).placeholder(R.drawable.player_tshirt).into(playerImage);
                    playerName.setText(obj.getPlayer().getName());
                    playerCountry.setText(obj.getPlayer().getNationality());
                    parentLayout.addView(view);
                    progressbar.setVisibility(View.GONE);
                    linearlayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}

package com.football.matches.livescores.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.PlayerData;
import com.football.matches.livescores.pojo.TeamFromAnotherApi;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerInfoFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.team_image)
    ImageView teamImage;
    @BindView(R.id.team_name)
    TextView teamName;
    @BindView(R.id.team_image2)
    ImageView teamImage2;
    @BindView(R.id.team_name2)
    TextView teamName2;
    @BindView(R.id.player_number)
    TextView playerNumber;
    @BindView(R.id.player_age)
    TextView playerAge;
    @BindView(R.id.player_country)
    TextView playerCountry;
    @BindView(R.id.player_height)
    TextView playerHeight;
    @BindView(R.id.player_weight)
    TextView playerWeight;
    MyViewModel myViewModel;
    PlayerData playerData;
    @BindView(R.id.facebook)
    ImageView facebook;
    @BindView(R.id.twitter)
    ImageView twitter;
    @BindView(R.id.instgram)
    ImageView instgram;
    @BindView(R.id.contacts)
    ConstraintLayout contacts;
    @BindView(R.id.cons1)
    ConstraintLayout cons1;
    @BindView(R.id.cons2)
    ConstraintLayout cons2;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.info_title)
    TextView infoTitle;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.contacts_title)
    TextView contactsTitle;
    @BindView(R.id.view4)
    View view4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playerinfo_fragment, container, false);
        ButterKnife.bind(this, view);
        new CheckConnectivity(getActivity(), view.findViewById(R.id.parent));
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        getPlayer(((PlayerActivity) getActivity()).playerData.getStrPlayer());
        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instgram.setOnClickListener(this);

        return view;
    }


    private void getPlayer(String newText) {
        myViewModel.getSearchedPlayers(newText).observe(this, new Observer<List<PlayerData>>() {
            @Override
            public void onChanged(List<PlayerData> playerDataList) {
                playerData = playerDataList.get(0);
                teamName.setText(playerData.getStrTeam());
                getTeamImage(teamName.getText().toString(), teamImage);
                if (playerData.getStrTeam2() != null) {
                    teamName2.setText(playerData.getStrTeam2());
                    getTeamImage(teamName2.getText().toString(), teamImage2);
                } else {
                    teamName2.setVisibility(View.GONE);
                    teamImage2.setVisibility(View.GONE);
                }

                playerNumber.setText(playerData.getStrNumber());
                playerCountry.setText(playerData.getStrNationality());
                String heightString = playerData.getStrHeight();
                try {
                    float height = Float.valueOf(heightString.substring(0, heightString.indexOf("m"))) * 100;
                    playerHeight.setText(height + "cm");
                } catch (Exception e) {
                    playerHeight.setText(heightString);
                }

                playerWeight.setText(playerData.getStrWeight());
                int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.valueOf(playerData.getDateBorn().substring(0, 4));
                playerAge.setText(age + "years");
                if (playerData.getStrFacebook() == null && playerData.getStrTwitter() == null && playerData.getStrInstagram() == null)
                    contacts.setVisibility(View.GONE);
                {
                    if (playerData.getStrFacebook() == null)
                        facebook.setVisibility(View.GONE);
                    if (playerData.getStrTwitter() == null)
                        twitter.setVisibility(View.GONE);
                    if (playerData.getStrInstagram() == null)
                        instgram.setVisibility(View.GONE);
                }
                setAvailability();
            }
        });


    }

    public void getTeamImage(String teamname, ImageView imageView) {
        myViewModel.getTeamImageFromAnotherApi(teamname).observe(getActivity(), new Observer<List<TeamFromAnotherApi>>() {
            @Override
            public void onChanged(List<TeamFromAnotherApi> strings) {
                String team1 = teamName.getText().toString();
                String team2 = teamName2.getText().toString();
                if (strings.size() == 2) {
                    for (TeamFromAnotherApi team : strings) {
                        if (team.getStrTeam().equals(team1))
                            Picasso.get().load(team.getStrTeamBadge()).placeholder(R.drawable.team_icon).into(teamImage);
                        else
                            Picasso.get().load(team.getStrTeamBadge()).placeholder(R.drawable.team_icon).into(teamImage2);
                    }
                } else if (team2.isEmpty() && strings.size() == 1)
                    Picasso.get().load(strings.get(0).getStrTeamBadge()).placeholder(R.drawable.team_icon).into(teamImage);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebook:
                openWebView(playerData.getStrFacebook());
                break;
            case R.id.twitter:
                openWebView(playerData.getStrTwitter());
                break;
            case R.id.instgram:
                openWebView(playerData.getStrInstagram());
                break;
        }

    }

    public void openWebView(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + url));
        startActivity(browserIntent);
    }

    public void setAvailability() {
        progressbar.setVisibility(View.GONE);
        cons1.setVisibility(View.VISIBLE);
        cons2.setVisibility(View.VISIBLE);
        contacts.setVisibility(View.VISIBLE);
    }
}
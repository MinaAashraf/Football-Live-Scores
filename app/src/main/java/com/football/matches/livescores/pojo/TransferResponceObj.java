package com.football.matches.livescores.pojo;

import java.util.List;

public class TransferResponceObj {
    TransferPlayer player ;
    String update ;
    List <Transfer> transfers;

    public TransferPlayer getPlayer() {
        return player;
    }

    public String getUpdate() {
        return update;
    }

    public Transfer getTransfer() {
        return transfers.get(0);
    }
}

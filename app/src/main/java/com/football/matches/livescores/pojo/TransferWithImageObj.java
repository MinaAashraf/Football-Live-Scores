package com.football.matches.livescores.pojo;

public class TransferWithImageObj {
    TransferResponceObj transferResponceObj ;
    String player_image ;

    public TransferWithImageObj(TransferResponceObj transferResponceObj, String player_image) {
        this.transferResponceObj = transferResponceObj;
        this.player_image = player_image;
    }

    public TransferResponceObj getTransferResponceObj() {
        return transferResponceObj;
    }

    public String getPlayer_image() {
        return player_image;
    }

}

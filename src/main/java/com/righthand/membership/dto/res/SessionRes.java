package com.righthand.membership.dto.res;

import lombok.Data;

@Data
public class SessionRes {
    private int userSeq;
    private int authorityLevel;
    private String nickName;
    private double rhCoin;
    private double rewardPower;
}


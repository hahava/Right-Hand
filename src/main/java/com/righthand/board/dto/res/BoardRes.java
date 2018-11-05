package com.righthand.board.dto.res;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BoardRes {
    private String nickname;
    private String title;
    private String content;
    private Timestamp timestamp;
}

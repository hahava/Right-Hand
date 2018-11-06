package com.righthand.board.dto.model;

import lombok.Data;

@Data
public class BoardSearchVO {
    int start;
    int offset;
    String searchedWord;
}


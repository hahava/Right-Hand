package com.righthand.board.domain.boards;

import com.righthand.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "TB_DEVELOP_STORY_BOARD")
public class DevBoard extends BaseTimeEntity {

    @Id
    @Column(name = "BOARD_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardSeq;

    @Column(name = "BOARD_TITLE", nullable = false)
    private String boardTitle;

    @Column(columnDefinition = "TEXT", name = "BOARD_CONTENT", nullable = false)
    private String boardContent;

    @Lob
    @Column(name = "BOARD_CONTENT4SEARCHING")
    private String boardContent4Searching;

    @Column(name = "BOARD_PROFILE_SEQ")
    private Long boardProfileSeq;

    @Builder
    public DevBoard(String boardTitle, String boardContent, String boardContent4Searching,
                    Long boardProfileSeq) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardContent4Searching = boardContent4Searching;
        this.boardProfileSeq = boardProfileSeq;
    }
}

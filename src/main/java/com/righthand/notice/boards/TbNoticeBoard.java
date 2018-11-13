package com.righthand.notice.boards;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TB_NOTICE_BOARD")
public class TbNoticeBoard extends BaseTimeEntity{

    @Id
    @Column(name = "BOARD_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardSeq;

    @Column(length = 50, name = "BOARD_TITLE")
    @NotNull
    private String boardTitle;

    @Column(columnDefinition = "TEXT", name = "BOARD_CONTENT")
    @NotNull
    private String boardContent;

    @Builder
    public TbNoticeBoard(String boardTitle, String boardContent){
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }

}

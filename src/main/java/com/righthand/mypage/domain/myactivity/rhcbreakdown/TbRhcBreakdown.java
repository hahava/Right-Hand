package com.righthand.mypage.domain.myactivity.rhcbreakdown;

import com.righthand.mypage.domain.myactivity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TB_RHC_BREAKDOWN")
public class TbRhcBreakdown extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQUENCE")
    private Long sequence;

    @Column(name = "ACTIVITY_TYPE")
    private String activityType;

    @Column(name = "RH_COIN")
    private double rhCoin;

    @Column(name = "CONTENT")
    private String content;

    @Column(name="RHC_PROFILE_SEQ")
    private Long rhcProfileSeq;

    @Column(name = "BOARD_TYPE")
    private String boardType;

    @Column(name = "BOARD_SEQ")
    private Long boardSeq;

    @Column(name = "IS_SENDER")
    private boolean isSender;

    @Builder
    public TbRhcBreakdown(String activityType, double rhCoin, String content, Long rhcProfileSeq, String boardType, Long boardSeq, boolean isSender) {
        this.activityType = activityType;
        this.rhCoin = rhCoin;
        this.content = content;
        this.rhcProfileSeq = rhcProfileSeq;
        this.boardType = boardType;
        this.boardSeq = boardSeq;
        this.isSender = isSender;
    }
}

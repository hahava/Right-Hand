package com.righthand.mypage.domain.myactivity.rhpbreakdown;

import com.righthand.mypage.domain.myactivity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TB_RHP_BREAKDOWN")
public class TbRhpBreakdown extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQUENCE")
    private Long sequence;

    @Column(name = "ACTIVITY_TYPE")
    private String activityType;

    @Column(name = "RH_POWER")
    private Long rhPower;

    @Column(name = "CONTENT")
    private String content;

    @Column(name="RHP_PROFILE_SEQ")
    private Long rhpProfileSeq;

    @Column(name = "BOARD_TYPE")
    private String boardType;

    @Column(name = "BOARD_SEQ")
    private Long boardSeq;

    @Builder
    public TbRhpBreakdown(String activityType, Long rhPower, String content, Long rhpProfileSeq,
                          String boardType, Long boardSeq) {
        this.activityType = activityType;
        this.rhPower = rhPower;
        this.content = content;
        this.rhpProfileSeq = rhpProfileSeq;
        this.boardType = boardType;
        this. boardSeq = boardSeq;
    }
}

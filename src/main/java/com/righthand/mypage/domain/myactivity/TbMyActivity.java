package com.righthand.mypage.domain.myactivity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TB_MY_ACTIVITY")
public class TbMyActivity extends BaseTimeEntity{

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

    @Column(name="ACTIVITY_PROFILE_SEQ")
    private Long activityProfileSeq;

    @Column(name = "BOARD_TYPE")
    private String boardType;

    @Column(name = "BOARD_SEQ")
    private Long boardSeq;

    @Builder
    public TbMyActivity(String activityType, Long rhPower, String content, Long activityProfileSeq,
        String boardType, Long boardSeq) {
        this.activityType = activityType;
        this.rhPower = rhPower;
        this.content = content;
        this.activityProfileSeq = activityProfileSeq;
        this.boardType = boardType;
        this. boardSeq = boardSeq;
    }
}

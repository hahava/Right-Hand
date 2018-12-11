package com.righthand.mypage.domain.profile;

import com.righthand.mypage.domain.file.TbFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TB_PROFILE")
public class TbProfile {

    @Id
    @Column(name = "PROFILE_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileSeq;

    @Column(name = "USER_SEQ")
    private Long userSeq;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "BIRTH_YEAR")
    private Long birthYear;

    @Column(name = "NICK_NAME")
    private String nickName;

    @ManyToOne
    @JoinColumn(name = "FILE_SEQ", insertable = false, updatable = false)
    private TbFile tbFile;

    @Column(name = "FILE_SEQ")
    private Long fileSeq;

    @Column(name = "RH_COIN")
    private Double rhCoin;

    @Column(name = "REWARD_POWER")
    private Double rewardPower;
}

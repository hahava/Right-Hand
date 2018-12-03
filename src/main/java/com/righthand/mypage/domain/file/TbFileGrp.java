package com.righthand.mypage.domain.file;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity(name = "TB_FILE_GRP")
@Getter
public class TbFileGrp extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileGrpSeq;

}

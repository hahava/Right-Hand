package com.righthand.mypage.domain.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "TB_FILE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TbFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileSeq;
}

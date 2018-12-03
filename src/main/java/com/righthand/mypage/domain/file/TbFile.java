package com.righthand.mypage.domain.file;

import com.righthand.mypage.domain.profile.TbProfile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "TB_FILE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TbFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_SEQ")
    private Long fileSeq;

    @OneToMany(mappedBy = "tbFile", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<TbProfile> profileList = new ArrayList<>();

    @Column(name = "FILE_PATH")
    private String filePath;
}

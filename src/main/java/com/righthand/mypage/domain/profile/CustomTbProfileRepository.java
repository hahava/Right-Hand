package com.righthand.mypage.domain.profile;

import com.righthand.mypage.domain.file.TbFile;

import java.util.List;

public interface CustomTbProfileRepository {
    List<TbFile> findAllJoinProfileAndFile(long profileSeq);
}

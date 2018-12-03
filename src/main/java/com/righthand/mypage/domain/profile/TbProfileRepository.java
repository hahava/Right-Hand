package com.righthand.mypage.domain.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TbProfileRepository extends JpaRepository<TbProfile, Long>, CustomTbProfileRepository {

    TbProfile findByUserSeq(long profileSeq);

    @Modifying
    @Query(value = "UPDATE TB_PROFILE SET NICK_NAME = ?1, TEL = ?2 WHERE PROFILE_SEQ = ?3", nativeQuery = true)
    int updateUserProfile(String nickname, String tel, int profileSeq);
}

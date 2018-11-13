package com.righthand.mypage.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TbProfileRepository extends JpaRepository<TbProfile, Long> {

    @Query(value = "SELECT USER_SEQ, USER_NAME, GENDER, BIRTH_YEAR, TEL, PROFILE_SEQ FROM TB_PROFILE WHERE PROFILE_SEQ = ?1", nativeQuery = true)
    TbProfile findUserSeq(int profileSeq);

    @Modifying
    @Query(value = "UPDATE TB_PROFILE SET t.NICK_NAME = ?1 WHERE PROFILE_SEQ = ?2", nativeQuery = true)
    int updateUserProfile(String nickname, int profileSeq);
}

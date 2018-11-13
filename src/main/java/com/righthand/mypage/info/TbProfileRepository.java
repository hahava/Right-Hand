package com.righthand.mypage.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TbProfileRepository extends JpaRepository<TbProfile, Long> {

//    @Query(value = "SELECT t.USER_SEQ FROM TB_PROFILE t WHERE t.PROFILE_SEQ = ?1", nativeQuery = true)
//    int findUserSeq(int profileSeq);

    @Query(value = "SELECT t.USER_SEQ, t.GENDER, t.USER_NAME, t.BIRTH_YEAR, t.TEL FROM TB_PROFILE t WHERE t.PROFILE_SEQ = ?1", nativeQuery = true)
    List<TbProfile> findUserSeq(int profileSeq);

    @Modifying
    @Query(value = "UPDATE TB_PROFILE SET NICK_NAME = ?1 WHERE PROFILE_SEQ = ?2", nativeQuery = true)
    int updateUserProfile(String nickname, int profileSeq);
}

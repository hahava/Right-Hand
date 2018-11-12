package com.righthand.mypage.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TbProfileRepository extends JpaRepository<TbProfile, Long> {

    @Query(value = "SELECT t.USER_SEQ FROM TB_PROFILE t WHERE t.PROFILE_SEQ = ?1", nativeQuery = true)
    int findUserSeq(int profileSeq);
}

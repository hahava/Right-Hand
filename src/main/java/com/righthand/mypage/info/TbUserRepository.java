package com.righthand.mypage.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TbUserRepository extends JpaRepository<TbUser, Long> {

    @Query(value = "SELECT t.EMAIL FROM TB_USER t WHERE USER_SEQ = ?1", nativeQuery = true)
    String findEmail(int userSeq);
}

package com.righthand.mypage.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TbUserRepository extends JpaRepository<TbUser, Long> {
//     TbUser getOne(Long userSeq);
     @Modifying
     @Query(value = "UPDATE TB_USER SET USER_PWD = ?1 WHERE USER_SEQ = ?2",nativeQuery = true)
     public int updatePwd(String newPwd, int userSeq);
}

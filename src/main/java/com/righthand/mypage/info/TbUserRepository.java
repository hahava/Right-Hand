package com.righthand.mypage.info;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TbUserRepository extends JpaRepository<TbUser, Long> {
     TbUser getOne(Long userSeq);
}

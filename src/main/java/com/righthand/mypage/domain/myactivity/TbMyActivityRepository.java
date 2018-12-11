package com.righthand.mypage.domain.myactivity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TbMyActivityRepository extends JpaRepository<TbMyActivity, Long> {
    Page<TbMyActivity> findAllByActivityProfileSeq(Long id, Pageable pageable);

    @Query("SELECT SUM(t.rhPower) FROM TbMyActivity t WHERE t.activityProfileSeq = ?1")
    int getSumRhPower(Long id);
}

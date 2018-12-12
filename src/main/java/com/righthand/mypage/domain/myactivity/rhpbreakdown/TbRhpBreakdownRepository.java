package com.righthand.mypage.domain.myactivity.rhpbreakdown;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TbRhpBreakdownRepository extends JpaRepository<TbRhpBreakdown, Long> {
    Page<TbRhpBreakdown> findAllByRhpProfileSeq(Long id, Pageable pageable);

    @Query("SELECT SUM(t.rhPower) FROM TbRhpBreakdown t WHERE t.rhpProfileSeq = ?1")
    int getSumRhPower(Long id);
}

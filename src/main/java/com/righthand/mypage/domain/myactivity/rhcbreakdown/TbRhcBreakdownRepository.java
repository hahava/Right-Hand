package com.righthand.mypage.domain.myactivity.rhcbreakdown;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TbRhcBreakdownRepository extends JpaRepository<TbRhcBreakdown, Long> {
    Page<TbRhcBreakdown> findAllByRhcProfileSeq(Long id, Pageable pageable);
}

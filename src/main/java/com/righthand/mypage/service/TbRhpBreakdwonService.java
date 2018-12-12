package com.righthand.mypage.service;

import com.righthand.mypage.domain.myactivity.rhpbreakdown.TbRhpBreakdown;
import com.righthand.mypage.domain.myactivity.rhpbreakdown.TbRhpBreakdownRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TbRhpBreakdwonService {
    private final TbRhpBreakdownRepository tbRhpBreakdownRepository;

    @Transactional(readOnly = true)
    public Page<TbRhpBreakdown> findAllByRhpProfileSeq(Long id, int page, int size){
        Pageable pageable = new PageRequest(page - 1, size, new Sort(Sort.Direction.DESC, "activityDate"));
        return tbRhpBreakdownRepository.findAllByRhpProfileSeq(id, pageable);
    }

    @Transactional(readOnly = true)
    public int getSumRhPower(Long id){
        return tbRhpBreakdownRepository.getSumRhPower(id);
    }
}

package com.righthand.mypage.service;

import com.righthand.mypage.domain.myactivity.TbMyActivity;
import com.righthand.mypage.domain.myactivity.TbMyActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TbMyActivityService {
    private final TbMyActivityRepository tbMyActivityRepository;

    @Transactional(readOnly = true)
    public Page<TbMyActivity> findAllByActivityProfileSeq(Long id, int page, int size){
        Pageable pageable = new PageRequest(page - 1, size, new Sort(Sort.Direction.DESC, "activityDate"));
        return tbMyActivityRepository.findAllByActivityProfileSeq(id, pageable);
    }
}

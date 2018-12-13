package com.righthand.mypage.service;

import com.righthand.common.type.ReturnType;
import com.righthand.mypage.domain.profile.TbProfile;
import com.righthand.mypage.domain.profile.TbProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TbProfileService {

    private final TbProfileRepository tbProfileRepository;

    @Transactional(readOnly = true)
    public TbProfile getOne(long profileSeq){
        return tbProfileRepository.getOne(profileSeq);
    }

}

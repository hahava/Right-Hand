package com.righthand.mypage.service;

import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.info.TbProfileRepository;
import com.righthand.mypage.info.TbUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class TbUserService {

    private TbUserRepository tbUserRepository;
    private TbProfileRepository tbProfileRepository;
    private MembershipService membershipService;

    @Transactional
    public Map<String, Object> findUserAndProfile() throws Exception{
        MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
        Map<String, Object> map = new HashMap<>();
        int userSeq = tbProfileRepository.findUserSeq(membershipInfo.getProfileSeq());
        String email = tbUserRepository.findEmail(userSeq);
        map.put("email", email);
        map.put("nickname", membershipInfo.getNickname());
        return map;
    }
}

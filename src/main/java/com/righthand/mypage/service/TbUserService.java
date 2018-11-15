package com.righthand.mypage.service;

import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.info.TbProfile;
import com.righthand.mypage.info.TbProfileRepository;
import com.righthand.mypage.info.TbUser;
import com.righthand.mypage.info.TbUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
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
        TbProfile tbProfile = tbProfileRepository.findByProfileSeq((long) membershipInfo.getProfileSeq());
        System.out.println("profileSeq : " + tbProfile.getProfileSeq());
        TbUser tbUser = tbUserRepository.getOne(tbProfile.getUserSeq());
        map.put("userId", tbUser.getUserId());
        map.put("nickname", membershipInfo.getNickname());
        map.put("userName", tbProfile.getUserName());
        map.put("gender", tbProfile.getGender());
        map.put("tel", tbProfile.getTel());
        map.put("birthYear", tbProfile.getBirthYear());
        return map;
    }

    @Transactional
    public void updateUserProfile(String nickname, String tel) throws Exception{
        MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
        tbProfileRepository.updateUserProfile(nickname, tel, membershipInfo.getProfileSeq());
    }
}

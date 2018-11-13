package com.righthand.mypage.service;

import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.info.TbProfile;
import com.righthand.mypage.info.TbProfileRepository;
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
        List<TbProfile> tbProfile = tbProfileRepository.findUserSeq(membershipInfo.getProfileSeq());
        String email = tbUserRepository.findEmail(tbProfile.get(0).getProfileSeq().intValue());
        map.put("email", email);
        map.put("nickname", membershipInfo.getNickname());
        map.put("userName", tbProfile.get(0).getUserName());
        map.put("gender", tbProfile.get(0).getGender());
        map.put("tel", tbProfile.get(0).getTel());
        map.put("birthYear", tbProfile.get(0).getBirthYear());
        return map;
    }

    @Transactional
    public void updateUserProfile(String nickname) throws Exception{
        MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
        tbProfileRepository.updateUserProfile(nickname, membershipInfo.getProfileSeq());
    }
}

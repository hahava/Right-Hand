package com.righthand.mypage.service;

import com.righthand.common.VaildationCheck.ConfigValidationCheck;
import com.righthand.common.type.ReturnType;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.info.TbProfile;
import com.righthand.mypage.info.TbProfileRepository;
import com.righthand.mypage.info.TbUser;
import com.righthand.mypage.info.TbUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;
    private ConfigValidationCheck configValidationCheck;

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

    @Transactional
    public ReturnType updateUserPwd(String oldPwd, String newPwd, String newPwdDup, MembershipInfo membershipInfo)
            throws Exception{
        /*
        * 1. Old Pwd == DB Pwd AND New Pwd == New Pwd Dup
        * */
        int userSeq = membershipInfo.getUserSeq();
        TbUser tbUser = tbUserRepository.getOne((long) userSeq);
        if(!passwordEncoder.matches(oldPwd, tbUser.getUserPwd())
            || !newPwd.equals(newPwdDup)){
            return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG;
        }

        /*
        * 2. New Pwd Validation Check
        * */
        if(configValidationCheck.checkPwd(newPwd) != 0) return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG;

        /*
        * Update Pwd!
        * */
        newPwd = passwordEncoder.encode(newPwd);
        tbUserRepository.updatePwd(newPwd, userSeq);
        return ReturnType.RTN_TYPE_OK;
    }
}

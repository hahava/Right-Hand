package com.righthand.mypage.service;

import com.righthand.common.VaildationCheck.ConfigValidationCheck;
import com.righthand.common.type.ReturnType;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.domain.file.TbFile;
import com.righthand.mypage.domain.profile.TbProfile;
import com.righthand.mypage.domain.profile.TbProfileRepository;
import com.righthand.mypage.domain.user.TbUser;
import com.righthand.mypage.domain.user.TbUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class TbUserService {

    private TbUserRepository tbUserRepository;
    private TbProfileRepository tbProfileRepository;
    private MembershipService membershipService;
    private PasswordEncoder passwordEncoder;
    private ConfigValidationCheck configValidationCheck;

    @Transactional(readOnly = true)
    @Cacheable(value = "findUserAndProfileCache", key = "#userSeq")
    public Map<String, Object> findUserAndProfile(int userSeq) throws Exception{
        Map<String, Object> map = new HashMap<>();
        TbProfile tbProfile = tbProfileRepository.findByUserSeq(userSeq);
        TbUser tbUser = tbUserRepository.getOne((long) userSeq);

        map.put("userId", tbUser.getUserId());

        map.put("nickname", tbProfile.getNickName());
        map.put("userName", tbProfile.getUserName());
        map.put("gender", tbProfile.getGender());
        map.put("tel", tbProfile.getTel());
        map.put("birthYear", tbProfile.getBirthYear());
        map.put("filePath", null);
        if(tbProfile.getFileSeq() !=null){
            List<TbFile> fileList = tbProfileRepository.findAllJoinProfileAndFile(tbProfile.getProfileSeq());
            map.replace("filePath", fileList.get(0).getFilePath());
        }

        return map;
    }

    @Transactional
    public void updateUserProfile(String nickname, String tel) throws Exception{
        MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
        tbProfileRepository.updateUserProfile(nickname, tel, membershipInfo.getProfileSeq());
    }

    @Transactional
    public ReturnType updateUserPwd(String newPwd, String newPwdDup, MembershipInfo membershipInfo)
            throws Exception{
        /*
        * 1. Old Pwd == DB Pwd AND New Pwd == New Pwd Dup
        * */
        int userSeq = membershipInfo.getUserSeq();
        TbUser tbUser = tbUserRepository.getOne((long) userSeq);

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

    @CacheEvict(value = "findUserAndProfileCache", key = "#userSeq")
    public void refreshCache(int userSeq) {
        log.info("[Cache][Refresing] userSeq : " + userSeq);
    }
}

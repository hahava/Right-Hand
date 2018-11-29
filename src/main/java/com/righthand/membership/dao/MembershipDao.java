package com.righthand.membership.dao;

import com.righthand.membership.dto.model.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
@ComponentScan(basePackages = "com.righthand.membership.dao")
public interface MembershipDao
{
    void insertUser(Map userData);
    void insertProfile(Map profileData);
    Map selectUser(Map userData);
    int countID(Map userData);
    int getProfileSeq(int userSeq);
    String getProfileNickname(int userSeq);
    void resign(Map reason);
    int checkNickname(Map userData);
    String getUserPwd(int userSeq);
    void changePwd(UserVO vo);
    int getRecommender(Map userData);
    void rewardRecommendProfile(Map userData);
    void rewardRecommendPromote(Map userData);
    int getUserSeq(String id);
}

package com.righthand.membership.dao;

import com.righthand.common.type.ReturnType;
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
    int getProfileSeqByBoardSeq(int boardSeq);
    String getProfileNickname(int userSeq);
    void resign(Map reason);
    int checkNickname(Map userData);
    String getUserPwd(int userSeq);
    void changePwd(UserVO vo);
    int getRecommender(Map userData);
    void rewardRecommendProfile(Map userData);
    void rewardRecommendPromote(Map userData);
    int getUserSeq(String id);
    Integer checkFileGrpSeq(int profileSeq);
    void saveFileGrpSeq(Map profileData);
    void updateFileSeq(Map profileData);
    Map getRewardPowerAndRhCoin(int profileSeq);
    void updateRhCoin(Map profileData);
    void updateRewardPower(Map map);

    int getLoginLimit(int profileSeq);

    void decreaseLoginLimit(int profileSeq);

    void updateRhPower(Map map);

    int getBoardWriteLimit(int profileSeq);

    void decreaseBoardWriteLimit(int profileSeq);

    int getReplyWriteLimit(int profileSeq);

    void decreaseReplyWriteLimit(int profileSeq);

    int checkTel(Map params);
}

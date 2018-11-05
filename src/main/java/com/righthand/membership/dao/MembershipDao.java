package com.righthand.membership.dao;

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
    int countEmail(Map userData);
    int getProfileSeq(int userSeq);
}

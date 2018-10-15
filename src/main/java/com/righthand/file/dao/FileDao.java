package com.righthand.file.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FileDao
{
    void insertFileGrp(Map FileData);
    void insertFile(Map FileData);
    void updateFileGrp(Map FileData);
    void updateFile(Map FileData);
    List<Map<String, Object>> selectFile(Map FileData);

}

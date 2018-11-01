package com.righthand.board.dao;

import com.righthand.board.dto.model.BoardCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
@ComponentScan(basePackages = "com.righthand.board.dao")
public interface BoardDao {
    List<Map<String, Object>> selectBoardListTech(BoardCountVO vo);
}

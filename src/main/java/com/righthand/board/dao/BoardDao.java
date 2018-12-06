package com.righthand.board.dao;

import com.righthand.board.dto.model.BoardCountVO;
import com.righthand.board.dto.model.BoardDetailVO;
import com.righthand.board.dto.model.BoardSearchVO;
import com.righthand.board.dto.model.MyBoardVO;
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
    List<Map<String, Object>> selectBoardListDev(BoardCountVO vo);

    List<Map<String, Object>> searchedBoardListTech(BoardSearchVO vo);
    List<Map<String, Object>> searchedBoardListDev(BoardSearchVO vo);

    Map<String, Object> showBoardDetailTech(BoardDetailVO vo);
    Map<String, Object> showBoardDetailDev(BoardDetailVO vo);

    List<Map<String, Object>> showReplyBoardTech(BoardDetailVO vo);
    List<Map<String, Object>> showReplyBoardDev(BoardDetailVO vo);

    List<Map<String, Object>> showNewBoard();

    void insertBoardListTech(Map boardData);
    void insertBoardListDev(Map boardData);

    void insertReplyListTech(Map replyData);
    void insertReplyListDev(Map input_data);

    int selectCountListTech();
    int selectCountListDev();

    int selectSearchedCountListTech(BoardSearchVO vo);
    int selectSearchedCountListDev(BoardSearchVO vo);

    List<Map<String, Object>> getMyBoardList(MyBoardVO vo);
    int countMyBoard(int profileSeq);
    int findProfileSeqByBoardSeq(int boardSeq);
}

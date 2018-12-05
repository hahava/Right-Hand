package com.righthand.board.service;

import com.righthand.common.type.ReturnType;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BoardService {
    List<Map<String, Object>> selectBoardListTech(int page) throws Exception;
    List<Map<String, Object>> selectBoardListDev(int page) throws Exception;

    List<Map<String, Object>> searchedBoardListTech(String searchedWord, int page) throws Exception;
    List<Map<String, Object>> searchedBoardListDev(String searchedWord, int page) throws Exception;

    Map<String, Object> showBoardDetailTech(int boardSeq) throws Exception;
    Map<String, Object> showBoardDetailDev(int boardSeq) throws Exception;

    ReturnType insertBoardListTech(Map input_data) throws Exception;
    ReturnType insertBoardListDev(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> showReplyBoardTech(int boardSeq) throws Exception;
    List<Map<String, Object>> showReplyBoardDev(int boardSeq) throws Exception;

    List<Map<String, Object>> showNewBoard() throws Exception;

    Map<String, Object> getMyBoardList(int profileSeq, int page) throws Exception;

    ReturnType insertReplyListTechWithRewardPower(Map input_data) throws Exception;
    ReturnType insertReplyListDevWithRewardPower(Map input_data) throws Exception;

    ReturnType insertReplyListTechWithRhCoin(Map input_data) throws Exception;
    ReturnType insertReplyListDevWithRhCoin(Map input_data) throws Exception;
}

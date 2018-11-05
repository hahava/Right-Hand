package com.righthand.board.service;

import com.righthand.board.dto.req.BoardReq;
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

    ReturnType insertReplyListTech(Map input_data) throws Exception;
    ReturnType insertReplyListDev(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> showReplyBoardTech(int boardSeq) throws Exception;
    List<Map<String, Object>> showReplyBoardDev(int boardSeq) throws Exception;

}

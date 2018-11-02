package com.righthand.board.service;

import com.righthand.board.dto.req.BoardReq;
import com.righthand.common.type.ReturnType;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BoardService {
    List<Map<String, Object>> selectBoardListTech(int page) throws Exception;
    List<Map<String, Object>> searchedBoardListTech(String searchedWord, int page) throws Exception;

    Map<String, Object> showBoardDetailTech(int boardSeq) throws Exception;

    ReturnType insertBoardListTech(Map input_data) throws Exception;

}

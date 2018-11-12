package com.righthand.notice.service;

import com.righthand.notice.boards.TbNoticeBoard;
import com.righthand.notice.boards.TbNoticeBoardRepository;
import com.righthand.notice.dto.req.BoardReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbNoticeService {

    @Autowired
    private TbNoticeBoardRepository tbNoticeBoardRepository;

    @Transactional
    public Map<String, Object> findAllBoardDateDesc(int start, int offset) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("data", tbNoticeBoardRepository.findAllBoardDateDesc(start, offset));
        map.put("total", tbNoticeBoardRepository.count());
        return map;
    }

    @Transactional
    public Map<String, Object> findAllBySearchedWord(String searchedWord, int start, int offset) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("data", tbNoticeBoardRepository.findAllBySearchedWord(searchedWord, start, offset));
        map.put("total", tbNoticeBoardRepository.countSearchedBoard(searchedWord));
        return map;
    }

    @Transactional
    public TbNoticeBoard save(BoardReq boardReq) throws Exception{
        return tbNoticeBoardRepository.save(boardReq.toEntity());
    }

    @Transactional
    public List<TbNoticeBoard> findByBoardSeq(long boardSeq) throws Exception{
        return tbNoticeBoardRepository.findByBoardSeq(boardSeq);
    }
}

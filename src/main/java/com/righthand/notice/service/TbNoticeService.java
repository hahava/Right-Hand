package com.righthand.notice.service;

import com.righthand.notice.boards.TbNoticeBoard;
import com.righthand.notice.boards.TbNoticeBoardRepository;
import com.righthand.notice.dto.req.BoardReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<TbNoticeBoard> findAllBoardDateDesc(int page, int size) throws Exception{
        Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "boardDate"));
        return tbNoticeBoardRepository.findAll(pageable);
    }

    @Transactional
    public Page<TbNoticeBoard> findAllBySearchedWord(String searchedWord, int page, int size) throws Exception{
        Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "BOARD_DATE"));
        return tbNoticeBoardRepository.findByBoardTitleOrBoardContent(searchedWord, pageable);
    }

    @Transactional
    public TbNoticeBoard save(BoardReq boardReq) throws Exception{
        return tbNoticeBoardRepository.save(boardReq.toEntity());
    }

    @Transactional
    public TbNoticeBoard findByBoardSeq(long boardSeq) throws Exception{
        return tbNoticeBoardRepository.findByBoardSeq(boardSeq);
    }
}

package com.righthand.board.service;

import com.righthand.board.dao.BoardDao;
import com.righthand.board.dto.model.BoardCountVO;
import com.righthand.board.dto.model.BoardSearchVO;
import com.righthand.board.dto.req.BoardReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    BoardDao boardDao;

    static Semaphore boardSemaphore = new Semaphore(1);

    @Override
    public List<Map<String, Object>> selectBoardListTech(int page) throws Exception {
        int start, end;
        BoardCountVO vo = new BoardCountVO();
        start = (page - 1) * 5;
        end = start + 5;
        vo.setStart(start); vo.setEnd(end);
        List<Map<String, Object>> resBoardData;
        boardSemaphore.acquire();
        try{
            resBoardData = boardDao.selectBoardListTech(vo);
            System.out.println("resBoard : " + resBoardData);
            boardSemaphore.release();
        }catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }
        return resBoardData;
    }

    @Override
    public List<Map<String, Object>> searchedBoardListTech(String searchedWord, int page) throws Exception {
        int start, end;
        BoardSearchVO vo = new BoardSearchVO();
        start = (page - 1) * 5;
        end = start + 5;
        vo.setStart(start); vo.setEnd(end);
        vo.setSearchedWord(searchedWord);
        List<Map<String, Object>> searchedBoardData;
        boardSemaphore.acquire();
        try {
            searchedBoardData = boardDao.searchedBoardListTech(vo);
            System.out.println("resBoard : " + searchedBoardData);
            boardSemaphore.release();
        }catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }
        return searchedBoardData;
    }
}
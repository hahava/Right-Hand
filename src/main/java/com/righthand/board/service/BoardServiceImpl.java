package com.righthand.board.service;

import com.righthand.board.dao.BoardDao;
import com.righthand.board.dto.model.BoardCountVO;
import com.righthand.board.dto.model.BoardDetailVO;
import com.righthand.board.dto.model.BoardSearchVO;


import com.righthand.common.type.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardDao boardDao;

    static Semaphore boardSemaphore = new Semaphore(1);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Map<String, Object>> selectBoardListTech(int page) throws Exception {
        int start;
        final int offset = 5;
        BoardCountVO vo = new BoardCountVO();
        start = (page - 1) * 5;
        vo.setStart(start); vo.setOffset(offset);
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
    public List<Map<String, Object>> selectBoardListDev(int page) throws Exception {
        int start;
        final int offset = 5;
        BoardCountVO vo = new BoardCountVO();
        start = (page - 1) * 5;
        vo.setStart(start); vo.setOffset(offset);
        List<Map<String, Object>> resBoardData;
        boardSemaphore.acquire();
        try{
            resBoardData = boardDao.selectBoardListDev(vo);
            boardSemaphore.release();
        }catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }
        return resBoardData;
    }

    @Override
    public List<Map<String, Object>> searchedBoardListTech(String searchedWord, int page) throws Exception {
        int start;
        final int offset = 5;
        BoardSearchVO vo = new BoardSearchVO();
        start = (page - 1) * 5;
        vo.setStart(start); vo.setOffset(offset);
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

    @Override
    public List<Map<String, Object>> searchedBoardListDev(String searchedWord, int page) throws Exception{
        int start;
        final int offset = 5;
        BoardSearchVO vo = new BoardSearchVO();
        start = (page - 1) * 5;
        vo.setStart(start); vo.setOffset(offset);
        vo.setSearchedWord(searchedWord);
        List<Map<String, Object>> searchedBoardData;
        boardSemaphore.acquire();
        try {
            searchedBoardData = boardDao.searchedBoardListDev(vo);
            System.out.println("resBoard : " + searchedBoardData);
            boardSemaphore.release();
        }catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }
        return searchedBoardData;
    }

    @Override
    public Map<String, Object> showBoardDetailTech(int boardSeq) throws Exception {
        BoardDetailVO vo = new BoardDetailVO();
        Map<String, Object> boardDetailData;
        vo.setBoardSeq(boardSeq);
        boardSemaphore.acquire();
        try {
            boardDetailData = boardDao.showBoardDetailTech(vo);
            boardSemaphore.release();
        } catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }
        return boardDetailData;
    }

    @Override
    public Map<String, Object> showBoardDetailDev(int boardSeq) throws Exception {
        BoardDetailVO vo = new BoardDetailVO();
        Map<String, Object> boardDetailData;
        vo.setBoardSeq(boardSeq);
        boardSemaphore.acquire();
        try {
            boardDetailData = boardDao.showBoardDetailDev(vo);
            boardSemaphore.release();
        } catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }
        return boardDetailData;
    }

    @Override
    public List<Map<String, Object>> showReplyBoardTech(int boardSeq) throws Exception {
        BoardDetailVO vo = new BoardDetailVO();
        List<Map<String, Object>> replyDetailData;
        vo.setBoardSeq(boardSeq);
        boardSemaphore.acquire();
        try {
            replyDetailData = boardDao.showReplyBoardTech(vo);
            boardSemaphore.release();
        }
        catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }

        return replyDetailData;
    }

    @Override
    public List<Map<String, Object>> showReplyBoardDev(int boardSeq) throws Exception {
        BoardDetailVO vo = new BoardDetailVO();
        List<Map<String, Object>> replyDetailData;
        vo.setBoardSeq(boardSeq);
        boardSemaphore.acquire();
        try {
            replyDetailData = boardDao.showReplyBoardDev(vo);
            boardSemaphore.release();
        }
        catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }

        return replyDetailData;
    }

    @Override
    public List<Map<String, Object>> showNewBoard() throws Exception {
        List<Map<String, Object>> newBoards;
        try {
            newBoards = boardDao.showNewBoard();
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        return newBoards;
    }

    @Override
    public ReturnType insertBoardListTech(Map input_data) throws Exception {
        logger.info("[Service][boardTech]");
        boardSemaphore.acquire();
        try {
            boardDao.insertBoardListTech(input_data);
        } catch (Exception e) {
            boardSemaphore.release();
            return ReturnType.RTN_TYPE_NG;
        }
        boardSemaphore.release();
        return ReturnType.RTN_TYPE_OK;
    }

    @Override
    public ReturnType insertBoardListDev(Map input_data) throws Exception {
        logger.info("[Service][boardDev]");
        boardSemaphore.acquire();
        try {
            boardDao.insertBoardListDev(input_data);
        } catch (Exception e) {
            boardSemaphore.release();
            return ReturnType.RTN_TYPE_NG;
        }
        boardSemaphore.release();
        return ReturnType.RTN_TYPE_OK;
    }

    @Override
    public ReturnType insertReplyListTech(Map input_data) throws Exception {
        logger.info("[Service][replyTech]");
        boardSemaphore.acquire();
        try {
            boardDao.insertReplyListTech(input_data);
        } catch (Exception e) {
            boardSemaphore.release();
            return ReturnType.RTN_TYPE_NG;
        }
        boardSemaphore.release();
        return ReturnType.RTN_TYPE_OK;
    }

    @Override
    public ReturnType insertReplyListDev(Map input_data) throws Exception {
        logger.info("[Service][replyDev]");
        boardSemaphore.acquire();
        try {
            boardDao.insertReplyListDev(input_data);
        } catch (Exception e) {
            boardSemaphore.release();
            return ReturnType.RTN_TYPE_NG;
        }
        boardSemaphore.release();
        return ReturnType.RTN_TYPE_OK;
    }

}

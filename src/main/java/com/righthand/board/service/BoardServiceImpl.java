package com.righthand.board.service;

import com.righthand.board.dao.BoardDao;
import com.righthand.board.dto.model.BoardCountVO;
import com.righthand.board.dto.model.BoardDetailVO;
import com.righthand.board.dto.model.BoardSearchVO;


import com.righthand.board.dto.model.MyBoardVO;
import com.righthand.common.type.ReturnType;
import com.righthand.membership.dao.MembershipDao;
import com.righthand.membership.service.MembershipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardDao boardDao;

    @Autowired
    MembershipDao membershipDao;

    static Semaphore boardSemaphore = new Semaphore(1);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Map<String, Object>> selectBoardListTech(int page) throws Exception {
        int start;
        final int offset = 5;
        BoardCountVO vo = new BoardCountVO();
        start = (page - 1) * 5;
        vo.setStart(start);
        vo.setOffset(offset);
        List<Map<String, Object>> resBoardData;
        boardSemaphore.acquire();
        try {
            resBoardData = boardDao.selectBoardListTech(vo);
            boardSemaphore.release();
        } catch (Exception e) {
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
        vo.setStart(start);
        vo.setOffset(offset);
        List<Map<String, Object>> resBoardData;
        boardSemaphore.acquire();
        try {
            resBoardData = boardDao.selectBoardListDev(vo);
            boardSemaphore.release();
        } catch (Exception e) {
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
        vo.setStart(start);
        vo.setOffset(offset);
        vo.setSearchedWord(searchedWord);
        List<Map<String, Object>> searchedBoardData;
        boardSemaphore.acquire();
        try {
            searchedBoardData = boardDao.searchedBoardListTech(vo);
            System.out.println("resBoard : " + searchedBoardData);
            boardSemaphore.release();
        } catch (Exception e) {
            boardSemaphore.release();
            throw new Exception(e);
        }
        return searchedBoardData;
    }

    @Override
    public List<Map<String, Object>> searchedBoardListDev(String searchedWord, int page) throws Exception {
        int start;
        final int offset = 5;
        BoardSearchVO vo = new BoardSearchVO();
        start = (page - 1) * 5;
        vo.setStart(start);
        vo.setOffset(offset);
        vo.setSearchedWord(searchedWord);
        List<Map<String, Object>> searchedBoardData;
        boardSemaphore.acquire();
        try {
            searchedBoardData = boardDao.searchedBoardListDev(vo);
            System.out.println("resBoard : " + searchedBoardData);
            boardSemaphore.release();
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new Exception(e);
        }
        return newBoards;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getMyBoardList(int profileSeq, int page) throws Exception {
        int start;
        final int offset = 5;
        MyBoardVO vo = new MyBoardVO();
        List<Map<String, Object>> myBoard;
        Map<String, Object> result = new HashMap<>();

        start = (page - 1) * 5;
        vo.setStart(start);
        vo.setOffset(offset);
        vo.setProfileSeq(profileSeq);

        try {
            int count = boardDao.countMyBoard(profileSeq);
            myBoard = boardDao.getMyBoardList(vo);
            result.put("total", count);
            result.put("data", myBoard);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return result;
    }

    @Override
    public ReturnType insertBoardListTech(Map input_data) throws Exception {
        logger.info("[Service][boardTech]");
        boardSemaphore.acquire();
        try {
            boardDao.insertBoardListTech(input_data);
        } catch (Exception e) {
            boardSemaphore.release();
            return ReturnType.RTN_TYPE_BOARD_INSERT_NG;
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
            return ReturnType.RTN_TYPE_BOARD_INSERT_NG;
        }
        boardSemaphore.release();
        return ReturnType.RTN_TYPE_OK;
    }

    /*
     * Reward Power를 사용하여 코인을 지급한다.
     * */
    @Override
    @Transactional
    public ReturnType insertReplyListTechWithRewardPower(Map input_data) throws Exception {
        logger.info("[Service][replyTechWithRewardPower]");
        try {

            //게시판 업데이트
            boardDao.insertReplyListTech(input_data);

            //수신자의 정보를 가져오기
            int receiverProfileSeq = membershipDao.getProfileSeqByBoardSeq((int) input_data.get("boardSeq"));
            Map<String, Object> map = new HashMap<>();
            double reqCoin = (double) input_data.get("reqCoin");
            map.put("reqCoin", reqCoin);

            // 수신자 코인 획득
            map.put("profileSeq", receiverProfileSeq);
            membershipDao.updateRhCoin(map);

            // 발신자 ***리워드 파워*** 차감
            map.replace("profileSeq", input_data.get("replyProfileSeq"));
            map.replace("reqCoin", reqCoin * (-1));
            membershipDao.updateRewardPower(map);
        } catch (Exception e) {
            return ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST;
        }
        return ReturnType.RTN_TYPE_OK;
    }

    /*
     * Reward Power를 사용하여 코인을 지급한다.
     * */
    @Override
    @Transactional
    public ReturnType insertReplyListDevWithRewardPower(Map input_data) throws Exception {
        logger.info("[Service][replyDevWithRewardPower]");
        try {

            //게시판 업데이트
            boardDao.insertReplyListDev(input_data);

            //수신자의 정보를 가져오기
            int receiverProfileSeq = membershipDao.getProfileSeqByBoardSeq((int) input_data.get("boardSeq"));
            Map<String, Object> map = new HashMap<>();
            double reqCoin = (double) input_data.get("reqCoin");
            map.put("reqCoin", reqCoin);

            // 수신자 코인 획득
            map.put("profileSeq", receiverProfileSeq);
            membershipDao.updateRhCoin(map);

            // 발신자 ***리워드 파워*** 차감
            map.replace("profileSeq", input_data.get("replyProfileSeq"));
            map.replace("reqCoin", reqCoin * (-1));
            membershipDao.updateRewardPower(map);
        } catch (Exception e) {
            return ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST;
        }
        return ReturnType.RTN_TYPE_OK;
    }


    /*
    * RhCoin을 사용하여 코인을 지급한다.
    * */
    @Override
    public ReturnType insertReplyListTechWithRhCoin(Map input_data) throws Exception {
        logger.info("[Service][replyTechWithRhCoin]");
        try {

            //게시판 업데이트
            boardDao.insertReplyListTech(input_data);

            //수신자의 정보를 가져오기
            int receiverProfileSeq = membershipDao.getProfileSeqByBoardSeq((int) input_data.get("boardSeq"));
            Map<String, Object> map = new HashMap<>();
            double reqCoin = (double) input_data.get("reqCoin");
            map.put("reqCoin", reqCoin);

            // 수신자 코인 획득
            map.put("profileSeq", receiverProfileSeq);
            membershipDao.updateRhCoin(map);

            // 발신자 ***리워드 파워*** 차감
            map.replace("profileSeq", input_data.get("replyProfileSeq"));
            map.replace("reqCoin", reqCoin * (-1));
            membershipDao.updateRhCoin(map);
        } catch (Exception e) {
            return ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST;
        }
        return ReturnType.RTN_TYPE_OK;
    }

    @Override
    public ReturnType insertReplyListDevWithRhCoin(Map input_data) throws Exception {
        logger.info("[Service][replyDevWithRhCoin]");
        try {

            //게시판 업데이트
            boardDao.insertReplyListDev(input_data);

            //수신자의 정보를 가져오기
            int receiverProfileSeq = membershipDao.getProfileSeqByBoardSeq((int) input_data.get("boardSeq"));
            Map<String, Object> map = new HashMap<>();
            double reqCoin = (double) input_data.get("reqCoin");
            map.put("reqCoin", reqCoin);

            // 수신자 코인 획득
            map.put("profileSeq", receiverProfileSeq);
            membershipDao.updateRhCoin(map);

            // 발신자 ***리워드 파워*** 차감
            map.replace("profileSeq", input_data.get("replyProfileSeq"));
            map.replace("reqCoin", reqCoin * (-1));
            membershipDao.updateRhCoin(map);
        } catch (Exception e) {
            return ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST;
        }
        return ReturnType.RTN_TYPE_OK;
    }


}

package com.righthand.board.service;

import com.righthand.board.dao.BoardDao;
import com.righthand.board.dto.model.BoardCountVO;
import com.righthand.board.dto.model.BoardDetailVO;
import com.righthand.board.dto.model.BoardSearchVO;


import com.righthand.board.dto.model.MyBoardVO;
import com.righthand.common.type.ReturnType;
import com.righthand.membership.dao.MembershipDao;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.mypage.domain.myactivity.rhcbreakdown.TbRhcBreakdown;
import com.righthand.mypage.domain.myactivity.rhcbreakdown.TbRhcBreakdownRepository;
import com.righthand.mypage.domain.myactivity.rhpbreakdown.TbRhpBreakdown;
import com.righthand.mypage.domain.myactivity.rhpbreakdown.TbRhpBreakdownRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao;

    private final MembershipDao membershipDao;

    private final TbRhpBreakdownRepository tbRhpBreakdownRepository;

    private final TbRhcBreakdownRepository tbRhcBreakdownRepository;

    static Semaphore boardSemaphore = new Semaphore(1);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
     * 게시글 작성 갯수를 확인하여 RH파워 지급 후, LIMIT을 차감.
     * */

    private ReturnType giveRhPowerAndDecreaseLimitAtBoard(MembershipInfo membershipInfo) {
        int profileSeq = membershipInfo.getProfileSeq();
        int boardWriteAvailable = membershipDao.getBoardWriteLimit(profileSeq);
        if (boardWriteAvailable == 0) return ReturnType.RTN_TYPE_BOARD_ALL_REWARDED;
        Map<String, Object> map = new HashMap<>();
        map.put("reqPower", 10);
        map.put("profileSeq", profileSeq);
        membershipDao.updateRhPower(map);
        membershipDao.decreaseBoardWriteLimit(profileSeq);
        return ReturnType.RTN_TYPE_OK;
    }

    private ReturnType giveRhPowerAndDecreaseLimitAtReply(MembershipInfo membershipInfo) {
        int profileSeq = membershipInfo.getProfileSeq();
        if (membershipDao.getReplyWriteLimit(profileSeq) == 0) return ReturnType.RTN_TYPE_REPLY_ALL_REWARDED;
        Map<String, Object> map = new HashMap<>();
        map.put("reqPower", 10);
        map.put("profileSeq", profileSeq);
        membershipDao.updateRhPower(map);
        membershipDao.decreaseReplyWriteLimit(profileSeq);
        return ReturnType.RTN_TYPE_BOARD_REPLY_SUCCESS;
    }

    /**
     * @author: Danny
     * Comment: RH 파워에 대한 내역 삽입
     * */
    private void insertRhpBreakdown(Map input_data, MembershipInfo membershipInfo, String boardType, char activityType) {
        int boardSeq;
        String boardTitle;
        String type;
        final int profileSeq = membershipInfo.getProfileSeq();
        if (activityType == 'r') {
            boardSeq = (int) input_data.get("boardSeq");
            if (boardType.equals("dev")) {
                boardTitle = boardDao.findBoardTitleDev(boardSeq);
            } else {
                boardTitle = boardDao.findBoardTitleTech(boardSeq);
            }
            type = "댓글 작성";
        } else {
            boardSeq = (int) input_data.get("BOARD_SEQ");
            boardTitle = (String) input_data.get("boardTitle");
            type = "게시글 작성";
        }
        tbRhpBreakdownRepository.save(
                TbRhpBreakdown.builder()
                        .activityType(type)
                        .rhpProfileSeq((long) profileSeq)
                        .rhPower((long) 10)
                        .content(boardTitle)
                        .boardSeq((long) boardSeq)
                        .boardType(boardType)
                        .build()
        );
    }

    /**
     * @author: Danny
     * Comment: RH 코인에 대한 내역 삽입
     * */
    void insertRhcBreakdown(Map input_data, long receiverProfileSeq, String boardType, String senderType){
        final String receiverType = "RH 코인 획득";
        final int boardSeq = (int) input_data.get("boardSeq");
        String boardTitle;
        if(boardType.equals("tech")) boardTitle = boardDao.findBoardTitleTech(boardSeq);
        else boardTitle = boardDao.findBoardTitleDev(boardSeq);
        final double rhCoin = (double) input_data.get("reqCoin");
        final int senderProfileSeq = (int) input_data.get("senderSeq");
        // Sender의 지출 내역
        tbRhcBreakdownRepository.save(
                TbRhcBreakdown.builder()
                        .activityType(senderType)
                        .boardSeq((long) boardSeq)
                        .boardType(boardType)
                        .content(boardTitle)
                        .rhCoin(rhCoin)
                        .rhcProfileSeq((long)senderProfileSeq)
                        .isSender(true)
                        .build()
        );

        // Receiver의 지출 내역
        tbRhcBreakdownRepository.save(
                TbRhcBreakdown.builder()
                        .activityType(receiverType)
                        .boardSeq((long) boardSeq)
                        .boardType(boardType)
                        .content(boardTitle)
                        .rhCoin(rhCoin)
                        .rhcProfileSeq(receiverProfileSeq)
                        .isSender(false)
                        .build()
        );
    }


    @Override
    public List<Map<String, Object>> selectBoardListTech(int page) throws Exception {
        int start;
        final int offset = 5;
        BoardCountVO vo = new BoardCountVO();
        start = (page - 1) * 5;
        vo.setStart(start);
        vo.setOffset(offset);
        List<Map<String, Object>> resBoardData;
        Map<String, Object> result = new HashMap<>();
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
            logger.error("[getMyBoardList][Exception] : {}", e.toString());
        }
        return result;
    }

    @Override
    public ReturnType insertBoardListTech(Map input_data, MembershipInfo membershipInfo) throws Exception {
        logger.info("[Service][boardTech]");
        ReturnType rtn;
        boardSemaphore.acquire();
        try {
            boardDao.insertBoardListTech(input_data);

            // 유저에게 Rh 파워를 주고 제한을 감소한다.
            rtn = giveRhPowerAndDecreaseLimitAtBoard(membershipInfo);

            // TB_MY_ACTIVITY DB의 데이터를 갱신한다.
            if(rtn == ReturnType.RTN_TYPE_OK)insertRhpBreakdown(input_data, membershipInfo, "tech", 'b');
        } catch (Exception e) {
            boardSemaphore.release();
            return ReturnType.RTN_TYPE_BOARD_INSERT_NG;
        }
        boardSemaphore.release();
        return rtn;
    }

    @Override
    public ReturnType insertBoardListDev(Map input_data, MembershipInfo membershipInfo) throws Exception {
        logger.info("[Service][boardDev]");
        ReturnType rtn;
        boardSemaphore.acquire();
        try {
            String boardContent = (String) input_data.get("boardContent");
            boardContent = boardContent.replaceAll("\\\\", "")
                            .replaceAll("<", "&lt;")
                            .replaceAll(">", "&gt;");
            input_data.replace("boardContent", boardContent);
            boardDao.insertBoardListDev(input_data);
            rtn = giveRhPowerAndDecreaseLimitAtBoard(membershipInfo);
            if(rtn == ReturnType.RTN_TYPE_OK) insertRhpBreakdown(input_data, membershipInfo, "dev", 'b');
        } catch (Exception e) {
            boardSemaphore.release();
            return ReturnType.RTN_TYPE_BOARD_INSERT_NG;
        }
        boardSemaphore.release();
        return rtn;
    }

    /*
     * Reward Power를 사용하여 코인을 지급한다.
     * */
    @Override
    @Transactional
    public ReturnType insertReplyListTechWithRewardPower(Map input_data, MembershipInfo membershipInfo) throws Exception {
        logger.info("[Service][replyTechWithRewardPower]");
        ReturnType rtn;
        try {
            //게시판 업데이트
            boardDao.insertReplyListTech(input_data);
        } catch (Exception e) {
            return ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST;
        }
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

        rtn = giveRhPowerAndDecreaseLimitAtReply(membershipInfo);

        /**
         * RH 파워 획득 내역 삽입
         * */
        insertRhpBreakdown(input_data, membershipInfo, "tech", 'r');

        /**
         * RH 코인 획득 내역 삽입
         * */
        /** insertRhcBreakdown에서 senderSeq를 필요로하기 때문!*/
        input_data.put("senderSeq", input_data.get("replyProfileSeq"));
        insertRhcBreakdown(input_data, receiverProfileSeq, "tech", "리워드 파워 지급");


        return rtn;
    }


    @Override
    @Transactional
    public ReturnType insertReplyListTechWithRhCoin(Map input_data, MembershipInfo membershipInfo) throws Exception {
        logger.info("[Service][replyTechWithRhCoin]");
        ReturnType rtn;
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

            // 발신자 코인 차감
            map.replace("profileSeq", input_data.get("replyProfileSeq"));
            map.replace("reqCoin", reqCoin * (-1));
            membershipDao.updateRhCoin(map);

            rtn = giveRhPowerAndDecreaseLimitAtReply(membershipInfo);

            /**
             * RH 파워 획득 내역 삽입
             * */
            insertRhpBreakdown(input_data, membershipInfo, "tech", 'r');

            /**
             * RH 코인 획득 내역 삽입
             * */
            /** insertRhcBreakdown에서 senderSeq를 필요로하기 때문!*/
            input_data.put("senderSeq", input_data.get("replyProfileSeq"));
            insertRhcBreakdown(input_data, receiverProfileSeq, "tech", "RH 코인 지급");

        } catch (Exception e) {
            return ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST;
        }
        return rtn;
    }

    @Override
    @Transactional
    public ReturnType insertReplyListDev(Map input_data, MembershipInfo membershipInfo) throws Exception {
        logger.info("[Service][insertReplyListDev]");
        ReturnType rtn;
        try {
            boardDao.insertReplyListDev(input_data);
            rtn = giveRhPowerAndDecreaseLimitAtReply(membershipInfo);

            /**
             * RH 파워 획득 내역 삽입
             * */
            insertRhpBreakdown(input_data, membershipInfo, "dev", 'r');

        } catch (Exception e) {
            logger.error("[InsertReplyListDev][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_BOARD_REPLY_NG;
        }
        return rtn;
    }

    @Override
    @Transactional
    public ReturnType sendDevWithRewardPower(Map input_data, MembershipInfo membershipInfo) throws Exception {
        logger.info(("[Service][sendDevWithRewardPower]"));
        boardDao.findReplyIsRewarded((int) input_data.get("replySeq"));

        try {
            // 댓글 업데이트 (지급하는 코인의 양 추가)
            // 그리고 댓글을 보상 받은 상태로 바꾼다.
            boardDao.updateReplyListDev(input_data);
        } catch (Exception e) {
            logger.error("[sendRewardPowerInDev][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_COIN_SEND_NG;
        }
        Map<String, Object> map = new HashMap<>();

        double reqCoin = (double) input_data.get("reqCoin");
        map.put("reqCoin", reqCoin);

        Integer receiverProfileSeq = null;
        try {
            receiverProfileSeq = boardDao.findProfileSeqByReplySeq((int) input_data.get("replySeq"));
        } catch (Exception e) {
            logger.error("[findProfileSeqByReplySeq][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_NG;
        }
        map.put("profileSeq", receiverProfileSeq);
        try {
            // 수신자 코인 획득
            membershipDao.updateRhCoin(map);
        } catch (Exception e) {
            logger.error("[updateReceiver][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_NG;
        }
        // 발신자 ***리워드 파워*** 차감
        map.replace("profileSeq", input_data.get("senderSeq"));
        map.replace("reqCoin", reqCoin * (-1));
        try {
            membershipDao.updateRewardPower(map);
        } catch (Exception e) {
            logger.error("[updateSender][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_NG;
        }

        /**
         * RH 코인 획득 내역 삽입
         * */
        insertRhcBreakdown(input_data, receiverProfileSeq, "dev", "리워드 파워 지급");

        return ReturnType.RTN_TYPE_SEND_REWARD_POWER;
    }

    @Override
    @Transactional
    public ReturnType sendDevWithRhCoin(Map input_data, MembershipInfo membershipInfo) throws Exception {
        logger.info(("[Service][sendDevWithRhCoin]"));
        try {
            // 댓글 업데이트 (지급하는 코인의 양 추가)
            boardDao.updateReplyListDev(input_data);
        } catch (Exception e) {
            logger.error("[sendRewardPowerInDev][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_COIN_SEND_NG;
        }
        Map<String, Object> map = new HashMap<>();

        double reqCoin = (double) input_data.get("reqCoin");
        map.put("reqCoin", reqCoin);

        Integer receiverProfileSeq = null;
        try {
            receiverProfileSeq = boardDao.findProfileSeqByReplySeq((int) input_data.get("replySeq"));
        } catch (Exception e) {
            logger.error("[findProfileSeqByReplySeq][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_NG;
        }
        map.put("profileSeq", receiverProfileSeq);
        try {
            // 수신자 코인 획득
            membershipDao.updateRhCoin(map);
        } catch (Exception e) {
            logger.error("[updateReceiver][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_NG;
        }
        // 발신자 ***코인 차감*** 차감
        map.replace("profileSeq", input_data.get("senderSeq"));
        map.replace("reqCoin", reqCoin * (-1));
        try {
            membershipDao.updateRhCoin(map);
        } catch (Exception e) {
            logger.error("[updateSender][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_NG;
        }

        logger.info("boardSeq : {}", input_data.get("boardSeq"));

        /**
         * RH 코인 획득 내역 삽입
         * */
        insertRhcBreakdown(input_data, receiverProfileSeq, "dev", "RH 코인 지급");
        return ReturnType.RTN_TYPE_SEND_RH_COIN;
    }

    @Override
    @Transactional(readOnly = true)
    public int findProfileSeqByBoardSeqTech(int boardSeq) throws Exception {
        logger.info("[Service][findProfileSeqByBoardSeqTech]");
        return boardDao.findProfileSeqByBoardSeqTech(boardSeq);
    }

    @Override
    @Transactional(readOnly = true)
    public int findProfileSeqByBoardSeqDev(int boardSeq) throws Exception {
        logger.info("[Service][findProfileSeqByBoardSeqDev]");
        return boardDao.findProfileSeqByBoardSeqDev(boardSeq);
    }



}


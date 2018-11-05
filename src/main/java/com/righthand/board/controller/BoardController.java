package com.righthand.board.controller;

import com.righthand.board.dao.BoardDao;
import com.righthand.board.dto.model.BoardSearchVO;
import com.righthand.board.dto.req.BoardReq;
import com.righthand.board.service.BoardService;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;

import com.righthand.common.util.ConvertUtil;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    MembershipService membershipService;

    @Autowired
    BoardDao boardDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/board/list/{btype}")
    public ResponseHandler<?> showBoardListTech(@RequestParam int page, @PathVariable String btype){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        Map<String, Object> tempBoardData = new HashMap<>();
        List<Map<String, Object>> tempBoardList;
        if(btype.equals("tech")){
            try {
                tempBoardData.put("total", boardDao.selectCountListTech());
                tempBoardList = boardService.selectBoardListTech(page);
                tempBoardData.put("data", tempBoardList);
                if(!(tempBoardList.isEmpty() || tempBoardList == null)) {
                    result.setData(tempBoardData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                }else{
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            } catch (Exception e) {
                logger.error("[ShowTechBoardList][Exception] " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_NG);
            }
        }else if(btype.equals("dev")){
            try {
                tempBoardData.put("total", boardDao.selectCountListDev());
                tempBoardList = boardService.selectBoardListDev(page);
                tempBoardData.put("data", tempBoardList);
                if(!(tempBoardList.isEmpty() || tempBoardList == null)) {
                    result.setData(tempBoardData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                }else{
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            } catch (Exception e) {
                logger.error("[ShowDevBoardList][Exception] " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_NG);
            }
        }
        return result;
    }

    @GetMapping("/board/list/searched/{btype}")
    public ResponseHandler<?> searchedBoardListTech(
            @RequestParam String searchedWord, @RequestParam int page,
            @PathVariable String btype
    ) {
        final ResponseHandler<Object> result = new ResponseHandler<>();
        Map<String, Object> tempBoardData = new HashMap<>();
        List<Map<String, Object>> tempBoardList;
        BoardSearchVO vo = new BoardSearchVO();
        vo.setSearchedWord(searchedWord);
        if(btype.equals("tech")) {
            try {
                tempBoardData.put("total", boardDao.selectSearchedCountListTech(vo));
                tempBoardList = boardService.searchedBoardListTech(searchedWord, page);
                tempBoardData.put("data", tempBoardList);
                if (!(tempBoardList.isEmpty() || tempBoardList == null)) {
                    result.setData(tempBoardData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                } else {
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            } catch (Exception e) {
                logger.error("[SearchTechBoardList] [Exception " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_NG);
            }
        }else if(btype.equals("dev")){
            try {
                tempBoardData.put("total", boardDao.selectSearchedCountListDev(vo));
                tempBoardList = boardService.searchedBoardListDev(searchedWord, page);
                tempBoardData.put("data", tempBoardList);
                if (!(tempBoardList.isEmpty() || tempBoardList == null)) {
                    result.setData(tempBoardData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                } else {
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            } catch (Exception e) {
                logger.error("[SearchDevBoardList] [Exception " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_NG);
            }
        }
        return result;
    }


    @PostMapping("/board/{btype}")
    public ResponseHandler<?> writeBoardTech(@Valid @RequestBody final BoardReq _params,
                                             @PathVariable String btype) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        try {
            MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
            System.out.println("profile seq : " + membershipInfo.getProfileSeq());
            params.put("boardProfileSeq", membershipInfo.getProfileSeq());
            ReturnType rtn;
            if(btype.equals("tech")){
                try {
                    rtn = boardService.insertBoardListTech(params);
                    result.setReturnCode(rtn);
                } catch (Exception e) {
                    logger.error("[TechBoard][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_NG);
                }
            }else if(btype.equals("dev")){
                try {
                    rtn = boardService.insertBoardListDev(params);
                    result.setReturnCode(rtn);
                } catch (Exception e) {
                    logger.error("[DevBoard][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_NG);
                }
            }
        } catch (Exception e) {
            logger.error("[Board][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    @GetMapping("/board/detail/{btype}")
    public  ResponseHandler<?> showBoardDetail(@RequestParam int boardSeq, @PathVariable String btype) {
        final ResponseHandler<Object> result = new ResponseHandler<>();
        Map<String, Object> boardDetailData = null;
        List<Map<String, Object>> replyDetilData = null;
        Map<String, Object> resultData = new HashMap<>();
        try {
            if(btype.equals("tech")){
                boardDetailData = boardService.showBoardDetailTech(boardSeq);
                replyDetilData = boardService.showReplyBoardTech(boardSeq);
            }else if(btype.equals("dev")){
                boardDetailData = boardService.showBoardDetailDev(boardSeq);
                replyDetilData = boardService.showReplyBoardDev(boardSeq);
            }
            if(!(boardDetailData.isEmpty() || boardDetailData == null)) {
                if(replyDetilData.isEmpty() || replyDetilData == null) {
                    resultData.put("boardDetailData", boardDetailData);
                    result.setData(resultData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                }
                else {
                    resultData.put("boardDetailData", boardDetailData);
                    resultData.put("replyDetailData", replyDetilData);
                    result.setData(resultData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                }
            }
            else {
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
            }
        }
        catch (Exception e) {
            logger.error("[BoardDetail] [Exception " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    @PostMapping("/reply/{btype}")
    public ResponseHandler<?> writeReplyTech(@PathVariable String btype,
                                             @RequestParam int boardSeq,
                                             @RequestParam String replyContent){
        final ResponseHandler<?> result = new ResponseHandler<>();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
            params.put("replyProfileSeq", membershipInfo.getProfileSeq());
            params.put("boardSeq", boardSeq);
            params.put("replyContent", replyContent);
            ReturnType rtn;
            try {
                if(btype.equals("tech")){
                    rtn = boardService.insertReplyListTech(params);
                    result.setReturnCode(rtn);
                }else if(btype.equals("dev")){
                    rtn = boardService.insertReplyListDev(params);
                    result.setReturnCode(rtn);
                }
            }catch (Exception e){
                logger.error("[TechReply][Exception] " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_NG);
            }
        } catch (Exception e) {
            logger.error("[TechReply][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }
}

package com.righthand.board.controller;

import com.amazonaws.log.InternalLogApi;
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
import java.util.List;
import java.util.Map;

@RestController
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    MembershipService membershipService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/board/list/tech/{page}")
    public ResponseHandler<List<Map<String, Object>>> showBoardListTech(@PathVariable(required = true) int page){
        final ResponseHandler<List<Map<String, Object>>> result = new ResponseHandler<>();
        List<Map<String, Object>> tempBoardList;
        try {
            tempBoardList = boardService.selectBoardListTech(page);
            if(!(tempBoardList.isEmpty() || tempBoardList == null)) {
                result.setData(tempBoardList);
                result.setReturnCode(ReturnType.RTN_TYPE_OK);
            }else{
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
            }
        } catch (Exception e) {
            logger.error("[ShowBoardList][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return result;
    }

    @GetMapping("/board/list/tech/searched")
    public ResponseHandler<List<Map<String, Object>>> searchedBoardListTech(
            @RequestParam String searchedWord, @RequestParam int page
//            @Valid @RequestBody final BoardReq _params
    ) {
        final ResponseHandler<List<Map<String, Object>>> result = new ResponseHandler<>();
        List<Map<String, Object>> tempBoardList;
        try {
            tempBoardList = boardService.searchedBoardListTech(searchedWord, page);
            if(!(tempBoardList.isEmpty() || tempBoardList == null)) {
                result.setData(tempBoardList);
                result.setReturnCode(ReturnType.RTN_TYPE_OK);
            }
            else {
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
            }
        }
        catch (Exception e) {
            logger.error("[SearchBoardList] [Exception " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return result;
    }


    @PostMapping("/board/tech")
    public ResponseHandler<?> writeBoardTech(@Valid @RequestBody final BoardReq _params) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        try {
            MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
            System.out.println("profile seq : " + membershipInfo.getProfileSeq());
            params.put("boardProfileSeq", membershipInfo.getProfileSeq());
            ReturnType rtn;
            try {
                rtn = boardService.insertBoardListTech(params);
                result.setReturnCode(rtn);
            } catch (Exception e) {
                logger.error("[TechBoard][Exception] " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_NG);
            }
        } catch (Exception e) {
            logger.error("[TechBoard][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    @GetMapping("/board/tech/detail/{boardSeq}")
    public  ResponseHandler<Map<String, Object>> showBoardDetail(@PathVariable(required = true) int boardSeq) {
        final ResponseHandler<Map<String, Object>> result = new ResponseHandler<>();
        Map<String, Object> boardDetailData;
        try {
            boardDetailData = boardService.showBoardDetailTech(boardSeq);
            if(!(boardDetailData.isEmpty() || boardDetailData == null)) {
                result.setData(boardDetailData);
                result.setReturnCode(ReturnType.RTN_TYPE_OK);
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



}

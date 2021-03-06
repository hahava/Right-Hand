package com.righthand.notice.controller;

import com.righthand.common.board.BoardChecker;
import com.righthand.common.GetClientProfile;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.notice.domain.boards.TbNoticeBoard;
import com.righthand.notice.dto.req.BoardReq;
import com.righthand.notice.service.TbNoticeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController{

    private final TbNoticeService tbNoticeService;

    private final MembershipService membershipService;

    private List<HashMap<String, Object>> transform(Page<TbNoticeBoard> params){
        final String nickname = "운영자";
        List<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < params.getNumberOfElements(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("BOARD_TITLE", params.getContent().get(i).getBoardTitle());
            map.put("BOARD_CONTENT", params.getContent().get(i).getBoardContent());
            map.put("BOARD_SEQ", params.getContent().get(i).getBoardSeq());
            map.put("BOARD_DATE", params.getContent().get(i).getBoardDate());
            map.put("BOARD_TYPE", "notice");
            map.put("NICK_NAME", nickname);
            datas.add(i, map);
        }
        return datas;
    }

    private Map<String, Object> transform(TbNoticeBoard param){
        final String nickname = "운영자";
        HashMap<String, Object> data = new HashMap<>();
        data.put("BOARD_TITLE", param.getBoardTitle());
        data.put("BOARD_CONTENT", param.getBoardContent());
        data.put("BOARD_SEQ", param.getBoardSeq());
        data.put("BOARD_DATE", param.getBoardDate());
        data.put("BOARD_TYPE", "notice");
        data.put("NICK_NAME", nickname);
        return data;
    }

    @ApiOperation("공지사항 리스트")
    @GetMapping("/board/list/notice")
    public ResponseHandler<?> showNoticeList(@ApiParam(value = "페이지 번호")@RequestParam int page) {
        final ResponseHandler<Map<String,Object>> result = new ResponseHandler<>();
        final int size = 5;
        // RESPONSE
        Map<String, Object> res = new HashMap<>();
        Page<TbNoticeBoard> allBoardDateDesc;

        //유저 Profile 가져옴
        Map<String, Object> userInfo= null;
        try {
            userInfo = GetClientProfile.getUserInfo(membershipService);
        } catch (Exception e) {
            log.error("[getUserInfo][Exception]" + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
        }
        try {
            allBoardDateDesc = tbNoticeService.findAllBoardDateDesc(page - 1, size);
        }
        catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
            return result;
        }
        if(allBoardDateDesc.getTotalElements() == 0) {
            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
            result.setMessage("Board is not exist.");
        }
        else {
            res.put("total", allBoardDateDesc.getTotalElements());
            res.put("data", transform(allBoardDateDesc));
            res.put("authority", userInfo.get("authority"));
            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        }
        return result;
    }

    @ApiOperation("공지사항 검색")
    @GetMapping("/board/list/searched/notice")
    public ResponseHandler<?> searchedNoticeList(@ApiParam(value = "검색어")@RequestParam String searchedWord,
                                                 @ApiParam(value = "페이지 번호")@RequestParam int page){
        Map<String, Object> res = new HashMap<>();
        final ResponseHandler<Object> result = new ResponseHandler<>();
        final int size = 5;
        System.out.println("[Service][searchNotice]");
        //유저 Profile 가져옴
        Map<String, Object> userInfo= null;
        try {
            userInfo = GetClientProfile.getUserInfo(membershipService);
        } catch (Exception e) {
            log.error("[getUserInfo][Exception]" + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
        }
        try {
            Page<TbNoticeBoard> allBySearchedWord = tbNoticeService.findAllBySearchedWord(searchedWord, page - 1, size);
            if(allBySearchedWord.getTotalElements() == 0){
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                return result;
            }
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            res.put("total", allBySearchedWord.getTotalElements());
            res.put("data", transform(allBySearchedWord));
            res.put("authority", userInfo.get("authority"));
            result.setData(res);
        }catch (Exception e){
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return result;
    }

    @ApiOperation("공지시항 작성")
    @PostMapping("/board/notice")
    public ResponseHandler<?> writeNotice(@ApiParam(value = "글 번호")@Valid @RequestBody BoardReq boardReq, BindingResult bindingResult){
        final ResponseHandler<TbNoticeBoard> result = new ResponseHandler<>();
        if(!bindingResult.hasErrors()) {
            try {
                MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
                if (membershipInfo == null) {
                    result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                } else {
                    ReturnType returnType = BoardChecker.checkParam(boardReq);
                    if (returnType.equals(ReturnType.RTN_TYPE_OK)) {
                        System.out.println("[Service][boardNotice]");
                        try {
                            TbNoticeBoard tbNoticeBoard = tbNoticeService.save(boardReq);
                            result.setReturnCode(ReturnType.RTN_TYPE_OK);
                            result.setData(tbNoticeBoard);
                        } catch (Exception e) {
                            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_INSERT_NG);
                        }
                    } else {
                        result.setReturnCode(returnType);
                    }
                }
            } catch (Exception e) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            return result;
        }
        result.setReturnCode(ReturnType.RTN_TYPE_NO_DATA);
        return result;
    }

    @ApiOperation("공지사항 글 상세보기")
    @GetMapping("/board/detail/notice")
    public ResponseHandler<?> showNoticeDetail(@ApiParam(value = "글 번호")@RequestParam long boardSeq){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        //유저 Profile 가져옴
        Map<String, Object> userInfo= null;
        try {
            userInfo = GetClientProfile.getUserInfo(membershipService);
        } catch (Exception e) {
            log.error("[getUserInfo][Exception]" + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
        }
        Map<String, Object > res = new HashMap<>();
        System.out.println("[Service][detailNotice]");
        try {
            TbNoticeBoard tbNoticeBoard = tbNoticeService.findByBoardSeq(boardSeq);
            if(tbNoticeBoard == null){
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                return result;
            }
            res.put("data", transform(tbNoticeBoard));
            res.put("authority", userInfo.get("authority"));
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            result.setData(res);
        }catch (Exception e){
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return result;
    }
}

package com.righthand.notice.controller;

import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.notice.domain.boards.TbNoticeBoard;
import com.righthand.notice.domain.boards.TbNoticeBoardRepository;
import com.righthand.notice.dto.req.BoardReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@AllArgsConstructor
public class NoticeController{

    @Autowired
    private TbNoticeBoardRepository tbNoticeBoardRepository;

    @Autowired
    MembershipService membershipService;

    @ApiOperation("공지사항 리스트")
    @GetMapping("/notice/list")
    public ResponseHandler<?> showNoticeList(@ApiParam(value = "페이지 번호")@RequestParam int page) {
        final ResponseHandler<Map<String,Object>> result = new ResponseHandler<>();
        List<TbNoticeBoard> tbNoticeBoardList = null;
        Map<String, Object> res = new HashMap<>();
        final int offset = 5;
        int start = (page - 1) * offset;
        try {
            tbNoticeBoardList = tbNoticeBoardRepository.findAllBoardDateDesc(start, offset);
        }
        catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            return result;
        }
        if(tbNoticeBoardList.isEmpty() || tbNoticeBoardList == null) {
            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
            result.setMessage("Board is not exist.");
        }
        else {
            MembershipInfo membershipInfo = null;
            try {
                membershipInfo = membershipService.currentSessionUserInfo();
                res.put("total", tbNoticeBoardRepository.count());
                res.put("authority", membershipInfo.getAuthoritiesLevel());
                res.put("data", tbNoticeBoardList);
                result.setData(res);
                result.setReturnCode(ReturnType.RTN_TYPE_OK);
                result.setMessage("Success");
            } catch (Exception e) {
                res.put("total", tbNoticeBoardRepository.count());
                res.put("authority", 0);
                res.put("data", tbNoticeBoardList);
                result.setData(res);
                result.setReturnCode(ReturnType.RTN_TYPE_OK);
                result.setMessage("Success");
            }
        }
        return result;
    }

    @ApiOperation("공지사항 검색")
    @GetMapping("/notice/searched")
    public ResponseHandler<?> searchedNoticeList(@ApiParam(value = "검색어")@RequestParam String searchedWord,
                                                 @ApiParam(value = "페이지 번호")@RequestParam int page){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        System.out.println("[Service][searchNotice]");
        final int offset = 5;
        int start = (page - 1) * offset;
        Map<String, Object> res = new HashMap<>();

        try {
            List<TbNoticeBoard> tbNoticeBoardList = tbNoticeBoardRepository.findAllBySearchedWord(searchedWord, start, offset);
            if(tbNoticeBoardList.isEmpty() || tbNoticeBoardList == null){
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                return result;
            }
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            res.put("total", tbNoticeBoardRepository.countSearchedBoard(searchedWord));
            res.put("data", tbNoticeBoardList);
            result.setData(res);
        }catch (Exception e){
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return result;
    }

    @ApiOperation("공지시항 작성")
    @PostMapping("/notice/board")
    public ResponseHandler<?> writeNotice(@ApiParam(value = "글 번호")@RequestBody BoardReq boardReq){
        final ResponseHandler<TbNoticeBoard> result = new ResponseHandler<>();
        System.out.println("[Service][boardNotice]");
        try{
            TbNoticeBoard tbNoticeBoard = tbNoticeBoardRepository.save(boardReq.toEntity());
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            result.setData(tbNoticeBoard);
            result.setMessage("Success");
        }catch (Exception e){
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return result;
    }

    @ApiOperation("공지사항 글 상세보기")
    @GetMapping("/notice/detail")
    public ResponseHandler<?> showNoticeDetail(@ApiParam(value = "글 번호")@RequestParam long boardSeq){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        System.out.println("[Service][detailNotice]");
        try {
            List<TbNoticeBoard> tbNoticeBoardList = tbNoticeBoardRepository.findByBoardSeq(boardSeq);
            if(tbNoticeBoardList.isEmpty() || tbNoticeBoardList == null){
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                return result;
            }
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            result.setData(tbNoticeBoardList.get(0));
        }catch (Exception e){
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return result;
    }
}

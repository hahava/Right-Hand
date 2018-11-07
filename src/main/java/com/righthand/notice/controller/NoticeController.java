package com.righthand.notice.controller;

import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.notice.domain.boards.TbNoticeBoard;
import com.righthand.notice.domain.boards.TbNoticeBoardRepository;
import com.righthand.notice.dto.req.BoardReq;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
//@AllArgsConstructor
public class NoticeController{

    @Autowired
    private TbNoticeBoardRepository tbNoticeBoardRepository;

    @GetMapping("/notice/list")
    public ResponseHandler<?> showNoticeList(){
        final ResponseHandler<List<TbNoticeBoard>> result = new ResponseHandler<>();
        List<TbNoticeBoard> tbNoticeBoardList = null;
        try {
            tbNoticeBoardList = tbNoticeBoardRepository.findAll();
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
            result.setData(tbNoticeBoardList);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            result.setMessage("Success");
        }

        return result;
    }

    @PostMapping("/notice/board")
    public ResponseHandler<?> writeBoard(@RequestBody BoardReq boardReq){
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

    @GetMapping("/notice/detail")
    public ResponseHandler<?> showNoticeDetail(@RequestParam long boardSeq){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        System.out.println("[Service][searchNotice]");
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

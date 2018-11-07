package com.righthand.notice.controller;

import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.notice.domain.boards.TbNoticeBoard;
import com.righthand.notice.domain.boards.TbNoticeBoardRepository;
import com.righthand.notice.dto.req.BoardReq;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class NoticeController {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
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

    @PostMapping("/board/notice")
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
}

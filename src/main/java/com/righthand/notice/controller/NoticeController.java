package com.righthand.notice.controller;

import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.notice.domain.boards.TbNoticeBoard;
import com.righthand.notice.domain.boards.TbNoticeBoardRepository;
import com.righthand.notice.dto.req.BoardReq;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

@RestController
@AllArgsConstructor
public class NoticeController {

    private TbNoticeBoardRepository tbNoticeBoardRepository;

    @GetMapping("/notice/list")
    public ResponseHandler<?> showNoticeList(){
        return null;
    }

    @PostMapping("/board/notice")
    public ResponseHandler<?> writeBoard(@RequestBody BoardReq boardReq){
        final ResponseHandler<TbNoticeBoard> result = new ResponseHandler<>();
        TbNoticeBoard tbNoticeBoard = null;
        tbNoticeBoard = tbNoticeBoardRepository.save(boardReq.toEntity());
        result.setReturnCode(ReturnType.RTN_TYPE_OK);
        result.setData(tbNoticeBoard);
        result.setMessage("Success");
        return result;
    }
}

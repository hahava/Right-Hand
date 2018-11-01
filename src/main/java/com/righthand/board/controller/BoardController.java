package com.righthand.board.controller;

import com.amazonaws.log.InternalLogApi;
import com.righthand.board.service.BoardService;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BoardController {

    @Autowired
    BoardService boardService;
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

}

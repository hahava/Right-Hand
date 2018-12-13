package com.righthand.common.board;

import com.righthand.board.dto.req.BoardReq;
import com.righthand.common.type.ReturnType;

public class BoardChecker {
    public static ReturnType checkParam(Board _params){
        if(_params.getBoardTitle() == null) {
            return ReturnType.RTN_TYPE_BOARD_TITLE_NO_EXIST;
        }
        if(_params.getBoardContent() == null) {
            return ReturnType.RTN_TYPE_BOARD_CONTENT_NO_EXIST;
        }
        return ReturnType.RTN_TYPE_OK;
    }

}

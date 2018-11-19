package com.righthand.notice.dto.req;

import com.righthand.common.board.Board;
import com.righthand.notice.boards.TbNoticeBoard;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("게시글 파라미터")
public class BoardReq extends Board {

    public TbNoticeBoard toEntity(){
        return TbNoticeBoard.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .build();
    }
}

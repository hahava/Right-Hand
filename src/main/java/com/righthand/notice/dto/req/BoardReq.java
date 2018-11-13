package com.righthand.notice.dto.req;

import com.righthand.notice.boards.TbNoticeBoard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("게시글 파라미터")
public class BoardReq {

    @ApiParam(value = "제목", required = true)
    private String boardTitle;

    @ApiParam(value = "내용", required = true)
    private String boardContent;

    public TbNoticeBoard toEntity(){
        return TbNoticeBoard.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .build();
    }
}

package com.righthand.notice.dto.req;

import com.righthand.notice.boards.TbNoticeBoard;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardReq {

    private String boardTitle;
    private String boardContent;

    public TbNoticeBoard toEntity(){
        return TbNoticeBoard.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .build();
    }
}

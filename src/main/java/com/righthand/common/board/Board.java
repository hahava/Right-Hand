package com.righthand.common.board;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class Board {

    @ApiParam(value = "제목", required = true)
    @NotBlank
    private String boardTitle;

    @ApiParam(value = "내용", required = true)
    @NotBlank
    private String boardContent;

}

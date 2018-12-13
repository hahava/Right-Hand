package com.righthand.common.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Page 컨틀롤러
 */


@Controller
public class PageController {

    // 게시판 리스트 화면 리턴
    @ApiOperation(value = "게시판 리스트 반환한다. IT 게시판은 type=it, Dev 게시판은 type = dev")
    @GetMapping("/board/list")
    public String boardList() {
        return "board";
    }

    //게시판 검색 결과 화면 리스트
    @ApiOperation(value = "게시판별 검색 화면 리스트를 리턴한다")
    @GetMapping("/board/search")
    public String boardSearchResult() {
        return "searchResult";
    }

    //게시글 작성화면
    @ApiOperation(value = "게시글 작성을 위한 화면")
    @GetMapping("/board/writer")
    public String boardWriter() {
        return "boardWriter";
    }

    //게시글 상세화면
    @ApiOperation(value = "게시글 상세 화면")
    @GetMapping("/board/content")
    public String boardContent() {
        return "boardContent";
    }

    //유저 개인정보
    @ApiOperation(value = "유저 정보")
    @GetMapping("/user/info")
    public String userInfo() {
        return "userInfo";
    }

    //유저 개인정보
    @ApiOperation(value = "유저 정보 수정")
    @GetMapping("/user/edit")
    public String userInfoEdit() {
        return "userPwCheck";
    }

    @ApiOperation(value = "에러 화면")
    @GetMapping("/error/{type}")
    public String error(@PathVariable String type) {
        return "error/" + type;
    }

    @ApiOperation(value = "사용자의 활동 내역 화면")
    @GetMapping("/user/board")
    public String userBoard() {
        return "userBoard";
    }

    @ApiOperation(value = "사용자의 토큰 사용내역  화면")
    @GetMapping("/user/token")
    public String userToken() {
        return "userToken";
    }

    @ApiOperation(value = "사용자의 토큰 사용내역  화면")
    @GetMapping("/user/activity")
    public String userActivity() {
        return "userActivity";
    }
//    @RequestMapping("/")
//    public String index(HttpServletRequest request) {
//        return "/index.html";
//    }
//
//    @RequestMapping("/page/{depth1}")
//    public String depth1(HttpServletRequest request, @PathVariable String depth1) {
//        return depth1;
//    }
//
//    @RequestMapping("/page/{depth1}/{depth2}")
//    public String depth2(HttpServletRequest request, @PathVariable String depth1, @PathVariable String depth2){
//        return depth1+"/"+depth2;
//    }
//
//    @RequestMapping("/page/{depth1}/{depth2}/{depth3}")
//    public String depth3(HttpServletRequest request, @PathVariable String depth1, @PathVariable String depth2, @PathVariable String depth3) {
//        return depth1+"/"+depth2+"/"+depth3;
//    }
}



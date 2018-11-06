package com.righthand.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Page 컨틀롤러
 */


@Controller
public class PageController {

    @ApiOperation(value = "로그인")
    @GetMapping("/login")
    public String customLogin() {
        return "login";
    }

    // tech 작성화면 화면 리턴
    @ApiOperation(value = "IT 게시판 글 작성")
    @GetMapping("/boardlist")
    public String boardList() {
        return "boardlist";
    }

    // blog 리스트 화면 리턴
    @ApiOperation(value = "IT 게시판 리스트 반환")
    @GetMapping("/it/board")
    public String itBoardList() {
        return "itboard";
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



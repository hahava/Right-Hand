package com.righthand.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 *  Page 컨틀롤러
 *
 *
 */


@Controller
public class PageController {


    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        return "/index.html";
    }

    @RequestMapping("/page/{depth1}")
    public String depth1(HttpServletRequest request, @PathVariable String depth1) {
        return depth1;
    }

    @RequestMapping("/page/{depth1}/{depth2}")
    public String depth2(HttpServletRequest request, @PathVariable String depth1, @PathVariable String depth2){
        return depth1+"/"+depth2;
    }

    @RequestMapping("/page/{depth1}/{depth2}/{depth3}")
    public String depth3(HttpServletRequest request, @PathVariable String depth1, @PathVariable String depth2, @PathVariable String depth3) {
        return depth1+"/"+depth2+"/"+depth3;
    }
}



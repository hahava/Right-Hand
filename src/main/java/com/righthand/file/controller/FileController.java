package com.righthand.file.controller;

import com.righthand.common.CheckData;
import com.righthand.common.type.ReturnType;
import com.righthand.file.config.ConfigFile;
import com.righthand.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/file" )
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    ConfigFile configFile;

    @Autowired
    CheckData checkData;

    /**
     * 1개의 파일을 저장
     *
     * @param file
     *      "저장할 파일"
     * @param
     *
     * @return
     */
    @RequestMapping(value="/store" , method = RequestMethod.POST)
    public String storeFile(@RequestParam("file") MultipartFile file) {

        ArrayList<HashMap<String,Object>> urlMap = null;
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String url = null;
        ReturnType rtn ;
        MultipartFile[] files = new MultipartFile[1];

        Map<String, Object> param2 = new  HashMap<String, Object>();
        files[0] = file;

        try {
            System.out.println("[StoreFile]");
            urlMap = fileService.storeFile(files, param2);

            System.out.println("url :" + urlMap.get(0).get("fileUrl"));
        }
        catch(Exception e) {
            System.out.println("[StoreFile][Exception] " + e.toString());
        }
        return urlMap.get(0).get("fileUrl").toString();
    }
}

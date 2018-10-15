package com.righthand.file.service;

import com.righthand.common.type.ReturnType;
import com.righthand.file.dto.FileDownloadRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FileService {

    // 파일을 저장한다.
    ArrayList<HashMap<String,Object>> storeFile(MultipartFile[] files, Map<String, Object> param) throws  Exception;
    // 파일을 삭제 한다.
    public ReturnType deleteFile(Map<String, Object> param) throws  Exception;
    // 파일 다운로드 함수
    public FileDownloadRes loadFileAsResource(int category, String fileName) throws  Exception;

    public int insertFileGrpInfo(Map<String, Object> param) throws  Exception;
    public int insertFileInfo(Map<String, Object> param) throws  Exception;
    public ReturnType updateFileGrpInfo(Map<String, Object> param) throws  Exception;
    public ReturnType updateFileInfo(Map<String, Object> param) throws  Exception;
    List<Map<String, Object>> selectFileByFileGrp(int fileGrpSeq) throws  Exception;
}

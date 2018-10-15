package com.righthand.file.storage;

import com.righthand.common.type.ReturnType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *  서버 로컬 파일 제어 인터페이스 클래스
 *
 */
public interface StorigeHandler {

    public ReturnType store(MultipartFile file, String subUrl, String fileName) throws Exception;
    public ReturnType store(List<String> file, String storagePath, String fileName) throws Exception;
    public ReturnType delete(String subUrl, String fileName) throws Exception;

}

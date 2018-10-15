package com.righthand.file.ftp;

import com.righthand.common.type.ReturnType;
import org.springframework.web.multipart.MultipartFile;

/**
 * FTP 파일 제어(+이미지 서버) 인터페이스 클래스
 *
 *
 */
public interface FtpHandler {
    public ReturnType connect(String addr, String Id, String pwd) throws Exception;
    public ReturnType store(MultipartFile file, String subUrl, String fileName) throws Exception;
    public ReturnType delete(String subUrl, String fileName) throws Exception;
    public void close() throws Exception;
    public boolean isConnected();
}

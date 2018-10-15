package com.righthand.file.storage;

import com.righthand.common.type.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 *  서버 로컬 폴더 제어 클래스
 *
 *  서버 로컬 드라이브 폴더에 파일 저장, 삭제 등의 동작을 실시 한다.
 *
 */
public class StorigeHandlerImpl implements StorigeHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 파일 저장
     *
     *  MultipartFile 형식 파일을 서버 스토리지 내에  저장 한다.
     *
     * @param file
     * @param storagePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType store(MultipartFile file, String storagePath , String fileName ) throws Exception {

        String toFileLoc;

        System.out.println("[STORAGE] store");

        if(fileName== null || file ==null)
        {
            return  ReturnType.RTN_TYPE_NG;
        }

        // 디레토리가 없다면 생성
        File dir = new File(storagePath);

        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        // 파일 생성 및 저장
        File serverFile = new File(storagePath + File.separator + fileName);

        file.transferTo(serverFile);

        System.out.println("[STORAGE] store OK");
        return ReturnType.RTN_TYPE_OK;
    }

    /**
     * 파일 저장
     *
     * Array 형식 파일을 서버 스토리지 내에 저장 한다.
     *
     * @param file
     * @param storagePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType store(List<String> file, String storagePath , String fileName ) throws Exception {

        String toFileLoc;

        System.out.println("[STORAGE] store");

        if(fileName== null || file ==null)
        {
            return  ReturnType.RTN_TYPE_NG;
        }

        // 디레토리가 없다면 생성
        File dir = new File(storagePath);

        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        // 파일 생성 및 저장
        File infoFile = new File(storagePath + File.separator + fileName);

        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(infoFile));

        for(String info : file) {
            outputWriter.write(info);
            outputWriter.newLine();
        }

        outputWriter.flush();
        outputWriter.close();

        System.out.println("[STORAGE] store OK");
        return ReturnType.RTN_TYPE_OK;
    }


    /**
     * 파일 삭제
     *
     * 서버 스토리지 내 파일을 삭제 한다.
     *
     * @param storagePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType delete(String storagePath , String fileName ) throws Exception {
        logger.info("[STORAGE] delete" + storagePath + File.separator + fileName);

        File serverFile = new File(storagePath + File.separator + fileName);

        if( serverFile.exists() ){
            if(!serverFile.delete()) {
                return ReturnType.RTN_TYPE_NG;
            }
            return ReturnType.RTN_TYPE_OK;
        } else {
            logger.error("[STORAGE] file not exisits");
            return ReturnType.RTN_TYPE_NG;
        }
    }
}

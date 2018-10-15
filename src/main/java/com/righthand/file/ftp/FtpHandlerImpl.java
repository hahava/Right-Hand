package com.righthand.file.ftp;

import com.righthand.common.type.ReturnType;
import org.apache.commons.net.ftp.FTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 * FTP 파일 제어
 *
 *
 */
public class FtpHandlerImpl implements FtpHandler {

    boolean isConnectedFtp;
    //FTPSClient fctCon ;
    org.apache.commons.net.ftp.FTPClient fctCon;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FtpHandlerImpl() {

        isConnectedFtp = false;
        fctCon = new org.apache.commons.net.ftp.FTPClient();
    }

    public boolean isConnected() {

        return isConnectedFtp;
    }

    /**
     * FCT 서버 연결 함수
     *
     * 연결과 저장을 분리한 이유는, 연결 후
     * 여러 파일을 저장하는 경우 로그인 동작을 한번만 하기 위해서 이다.
     *
     *
     * @param addr
     * @param id
     * @param pwd
     * @return
     * @throws Exception
     */
    public ReturnType connect(String addr, String id, String pwd) throws Exception {

        logger.info("[FCT] Connect");
        logger.info("Addr:" + addr + " ID:" + id + " Pwd:" + pwd);

        if(addr== null || id ==null || pwd==null)
        {
            return  ReturnType.RTN_TYPE_NG;
        }

        fctCon.connect(addr);

        if(fctCon.login(id, pwd))
        {
            fctCon.setFileType(FTP.BINARY_FILE_TYPE);
        }
        else
        {
            logger.error("[FCT][Error] Connect");
            return  ReturnType.RTN_TYPE_NG;
        }

        isConnectedFtp = true;

        logger.info("[FCT] Connect OK");
        return ReturnType.RTN_TYPE_OK;
    }

    /**
     *
     *  파일 저장 함수 .
     *
     *
     * @param file
     * @param subUrl
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType store(MultipartFile file, String subUrl , String fileName ) throws Exception {
        String toFileLoc;

        logger.info("[FCT] store");
        logger.info("File:" + file.getOriginalFilename() + " SubUrl:" + subUrl + " Name:" + fileName);

        if(!isConnectedFtp)
        {
            logger.error("[FCT][Error] Not connect");
            return ReturnType.RTN_TYPE_NG;
        }

        if(fileName== null || file ==null)
        {
            return  ReturnType.RTN_TYPE_NG;
        }


        if(subUrl != null && !subUrl.isEmpty()) {

            toFileLoc = subUrl + "/" + fileName;
        }
        else
        {
            toFileLoc = fileName;
        }

        logger.info("[FCT][toFileLoc]"+toFileLoc);

        if(!fctCon.storeFile(toFileLoc, file.getInputStream()))
        {
            logger.error("[FCT][Error] Store");
            this.close();
            return ReturnType.RTN_TYPE_NG;
        }

        logger.info("[FCT] store OK");
        return ReturnType.RTN_TYPE_OK;
    }

    /**
     * 파일 삭제
     *
     * @param subUrl
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType delete(String subUrl , String fileName ) throws Exception {

        String toFileLoc;

        logger.info("[FTP] Delete");
        logger.info("File:" + " SubUrl:" + subUrl + " Name:" + fileName);

        if(!isConnectedFtp)
        {
            System.out.println("[FTP][Error] Not connect");
            return ReturnType.RTN_TYPE_NG;
        }

        if(subUrl != null && !subUrl.isEmpty()) {

            toFileLoc = subUrl + "/" + fileName;
        }
        else
        {
            toFileLoc = fileName;
        }

        logger.info("[FTP][toFileLoc]"+toFileLoc);

        if(!fctCon.deleteFile(toFileLoc))
        {
            logger.error("[FTP][Error] Delete");
            this.close();
            return ReturnType.RTN_TYPE_NG;
        }

        logger.info("[FTP] Delete OK");
        return ReturnType.RTN_TYPE_OK;
    }

    /**
     * FCP 연결 종료
     *
     * @throws Exception
     */
    public void close() throws Exception {

        if(!isConnectedFtp)
        {
            logger.error("[FCT] Already Disconnect");
            return ;
        }

        fctCon.logout();
        fctCon.disconnect();
        isConnectedFtp = false;

        logger.info("[FCT] disconnect");
    }

}

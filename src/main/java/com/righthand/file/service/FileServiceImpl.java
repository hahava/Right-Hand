package com.righthand.file.service;

import com.righthand.common.CheckData;
import com.righthand.common.GetNowTime;
import com.righthand.common.PasswordHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.file.aws.AwsHandler;
import com.righthand.file.aws.AwsHandlerImpl;
import com.righthand.file.config.ConfigFile;
import com.righthand.file.dao.FileDao;
import com.righthand.file.dto.FileDownloadRes;
import com.righthand.file.ftp.FtpHandler;
import com.righthand.file.ftp.FtpHandlerImpl;
import com.righthand.file.storage.StorigeHandler;
import com.righthand.file.storage.StorigeHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    ConfigFile configFile;

    @Autowired
    PasswordHandler passwordHandler;

    @Autowired
    CheckData checkData;

    @Autowired
    FileDao fileDao;

    @Autowired
    GetNowTime getNowTime;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 파일 그룹 디비에 정보를 넣는 함수
     *
     * @param param
     * @return
     * @throws Exception
     */
    public int insertFileGrpInfo(Map<String, Object> param) throws  Exception {

        int fileGrpSeq;

        // Insert file group info.
        fileDao.insertFileGrp(param);

        // Get file grp seq
        return Integer.parseInt(param.get("fileGrpSeq").toString());
    }

    /**
     * 파일 디비에 정보를 넣는 함수
     *
     * @param param
     * @return
     * @throws Exception
     */
    public int insertFileInfo(Map<String, Object> param) throws  Exception {

        int fileGrpSeq;
        Map<String, Object> mapFileGrp;

        if(!checkData.isExist(param.get("fileGrpSeq"))) {
            return -1;
        }

        fileDao.insertFile(param);

        // Get file grp seq
        return Integer.parseInt(param.get("fileSeq").toString());
    }

    public ReturnType updateFileGrpInfo(Map<String, Object> param) throws  Exception {

        if(!checkData.isExist(param.get("fileGrpSeq"))) {
            return ReturnType.RTN_TYPE_NG;
        }

        fileDao.updateFileGrp(param);

        // Get file grp seq
        return ReturnType.RTN_TYPE_OK;
    }

    /**
     * 파일 정보를 갱신하는 함수
     *
     * @param param
     * @return
     * @throws Exception
     */
    public ReturnType updateFileInfo(Map<String, Object> param) throws  Exception {

        if(!checkData.isExist(param.get("fileSeq"))) {
            return ReturnType.RTN_TYPE_NG;
        }

        fileDao.updateFile(param);

        return ReturnType.RTN_TYPE_OK;
    }

    /**
     *  해당 파일 그룹으로 파일정보를 가져 오는 함수
     *
     * @param fileGrpSeq
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectFileByFileGrp(int fileGrpSeq) throws  Exception {

        Map fileGrpData = new HashMap();

        if(fileGrpSeq <= 0 ) {
            return null;
        }

        fileGrpData.put("fileGrpSeq", fileGrpSeq);

        return fileDao.selectFile(fileGrpData);
    }


    /**
     * 파일 저장 함수.
     *
     * 복수 파일 저장 시 제약 조건
     *  - 복수로 저장 되는 파일은 반드시 동일한 카테고리, 문서(혹은 페이지) 번호를 사용 해야 한다.
     *  - 따라서 복수 파일 이라도 설정 param은 1개만 사용.
     *
     * @param files
     * @param param
     * @return
     * @throws Exception
     */
    public ArrayList<HashMap<String,Object>> storeFile(MultipartFile[] files, Map<String, Object> param) throws  Exception {

        String subUrl = null;
        String fileName = null;
        ReturnType rtn = ReturnType.RTN_TYPE_NG;
        String fileUrl = null;
        String subFileUrl = null;
        String fileOriginName = null;
        int category;

        ArrayList<HashMap<String,Object>> resultMap = new ArrayList<HashMap<String,Object>>();

        /////////////////////////////////////////////////////////////////////////////////
        // 1. 파라미터 확인
        int maxFilesCnt = configFile.getMaxFilesCnt();

        // 개수 확인
        if(files.length  > maxFilesCnt) {
            logger.error("[Service][Store File] Count is over");
            return null;
        }

        // 파일 확인
        if(!checkData.isExist(files)) {
            logger.error("[Service][Store File] No File");
            return null;
        }

        // 카테고리 확인
        if(!checkData.isExist(param.get(configFile.getMapCategoryVar()))) {
            // 카테고리를 사용하지 않는 일반 적인 파일의 경우 -> No category
            category = configFile.getSelectNoCategory();
        }
        else {
            category = Integer.parseInt(param.get(configFile.getMapCategoryVar()).toString());
        }

        // sub url 가져오기
        subUrl = configFile.getSubUrlBySelection(category);

        int selection = 0;

        if(configFile.getSelectSavingWay() == configFile.getSelectSavingFtp()) {
            selection = 1;
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingStorage()){
            selection = 2;
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingAws()) {
            selection = 3;
        }
        else {
            logger.error("[Service][Store File] Wrong property");
            return null;
        }

        // Context
        FtpHandler ftpHandler = null;
        StorigeHandler storigeHandler = null;
        AwsHandler awsHandler = null;

        // Connect
        switch (selection) {
            case 1:
                // FTP 이미지 서버 사용 시
                ftpHandler = new FtpHandlerImpl();

                // 연결
                rtn = ftpHandler.connect(configFile.getSettingFtpServer(), configFile.getSettingFtpUserId()
                        ,configFile.getSettingFtpPassword());

                if(rtn != ReturnType.RTN_TYPE_OK)
                {
                    logger.error("[Service][Store File] Fail to connect");
                    return null;
                }
                break;

            case 2:
                storigeHandler = new StorigeHandlerImpl();
                break;

            case 3:
                awsHandler = new AwsHandlerImpl();

                // Connect
                awsHandler.connect(configFile.getSettingAwsFileBucketName(), configFile.getSettingAwsAccessKey(),
                        configFile.getSettingAwsSecretKey(), configFile.getRegions());
                break;
        }

        for(MultipartFile file : files) {
            if(file.isEmpty()){
                break;
            }

            HashMap<String, Object> tempResultMap = new  HashMap<>();

            // original name
            fileOriginName = file.getOriginalFilename();

            // 암호화 된 랜덤 파일이름 생성
            fileName = passwordHandler.makeRandomKey(configFile.getLengthFilename());

            // 파일 저장
            switch (selection) {
                case 1:
                    rtn = ftpHandler.store(file, subUrl, fileName);
                    fileUrl = configFile.getImageServerUrl() + "/" + subUrl + "/" + fileName;
                    subFileUrl = "/" + subUrl + "/" + fileName;
                    break;

                case 2:
                    rtn = storigeHandler.store(file,configFile.getStroageLoc()+"/"+subUrl,fileName);
//                    fileUrl =  configFile.getUrlStorage()+ "/" + subUrl +  "/" + fileName;
                    fileUrl =  configFile.getStroageLoc() + "/" + subUrl +  "/" + fileName;
                    subFileUrl = "/" + subUrl + "/" + fileName;
                    break;

                case 3:
                    rtn = awsHandler.store(file, subUrl, fileName);
                    fileUrl = configFile.getUrlAwsS3() + "/" + subUrl+ "/"+fileName;
                    subFileUrl = "/" + subUrl + "/" + fileName;
                    break;
            }

            if(rtn != ReturnType.RTN_TYPE_OK)
            {
                logger.error("[Service][Store File] Fail to store");
                return null;
            }

            //saveFileInfoToDB(file, fileName, param);

            tempResultMap.put("fileUrl", fileUrl);
            tempResultMap.put("subFileUrl", subFileUrl);
            tempResultMap.put("fileOriginName", fileOriginName);
            tempResultMap.put("fileName",fileName);
            resultMap.add(tempResultMap);
        }

        return resultMap;
    }


    /**
     *  파일 삭제
     *
     *  Storage - 실제 파일을 삭제 한다.
     *  DB - 파일 정보의 DELETE_YN을 Y로 설정 한다.
     *
     * @param param
     * @return
     * @throws Exception
     */
    public ReturnType deleteFile(Map<String, Object> param) throws  Exception{

        ReturnType rtn ;
        String subUrl;
        int category;

        /////////////////////////////////////////////////////////////////////////////////
        // 1. 파라미터 확인

        // 카테고리 확인
        if(!checkData.isExist(param.get(configFile.getMapCategoryVar()))) {
            // 카테고리를 사용하지 않는 일반 적인 파일의 경우 -> No category
            category = configFile.getSelectNoCategory();
        }
        else {
            category = Integer.parseInt( param.get(configFile.getMapCategoryVar()).toString() );
        }

        // 파일 이름
        if(!checkData.isExist(param.get(configFile.getMapFileNameVar())))
        {
            logger.error("[Service][DeleteFile] No FileName");
            return ReturnType.RTN_TYPE_NG;
        }

        String fileName = param.get(configFile.getMapFileNameVar()).toString();

        // 서브 url 가지고 오기
        subUrl = configFile.getSubUrlBySelection(category);

        /////////////////////////////////////////////////////////////////////////////////
        // 2. 기능 별 삭제 동작
        if(configFile.getSelectSavingWay() == configFile.getSelectSavingFtp()) {

            /////////////////////////////////////////////////////////////////////////////////
            // FTP 이미지 서버 사용 시
            /////////////////////////////////////////////////////////////////////////////////

            FtpHandler ftpHandler = new FtpHandlerImpl();

            // 연결
            rtn = ftpHandler.connect(configFile.getSettingFtpServer(), configFile.getSettingFtpUserId(), configFile.getSettingFtpPassword());

            if(rtn != ReturnType.RTN_TYPE_OK)
            {
                logger.error("[Service][DeleteFile] Fail to connect");
                return ReturnType.RTN_TYPE_NG;
            }

            // 파일 삭제
            rtn = ftpHandler.delete(subUrl, fileName);

            if(rtn != ReturnType.RTN_TYPE_OK)
            {
                logger.error("[Service][DeleteFile] fail");
                ftpHandler.close();
                return ReturnType.RTN_TYPE_NG;
            }

            ftpHandler.close();

        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingStorage()){

            /////////////////////////////////////////////////////////////////////////////////
            // 서버 스토리지 사용 시
            /////////////////////////////////////////////////////////////////////////////////
            // 로컬 파일 스토리지 이용

            StorigeHandler storigeHandler = new StorigeHandlerImpl();

            rtn = storigeHandler.delete(configFile.getStroageLoc()+"/"+subUrl,fileName);

            if(rtn != ReturnType.RTN_TYPE_OK)
            {
                logger.error("[Service][Delete File] Fail to delete");
                return ReturnType.RTN_TYPE_NG;
            }
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingAws()) {

            logger.info("[Service][Store File] AWS");

            AwsHandler awsHandler = new AwsHandlerImpl();

            // Connect
            awsHandler.connect(configFile.getSettingAwsFileBucketName(), configFile.getSettingAwsAccessKey(),
                    configFile.getSettingAwsSecretKey(), configFile.getRegions());

            awsHandler.delete(subUrl, fileName);
        }
        else{
            return ReturnType.RTN_TYPE_NG;
        }
        return ReturnType.RTN_TYPE_OK;
    }


    /**
     * 파일 다운로드 한다.
     *
     * @param
     * @param fileName
     * @return
     * @throws Exception
     */
    public FileDownloadRes loadFileAsResource(int category, String fileName) throws  Exception {

        String subUrl = null;

        logger.info("Load File As Resource");

        ////////////////////////////////////////////////////////////////////////
        // Get category
        ////////////////////////////////////////////////////////////////////////
        /*
        if(!checkData.isExist(categoryStr)) {

            // 카테고리를 사용하지 않는 일반 적인 파일의 경우 -> No category
            category = configFile.getSelectNoCategory();
        }
        else {
            category = Integer.parseInt(categoryStr);
        }
        */

        if(category < 0) {
            category = configFile.getSelectNoCategory();
        }

        ////////////////////////////////////////////////////////////////////////
        // Get filename
        ////////////////////////////////////////////////////////////////////////
        if(!checkData.isExist(fileName)) {
            return null;
        }

        // 폴더 위치를 가지고 온다.
        subUrl = configFile.getSubUrlBySelection(category);

        // 이미지 FTP 서버 사용 시
        if(configFile.getSelectSavingWay() == configFile.getSelectSavingFtp()) {

            ;
        }

        // 로컬 폴더 사용 시
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingStorage()) {

            File file = new File(configFile.getStroageLoc() + "/" + subUrl + "/" + fileName);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            FileDownloadRes fileDownloadRes = new FileDownloadRes();
            fileDownloadRes.setInputStreamResource(resource);
            return fileDownloadRes;
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingAws()) {

            AwsHandler awsHandler = new AwsHandlerImpl();

            // Connect
            awsHandler.connect(configFile.getSettingAwsFileBucketName(), configFile.getSettingAwsAccessKey(),
                    configFile.getSettingAwsSecretKey(), configFile.getRegions());

            // get data
            return awsHandler.download(subUrl, fileName);
        }
        // 설정이 잘 못 되었을 때
        else {

            logger.error("[Service][Store File] Wrong property");
            return null;
        }

        return null;
    }


}

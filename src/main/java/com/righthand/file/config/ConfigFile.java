package com.righthand.file.config;

import com.amazonaws.regions.Regions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix="file")
@PropertySource("classpath:properties/configFile.properties")
public class ConfigFile {

    // 저장 방식 결정
    private int selectSavingWay ; //  1
    // FTP & 파일 서버 방식
    private int selectSavingFtp; // 1
    // 서버 내 파일 저장
    private int selectSavingStorage; // 2

    //// FTP
    // FTP 설정정보
    private String settingFtpServer; // causeworks.speedgabia.com
    private String settingFtpUserId; // causeworks
    private String settingFtpPassword; // works0415!!

    //이미지 서버 url
    private String imageServerUrl; // http://causeworks.speedgabia.com
    // Storage 위치 정보
    private String stroageLoc; // D:/files



    private int lengthFilename; // 32

    // map info
    private String mapFileNoVar ; // fileNo
    private String mapCategoryVar ; // category
    private String mapFileNameVar ; // fileName
    private String mapFileOrgNameVar ; // orgName
    private String mapDeleteYnVar ; // deleteYn
    private String mapUseYnVar ; // useYn
    private String mapDocNoVar ; // docNo
    private String mapDisplayNoVar ; // displayNo
    private String mapUpdIdVar ; // updId

    private String mapFileNoDb ; // FILE_NO
    private String mapCategoryDb ; // CATEGORY
    private String mapFileNameDb ; // FILE_NAME
    private String mapFileOrgNameDb ; // FILE_ORG_NAME
    private String mapDeleteYnDb ; // DELETE_YN
    private String mapUrl ; //= URL

    private String mapStoreTimeVar;
    private String mapStoreTimeDB;
    private String mapUseYnDb ; // USE_YN
    private String mapDocNoDb ; // DOC_NO
    private String mapDisplayNoDb ; // DISPLAY_NO
    private String mapUpdIdDb ; // UPD_ID

    private int selectSavingAws  ; // 3

    private String settingAwsAccessKey ; // AKIAI4I7EYQP3RKVRMHQ
    private String settingAwsSecretKey ; // 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

    private int maxFilesCnt ;//=10
    private String urlStorage; //

    private String urlAwsS3 ;
    private int selectAwsRegion;

    public Regions getRegions() {

        switch(selectAwsRegion) {
            case 0:
                return Regions.AP_NORTHEAST_1;
            case 1:
                return Regions.AP_NORTHEAST_2;
            case 2:
                return Regions.AP_SOUTH_1;
            case 3:
                return Regions.AP_SOUTHEAST_1;
            case 4:
                return Regions.AP_SOUTHEAST_2;
            case 5:
                return Regions.CA_CENTRAL_1;
            case 6:
                return Regions.CN_NORTH_1;
            case 7:
                return Regions.CN_NORTHWEST_1;
            case 8:
                return Regions.EU_CENTRAL_1;
            case 9:
                return Regions.EU_WEST_1;
            case 10:
                return Regions.EU_WEST_2;
            case 11:
                return Regions.EU_WEST_3;
            case 12:
                return Regions.GovCloud;
            case 13:
                return Regions.SA_EAST_1;
            case 14:
                return Regions.US_EAST_1;
            case 15:
                return Regions.US_EAST_2;
            case 16:
                return Regions.US_WEST_1;
            case 17:
                return Regions.US_WEST_2;
        }

        return Regions.AP_NORTHEAST_1;
    }

    private String settingAwsImageBucketName ; //= testtodosbucket
    private String settingAwsFileBucketName ; //= testtodosbucket

    private int selectCategory0; //0
    private int selectCategory1; //1
    private int selectCategory2; //2
    private int selectCategory3; //3
    private int selectCategory4; //4
    private int selectCategory5; //5
    private int selectCategory6; //6
    private int selectCategory7; //7
    private int selectCategory8; //8

    private String subUrlCategory0; //general
    private String subUrlCategory1; //feed
    private String subUrlCategory2; //profile
    private String subUrlCategory3; //photo
    private String subUrlCategory4; //ideabook
    private String subUrlCategory5; //TBD
    private String subUrlCategory6; //TBD
    private String subUrlCategory7; //TBD
    private String subUrlCategory8; //TBD


    public String getSubUrlBySelection(int select) {

        if(select == this.selectCategory0) {
            return subUrlCategory0;
        }
        else if (select == this.selectCategory1) {
            return subUrlCategory1;
        }
        else if (select == this.selectCategory2) {
            return subUrlCategory2;
        }
        else if (select == this.selectCategory3) {
            return subUrlCategory3;
        }
        else if (select == this.selectCategory4) {
            return subUrlCategory4;
        }
        else if (select == this.selectCategory5) {
            return subUrlCategory5;
        }
        else if (select == this.selectCategory6) {
            return subUrlCategory6;
        }
        else if (select == this.selectCategory7) {
            return subUrlCategory7;
        }
        else if (select == this.selectCategory8) {
            return subUrlCategory8;
        }

        return subUrlCategory0;
    }

    public String getHostName() {

        if(selectSavingWay == selectSavingFtp) {
            return this.imageServerUrl;
        }
        else if(selectSavingWay == selectSavingStorage) {
            return this.urlStorage;
        }

        return this.urlAwsS3;
    }

    public int getSelectNoCategory() {
        return selectCategory0;
    }
}

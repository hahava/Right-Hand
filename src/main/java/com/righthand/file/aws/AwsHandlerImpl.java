package com.righthand.file.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.righthand.common.type.ReturnType;
import com.righthand.file.dto.FileDownloadRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;


public class AwsHandlerImpl implements AwsHandler {

    AmazonS3 s3 ;
    String bucket;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // File metadata를 확인 한다.
    private ObjectMetadata getObjectMetadata(MultipartFile file){

        ObjectMetadata metaData =  new ObjectMetadata();
        metaData.setContentLength(file.getSize());
        metaData.setContentType(file.getContentType());
        logger.info("[FileInfo] File Size : " + file.getSize());
        logger.info("[FileType] File Type : " + file.getContentType());
        return metaData;
    }

    /**
     *  Aws S3 서비스 접속
     *
     * @param bucketName
     * @param accessKey
     * @param secretKey
     * @param region
     * @return
     * @throws Exception
     */
    public ReturnType connect(String bucketName, String accessKey, String secretKey , Regions region) throws Exception {

        BasicAWSCredentials awsCreds  = new BasicAWSCredentials(accessKey, secretKey);

        bucket = bucketName;

        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();

        if(s3 == null) {
            return ReturnType.RTN_TYPE_NG;
        }

        return ReturnType.RTN_TYPE_OK;
    }

    
    /**
     *  AWS S3 스토리지에 파일 저장 하는 함수
     *
     *  S3 상의 버킷에 파일을 저장 한다.
     *  이 때, 외부에서 올려진 파일을 접근 할 수 있도록, 모든 유저에게 읽기 권한을 줘야 프론트
     *  에서 이미지를 확인 할 수 있다.
     *
     * @param file
     * @param subUrl
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType store(MultipartFile file, String subUrl , String fileName) throws Exception{

        String fullFileName = subUrl + "/"+ fileName;
        logger.info("[StoreFile] FileName:" + fullFileName);
        int orientation;

        PutObjectResult putObjectResult = s3.putObject(bucket, fullFileName, file.getInputStream(), getObjectMetadata(file));

        // Save object
        //putObjectResult.
        // 파일 권한 설정

        // 유저 별 권한 설정
        Collection<Grant> grantCollection = new ArrayList<Grant>();

        // 소유주에게는 모든 권한
        Grant grant1 = new Grant(new CanonicalGrantee(s3.getS3AccountOwner().getId()), Permission.FullControl);
        grantCollection.add(grant1);

        // 모든 유저에게는 읽기 권한만 부여
        Grant grant2 = new Grant(GroupGrantee.AllUsers, Permission.Read);
        grantCollection.add(grant2);

        AccessControlList bucketAcl = s3.getBucketAcl(bucket);

        bucketAcl.getGrantsAsList().clear();
        bucketAcl.getGrantsAsList().addAll(grantCollection);

        // 저장한 파일에 권한을 설정한다.
        s3.setObjectAcl(bucket,fullFileName, bucketAcl);

        return ReturnType.RTN_TYPE_OK;
    }

    /**
     *  파일 삭제.
     *
     *  AWS S3 서버에 올려진 파일을 삭제한다.
     *
     * @param subUrl
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType delete(String subUrl , String fileName ) throws Exception {
        String fullFileName = subUrl + "/"+ fileName;

        logger.info("[Delete] :" + fullFileName);
        s3.deleteObject(bucket,fullFileName);
        return ReturnType.RTN_TYPE_OK;
    }

    /**
     *  파일 삭제.
     *
     *  AWS S3 서버에 올려진 파일을 삭제한다.
     *
     * @param subUrl
     * @param fileName
     * @return
     * @throws Exception
     */
    public FileDownloadRes download(String subUrl , String fileName ) throws Exception {
        String fullFileName = subUrl + "/"+ fileName;

        logger.info("[Download] Bucket :" + bucket);
        logger.info("[Download] File :" + fullFileName);
        S3Object object = s3.getObject(bucket, fullFileName);

        ObjectMetadata objectMetadata = object.getObjectMetadata();

        logger.info(objectMetadata.getContentType() + "/" + objectMetadata.getContentLength());

        FileDownloadRes fileDownloadRes = new FileDownloadRes();

        fileDownloadRes.setInputStreamResource(new InputStreamResource(object.getObjectContent()));
        fileDownloadRes.setContentType(objectMetadata.getContentType());
        fileDownloadRes.setContentLength(objectMetadata.getContentLength());

        return fileDownloadRes;
    }
}

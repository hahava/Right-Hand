package com.righthand.file.aws;

import com.amazonaws.regions.Regions;
import com.righthand.common.type.ReturnType;
import com.righthand.file.dto.FileDownloadRes;
import org.springframework.web.multipart.MultipartFile;

public interface AwsHandler {
    public ReturnType connect(String bucketName, String accessKey, String secretKey, Regions region) throws Exception;
    public ReturnType store(MultipartFile file, String subUrl, String fileName) throws Exception;
    public ReturnType delete(String subUrl, String fileName) throws Exception;
    public FileDownloadRes download(String subUrl, String fileName) throws Exception;
}

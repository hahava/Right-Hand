package com.righthand.mypage.service;

import com.righthand.common.type.ReturnType;
import com.righthand.file.service.FileService;
import com.righthand.membership.dao.MembershipDao;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.domain.file.TbFileGrp;
import com.righthand.mypage.domain.file.TbFileGrpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TbFileGrpService {

    private final MembershipDao membershipDao;
    private final MembershipService membershipService;
    private final TbFileGrpRepository tbFileGrpRepository;
    private final FileService fileService;
    final String bucketUrl = "https://s3.ap-northeast-2.amazonaws.com/right-hand-dev";

    @Transactional
    public ReturnType save(TbFileGrp tbFileGrp, MembershipInfo membershipInfo,
                           ArrayList<HashMap<String, Object>> urlMap, Integer fileGrpSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("profileSeq", membershipInfo.getProfileSeq());
        map.put("fileGrpSeq", fileGrpSeq);
        // File Group이 존재 하지 않을 때
        // File Group을 생성 한다.
        if (fileGrpSeq == null) {
            TbFileGrp save = tbFileGrpRepository.save(tbFileGrp);
            map.replace("fileGrpSeq", save.getFileGrpSeq());
            try {
                //TB_PROFILE에 File GRP SEQ 삽입
                membershipService.saveFileGrpSeq(map);
            } catch (Exception e) {
                log.error("[SaveFileGrpSeq][Exception] : {}", e.toString());
            }
        }

        //TB_FILE 생성
        map.put("filePath", bucketUrl + urlMap.get(0).get("subFileUrl"));
        map.put("orgImgNm", urlMap.get(0).get("fileOriginName"));
        map.put("sysImgNm", null);
        map.put("rmk", null);
        map.put("creId", null);
        map.put("updId", null);
        try {
            int fileSeq = fileService.insertFileInfo(map);
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("fileSeq", fileSeq);
            tempMap.put("profileSeq", membershipInfo.getProfileSeq());
            membershipDao.updateFileSeq(tempMap);
        } catch (Exception e) {
            log.error("[InsertFileInfo][Exception] : {}", e.toString());
            return ReturnType.RTN_TYPE_NG;
        }

        return ReturnType.RTN_TYPE_OK;
    }
}

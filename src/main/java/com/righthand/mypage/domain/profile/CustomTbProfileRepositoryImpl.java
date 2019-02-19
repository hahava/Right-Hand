package com.righthand.mypage.domain.profile;

import com.querydsl.jpa.JPQLQuery;
import com.righthand.mypage.domain.file.QTbFile;
import com.righthand.mypage.domain.file.TbFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class CustomTbProfileRepositoryImpl extends QuerydslRepositorySupport implements CustomTbProfileRepository {

    public CustomTbProfileRepositoryImpl() {
        super(TbProfile.class);
    }

    @Override
    public List<TbFile> findAllJoinProfileAndFile(long profileSeq) {
        QTbProfile qTbProfile = new QTbProfile("profile");
        QTbFile qTbFile = new QTbFile("file");
        JPQLQuery query = from(qTbFile);
        query.join(qTbFile.profileList, qTbProfile)
        .on(qTbProfile.profileSeq.eq(profileSeq));
        List<TbFile> result = query.fetch();
        return result;
    }
}
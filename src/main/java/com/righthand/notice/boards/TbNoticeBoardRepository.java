package com.righthand.notice.boards;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TbNoticeBoardRepository extends JpaRepository <TbNoticeBoard, Long>{

    // JPA Paging 사용
    Page<TbNoticeBoard> findAll(Pageable pageable);

    // 상세 검색
    TbNoticeBoard findByBoardSeq(long boardSeq);

    // 검색어
    @Query(value = "SELECT * FROM TB_NOTICE_BOARD t WHERE t.BOARD_TITLE LIKE CONCAT('%', ?1, '%') OR t.BOARD_CONTENT LIKE CONCAT('%', ?1, '%') ",
            countQuery = "SELECT COUNT(*) FROM TB_NOTICE_BOARD t WHERE t.BOARD_TITLE LIKE CONCAT('%', ?1, '%') OR t.BOARD_CONTENT LIKE CONCAT('%', ?1, '%')",
            nativeQuery = true)
    Page<TbNoticeBoard> findByBoardTitleOrBoardContent(String searchedWord, Pageable pageable);
}

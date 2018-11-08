package com.righthand.notice.domain.boards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface TbNoticeBoardRepository extends JpaRepository <TbNoticeBoard, Long>{

    @Query(value = "SELECT * FROM TB_NOTICE_BOARD t ORDER BY t.BOARD_DATE DESC LIMIT ?1, ?2", nativeQuery = true)
    List<TbNoticeBoard> findAllBoardDateDesc(int start, int offset);

    @Query(value = "SELECT * FROM TB_NOTICE_BOARD t WHERE t.BOARD_SEQ = ?1", nativeQuery = true)
    List<TbNoticeBoard> findByBoardSeq(long boardSeq);

    @Query(value = "SELECT * FROM TB_NOTICE_BOARD t WHERE t.BOARD_TITLE LIKE CONCAT('%', ?1, '%') OR t.BOARD_CONTENT LIKE CONCAT('%', ?1, '%') ORDER BY t.BOARD_DATE DESC LIMIT ?2, ?3", nativeQuery = true)
    List<TbNoticeBoard> findAllBySearchedWord(String searchedWord, int start, int offset);

    @Query(value = "SELECT COUNT(*) FROM TB_NOTICE_BOARD t WHERE t.BOARD_TITLE LIKE CONCAT('%', ?1, '%') OR t.BOARD_CONTENT LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    int countSearchedBoard(String searchedWord);

}

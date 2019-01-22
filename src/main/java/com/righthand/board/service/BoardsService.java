package com.righthand.board.service;

import com.righthand.board.domain.boards.DevBoard;
import com.righthand.board.domain.boards.ItBoard;
import com.righthand.board.domain.boards.repository.DevBoardRepository;
import com.righthand.board.domain.boards.repository.ItBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardsService {

    private final DevBoardRepository devBoardRepository;
    private final ItBoardRepository itBoardRepository;

    @Transactional
    void insertBoard(String boardType, Map<String, Object> params) {
        switch (boardType) {
            case "tech":
                doInsert(boardType, params);
                break;
            case "dev":
                doInsert(boardType, params);
                break;
            default:
                log.error("[InsertNothingBoard][Exception]");
                break;
        }
    }

    private void doInsert(String boardType, Map<String, Object> params) {
        String boardTitle = (String) params.get("boardTitle");
        String boardContent = (String) params.get("boardContent");
        String boardContent4Searching = (String) params.get("boardContent4Searching");
        Long boardProfileSeq = (Long) params.get("boardProfileSeq");
        if (boardType.equals("tech")) {
            devBoardRepository.save(
                    DevBoard.builder()
                            .boardTitle(boardTitle)
                            .boardContent(boardContent)
                            .boardContent4Searching(boardContent4Searching)
                            .boardProfileSeq(boardProfileSeq)
                            .build()
            );
        }
        if (boardType.equals("dev")) {
            itBoardRepository.save(
                    ItBoard.builder()
                            .boardTitle(boardTitle)
                            .boardContent(boardContent)
                            .boardContent4Searching(boardContent4Searching)
                            .boardProfileSeq(boardProfileSeq)
                            .build()
            );
        }
    }


}

package com.righthand.board.domain.boards.repository;

import com.righthand.board.domain.boards.ItBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItBoardRepository extends JpaRepository<ItBoard, Long> {
}

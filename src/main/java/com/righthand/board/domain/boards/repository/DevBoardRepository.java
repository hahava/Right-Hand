package com.righthand.board.domain.boards.repository;

import com.righthand.board.domain.boards.DevBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevBoardRepository extends JpaRepository<DevBoard, Long> {
}

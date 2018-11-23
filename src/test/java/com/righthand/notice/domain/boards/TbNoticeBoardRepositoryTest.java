package com.righthand.notice.domain.boards;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TbNoticeBoardRepositoryTest {

    @Autowired
    TbNoticeBoardRepository tbNoticeBoardRepository;

    @After
    public void cleanup() {
        /**
         이후 테스트 코드에 영향을 끼치지 않기 위해
         테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
         **/
        tbNoticeBoardRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        //given
        tbNoticeBoardRepository.save(TbNoticeBoard.builder()
                .boardTitle("Test")
                .boardContent("Content")
                .build());

        //when
        List<TbNoticeBoard> tbNoticeBoardList = tbNoticeBoardRepository.findAll();

        //then
        TbNoticeBoard tbNoticeBoard = tbNoticeBoardList.get(0);
        assertThat(tbNoticeBoard.getBoardTitle(), is("Test"));
        assertThat(tbNoticeBoard.getBoardContent(), is("Content"));
    }

}
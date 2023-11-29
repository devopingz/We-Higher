package com.example.demo.reply;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.Board;
import com.example.demo.member.Member;

@Repository
public interface ReplyDao extends JpaRepository<Reply, Integer> {

    @Transactional
    @Modifying
    @Query(value = "update board set cnt=cnt+1 where num=:num", nativeQuery = true)
    void updateCnt(@Param("num") int num);

    ArrayList<Reply> findByBoardNum(int num);  //이름으로 검색

}


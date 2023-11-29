package com.example.demo.board;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {

    @Autowired
    private BoardDao dao;

    @Autowired
    private NotifyDao ndao;

    // pk로 검색. dao.findById()
    public BoardDto getBoard(int num) {
        Board b = dao.findById(num).orElse(null);
        return new BoardDto(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt());
    }
    
    // pk로 검색. dao.findById()
    public Board getBoard2(int num) {
        Board b = dao.findById(num).orElse(null);
        return b;
    }

    // pk로 공지사항 검색. dao.findById()
    public NotifyDto getNotify(int num) {
        Notify b = ndao.findById(num).orElse(null);
        return new NotifyDto(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt());
    }

    // 전체검색. dao.findAll()
    public ArrayList<BoardDto> getAll() {
        List<Board> list = dao.findAll();
        ArrayList<BoardDto> list2 = new ArrayList<>();
        for (Board b : list) {
            list2.add(new BoardDto(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
        }
        return list2;
    }

    // 공지사항 전체검색. ndao.findAll()
    public ArrayList<NotifyDto> getAllnotify() {
        List<Notify> list = ndao.findAll();
        ArrayList<NotifyDto> list2 = new ArrayList<>();
        for (Notify b : list) {
            list2.add(new NotifyDto(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
        }
        return list2;
    }

    // 옵션으로 검색
    public Page<BoardDto> getByOption(String type, String option, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Board> list;
        if (Objects.equals("name", type)) {
            list = dao.findByMemberNameLike("%" + option + "%", pageable);
        } else if (Objects.equals("title", type)) {
            list = dao.findByTitleLike("%" + option + "%", pageable);
        } else {
            list = dao.findAll(pageable);
        }
        return list.map(BoardDto::of);
    }

    // 공지사항 옵션으로 검색
    public Page<NotifyDto> getByOption2(String type, String option, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Notify> list;
        if (Objects.equals("name", type)) {
            list = ndao.findByMemberNameLike("%" + option + "%", pageable);
        } else if (Objects.equals("title", type)) {
            list = ndao.findByTitleLike("%" + option + "%", pageable);
        } else {
            list = ndao.findAll(pageable);
        }
        return list.map(NotifyDto::of);
    }

    // 게시글 추가, 수정. dao.save()
    public BoardDto saveBoard(BoardDto b, int check) {

        if (check == 0) { //추가

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedTime1 = LocalDateTime.now().format(formatter1);
            b.setUdate(formattedTime1);
            b.setWdate(formattedTime1);
            Board b2 = dao.save(new Board(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
            return new BoardDto(b2.getNum(), b2.getWdate(), b2.getUdate(), b2.getMember(), b2.getTitle(), b2.getContent(), b2.getCnt());
        } else if (check == 1) {  //수정

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedTime1 = LocalDateTime.now().format(formatter1);
            b.setUdate(formattedTime1);
            Board b2 = dao.save(new Board(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
            return new BoardDto(b2.getNum(), b2.getWdate(), b2.getUdate(), b2.getMember(), b2.getTitle(), b2.getContent(), b2.getCnt());
        } else return null;
    }

    // 공지사항 추가, 수정. ndao.save()
    public NotifyDto saveNotify(NotifyDto b, int check) {

        if (check == 0) { //추가

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedTime1 = LocalDateTime.now().format(formatter1);
            b.setUdate(formattedTime1);
            b.setWdate(formattedTime1);
            Notify b2 = ndao.save(new Notify(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
            return new NotifyDto(b2.getNum(), b2.getWdate(), b2.getUdate(), b2.getMember(), b2.getTitle(), b2.getContent(), b.getCnt());
        } else if (check == 1) {  //수정

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedTime1 = LocalDateTime.now().format(formatter1);
            b.setWdate(formattedTime1);
            Notify b2 = ndao.save(new Notify(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
            return new NotifyDto(b2.getNum(), b2.getWdate(), b2.getUdate(), b2.getMember(), b2.getTitle(), b2.getContent(), b.getCnt());
        } else return null;
    }

    // 게시글 pk 기준 삭제. dao.deleteById()
    public void delBoard(int num) {
        dao.deleteById(num);
    }

    // 공지사항 pk 기준 삭제. dao.deleteById()
    public void delNotify(int num) {
        ndao.deleteById(num);
    }

    // 조회수 증가
    public void editCnt(int num) {
        dao.updateCnt(num);
    }

    // 공지사항 조회수 증가
    public void editCnt2(int num) {
        ndao.updateCnt(num);
    }

    public Page<Board> getBoardList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.dao.findAll(pageable);
    }

    public Page<Notify> getNotifyList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.ndao.findAll(pageable);
    }

    public Page<Notify> getNotifyMain(Pageable pageable) {
        return this.ndao.findAll(pageable);
    }

}

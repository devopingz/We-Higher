package com.example.demo.reply;

import com.example.demo.board.Board;
import com.example.demo.board.BoardDto;
import com.example.demo.board.BoardService;
import com.example.demo.commute.Commute;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PreAuthorize("isAuthenticated()")
@Controller
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private BoardService bservice;
    @Autowired
    private MemberService mservice;
    @Autowired
    private ReplyService rservice;
    
    @GetMapping("/list/{com_bno}")
    @ResponseBody
	public ModelAndView getList(@PathVariable int com_bno) {
		System.out.println("댓글 목록 컨트롤러 동작");
		List<Reply> list = rservice.getList(com_bno);
		int total = rservice.getTotal(com_bno);
		
		ModelAndView mav = new ModelAndView("/board/edit?num="+com_bno);
		System.out.println("댓글 갯수 " + rservice.getTotal(com_bno));
		mav.addObject("list", list);
		mav.addObject("total", total);
		
		return mav;
	}
    
    //댓글 등록
    @PostMapping("/add")
    @ResponseBody
    public String addReply(Principal principal, @RequestBody List<Map<String, Object>> param) {
    	
    	int check = 0;
    	/*MemberDto mdto = mservice.getMember(principal.getName());
    	BoardDto bdto = bservice.getBoard(num);*/
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		int num = Integer.parseInt( (String) param.get(0).get("com_bno") );
		String content = (String) param.get(0).get("com_content");
		Member member = (Member) authentication.getPrincipal();

    	Board board = bservice.getBoard2(num);
    	System.out.println("num" + num);
    	System.out.println("content" + content);
		ReplyDto dto = ReplyDto.builder().num(num).wdate(null).udate(null).board(board).title("").content(content).member(member)
				.build();
		ReplyDto r = rservice.saveReply(dto, check);
    	/*c.setContent(content);
    	c.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(), mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(), mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(), mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(), mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(), mdto.getMonthMember()));
    	c.setBoard(new Board(bdto.getNum(), bdto.getWdate(), bdto.getUdate(), bdto.getMember(), bdto.getTitle(), bdto.getContent(), bdto.getCnt()));
    	cservice.saveReply(c, check);*/
    	return "success";
    }
    
    //댓글 수정
    @PostMapping("/edit")
    @ResponseBody
    public void editReply(ReplyDto c) {
        int check = 1;
        ReplyDto c2 = rservice.getReply(c.getNum());       
        c2.setContent(c2.getContent());
        rservice.saveReply(c2, check);
    }
 
    //댓글 삭제
    @RequestMapping("/del")	
    @ResponseBody
    public void del(int num) {
    	rservice.delCommute(num);
    }
}








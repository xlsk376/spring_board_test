package com.board.test.board;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BoardController {

	@Autowired
	BoardDAO boardDAO;
	
	@ModelAttribute("cp")
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	
	@RequestMapping(value = "/board/boardList")
	public String boardList(HttpServletRequest request, Model model) {
		System.out.println("==== boardList() ====");
		
		// 한 페이지에 보여줄 게시글 개수
		int pageSize = 3;
		
		// 현재 페이지 번호
		int currentPage = 1;
		if(request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));	
		}
		
		// 전체 게시글 수
		int count = boardDAO.getAllCount();
			
		// MySQL에서 꺼내올 게시글의 시작번호(= 0부터 시작하는 인덱스 값)
		int startRow = (currentPage - 1) * pageSize;
	
		ArrayList<Board> boardList = boardDAO.getAllBoard(startRow, pageSize);
		
		// boardList.jsp 페이지에서 현재 페이지의 게시글 시작 number
		int number = count - (currentPage - 1) * pageSize;	
		
		// 페이지 하단에 클릭 가능한 번호의 개수		예) [1][2][3][4][5][다음]
		int clickablePageCount = 5;
		// 전체 페이지 수
		int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
		
		// 페이지 하단 번호의 시작 값
		int startPage = 0;
		if(currentPage % clickablePageCount != 0) {
			startPage = (currentPage / clickablePageCount) * clickablePageCount + 1;
		}else {
			startPage = (currentPage / clickablePageCount - 1) * clickablePageCount + 1;
		}
		
		int endPage = startPage + clickablePageCount - 1;
		if(endPage > pageCount) endPage = pageCount;
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("number", number);
		model.addAttribute("count", count);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageSize", pageSize);
		
		model.addAttribute("clickablePageCount", clickablePageCount);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "board/boardList";
	}
	
	@RequestMapping(value = "/board/boardInfo")
	public String boardInfo(HttpServletRequest request, Model model) {
		System.out.println("==== boardInfo() ====");
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		Board bean = boardDAO.getOneBoard(num);
		
		model.addAttribute("bean", bean);
		
		return "board/boardInfo";
	}
	
	@RequestMapping(value = "/board/boardWriteForm")
	public String boardWriteForm() {
		System.out.println("==== boardWriteForm() ====");
		
		return "board/boardWriteForm";
	}
	
	@RequestMapping(value = "/board/boardWritePro", method = RequestMethod.POST)
	public String boardWritePro(HttpServletRequest request, Model model, Board bean) {
		System.out.println("==== boardWritePro() ====");
		
//		Board bean = new Board();
//		bean.setWriter(request.getParameter("writer"));
//		bean.setSubject(request.getParameter("subject"));
//		bean.setEmail(request.getParameter("email"));
//		bean.setPassword(request.getParameter("password"));
//		bean.setContent(request.getParameter("content"));	
		
		boardDAO.insertBoard(bean);
		
		model.addAttribute("bean", bean);
		
		return "redirect:/board/boardList";
	}

	@RequestMapping(value = "/board/reWriteForm")
	public String reWriteForm(HttpServletRequest request, Model model) {
		System.out.println("==== reWriteForm() ====");
		
		int num = Integer.parseInt(request.getParameter("num"));
		Board bean = boardDAO.getOneBoard(num);
		
		int ref = bean.getRef();
		int re_step = bean.getRe_step();
		int re_level = bean.getRe_level();
		
		
		model.addAttribute("ref", ref);
		model.addAttribute("re_step",re_step);
		model.addAttribute("re_level", re_level);
		
		return "board/reWriteForm";
	}
	
	@RequestMapping(value = "/board/reWritePro", method = RequestMethod.POST)
	public String reWritePro(HttpServletRequest request, Model model, Board bean) {
		System.out.println("==== reWritePro() ====");
		
//		Board bean = new Board();
//		bean.setWriter(request.getParameter("writer"));
//		bean.setSubject(request.getParameter("subject"));
//		bean.setEmail(request.getParameter("email"));
//		bean.setPassword(request.getParameter("password"));
//		bean.setContent(request.getParameter("content"));
//
//		bean.setRef(Integer.parseInt(request.getParameter("ref")));
//		bean.setRe_step(Integer.parseInt(request.getParameter("re_step")));
//		bean.setRe_level(Integer.parseInt(request.getParameter("re_level")));
		
		boardDAO.reWriteBoard(bean);
		
		return "redirect:/board/boardList";
	}
	
}





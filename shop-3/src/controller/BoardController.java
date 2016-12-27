package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.PasswordFailException;
import logic.Board;
import logic.BoardService;


@Controller
public class BoardController {
	
	@Autowired
	public BoardService boardService;

	@RequestMapping("board/delete")
	public ModelAndView delete(int num, String pass){
		ModelAndView mav = new ModelAndView("board/deleteForm");
		String dbPass=boardService.getPassword(num);
		if(!pass.equals(dbPass)){
			throw new PasswordFailException();
		}
		boardService.delete(num);
		mav.setViewName("redirect:list.shop");
		return mav;
	}
	
	@RequestMapping("board/deleteForm")
	public String deleteForm(){
		return "board/deleteForm";
	}
	
	@RequestMapping("board/update")
	public ModelAndView update(@Valid Board board, BindingResult bindingResult, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("board/updateForm");
		if(bindingResult.hasErrors()){
			mav.getModel().putAll(bindingResult.getModel());
			mav.addObject("board",boardService.detail(board.getNum()));
			return mav;
		}
		String pass = boardService.getPassword(board.getNum());
		if(!pass.equals(board.getPass())){
			mav.setViewName("board/updateForm");
			bindingResult.rejectValue("pass", "error.board.password");
			mav.getModel().putAll(bindingResult.getModel());
			mav.addObject("board",boardService.detail(board.getNum()));
			return mav;
		}
		
		//������ ���� ���ε� �� ������ ����
		if(board.getFile1().isEmpty()){
			board.setFileUrl(request.getParameter("file2"));
		}else{//���� ���ε带 �ٽ���
			board.setFileUrl(board.getFile1().getOriginalFilename());
		}
		boardService.update(board,request);
		mav.addObject("board",board);
		mav.setViewName("redirect:list.shop");
		return mav;
	}
	
	@RequestMapping("board/updateForm")
	public ModelAndView updateForm(int num){
		ModelAndView mav = new ModelAndView();
		Board board = boardService.detail(num);
		mav.addObject("board",board);
		
		return mav;
	}
	
	@RequestMapping("board/reply")
	public ModelAndView reply(Board board){
		ModelAndView mav = new ModelAndView();
		boardService.reply(board);
		mav.setViewName("redirect:/board/list.shop");
		return mav;
	}
	
	@RequestMapping("board/replyForm")
	public ModelAndView replyForm(int num){
		ModelAndView mav = new ModelAndView();
		Board board = boardService.detail(num);
		mav.addObject("board",board);
		return mav;
	}
	
	@RequestMapping("board/filedown")
	public void filedown(String filename, HttpServletRequest request ,HttpServletResponse response){//filedown�� ��� view���� �ʿ䰡 �����Ƿ�, ����Ÿ���� �ʿ����
		

		//��Ĺ 8.0������ ���ڵ� �Ұ�. (���� ��Ĺ 7.0�����̶� �����Ѱ�)
		try {
			filename = new String (filename.getBytes("8859_1"),"euc-kr");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String filepath = request.getServletContext().getRealPath("/")+"/fileupload/"+filename;
		byte[] buf = new byte[4096];
		FileInputStream fis = null;
		OutputStream out = null;
		
		try{
			fis = new FileInputStream(filepath);
			out = response.getOutputStream();
			
			/*
			 * ���� �ٿ�ε� (���� ���� �������, �ѱ� ���ڵ� �������)�� �ϱ����� �Ʒ� 3���� ������ �� �ʿ���.
			 */
			
			//1. MIMEŸ�� ���� : ���� ���� ������� ��� �ٿ�޵��� ��
			response.setContentType("application/octet-stream"); // ���� �޴� ��� ���ϵ��� stream���·� �޾ƿ��ڴٴ� ��.
			
			response.setHeader("Content-Disposition", "attachment; filename="+new String(filename.getBytes("euc-kr"),"8859_1"));
			//2. ������ ÷���ϴµ�, �������� ���ϸ��� �ν��� �� �ֵ��� ���ڵ� ���¸� �������ش�.(euc-kr - > 8859_1�� �ٽ� ���������)
			
			int readcnt=0;
			
			//3. ������ �������� �������ִ� �ݺ���.
			while((readcnt=fis.read(buf))!=-1){
				out.write(buf,0,readcnt);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(fis!=null) 
					fis.close();
				if(out!=null){
					out.flush();
					out.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	};
	
	@RequestMapping("board/detail")
	public ModelAndView detail(int num){
		boardService.readCntAdd(num);
		Board board = boardService.detail(num);
		ModelAndView mav = new ModelAndView();
		mav.addObject("board",board);
		return mav;
	}
	
	@RequestMapping("board/write")
	public ModelAndView write(@Valid Board board, BindingResult bindingResult, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("board/add");
		if(bindingResult.hasErrors()){
			mav.getModel().putAll(bindingResult.getModel());
			return mav;
		}
		boardService.insert(board,request);
		mav.setViewName("redirect:/board/list.shop");
		return mav;
	}
	
	
	@RequestMapping("board/add")
	public ModelAndView add(){
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		mav.setViewName("board/add");
		return mav;
	}
	
	@RequestMapping("board/list")
	public ModelAndView list(Integer pageNum){
		if(pageNum == null || pageNum.toString().equals("")){
			pageNum=1;
		}
		ModelAndView mav = new ModelAndView();
		
		int limit = 10;
		int listcount = boardService.boardCount();
		List<Board> boardlist = boardService.list(pageNum, limit);
		int maxpage = (int)((double)listcount/limit + 0.95);
		int startpage = (((int)(pageNum/10.0 + 0.9))-1)*10+1;
		int endpage = startpage +9;
		if(endpage>maxpage) endpage = maxpage;
		int boardnum=listcount - ((pageNum-1)*limit);//�ش� ������ ���� ���� ��ȣ�� ã�� �Ѱ���
		SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdate.format(new Date());
		mav.addObject("today", today);
		mav.addObject("pageNum",pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage", endpage);
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardnum",boardnum);
		return mav;
	}
	
}

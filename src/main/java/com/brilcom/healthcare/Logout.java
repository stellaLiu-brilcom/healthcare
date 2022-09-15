package com.brilcom.healthcare;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//로그아웃을 처리하기 위한 Servlet doGet 메소드
		
		// 1. 세션 가져오기
		HttpSession session= request.getSession();
		// 2. 세션 파기
		session.invalidate();
		// 일반적인 로그아웃은 session을 찾아서 파기만 하면 된다.
		// 그런데 로그아웃에 대한 시점을 저장하고 싶다면 -> session에서 데이터(ID값)를 찾아서 DB 에 기록을 해야한다.
		
		// 3. 메인페이지로 이동
		response.sendRedirect("/");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

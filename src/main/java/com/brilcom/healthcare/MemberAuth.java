package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jepetto.bean.Facade;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.sql.XmlConnection;
import org.jepetto.util.PropertyReader;

public class MemberAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static PropertyReader reader = PropertyReader.getInstance();

	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = "logout";

	/*
	 * 로그아웃 처리
	 * 
	 * 1.현재 로그인 상태 여부 판단
	 * 
	 * 2.로그아웃 구현
	 * 
	 * HttpSession removeAttribute(name) or invalidate()
	 * 
	 */

	public MemberAuth() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");

		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute("userid") != null) {

			session.invalidate();

			out.print("<h3>로그아웃되었습니다.");

		} else {

			out.println("<h3>현재 로그인 상태가 아닙니다.");

		}

		out.close();
	}

	/*
	 * 로그인처리
	 * 
	 * 1.ID,비밀번호 추출
	 * 
	 * 2.유효성 체크=>입력페이지로
	 * 
	 * 3.DB 데이터와 비교=>입력페이지
	 * 
	 * 4.로그인 작업
	 * 
	 * 1)현재 로그인 상태 여부 판단
	 * 
	 * 2)로그인 구현
	 * 
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json; charset=utf-8");

		PrintWriter out = response.getWriter();

		//1.parameter 추출

		String userid = request.getParameter("userid");

		String pwd = request.getParameter("pwd");

		//2.유효성 체크

		if(userid.isEmpty()||pwd.isEmpty()){

			RequestDispatcher rd = request.getRequestDispatcher("\\WebContent\\user\\Login.jsp");

			rd.forward(request, response);

			return;

		}
		

		//3.DB데이터와 비교
		
		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();

		HashMap<String, String> dummy = new HashMap<String, String>();
		
		String arr[] = new String[] { userid, pwd };

		org.jdom2.Document doc;


		try{

			doc = remote.executeQuery(dataSource, QUERY_FILE, queryKey, dummy, arr);
			Connection con = new XmlConnection(doc);
			PreparedStatement stmt = con.prepareStatement("//recordset/row");
			ResultSet rs = stmt.executeQuery();

			stmt.setString(1, userid);

			if(rs.next()){

				if(rs.getString(2).trim().equals(pwd)){

					if(login(userid,request)){

						out.print("<h3>로그인 완료하였습니다.");

					}else{

						out.print("<h3>현재 로그인 상태입니다.");

					}

				}else{

					out.println("<h3>비밀번호가 틀립니다.");

				}

			}else{

				out.print("<h3>존재하지 않는 아이디입니다.");

			}

		}catch(Exception e){

			System.out.println(e);

		}

		out.close();

	}

	/*
	 * 4.로그인 작업
	 * 
	 * 1)현재 로그인 상태 여부 판단
	 * 
	 * 2)로그인 구현
	 * 
	 */

	public boolean login(String userid, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (session.isNew() || session.getAttribute("userid") == null) // 현재 로그인 상태가 아니라는 것

		{

			session.setAttribute("userid", userid);

			return true;

		} else {

			return false;

		}

	}
}

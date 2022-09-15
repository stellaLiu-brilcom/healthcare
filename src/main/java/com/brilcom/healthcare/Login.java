package com.brilcom.healthcare;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static String account_email = null;
	private static String password = null;
	static String scann;
	
	
    public Login() {
        super();

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	/*
		// 로그인을 처리하기 위한 Servlet
				// 1. 2개의 값을 가져와야한다 (userId, userPwd)
				String userid = request.getParameter("userid");
				String pwd = request.getParameter("pwd");

				// check(데이터 잘 넘어 왔나)
				
				  System.out.println("보내온 ID : "+userId);
				  System.out.println("보내온 PW : "+userPwd);
				 

				// 2. 비즈니스 로직 처리
				MemberService mService = new MemberServiceImpl();

				Member m = mService.selectOneMember(userid, pwd);

				if (m != null) { // 로그인 성공
					
					HttpSession session = request.getSession(true);
					session.setAttribute("member", m);
					
					//로그인 성공시 sendRedirect 메소드를 통하여 메인페이지로 이동
					// sendRedirect 메소드는 사용자의 URL을 변경시켜주는 response 객체의 메소드
					response.sendRedirect("/");
					
				} else { // 로그인 실패
					
					// RequestDispatcher를 이용한면 URL을 변경하지 않고, 이동할 수 있다.
					// 이때 pageContext.forward 처럼 request와 response 객체를 가지고 이동할 수 있다.
					RequestDispatcher view = request.getRequestDispatcher("/views/member/memberLoginFail.jsp");
					
					view.forward(request, response);
				}
				//*/

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}
	
	protected void reqPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		
		
		
		
	}
	
	public static void main(String[] args) throws IllegalAccessException {
		
    	
	      PreparedStatement pstmt = null; 
	  	  Connection con = null; 
	  	  ResultSet rs = null;
	  	  String dbPW = "";
	  	  int x = -1;
	  	  
	  	  try { 
	  		  con = DBConn.getConnection(); 
	  		  
	  		  String sql ="SELECT userid FROM user WHERE userid = ? ";
	  	  
		  	  pstmt = con.prepareStatement(sql);
		  	  
		  	  pstmt.setString(1, "userid"); 
		  	  
		  	  rs = pstmt.executeQuery();
		  	  
		  	  if(rs.next()) {
		  		  
		  		  dbPW = rs.getString("password");
		  		  
		  		  if(dbPW.equals(password))
		  			  x = 1;
		  		  else
		  			  x = 0;
		  	  }else {
		  		  x = -1;
		  	  }
		  	   return;
		  	  } catch (Exception e) { 
		  		  e.printStackTrace();
		  		  } finally { 
		  		  if (pstmt != null) try { pstmt.close(); pstmt = null; } catch (SQLException ex) { }; 
		  		  if(con != null) try{ con.close(); } catch(SQLException e){}; }
		        
		        }

	        
	    


}

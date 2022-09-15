<%@page import="java.io.PrintWriter"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.brilcom.healthcare.DBConn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>LoginPro</title>
</head>
<body>


    <%
    
    request.setCharacterEncoding("UTF-8");
    Connection con = null; 
    PreparedStatement pstmt = null;  
    ResultSet rs = null;
    String msg = "";
    
  //로그인 화면에 입력된 아이디와 비밀번호를 가져온다
   // String id = request.getParameter("id");
   // String pwd = request.getParameter("password");
   
   String id = (String)session.getAttribute("id");
   String pwd = (String)session.getAttribute("pwd");
    
   // PrintWriter out = response.getWriter();
    
    con = DBConn.getConnection(); 
    String sql ="SELECT * FROM user";
    
    pstmt = con.prepareStatement(sql);
    
    String account_email = (String) request.getAttribute("account_email");
    String password = (String) request.getAttribute("password");
    
    if(account_email == id && password == pwd){ //로그인 성공
    	msg = "./main.jsp";
    }else if(password != pwd){ // 비밀번호가 틀린 경우
        msg="LoginForm.jsp?msg=0";
    }else{ //아이디가 틀린 경우
        msg="LoginForm.jsp?msg=1";
       // alter("아이디가 틀렸습니다.");
        
    }
    response.sendRedirect(msg);
    
    
    
    
    %>

</body>
</html>
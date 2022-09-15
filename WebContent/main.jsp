<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Main</title>

    <script type="text/javascript">
    
    function logout(){
    	location.href="../user/Logout.jsp"
    }
    </script>
</head>
<body>
    <b><font size="5" color="skyblue">PiCOHOME</font></b><br><br>
    
    <%
    if(session.getAttribute("sessionID") == null){
    	response.sendRedirect("./user/LoginForm.jsp");
    }else
    
    {
    
    %>
    
    <h2>
        <font color="red"></font>
        로그인, 확인
    </h2>
    
    <br>
    <br>
    <input type="button" value="로그아웃" onclick="logout()">    
    
    
    <%} %>
    
</body>
</html>
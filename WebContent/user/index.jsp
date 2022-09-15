<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Index</title>
</head>
<body>
    <h1>인덱스 페이지 입니다.</h1>
  
    
    <%
    //로그인된 아이디가 있는지 읽어와보기
    String id =(String)session.getAttribute("id");
    %>
    
    <%if(id==null){%>
        <h2>로그인</h2>
        <form action="/healthcare/user/Login.jsp" method = "post">
        <input type="text" name="id" placeholder="아이디"/>
        <input type="password" name="pwd" placehloder="비밀번호"/>
        <button type="submit">로그인</button>
        </form>
    <%}else{ %> 
        <p>
            <strong><%=id %></strong>님 로그인 중 입니다.
            <a href="Logout.jsp">로그아웃</a>
        </p>
        
    <%} %>
    <ul>
        <li><a href="/healthcare/user/loginform.jsp">로그인 하러가기</a></li>

    </ul>

</body>
</html>
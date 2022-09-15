<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<%
    //1. 폼 전송되는 아이디와 비밀번호 읽어오기
    String id = request.getParameter("id");
    String pwd = request.getParameter("pwd");
    
    //2. DB에 해당 회원의 정보가 있는지와 있다면 비밀번호 일치 여부 확인하기
    //아이디: gura, 비밀번호:1234가 DB에 저장된 유효한 정보라고 가정해보자.
    boolean isValid =false;
    if(id.equals("info@brilcom.com")&&pwd.equals("info1234")){
        isValid = true;
        //로그인 했다는 의미에서 session scope 에 "id"라는 키값으로 로그인된 아이디를 담는다.
        session.setAttribute("id",id);
    }
    //3. 회원 정보가 맞으면 로그인 처리 후 응답, 틀리면 로그인 실패라고 응답
    
%>

<title>Login</title>
</head>
<body>

<%if(isValid){ %>
    <p>
        <strong><%=id %></strong>회원님 로그인 되었습니다.
        <a href="${pageContext.request.contextPath}/user/index.jsp">확인</a>
    </p>
<%}else{ %>
    <p>
        아이디 혹은 비밀번호가 틀렸습니다.
        <a href="${pageContext.request.contextPath}/user/index.jsp">다시 시도</a>
    </p>
<%} %>



</body>
</html>
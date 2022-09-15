<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>

<%
request.setCharacterEncoding("UTF-8");

%>

<script type="text/javascript">



function checktValue(){
	
	inputForm = eval("document.loginInfo");
	if(!inputForm.id.value){
		alert("아이디를 입력하세요");   
		inputForm.account_email.focus();
		return false;
	}
	if(!inputForm.password.value){
		alert("비밀번호를 입력하세요");  
		inputForm.password.focus();
		return false;
	}
	
}
</script>
</head>
<body>
    <div id="wrap" margin = -50px,0,0,-50px>
        <form name="loginInfo" method="post" action="LoginPro.jsp" onsubmit="return checkValue()">
        <br><br>
            <table>
                <tr>
                    <td bgcolor="skyblue">아이디</td>
                    <td><input type="text" name="id" maxlength="20"></td>
                </tr>
                <tr>
                    <td bgcolor="skyblue">비밀번호</td>
                    <td><input type="password" name="password" maxlength="10"></td>
                </tr>
            </table>
        <br>
        <input type="submit" value="로그인" />  
        </form>
    
    <%
    
    String msg = request.getParameter("msg");
    
    	if(msg !=null && msg.equals("0")){
    	out.println("<br>");
    	out.println("<font color='red' size='5'>비밀번호를 확인해 주세요.</font>");
    }else if(msg !=null && msg.equals("-1")){
    	out.println("<br>");
        out.println("<font color='red' size='5'>아이디를 확인해 주세요.</font>");
    }

    
    
    
    
    
    %>
    
    
    
    
    </div>

</body>
</html>
package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.jepetto.bean.Facade;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;

public class UpdateProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private Logger logger = LogManager.getLogger(MyThread.class);

	private static final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private static final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private static final String queryKey = "updateProfile";
	private final String code = "code";
	private final String message = "result";
	private static final int a = 404;
	private static final int b = 200;
	
       
    public UpdateProfile() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("updateProfile!");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		//String account_email = (String) request.getAttribute("account_email");
		//String password = (String) request.getAttribute("password");
		String username = (String) request.getAttribute("username");
		String nickname = (String) request.getAttribute("nickname");
		String gender = (String) request.getAttribute("gender");
		String user_status = (String) request.getAttribute("user_status");
		String protector = (String) request.getAttribute("protector");
		String protector_number = (String) request.getAttribute("protector_number");
		String userid = (String) request.getAttribute("userid");

		
 		
		
		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();
		
		String arr[] = new String[] { 
				username, 
				nickname, 
				gender, 
				user_status, 
				protector, 
				protector_number,
				userid
		};

		HashMap<String, String> dummy = new HashMap<String, String>();
		
		try {
			int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, queryKey, dummy , arr);
			//out.println(updatedCnt);
			if(updatedCnt > 0) {
				obj.put(code, b);
				obj.put(message, "Registed..");
				out.println(obj.toString());
			}else {
				obj.put(code, a);
				obj.put(message, "Disabled..");
				out.println(obj.toString());
			}
		} catch (SQLException | NamingException | JDOMException | IOException e) {
			obj.put(code, a);
			obj.put(message, e.getMessage());
			out.println(obj.toString());
		} 
		
		
	}
	
	
	public static void main(String[] args) {
		
		PreparedStatement pstmt = null; 
		  Connection con = null; 
		  try { 
			  con = DBConn.getConnection(); 
			  
			  
			  String sql = "UPDATE user set account_email = ? ,age = ?, gender = ?, birthDate = ?, password =? where 1=1 and username = ?";
		  
			  pstmt = con.prepareStatement(sql);
			  
			  pstmt.setString(1, "info1@info.com");
			  pstmt.setString(2, "15");
			  pstmt.setString(3, "male");
			  pstmt.setString(4, "2000-01-01");
			  pstmt.setString(5, "info1");
			  pstmt.setString(6, "사용자01");
			  
			  System.out.println(pstmt);
			  
			  int cnt = pstmt.executeUpdate(); System.out.println("UpdateCnt : " + cnt);
			  
			  if(cnt ==1) { 
				  System.out.println("cnt : " + cnt); 
				  }
			  
			  } catch (Exception e) { 
				  System.out.println(e.getMessage()); 
				  } finally {
					  if(pstmt != null) try { pstmt.close(); pstmt = null;  } catch (SQLException ex){}; 
					  if(con != null) try{ con.close(); } catch(SQLException e){}; 
					  }
		
		
		
		
		
		
		
	}
	
	

}

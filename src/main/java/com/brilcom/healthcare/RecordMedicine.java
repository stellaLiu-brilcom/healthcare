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

//@WebServlet("/RecordMedicine")
public class RecordMedicine extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private Logger logger = LogManager.getLogger(MyThread.class);
	
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = reader.getProperty("recordMedicine");
	
	private final String code = "code";
	private final String message = "result";
	
	private static final int a = 404;
	private static final int b = 200;
	
	//private static final String url = "http://mqtt.brilcom.com:18080/healthcare/RecordMedicine";
	private static final String url = "http://localhost:18080/healthcare/RecordMedicine";
	private static final String Content = "Content-Type";
	private static final String json = "application/json";	
	

    public RecordMedicine() {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("recordMedicine");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		String username = (String) request.getAttribute("username");
		String m_name = (String) request.getAttribute("m_name");
		
		String amount = (String) request.getAttribute("amount");
		String alarm = (String) request.getAttribute("alarm");
		String cnt = (String) request.getAttribute("cnt");
		String startTime = (String) request.getAttribute("startTime");
		String endTime = (String) request.getAttribute("endTime");
		String times = (String) request.getAttribute("times");
		String img = (String) request.getAttribute("img");
		String emegency = (String) request.getAttribute("emegency");
		String bookmark = (String) request.getAttribute("bookmark");
		
		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();
		
		String arr[] = new String[] { username, m_name, amount, alarm, cnt, startTime, endTime, times, img, emegency, bookmark  };

		HashMap<String, String> dummy = new HashMap<String, String>();
		
		try {
			int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, queryKey, dummy , arr);
			//out.println(updatedCnt);
			if(updatedCnt > 0) {
				obj.put(code, a);
				obj.put(message, "Disabled..");
				out.println(obj.toString());
			}else {
				obj.put(code, b);
				obj.put(message, "Registed..");
				out.println(obj.toString());
			}
		} catch (SQLException | NamingException | JDOMException | IOException e) {
			obj.put(code, a);
			obj.put(message, e.getMessage());
			out.println(obj.toString());
		}
		
		
		
		
	}
	
	
	public static void main(String[] args) throws Exception {
		
		PreparedStatement pstmt = null; 
		Connection con = null;
		
		String sql ="UPDATE medicine_result set amount =?, cnt =?, startTime =?, endTime =?, times =?, img =?, emegency =?,"
				+ "bookmark =? where 1=1 and username = ? and m_name =?;";
		
		
	  try {
		  con = DBConn.getConnection(); 
		  pstmt = con.prepareStatement(sql);
		  
		  pstmt.setString(1, "30"); 
		  pstmt.setString(2, "3");
		  pstmt.setString(3, "2021-04-14");
		  pstmt.setString(4, "2021-05-14");
		  pstmt.setString(5, "1일 1회");
		  pstmt.setString(6, "");
		  pstmt.setString(7, "0");
		  pstmt.setString(8, "0");
		  pstmt.setString(9, "사용자01");
		  pstmt.setString(10, "8806500004904");

		  
		  
		  System.out.println(pstmt);
		  
		  int cnt = pstmt.executeUpdate(); System.out.println("UpdateCnt : " + cnt);
		  
		  if(cnt ==1) { System.out.println("insert into~"); }
		  
		  } catch (Exception e) { System.out.println(e.getMessage()); } 
		  finally { 
			  if (pstmt != null) try { pstmt.close(); pstmt = null; } catch (SQLException ex) { }; 
			  if(con != null) try{ con.close(); } catch(SQLException e){}; }
		  
		  }
		
	
	

}

package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.jepetto.sql.XmlConnection;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;

public class RecordReason extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = "recordReason";
	
	private final String code = "code";
	private final String message = "message";
	
	private static final int a = 404;
	private static final int b = 200;
                                                               
    public RecordReason() {
   
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PrintWriter out = response.getWriter();
		out.println("recordReason!");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json;charset=utf-8");
		
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		String userid = (String) request.getAttribute("userid");
		String r_idx = (String) request.getAttribute("r_idx");
		
		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();
		
		String arr[] = new String[] {  r_idx, userid };

		HashMap<String, String> dummy = new HashMap<String, String>();
		
		try {
			int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, queryKey, dummy , arr);
			if (updatedCnt > 0) {
				obj.put(code, b);
				obj.put(message, "Registed..");
				out.println(obj.toString());
			} else {
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
			  
			  
			  String sql = "UPDATE symptom set r_idx = ? where 1=1 and username = ?";
		  
			  pstmt = con.prepareStatement(sql);
			  
			  pstmt.setString(1, "6");
			  pstmt.setString(2, "사용자01");
			  
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
	


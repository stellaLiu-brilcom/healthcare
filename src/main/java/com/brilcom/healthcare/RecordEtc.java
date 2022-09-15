package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class RecordEtc extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private Logger logger = LogManager.getLogger(MyThread.class);

	private static final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private static final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private static final String queryKey = "recordEtc";
	private final String code = "code";
	private final String message = "result";
	private static final int a = 404;
	private static final int b = 200;
	

    public RecordEtc() {
   
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PrintWriter out = response.getWriter();
		out.print("recordEtc");
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		HomeProxy proxy = HomeProxy.getInstance();
		String PEFR = (String) request.getAttribute("PEFR");
		String memo = (String) request.getAttribute("memo");
		String userid = (String) request.getAttribute("userid");

		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		String e_regDate = format.format (System.currentTimeMillis());

		Facade remote = proxy.getFacade();
		
		String arr[] = new String[] { 
				PEFR,
				memo,
				e_regDate,
				userid
		};

		HashMap<String, String> dummy = new HashMap<String, String>();
		
		try {
			int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, queryKey, dummy , arr);
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
			  
			  //String sql ="UPDATE symptom set PEFR = ?, memo = ?, e_regDate = ? where 1=1 and username =?";
			  String sql = "insert into etc (PEFR, memo, e_regDate, userid) value(?,?,?,?)";
			  
			  SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
				
			  String format_time1 = format1.format (System.currentTimeMillis());
		 		
			  System.out.println(format_time1);
			  
		  
			  pstmt = con.prepareStatement(sql);
			  
			  pstmt.setString(1, "130"); 
			  pstmt.setString(2, "~숨쉬기 힘들어서 복약하였습니다.");
			  pstmt.setString(3, format_time1);
			  pstmt.setString(4, "사용자01");
			  
			  System.out.println(pstmt);
			  
			  int cnt = pstmt.executeUpdate(); 
			  System.out.println("UpdateCnt : " + cnt);
			  
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
	


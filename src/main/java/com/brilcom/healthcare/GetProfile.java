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
import org.jepetto.sql.JsonTransfer;
import org.jepetto.sql.XmlConnection;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;


//@WebServlet("/GetProfile")
public class GetProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	static PropertyReader reader = PropertyReader.getInstance();
	private Logger logger = LogManager.getLogger(MyThread.class);
	
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private static final String queryKey = "getProfile";
	private final String code = "code";
	private final String message = "result";
	private static final int a = 404;
	private static final int b = 500;
	
    public GetProfile() {
        super();

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("getProfile");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();

		HomeProxy proxy = HomeProxy.getInstance();
		String userid = (String) request.getAttribute("userid");

		Facade remote = proxy.getFacade();

		HashMap<String, String> dummy = new HashMap<String, String>();
		String arr[] = new String[] { userid };

		org.jdom2.Document doc;

		try {
			doc = remote.executeQuery(dataSource, QUERY_FILE, queryKey, dummy, arr);
			out.println(new JsonTransfer().transferDom2JSON(doc).toString());
			

		} catch (SQLException | NamingException | JDOMException | IOException  err) {
			err.printStackTrace();
			obj.put(code, b);
			obj.put(message, err.getMessage());
			out.println(obj.toJSONString());
		} catch (Exception err) {
			err.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		
		  PreparedStatement pstmt = null; 
		  Connection con = null; 
		  try { 
			  con = DBConn.getConnection(); 
			  
			  String sql ="SELECT * FROM user WHERE userid = ?";
		 
			  //pstmt = con.prepareStatement(sql);
			  pstmt.setString(1, "사용자01"); 
			  System.out.println(sql);
			  
			  ResultSet rs = pstmt.executeQuery(sql); 
			  boolean isList = false;
			  try {
				  while(rs.next()) {
					  String userid = rs.getString("userid");
					  String account_email = rs.getString("account_email");
					  String gender = rs.getString("gender");
					  String birthDate = rs.getString("birthDate");
					  String profile_nickname = rs.getString("profile_nickname");
					  System.out.println(userid);
					  System.out.println(account_email);
					  System.out.println(gender);
					  
				  }
				  if(isList == false) {
					  System.out.println("none");
				  }
			  }catch(SQLException sqle) {
				  sqle.printStackTrace();
			  }
			  } catch (Exception e) { 
				  System.out.println(e.getMessage()); 
				  } finally {
					  if(pstmt != null) try { pstmt.close(); pstmt = null;  } catch (SQLException ex){}; 
					  if(con != null) try{ con.close(); } catch(SQLException e){}; 
					  }
		
		
	}
	

}

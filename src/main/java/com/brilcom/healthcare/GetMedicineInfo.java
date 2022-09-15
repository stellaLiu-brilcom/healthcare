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
import org.json.JSONObject;

//@WebServlet("/GetMedicineInfo")
public class GetMedicineInfo extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private Logger logger = LogManager.getLogger(MyThread.class);
	
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("mqtt_healthcare_file");
	private final String query = reader.getProperty("getMedicineInfo");
	
	private final String code = "code";
	private final String message = "result";
	private static final int a = 404;
	private static final int b = 500;
	
	//private static final String url = "http://mqtt.brilcom.com:8080/healthcare/GetSympthom";
	private static final String url = "http://localhost:18080/healthcare/GetMedicineInfo";
	private static final String Content = "Content-Type";
	private static final String json = "application/json";

	
    public GetMedicineInfo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("getMedicineInfo");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		String userid = (String) request.getAttribute("userid");
		
		HomeProxy proxy = HomeProxy.getInstance();
		
		Facade remote = proxy.getFacade();
		
		HashMap<String, String> dummy = new HashMap<String, String>();
		String arr[] = new String[] { userid };

		org.jdom2.Document doc;
				
		try {
			doc = remote.executeQuery(dataSource, QUERY_FILE, query, dummy, arr);
			Connection con = new XmlConnection(doc);
			PreparedStatement stmt = con.prepareStatement("//recordset/row");
			ResultSet rs = stmt.executeQuery();
				
			out.println(new JsonTransfer().transferDom2JSON(doc).toString());
			

		} catch (SQLException | NamingException | JDOMException | IOException  err) {
			err.printStackTrace();
			obj.put(code, b);
			obj.put(message, err.getMessage());
			out.println(obj.toString());
			
		} catch (Exception err) {
			err.printStackTrace();
		}
			
	}

	public static void main(String[] args) {
		
		  PreparedStatement pstmt = null; 
		  Connection con = null; 
		  try { 
			  con = DBConn.getConnection(); 
			  
			  String sql ="SELECT * FROM medicine;";
		 
			  pstmt = con.prepareStatement(sql);
			  System.out.println(sql);
			  
			  ResultSet rs = pstmt.executeQuery(); 
			  
			  try {
				  while(rs.next()) {
					  String m_code = rs.getString("m_code");
					  String m_name = rs.getString("m_name");
					  
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

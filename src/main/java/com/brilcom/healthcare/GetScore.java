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
import org.jepetto.util.PropertyReader;
import org.json.JSONObject;


public class GetScore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	static PropertyReader reader = PropertyReader.getInstance();
	private Logger logger = LogManager.getLogger(MyThread.class);
	
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = reader.getProperty("getScore");

	private final String code = "code";
	private final String message = "result";
	private static final int a = 404;
	private static final int b = 500;
	
	

    public GetScore() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("getScore!!");

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
			doc = remote.executeQuery(dataSource, QUERY_FILE, queryKey, dummy, arr);
			out.println(new JsonTransfer().transferDom2JSON(doc).toString());

		} catch (SQLException | NamingException | JDOMException | IOException err) {
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
			
			String sql = "SELECT * FROM mqtthealthcare.survey_result where 1=1 and userid = ?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "user01");
			System.out.println(sql);

			ResultSet rs = pstmt.executeQuery();
			
			try {
				while (rs.next()) {
					String userid = rs.getString("userid");
					String answer = rs.getString("answer");
					String score = rs.getString("score");
					String s_regDate = rs.getString("s_regDate");


				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			
			
		}catch(Exception e) {
			
		}
		
		
		
		
	}

}

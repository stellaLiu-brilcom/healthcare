package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.JDOMException;
import org.jepetto.bean.Facade;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;


public class RecordBadge extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = "recordBadge";

	private final String code = "code";
	private final String message = "message";

	private static final int a = 404;
	private static final int b = 200;
	
       
    public RecordBadge() {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("recordBadge!");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json;charset=utf-8");

		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();
		
		String userid = (String) request.getAttribute("userid");
		String b_idx = (String) request.getAttribute("b_idx");
		String b_set = (String) request.getAttribute("b_set");

		
		String arr[] = new String[] { userid, b_idx, b_set};
		
		HashMap<String, String> dummy = new HashMap<String, String>();
		
		try {
			int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, "recordBadge", dummy, arr);
			System.out.println(updatedCnt);
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

			String sql = "insert into badges_result \r\n"
					+ "			(userid,\r\n"
					+ "			b_idx, \r\n"
					+ "			b_set\r\n"
					+ "			)  \r\n"
					+ "			value\r\n"
					+ "			(\r\n"
					+ "			?,\r\n"
					+ "			?,\r\n"
					+ "			?\r\n"
					+ "			)";
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, "user01");
			pstmt.setString(2, "4");
			pstmt.setString(3, "0");
	

			System.out.println(pstmt);

			int cnt = pstmt.executeUpdate();
			System.out.println("UpdateCnt : " + cnt);

			if (cnt == 1) {
				System.out.println("insert into~");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException ex) {
				}
			;
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
				}
			;
		}

		
		
	}

}

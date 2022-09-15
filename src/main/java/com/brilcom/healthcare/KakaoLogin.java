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

import org.jdom2.JDOMException;
import org.jepetto.bean.Facade;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;



public class KakaoLogin extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = "login";

	private final String code = "code";
	private final String message = "message";

	private static final int a = 404;
	private static final int b = 200;
       

    public KakaoLogin() {

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("recordUser!");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json;charset=utf-8");

		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		String userid = (String) request.getAttribute("userid");
		String kind = (String) request.getAttribute("kind");
		String phoneType = (String) request.getAttribute("phoneType");
		String account_email = (String) request.getAttribute("account_email");
		String access_token = (String) request.getAttribute("access_token");

		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();
		
		String arr[] = new String[] { userid, phoneType, kind, account_email, access_token};

		HashMap<String, String> dummy = new HashMap<String, String>();
		
		
		try {
			int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, queryKey, dummy, arr);
			if (updatedCnt > 0) {
				obj.put(code, b);
				obj.put(message, "Login succeed");
				out.println(obj.toString());
			} else {
				obj.put(code, a);
				obj.put(message, "User Not Found, check row plz");
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

			String sql = "insert into user (userid, nickname, account_email, access_token, idx) value(?,?,?,?, 1)";
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, "user04");
			pstmt.setString(2, "사용자04");
			pstmt.setString(3, "user04@test.com");
			pstmt.setString(4, "test4123-4-4444");

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

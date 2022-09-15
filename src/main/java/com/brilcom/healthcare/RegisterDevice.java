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

public class RegisterDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static PropertyReader reader = PropertyReader.getInstance();

	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = "recordDevice";
	private final String findByUser = "findByUser";

	private final static String code = "code";
	private final static String message = "message";

	private static final int a = 200;
	private static final int b = 500;
	private static final int c = 404;

	public RegisterDevice() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("recordDevice!");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json;charset=utf-8");

		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();

		String userid = (String) request.getAttribute("userid");
		String serialNum = (String) request.getAttribute("serialNum");
		String deviceid = (String) request.getAttribute("deviceid");
		String description = (String) request.getAttribute("description");
		String firmwareVesion = (String) request.getAttribute("firmwareVesion");
		String lang = (String) request.getAttribute("lang");

		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();

		String arr[] = new String[] { userid, serialNum, deviceid, description, firmwareVesion, lang };

		HashMap<String, String> dummy = new HashMap<String, String>();

		org.jdom2.Document doc;

		// 사용언어 확인

		// 사용자 찾기
		try {
			doc = remote.executeQuery(dataSource, QUERY_FILE, findByUser, dummy, arr);
			Connection con = new XmlConnection(doc);
			PreparedStatement stmt = con.prepareStatement("//recordset/row");

			// 사용자 있으면, 기기 등록 진행
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, queryKey, dummy, arr);
				
				if (updatedCnt > 0) {
					obj.put(code, a);
					obj.put(message, "Registed..");
					out.println(obj.toString());
				} else {
					obj.put(code, c);
					obj.put(message, "Disabled..");
					out.println(obj.toString());
				}
				return;
				
				// 사용자 없으면, 사용자부터 찾기
			} else {
				obj.put(code, b);
				obj.put(message, "User Not Found, check userid!");
				out.println(obj.toJSONString());
			}
		} catch (SQLException | NamingException | JDOMException | IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {

		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = DBConn.getConnection();

			String sql = "insert into device (userid, serialNum, apiKey, deviceId, description, firmwareVersion, lang)  value(?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, "");
			pstmt.setString(2, "");
			pstmt.setString(3, "");
			pstmt.setString(4, "");
			pstmt.setString(5, "");
			pstmt.setString(6, "");
			pstmt.setString(7, ""); // kr, jp, en

			System.out.println(pstmt);

			int cnt = pstmt.executeUpdate();

			JSONObject obj = new JSONObject();

			if (cnt > 0) {
				obj.put(code, a);
				obj.put(message, "기기가 등록되었습니다");

				System.out.println(obj.toJSONString());
			} else if (cnt == 0) {
				obj.put(code, b);
				obj.put(message, "이미 등록된 기기입니다.");

				System.out.println(obj.toJSONString());
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

package com.brilcom.healthcare;

import java.awt.Image;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.naming.NamingException;
import javax.servlet.ServletException;
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

public class GetSympthom extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static PropertyReader reader = PropertyReader.getInstance();
	private Logger logger = LogManager.getLogger(MyThread.class);
	
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String query = "getSympthom";

	private final String code = "code";
	private final String message = "result";
	private static final int a = 404;
	private static final int b = 500;


	public GetSympthom() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("getSympthom!!");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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

			String sql = "SELECT * FROM symptom LEFT JOIN type ON type.t_idx = symptom.t_idx" + "UNION"
					+ "SELECT * FROM symptom LEFT JOIN reason ON reason.r_idx = symptom.r_idx";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "사용자01");
			System.out.println(sql);

			ResultSet rs = pstmt.executeQuery();

			try {
				while (rs.next()) {
					String username = rs.getString("username");
					String t_title = rs.getString("t_title");
					String r_idx = rs.getString("r_idx");
					String r_title = rs.getString("r_title");
					System.out.println(
							"username : " + username + " " + "t_title :" + t_title + "" + "r_title :" + "r_title");

				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {

			try {
				pstmt.close();
				pstmt = null;
			} catch (Exception ex) {

			}

			try {
				con.close();
			} catch (Exception e) {

			}

		}

	}


}

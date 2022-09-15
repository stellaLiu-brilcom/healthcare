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

public class GetBadge extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(MyThread.class);

	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = "getBadge";

	private final String code = "code";
	private final String message = "result";
	private static final int a = 404;
	private static final int b = 500;

	public GetBadge() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("getBadge!");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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

			Connection con = new XmlConnection(doc);
			PreparedStatement stmt = con.prepareStatement("//recordset/row");
			ResultSet rs = stmt.executeQuery();
			try {
				while (rs.next()) {
					userid = rs.getString("userid");
					String b_name = rs.getString("b_name");
					//System.out.println("userid : " + userid + " " + "b_name :" + b_name);
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
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

			String sql = "select * from badges_result A INNER JOIN badges B ON A.b_idx = B.b_idx where 1=1 and username = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "사용자01");
			System.out.println(sql);

			ResultSet rs = pstmt.executeQuery();

			try {
				while (rs.next()) {
					String username = rs.getString("username");
					String b_name = rs.getString("b_name");
					System.out.println("username : " + username + " " + "b_name :" + b_name);
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

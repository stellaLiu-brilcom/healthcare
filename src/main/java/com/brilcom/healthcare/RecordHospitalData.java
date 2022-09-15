package com.brilcom.healthcare;

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

import org.jdom2.JDOMException;
import org.jepetto.bean.Facade;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.sql.JsonTransfer;
import org.jepetto.sql.XmlConnection;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;

public class RecordHospitalData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");

	private final String findByUser = "findByUser";
	private final String recordHospital = "recordHospital";
	private final String updateHospital = "updateHospital";

	private final String code = "code";
	private final String message = "message";

	private static final int a = 404;
	private static final int b = 200;

	public RecordHospitalData() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("recordHospital!");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String h_regDate = format.format(System.currentTimeMillis());
		
		
		String userid = (String) request.getAttribute("userid");
		String h_name = (String) request.getAttribute("h_name");
		String addr = (String) request.getAttribute("addr");
		String telno = (String) request.getAttribute("telno");
		String part = (String) request.getAttribute("part");
		String doctor = (String) request.getAttribute("doctor");

		HashMap<String, String> dummy = new HashMap<String, String>();
		String arr[] = new String[] { userid };
		
		org.jdom2.Document doc;
		
		try {
			doc = remote.executeQuery(dataSource, QUERY_FILE, findByUser, dummy, arr);
			Connection con = new XmlConnection(doc);
			PreparedStatement stmt = con.prepareStatement("//recordset/row");
			ResultSet rs = stmt.executeQuery();
			
			arr = new String[] {userid, h_name, addr, telno, part, doctor, h_regDate};
			
			if(!rs.next()) {
				int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, recordHospital, dummy, arr);
				if (updatedCnt > 0) {
					obj.put(code, b);
					obj.put(message, "Registed..");
					out.println(obj.toString());
				} else {
					obj.put(code, a);
					obj.put(message, "Disabled..");
					out.println(obj.toString());
				}
				//out.println(new JsonTransfer().transferDom2JSON(doc).toString());
				//System.out.println(doc);
			}else {
				arr = new String[] {h_name, addr, telno, part, doctor, h_regDate, userid};
				int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, updateHospital, dummy, arr);
				if(updatedCnt > 0) {
					obj.put(code, b);
					obj.put(message, "Updated..");
					out.println(obj.toString());
				}else {
					obj.put(code, a);
					obj.put(message, "Disabled..");
					out.println(obj.toString());
				} 
				//out.println(new JsonTransfer().transferDom2JSON(doc).toString());
				//System.out.println(doc);
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
			String sql = "insert into hospital_result (userid, h_name, addr, telno, part, doctor, h_regDate) value(?,?,?,?,?,?,?)";

			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String format_time1 = format1.format(System.currentTimeMillis());

			System.out.println(format_time1);

			String queryCheck = "SELECT * from hospital_result WHERE userid = ?";
			pstmt = con.prepareStatement(queryCheck);
			pstmt.setString(1, "user01");
			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				final int count = rs.getInt(1);
				String updatesql = "UPDATE hospital_result set h_name = ? and addr =? and telno =? and part =? and doctor =? and h_regDate = ? where 1=1 and userid = ?";
				pstmt = con.prepareStatement(updatesql);

				pstmt.setString(1, "세란병원"); // h_name
				pstmt.setString(2, "서울특별시 종로구 통일로 256 (무악동)"); // addr
				pstmt.setString(3, "02-737-0181"); // telno
				pstmt.setString(4, "소아청소년과");
				pstmt.setString(5, "");
				pstmt.setString(6, format_time1);
				pstmt.setString(7, "user01");

				/*
				 * addr:서울특별시 종로구 통일로 256 (무악동) telno:02-737-0181 //
				 */

			} else {

				pstmt = con.prepareStatement(sql);
				// 담당과, 담당의만 입력
				pstmt.setString(1, "user01");
				pstmt.setString(2, "구로병원"); // h_name
				pstmt.setString(3, "서울특별시 구로구 구로동로 148 고려대부속구로병원 (구로동)"); // addr
				pstmt.setString(4, "02-2626-1114"); // telno
				pstmt.setString(5, ""); // 담당과
				pstmt.setString(6, ""); // doctor 담당의
				pstmt.setString(7, format_time1);

				int cnt = pstmt.executeUpdate();

				System.out.println("UpdateCnt : " + cnt);

				if (cnt == 1) {
					System.out.println("insert into~");
				}
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

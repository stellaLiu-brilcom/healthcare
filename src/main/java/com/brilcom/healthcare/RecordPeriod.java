package com.brilcom.healthcare;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/RecordPeriod")
public class RecordPeriod extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RecordPeriod() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	public static void main(String[] args) {

		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = DBConn.getConnection();

			String sql = "insert into medicine_result (username, code, startTime, endTime) value(?,?,?,?);";
			pstmt = con.prepareStatement(sql);
			
			int index = 0;
			
			Calendar cal = Calendar.getInstance();
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd");
			String startTime = format.format (System.currentTimeMillis());
			String endTime = format.format (System.currentTimeMillis()+604800000);
			//index = request.getAttribute("index");

			if(index == 0) { //기간을 정해볼게요.
				pstmt.setString(1, "사용자01");
				pstmt.setString(2, "8806500004904");
				pstmt.setString(3, "");
				pstmt.setString(4, "");
				System.out.println("기간정해야함");

			}else { //일주일 이내에 끝나요.
				pstmt.setString(1, "사용자01");
				pstmt.setString(2, "8806500004904");
				pstmt.setString(3, startTime);
				pstmt.setString(4, endTime);
				System.out.println("기간은일주일이내");
			}

			System.out.println(pstmt);

			int cnt = pstmt.executeUpdate();
			System.out.println("UpdateCnt : " + cnt);

			if (cnt == 1) {
				System.out.println("record");
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

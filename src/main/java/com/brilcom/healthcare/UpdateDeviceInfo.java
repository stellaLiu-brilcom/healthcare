package com.brilcom.healthcare;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/UpdateDeviceInfo")
public class UpdateDeviceInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String ver = "v";
	

    public UpdateDeviceInfo() {
        super();

    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
	
	public static void main(String[] args) {
		
		 PreparedStatement pstmt = null; 
		  Connection con = null; 
		  try { 
			  con = DBConn.getConnection(); 
			  
			  
			  String sql = "UPDATE device set description =?, frimwareVersion =?, lang =? where 1=1 and userid = ? and apiKey =?; ";
		  
			  pstmt = con.prepareStatement(sql);
			  
			  pstmt.setString(1, "01번방"); //description
			  pstmt.setString(2, ver + "01.02.01");
			  pstmt.setString(3, "kr");
			  pstmt.setString(4, ""); //userid
			  pstmt.setString(5, ""); //apiKey
			  
			  //if(rs.getString("serialNum") == null){
	
			  
			  
			  
			  System.out.println(pstmt);
			  
			  int cnt = pstmt.executeUpdate(); System.out.println("UpdateCnt : " + cnt);
			  
			  if(cnt ==1) { 
				  System.out.println("cnt : " + cnt); 
				  }
			  
			  } catch (Exception e) { 
				  System.out.println(e.getMessage()); 
				  } finally {
					  if(pstmt != null) try { pstmt.close(); pstmt = null;  } catch (SQLException ex){}; 
					  if(con != null) try{ con.close(); } catch(SQLException e){}; 
					  }
		
		
		
	}
	

}

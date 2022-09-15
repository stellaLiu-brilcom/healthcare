package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.jepetto.sql.JsonTransfer;
import org.jepetto.sql.XmlConnection;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;

public class GetDeviceInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String getDeviceInfo = "getDeviceInfo";
	private final String findByDevice = "findByDevice";
	
	private final static String code = "code";
	private final static String message = "message";
	
	private static final int a = 200;
	private static final int b = 404;
	private static final int c = 500;

	
    public GetDeviceInfo() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("getDeviceInfo");	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();

		HomeProxy proxy = HomeProxy.getInstance();
		String userid = (String) request.getAttribute("userid");
		String serialNum = (String) request.getAttribute("serialNum");

		Facade remote = proxy.getFacade();

		HashMap<String, String> dummy = new HashMap<String, String>();

		org.jdom2.Document doc;
		
		String arr[] = new String[] { serialNum };

		try {
			//기기 존재 여부 확인
			doc = remote.executeQuery(dataSource, QUERY_FILE, findByDevice, dummy, arr);
			Connection con = new XmlConnection(doc);
			PreparedStatement stmt = con.prepareStatement("//recordset/row");
			ResultSet rs = stmt.executeQuery();
			
			//기기 없으면
			if (!rs.next()) {
				obj.put(code, a);
				obj.put(message, "Device Not Found, check your device serialNum!");
				out.println(obj.toJSONString());
			//기기 있으면, 사용자에게 등록된 기기 정보 조회
			}else {
				arr = new String[] {userid };
				
				//int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, getDeviceInfo, dummy, arr);
				doc = remote.executeQuery(dataSource, QUERY_FILE, getDeviceInfo, dummy, arr);
				out.println(new JsonTransfer().transferDom2JSON(doc).toString());	
			}
		} catch (SQLException | NamingException | JDOMException | IOException  err) {
			err.printStackTrace();
			obj.put(code, b);
			obj.put(message, err.getMessage());
			out.println(obj.toJSONString());
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
	

}

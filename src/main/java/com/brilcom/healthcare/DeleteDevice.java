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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.JDOMException;
import org.jepetto.bean.Facade;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.sql.XmlConnection;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;

public class DeleteDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String deleteDevice = "deleteDevice";
	private final String findByDevice = "findByDevice";

	private final String code = "code";
	private final String message = "message";

	private static final int a = 404;
	private static final int b = 200;

	public DeleteDevice() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("deleteDevice!");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json;charset=utf-8");

		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();

		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();

		String userid = (String) request.getAttribute("userid");
		String serialNum = (String) request.getAttribute("serialNum");

		HashMap<String, String> dummy = new HashMap<String, String>();

		org.jdom2.Document doc;
		
		String arr[] = new String[] {serialNum};
		// 기기 확인
		try {
			doc = remote.executeQuery(dataSource, QUERY_FILE, findByDevice, dummy, arr);
			Connection con = new XmlConnection(doc);
			PreparedStatement stmt = con.prepareStatement("//recordset/row");
			ResultSet rs = stmt.executeQuery();

			//기기 없으면
			if (!rs.next()) {
				obj.put(code, a);
				obj.put(message, "Device Not Found, check your device serialNum!");
				out.println(obj.toJSONString());
				
			//기기 있으면, 삭제 진행
			} else {
				arr = new String[] {userid, serialNum };
				
				int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, deleteDevice, dummy, arr);
				System.out.println(updatedCnt);
				if (updatedCnt > 0) {
					obj.put(code, b);
					obj.put(message, "successfully deleted");
					out.println(obj.toString());
				} else {
					obj.put(code, a);
					obj.put(message, "Delete failed, checked serialNum");
					out.println(obj.toString());
				}
			}
		} catch (SQLException | NamingException | JDOMException | IOException e1) {
			e1.printStackTrace();
		}

	}
	



}

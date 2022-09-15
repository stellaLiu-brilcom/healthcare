package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
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


public class Withdrawal extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String queryKey = "withdrawal";
	
	private final String code = "code";
	private final String message = "message";
	
	private static final int a = 404;
	private static final int b = 200;
       

    public Withdrawal() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("withdrawal!");

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		String userid = (String) request.getAttribute("userid");
		String account_email = (String) request.getAttribute("account_email");
		String access_token= (String) request.getAttribute("access_token");		
		
		HomeProxy proxy = HomeProxy.getInstance();
		Facade remote = proxy.getFacade();
		
		String arr[] = new String[] {userid, account_email, access_token};

		HashMap<String, String> dummy = new HashMap<String, String>();
		
		try {
			int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, queryKey, dummy , arr);
			if (updatedCnt > 0) {
				obj.put(code, b);
				obj.put(message, "successfully deleted");
				out.println(obj.toString());
			} else {
				obj.put(code, a);
				obj.put(message, "Delete failed..");
				out.println(obj.toString());
			}
		} catch (SQLException | NamingException | JDOMException | IOException e) {
			obj.put(code, a);
			obj.put(message, e.getMessage());
			out.println(obj.toString());
		} 
		
		
		

	}
	
	public static void main(String[] args) {
		
		

}
}

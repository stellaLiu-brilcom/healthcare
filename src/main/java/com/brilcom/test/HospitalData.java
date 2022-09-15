package com.brilcom.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.input.SAXBuilder;
import org.jepetto.bean.Facade;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.sql.XmlConnection;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;

@WebServlet("/HospitalData")
public class HospitalData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	static PropertyReader reader = PropertyReader.getInstance();
	private final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private final String code = "code";
	private final String message = "result";
	private static final int a = 404;
	private static final int b = 500;
	
	private static final String Content = "Content-Type";
	private static final String json = "application/json";
	
       

    public HospitalData() {
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("getHospitalData");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		HomeProxy proxy = HomeProxy.getInstance();
		
		Facade remote = proxy.getFacade();
		
		String h_name = (String) request.getAttribute("h_name");
		
		
		HashMap<String, String> dummy = new HashMap<String, String>();
		String arr[] = new String[] { h_name };

		org.jdom2.Document doc = null;
		
		String serviceKey = "iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e%2FFctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g%3D%3D";
    	
    	
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("yadmNm","UTF-8") + "=" + URLEncoder.encode("구로병원", "UTF-8")); /*병원명(UTF-8 인코딩 필요)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
            Connection con = new XmlConnection(doc);
       
        }
		
		
		try {
			doc = remote.executeQuery(dataSource, QUERY_FILE, "findDeviceBySerialNum", dummy, arr);
			Connection con = new XmlConnection(doc);
			
		}
		catch(Exception e) {
			
			
			
		}
		

	}
	
	public static void main(String[] args) throws Exception {
		
		
		String serviceKey = "iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e%2FFctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g%3D%3D";
    	
    	
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("yadmNm","UTF-8") + "=" + URLEncoder.encode("구로병원", "UTF-8")); /*병원명(UTF-8 인코딩 필요)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
            org.jdom2.Document doc = null;
            doc = new SAXBuilder().build(new StringReader(line));
            Connection con = new XmlConnection(doc);
       
        }
		
		
		
	}
	

}

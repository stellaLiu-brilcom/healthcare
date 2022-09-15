package com.brilcom.healthcare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class GetLatestFirmware extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public GetLatestFirmware() {

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		StringBuffer sbuf = new StringBuffer();
		
		try {
			
			// URL 객체 생성
			URL url = new URL("https://us-central1-pico-home.cloudfunctions.net/GetLatestFirmware");
			
			// URLConnection 생성
			URLConnection urlConn = url.openConnection();
			
			
			InputStream is = urlConn.getInputStream();
		    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    
		    String str ;
		    while((str=br.readLine()) != null){			    	
		    	sbuf.append(str + "\r\n") ;	
		    	out.println(sbuf.toString());
		    }
		    
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

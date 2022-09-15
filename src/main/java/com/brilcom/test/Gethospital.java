package com.brilcom.test;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.OkHttpClient;


public class Gethospital {

	/*
	 String Url = "http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1?serviceKey=iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e%2FFctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g%3D%3D&yadmNm=%EA%B5%AC%EB%A1%9C%EB%B3%91%EC%9B%90";
		
		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
		Document doc = dBuilder.parse(Url);	 
	 //*/
	
	public static void main(String[] args) throws Exception {
		
		String result = null;
		// 충전소 정보
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		
		try {
			//http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1?serviceKey=iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e%2FFctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g%3D%3D&yadmNm=%EA%B5%AC%EB%A1%9C%EB%B3%91%EC%9B%90
			
			HttpUrl.Builder urlBuilder = HttpUrl.parse("http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1").newBuilder();
			urlBuilder.addQueryParameter("yadmNm", "구로병원");
			
			String serviceKey = "iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e/FctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g==";

			StringBuilder url = new StringBuilder(
					"http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1"); /* URL */
			url.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /* Service Key */

			url.append("&" + URLEncoder.encode("yadmNm", "UTF-8") + "="
					+ URLEncoder.encode("구로병원", "UTF-8")); /* 병원명(UTF-8 인코딩 필요) */
			
			
			//urlBuilder.append("&" + URLEncoder.encode("yadmNm", "UTF-8") + "=" + URLEncoder.encode("구로병원", "UTF-8")); /* 병원명(UTF-8 인코딩 필요) */
			
			
			
			String urlTotal = urlBuilder.build().toString();
			System.out.println(urlTotal);
			
		}catch(Exception e) {
			
			
		}
		
		
		
		String serviceKey = "iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e%2FFctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g%3D%3D";

		StringBuilder urlBuilder = new StringBuilder(
				"http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1"); /* URL */
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /* Service Key */

		urlBuilder.append("&" + URLEncoder.encode("yadmNm", "UTF-8") + "="
				+ URLEncoder.encode("구로병원", "UTF-8")); /* 병원명(UTF-8 인코딩 필요) */

		URL url = new URL(urlBuilder.toString());
		//System.out.println(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		String resultLine = "";
		while ((line = rd.readLine()) != null) {
			sb.append(line);
			resultLine += line; 
			
			/*
			
			if("telno".equals(tegName)) {
				
			}
			
			//*/
			System.out.println(resultLine);
		}
		rd.close();
		
	}
	
	
	
	
}

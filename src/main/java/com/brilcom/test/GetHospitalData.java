package com.brilcom.test;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class GetHospitalData {
	
	/* Java 1.8 샘플 코드 */



	    public static void main(String[] args) throws IOException {
	    	String serviceKey = "iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e/FctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g==";
	        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire"); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" +serviceKey); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("Q0","UTF-8") + "=" + URLEncoder.encode("서울특별시", "UTF-8")); /*주소(시도)*/
	        urlBuilder.append("&" + URLEncoder.encode("Q1","UTF-8") + "=" + URLEncoder.encode("강남구", "UTF-8")); /*주소(시군구)*/
	        urlBuilder.append("&" + URLEncoder.encode("QZ","UTF-8") + "=" + URLEncoder.encode("B", "UTF-8")); /*CODE_MST의'H000' 참조(B:병원, C:의원)*/
	        urlBuilder.append("&" + URLEncoder.encode("QD","UTF-8") + "=" + URLEncoder.encode("D001", "UTF-8")); /*CODE_MST의'D000' 참조(D001~D029)*/
	        urlBuilder.append("&" + URLEncoder.encode("QT","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*월~일요일(1~7), 공휴일(8)*/
	        urlBuilder.append("&" + URLEncoder.encode("QN","UTF-8") + "=" + URLEncoder.encode("삼성병원", "UTF-8")); /*기관명*/
	        urlBuilder.append("&" + URLEncoder.encode("ORD","UTF-8") + "=" + URLEncoder.encode("NAME", "UTF-8")); /*순서*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*목록 건수*/
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
	        }
	        rd.close();
	        conn.disconnect();
	        System.out.println(sb.toString());
	    }
	

}

package com.brilcom.test;

		
		import java.io.BufferedReader;
		import java.io.IOException;
		import java.io.InputStream;
		import java.io.InputStreamReader;
		import java.net.MalformedURLException;
		import java.net.URL;
		import java.net.URLConnection;


		public class URLReader {

			public static void main(String[] args) {

				
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
				    }
				    
				    
				    // 콘솔에 출력하기
				    System.out.println(sbuf.toString()) ;
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				

			}

		}

		



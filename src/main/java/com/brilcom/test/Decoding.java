package com.brilcom.test;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Decoding {

	public static void main(String[] args) {

		String result = null;
		// 병원 정보
		OkHttpClient client = new OkHttpClient().newBuilder().build();

		try {

			HttpUrl.Builder urlBuilder = HttpUrl
					.parse("http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1").newBuilder();

			// decoding...
			urlBuilder.addQueryParameter("serviceKey",
					"iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e/FctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g==");

			urlBuilder.addQueryParameter("yadmNm", "세란병원");

			String url = urlBuilder.build().toString();
			System.out.println(url);

			Request request = new Request.Builder().url(url).method("GET", null).build();

			Response response = client.newCall(request).execute();
			result = response.body().string();
			System.out.println(result);
			Document doc = convertStringToXMLDocument(result);

			NodeList items = doc.getElementsByTagName("items");
			Node node = items.item(0);
			items = node.getChildNodes();
			NodeList itemList = null;
			node = null;
			int j = 0;
			
			// child node 가 1개 이상인 경우
		      if(items.getLength() > 0) {
		        for(int i=0; i<items.getLength(); i++) {

		          NodeList childList = items.item(i).getChildNodes();

		          if(childList.getLength() > 0) {
		            for (j = 0; j < childList.getLength(); j++) {

		              // 데이터가 있는 애들만 출려되게 한다.
		              if(childList.item(j).getNodeName().equals("#text")==false) {
		            	  if("telno".equals(childList.item(j).getNodeName()) || "addr".equals(childList.item(j).getNodeName())) {
		            		  System.out.println(childList.item(j).getNodeName() + ":"+childList.item(j).getTextContent());
		            	  }
		                //System.out.println("\t xml tag name : " + childList.item(j).getNodeName() + ", xml값 : "+childList.item(j).getTextContent());                                
		              }
		            }
		          }
		          System.out.println();
		        }
		      }
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private static Document convertStringToXMLDocument(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilder db = null;
		Document doc = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlString));
			doc = (Document) db.parse(is);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

}

package com.brilcom.test;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

public class CallbackCallerX extends Thread {

	public static void main(String args[]) {
		
		String result = null;
		// 충전소 정보
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		
		try {
			
			HttpUrl.Builder urlBuilder = HttpUrl.parse("http://apis.data.go.kr/B552584/EvCharger/getChargerStatus").newBuilder();
			
			// decoding...
			urlBuilder.addQueryParameter("serviceKey","YWRncUmVrCMs8Nm7xSfh8Q2j0ao587l3WP2M/l4uSYyNrN16+Lo65V66u+IP1QUCWNki6e+6ejjwEaUJ+Iyaew==");
			
			
			urlBuilder.addQueryParameter("pageNo", "1");
			urlBuilder.addQueryParameter("numOfRows", "10");
			urlBuilder.addQueryParameter("period", "5");
			urlBuilder.addQueryParameter("zcode", "11");
			String url = urlBuilder.build().toString();
			System.out.println(url);
			//*/
			
			Request request = new Request.Builder()
					.url(url)
					.method("GET", null)
					.build();
			
			Response response = client.newCall(request).execute();
		
			
			result = response.body().string();
			System.out.println(result);
			Document doc = convertStringToXMLDocument(result);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://database-mqtt.cbr4uubmqr4e.ap-northeast-2.rds.amazonaws.com:3306/mqttairconditioner?characterEncoding=UTF-8&serverTimezone=UTC","picohome","admin4mqtt");
			
			String query ="INSERT INTO `mqttairconditioner`.`chargerstatus` (`busiId`,`statId`,`chgerId`,`stat`,`statUpdDt`,`lastTsdt`,`lastTedt`,`nowTsdt`) VALUES (?,?,?,?,?,?,?,?)";
			
			PreparedStatement pStmt = con.prepareStatement(query);
			
			
			
			NodeList  items = doc.getElementsByTagName("items");
			Node node = items.item(0);
			items = node.getChildNodes();
			NodeList itemList = null;
			node = null;
			int j = 0;
			String busiId		= null;
			String statId		= null;
			String chgerId		= null;
			String stat			= null;
			String statUpdDt	= null;
			String lastTsdt		= null;
			String lastTedt		= null;
			String nowTsdt		= null;
			for(int i = 0 ; i < items.getLength() ; i++) {
				node = items.item(i);
				itemList = node.getChildNodes();
				
				j = 0;
				node = itemList.item(j++);
				busiId = node.getTextContent();
				
				node = itemList.item(j++);
				statId = node.getTextContent();
				
				node = itemList.item(j++);
				chgerId = node.getTextContent();
				
				node = itemList.item(j++);
				stat = node.getTextContent();
				
				node = itemList.item(j++);
				statUpdDt = node.getTextContent();
				
				node = itemList.item(j++);
				lastTsdt = node.getTextContent();
				
				node = itemList.item(j++);
				lastTedt = node.getTextContent();
				
				node = itemList.item(j++);
				nowTsdt = node.getTextContent();
				
				updatedCnt += executeUpdate( pStmt,  query,  busiId, statId, chgerId, stat, statUpdDt, lastTsdt, lastTedt, nowTsdt);
				
				System.out.println(updatedCnt);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SQLException e) {
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
	
	static int updatedCnt = 0;
	
	public static int executeUpdate(PreparedStatement pStmt, String query, String busiId,String statId,String chgerId,String stat,String statUpdDt,String lastTsdt,String lastTedt,String nowTsdt) throws SQLException{
		int updatedCnt = 0;
		int i = 1;
		pStmt.setString(i++,busiId);
		pStmt.setString(i++,statId);
		pStmt.setString(i++,chgerId);
		pStmt.setString(i++,stat);
		pStmt.setString(i++,statUpdDt);
		pStmt.setString(i++,lastTsdt);
		pStmt.setString(i++,lastTedt);
		pStmt.setString(i++,nowTsdt); 	
		updatedCnt = pStmt.executeUpdate() ;
		return updatedCnt;
	}

}
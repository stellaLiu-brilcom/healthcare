package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jepetto.util.PropertyReader;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetHospitalInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static PropertyReader reader = PropertyReader.getInstance();

	private final String code = "code";
	private final String message = "message";
	private static final int a = 404;
	private static final int b = 500;

	public GetHospitalInfo() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("getHospitalInfo!");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("application/json; charset=utf-8");
		PrintWriter out = res.getWriter();
		JSONObject obj = new JSONObject();

		String result = null;
		// 병원 정보
		OkHttpClient client = new OkHttpClient().newBuilder().build();

		try {

			HttpUrl.Builder urlBuilder = HttpUrl
					.parse("http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1").newBuilder();

			// decoding...
			urlBuilder.addQueryParameter("serviceKey",
					"iHo5nIy5EdGVrbenA2NLPQtCq9hxMPRf43eYj562BJ88KH7e/FctARzeBmmfQWFhYWN6RMP0ROAFYu2UiNL82g==");

			String h_name = (String) req.getAttribute("h_name");

			urlBuilder.addQueryParameter("yadmNm", h_name);

			String url = urlBuilder.build().toString();

			Request request = new Request.Builder().url(url).method("GET", null).build();

			Response response = client.newCall(request).execute();
			result = response.body().string();

			Document doc = convertStringToXMLDocument(result);

			NodeList items = doc.getElementsByTagName("items");
			Node node = items.item(0);
			items = node.getChildNodes();
			NodeList childList = null;
			node = null;
			int j = 0;

			// child node 가 1개 이상인 경우
			if (items.getLength() > 0) {
				for (int i = 0; i < items.getLength(); i++) {

					childList = items.item(i).getChildNodes();

					if (childList.getLength() > 0) {
						for (j = 0; j < childList.getLength(); j++) {

							// 데이터가 있는 애들만 출력되게 한다.
							if (childList.item(j).getNodeName().equals("#text") == false) {
								// 검색하고자 하는 병원의 주소와 전화번호만 필요
								if ("telno".equals(childList.item(j).getNodeName()) || "addr".equals(childList.item(j).getNodeName())) {
									obj.put(childList.item(j).getNodeName(), childList.item(j).getTextContent());
									out.println(obj.toJSONString());
									// System.out.println(childList.item(j).getNodeName() +
									// ":"+childList.item(j).getTextContent());
								}
							}
						}
					}
				}
			}

		} catch (IOException e) {
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

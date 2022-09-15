package com.brilcom.healthcare;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.jepetto.bean.Facade;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.sql.JsonTransfer;
import org.jepetto.sql.XmlConnection;
import org.jepetto.util.PropertyReader;
import org.json.JSONObject;
import org.json.simple.JSONArray;

public class RecordSurvey4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	static PropertyReader reader = PropertyReader.getInstance();
	private Logger logger = LogManager.getLogger(MyThread.class);
	
	private static final String dataSource = reader.getProperty("mqtt_healthcare_datasource");
	private static final String QUERY_FILE = reader.getProperty("healthcare_query_file");
	private static final String queryKey = reader.getProperty("recordScore");
	
	private static String userid = "userid";
	

	private final String code = "code";
	private final String totalScore = "totalScore";
	private final String message = "result";
	private final String msg = "message";
	private static final int a = 200; //등록 성공
	private static final int b = 404; //등록 실패
	private static final int c = 500; //칼럼 오류
	
	
	

    public RecordSurvey4() {
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("getTotalScore!!");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();

		HomeProxy proxy = HomeProxy.getInstance();

		Facade remote = proxy.getFacade();

		userid = (String) request.getAttribute("userid");
		String select = (String) request.getAttribute("select");
		out.println(select);
		StringTokenizer st = new StringTokenizer(select, ",");
		
		HashMap<String, Integer> grade = new HashMap<>();
		grade.put("a", 0);
        grade.put("b", 1);
        grade.put("c", 2);
        grade.put("d", 3);
        grade.put("e", 4);
        grade.put("f", 5);
		
		
		try {
			String token = null;
			int sum = 0;
			int result = 0;
			while (st.hasMoreElements())
			{
				token = st.nextToken();
				result = grade.get(token);
				sum += result;
			}

			System.out.println(sum);
			if(sum >=27) {
				obj.put(totalScore, sum);
				obj.put(msg, "잘 관리됨");
			}else if(20<= sum && sum <27) {
				obj.put(totalScore, sum);
				obj.put(msg, "부분적으로 관리됨");
			}else {
				obj.put(totalScore, sum);
				obj.put(msg, "잘 관리되지 못함");
			}
			
			String total = Integer.toString(sum);
			
			SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
			String s_regDate = format.format (System.currentTimeMillis());
			
			//12세 이상 s_idx:2;
			String s_idx = "1";

			HashMap<String, String> dummy = new HashMap<String, String>();
			String arr[] = new String[] { userid, select, total, s_regDate, s_idx};

			org.jdom2.Document doc;
			
			try {
				int updatedCnt = remote.executeUpdate(dataSource, QUERY_FILE, "recordScore4", dummy , arr);
				System.out.println(updatedCnt);
				if(updatedCnt > 0) {
					obj.put(code, a);
					obj.put(message, "Registed..");
					out.println(obj.toString());
				}else {
					obj.put(code, b);
					obj.put(message, "Disabled..");
					out.println(obj.toString());
				}
			} catch (SQLException | NamingException | JDOMException | IOException e) {
				e.printStackTrace();
				obj.put(code, c);
				obj.put(message, e.getMessage());
				out.println(obj.toString());
			}
			
		
		}catch(NumberFormatException nume) {
			nume.printStackTrace();
		}
	}

}

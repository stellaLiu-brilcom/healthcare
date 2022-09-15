<%@page import="java.io.InputStream"%>
<%@page import="java.util.Properties"%>
<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<%@ page import="org.jepetto.util.PropertyReader" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.sql.DataSource" %>;

<html>
<head>
<meta charset="utf-8">
<title>properties</title>
</head>
<body>
<%
	PropertyReader reader = PropertyReader.getInstance();
	reader.load(System.getProperty("jepetto.properties"));
	String keys[] = reader.getProperties();
	String value = null;
	 
	for(int i = 0 ; i < keys.length ; i++){    
	 out.println(keys[i]+ ":" + reader.getProperty(keys[i])+"<br>");
	}  
	
	Context ctx = new InitialContext();
	Context initCtx  = (Context) ctx.lookup("java:/comp/env");
	DataSource ds = (DataSource) ctx.lookup(reader.getProperty("mqtt_healthcare_datasource"));
	out.println("db connection is closed ? " +  ds.getConnection().isClosed() ); 
	//*/


	/* Properties properties = new Properties();
	String propFileName = "jepetto.properties";  // properties파일명
	
	PropertyReader reader = PropertyReader.getInstance();
	
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	properties.load(inputStream);
	for(String key : properties.stringPropertyNames()) {
	     String value = properties.getProperty(key);
	     System.out.println(key + " => " + value);
	}
	     Context ctx = new InitialContext();
	     Context initCtx  = (Context) ctx.lookup("java:/comp/env");
	     DataSource ds = (DataSource) ctx.lookup(reader.getProperty("mqtt_healthcare_datasource"));
	     out.println("db connection is closed ? " +  ds.getConnection().isClosed() );  
	     //*/

	
 %>   
</body>
</html>
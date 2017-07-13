package TestTransaction;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Review {
	public  Connection getConnection() throws Exception{//连接数据库
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;
		
		Properties properties = new Properties();
		
		InputStream in = Review.class.getClassLoader().getResourceAsStream("jdbc.properties");
		
		properties.load(in);
		
		driverClass = properties.getProperty("driver");
		url = properties.getProperty("jdbcurl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		Class.forName(driverClass);
		return DriverManager.getConnection(url, user, password);
	} 
	
}

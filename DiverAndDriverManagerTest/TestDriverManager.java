package DiverAndDriverManagerTest;
//连接数据库

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;



public class TestDriverManager {
	public static void main(String[] args) throws Exception {
		TestDriverManager e = new TestDriverManager();
		System.out.println(e.getConnection1());//com.mysql.jdbc.JDBC4Connection@306a30c7
	}
	public Connection getConnection1() throws Exception{
//		1、创建连接数据库的基本信息
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;
//		2、创建Properties对象并获取jdbc.Properties对应的输入流
		Properties properties = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
//		3、加载对应的输入流
		properties.load(in);
//		获取输入流中的信息从而决定连接数据库的基本信息的内容
		driverClass = properties.getProperty("driver");
		url = properties.getProperty("jdbcurl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
//		5、利用反射加载数据库驱动程序
		Class.forName(driverClass);
//		6、通过DriverManager中的getConnection()方法获取数据库连接
		return DriverManager.getConnection(url, user, password);
	}
}

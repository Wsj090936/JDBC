package DiverAndDriverManagerTest;
//�������ݿ�

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
//		1�������������ݿ�Ļ�����Ϣ
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;
//		2������Properties���󲢻�ȡjdbc.Properties��Ӧ��������
		Properties properties = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
//		3�����ض�Ӧ��������
		properties.load(in);
//		��ȡ�������е���Ϣ�Ӷ������������ݿ�Ļ�����Ϣ������
		driverClass = properties.getProperty("driver");
		url = properties.getProperty("jdbcurl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
//		5�����÷���������ݿ���������
		Class.forName(driverClass);
//		6��ͨ��DriverManager�е�getConnection()������ȡ���ݿ�����
		return DriverManager.getConnection(url, user, password);
	}
}

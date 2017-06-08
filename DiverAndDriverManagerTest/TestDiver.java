package DiverAndDriverManagerTest;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class TestDiver {
 @Test
	public void testDriver() throws SQLException{
	 //1������Driver�������
		Driver driver = new com.mysql.jdbc.Driver();
	 //2��׼���������ݿ��������Ϣ
		String url = "jdbc:mysql://localhost:3306/test";
		Properties info = new Properties();
		info.put("user","root");
		info.put("password", "090936");
	 //3:����Driver�е�connect()������ȡ���ݿ������
		Connection connection = driver.connect(url, info);
		System.out.println(connection);//com.mysql.jdbc.JDBC4Connection@77afea7d
	}
	/**
	 * ����ķ�������ϵģ�ֻ������mysql�����ӣ����Ҫ�����������ݿ�����ӣ���Ҫ�������������޸�
	 * ��������
	 * @throws Exception 
	 */

		public Connection getConnection() throws Exception{
			String driverClass = null;
			String jdbcUrl = null;
			String user = null;
			String password = null;
			//��ȡ��·���µ�jdbc.properties�ļ�
			InputStream In =getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			Properties properties = new Properties();
			properties.load(In);
			//���ļ��е����ݴ�����ǰ���úõı�����
			driverClass = properties.getProperty("diver");
			jdbcUrl = properties.getProperty("jdbcUrl");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			
			Driver driver = 
					(Driver) Class.forName(driverClass).newInstance();//����
			
			Properties info = new Properties();
			info.put("user", user);
			info.put("password", password);
			Connection connection = driver.connect(jdbcUrl, info);
			return connection;
		}
		@Test
		public void testGetconnection() throws Exception{
			System.out.println(getConnection());//com.mysql.jdbc.JDBC4Connection@161cd475
		}
		
}

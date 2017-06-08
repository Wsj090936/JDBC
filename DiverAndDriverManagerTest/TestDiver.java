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
	 //1：创建Driver的类对象
		Driver driver = new com.mysql.jdbc.Driver();
	 //2：准备连接数据库所需的信息
		String url = "jdbc:mysql://localhost:3306/test";
		Properties info = new Properties();
		info.put("user","root");
		info.put("password", "090936");
	 //3:调用Driver中的connect()方法获取数据库的连接
		Connection connection = driver.connect(url, info);
		System.out.println(connection);//com.mysql.jdbc.JDBC4Connection@77afea7d
	}
	/**
	 * 上面的方法是耦合的，只能用于mysql的连接，如果要进行其他数据库的连接，需要对上述语句进行修改
	 * 具体如下
	 * @throws Exception 
	 */

		public Connection getConnection() throws Exception{
			String driverClass = null;
			String jdbcUrl = null;
			String user = null;
			String password = null;
			//读取类路径下的jdbc.properties文件
			InputStream In =getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			Properties properties = new Properties();
			properties.load(In);
			//将文件中的数据存入先前设置好的变量中
			driverClass = properties.getProperty("diver");
			jdbcUrl = properties.getProperty("jdbcUrl");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			
			Driver driver = 
					(Driver) Class.forName(driverClass).newInstance();//反射
			
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

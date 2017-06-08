package statementTest;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

public class Teststatement {
@Test
	public void testStatement() throws Exception{//利用Statement进行更新操作
			Connection con = null;
			Statement state = null;
	try{
	//		1、获取数据库连接
			con = getConnection();
	//		2、生成插入的sql语句
			String sql = "INSERT INTO animal (id,name,age) VALUES (1,'cat',3)";
	//		3、获取操作SQL语句的Statement对象
			state = con.createStatement();
	//		4、调用Statement对象的executeUpdate(String  aql)方法来执行sql语句
			state.executeUpdate(sql);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				//5、关闭Statement对象
				if(con != null){
					try {
						state.close();
					} catch (Exception e) {
						e.printStackTrace();
						}
					}
				//6、关闭数据库连接
				if(con != null){
					con.close();
					}
			}
	}
		public Connection getConnection() throws Exception{//建立数据库连接
			String driverclass = null;
			String url = null;
			String user = null;
			String password = null;
			
			Properties properties = new Properties();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			properties.load(in);
			
			driverclass = properties.getProperty("driver");
			url = properties.getProperty("jdbcurl");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			
			Class.forName(driverclass);
			return DriverManager.getConnection(url, user, password);
		}
}

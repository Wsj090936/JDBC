package TestResultSet;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

public class TestResuleSet {
	@Test
	public void testResultSet() throws Exception{
		Connection con = null;
		Statement state = null;
		ResultSet rs= null;
		
		try {
			//1、获取Conection，建立数据库连接
			con = getConnection3();
			//2、获取statement
			state = con.createStatement();
			//3、创建SQL语句
			String sql = "SELECT * FROM animal";
			//4、调用executeQuery(String  sql)方法获取ResultSet
			rs = state.executeQuery(sql);
			//5、处理ResultSet
			while(rs.next()){
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				System.out.println(id);
				System.out.println(name);
				System.out.println(age);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//6、关闭数据库
			if(rs != null){
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(con != null){
				try {
					state.close();
				} catch (Exception e) {
					e.printStackTrace();
					}
				}
			if(con != null){
				con.close();
				}
		}
	}

	public Connection getConnection3() throws Exception{//获取数据库连接
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;
		
		Properties properties = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		properties.load(in);
		
		driverClass = properties.getProperty("driver");
		url = properties.getProperty("jdbcurl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		
		Class.forName(driverClass);
		return DriverManager.getConnection(url, user, password);
	}
}
//输出结果如下
//	1
//	cat
//	3
//	2
//	dog
//	5
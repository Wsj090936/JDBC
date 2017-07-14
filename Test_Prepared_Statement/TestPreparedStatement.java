package Test_Prepared_Statement;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.junit.Test;

public class TestPreparedStatement {
@Test
	public void PreparedStatementTest() throws Exception{
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	try {
		//1、连接数据库
		con = getConnection();
		//2、准备sql语句
		String sql1 = "INSERT INTO Animal (id,name,age) VALUES (?,?,?)";
		//3、获取preparedStatement对象
		ps = con.prepareStatement(sql1);
		//4、调用SetXxx()方法插入数据
		ps.setInt(1, 6);//第一个占位符
		ps.setString(2, "egg");//第二个占位符
		ps.setInt(3, 8);//第三个占位符
		//5、执行更新操作
		ps.executeUpdate();

	} catch (Exception e) {
		e.printStackTrace();
	} finally{
		release(rs, ps, con);//关闭数据库
	}
}
	public Connection getConnection() throws Exception{//获取数据库连接
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
	public void release(ResultSet rs , PreparedStatement ps , Connection con){//释放数据库连接
		if(rs != null){
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		if(ps != null){
			try {
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		if(con != null){
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		}
		}
	}
}

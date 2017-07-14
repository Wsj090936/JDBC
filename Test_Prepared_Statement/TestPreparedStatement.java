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
		//1���������ݿ�
		con = getConnection();
		//2��׼��sql���
		String sql1 = "INSERT INTO Animal (id,name,age) VALUES (?,?,?)";
		//3����ȡpreparedStatement����
		ps = con.prepareStatement(sql1);
		//4������SetXxx()������������
		ps.setInt(1, 6);//��һ��ռλ��
		ps.setString(2, "egg");//�ڶ���ռλ��
		ps.setInt(3, 8);//������ռλ��
		//5��ִ�и��²���
		ps.executeUpdate();

	} catch (Exception e) {
		e.printStackTrace();
	} finally{
		release(rs, ps, con);//�ر����ݿ�
	}
}
	public Connection getConnection() throws Exception{//��ȡ���ݿ�����
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
	public void release(ResultSet rs , PreparedStatement ps , Connection con){//�ͷ����ݿ�����
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

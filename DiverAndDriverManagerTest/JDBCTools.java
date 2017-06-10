package DiverAndDriverManagerTest;
//JDBC工具类  包含数据库的连接，更新，关闭等功能
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//JDBC的工具类，用于关闭数据库连接操作，更新操作和查询操作
public class JDBCTools {

	public void select(String sql){//查询信息
		Connection con = null;
		Statement state = null;
		ResultSet rs = null;
		
		try {
			con = getConnection();
			
			state = con.createStatement();
			
			rs = state.executeQuery(sql);
		while(rs.next()){
			int FlowID = rs.getInt("FlowID");//流水号
			String Type = rs.getString("Type");//考试类型
			String IDcard = rs.getString("IDcard");//身份证号
			String Examcard = rs.getString("Examcard");//准考证号码
			String StudentName = rs.getString("StudentName");//学生姓名
			String Location = rs.getString("Location");//区域
			int Grade = rs.getInt("Grade");//成绩
			System.out.print("流水号:"+FlowID+"  ");
			System.out.print("考试类型:"+Type+"  ");
			System.out.print("身份证号:"+IDcard+"  ");
			System.out.print("准考证号:"+Examcard+"  ");
			System.out.print("学生姓名:"+StudentName+"  ");
			System.out.print("学生所在地:"+Location+"  ");
			System.out.print("成绩:"+Grade+"  ");
			System.out.println();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{//关闭连接
			release(rs, con, state);
		}
	}
	public void update(String sql){//实现数据库的更新操作
		Connection con = null;
		Statement state = null;
		
		try {
			con = getConnection();
			
			state = con.createStatement();
			
			state.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			release(con,state);
		}
		
	}
	public Connection getConnection() throws Exception{//连接数据库
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;
		
		Properties properties = new Properties();
		
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		properties.load(in);
		
		driverClass = properties.getProperty("driver");
		url = properties.getProperty("jdbcurl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		Class.forName(driverClass);
		return DriverManager.getConnection(url, user, password);
	} 
	public static void release(Connection con , Statement state){//关闭数据库连接
		if(state != null){
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static void release(ResultSet rs , Connection con , Statement state){//关闭数据库连接
		if(rs != null)
		{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(state != null){
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

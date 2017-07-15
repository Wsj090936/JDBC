package Test_dbutils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
/**
 * JDBC的工具类，利用数据库连接池连接数据库，以及相应的事务操作
 * @author wushijia
 *
 */
public class JDBCTools {
	/**
	 * 提交事务
	 * @param con
	 */
    public static void commit(Connection con){
    	if(con != null){
    		try {
				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    /**
     * 事务的回滚
     * @param con
     */
    public static void rollback(Connection con){
    	if(con != null){
    		try {
				con.rollback();
			} catch (Exception e) {
                e.printStackTrace();
			}
    	}
    }
    /**
     * 开始事务
     * @param con
     */
    public static void beginTransaction(Connection con){
    	if(con != null){
    		try {
				con.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    /**
     * 利用静态代码块初始化数据库连接池，数据库连接池应该只被初始化一次
     */
    public static DataSource datasource = null;
    static{
    	Properties properties = new Properties();
    	InputStream in = JDBCTools.class.getClassLoader().getResourceAsStream("dbcp.properties");
		try {
			properties.load(in);
			datasource = BasicDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 获取数据库连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
    	return datasource.getConnection();
    }
    /**
     * 释放连接
     * @param con
     * @param state
     * @param rs
     */
    public static void releaseDB(ResultSet rs,Statement state,Connection con){
    	if(rs != null){
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
    			con.close();//释放连接只是将连接放回数据库连接池，供其它需要连接的事务使用
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
}

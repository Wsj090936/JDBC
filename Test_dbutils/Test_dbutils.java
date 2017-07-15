package Test_dbutils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;
/**
 * DButils���ߵ�ʹ��
 * @author wushijia
 *
 */
public class Test_dbutils {
	/**
	 * ʹ��DButils�������ݿ�ĸ��²���
	 */
	@Test
    public void test1(){
    	Connection con = null;
    	//1������QueryRunner�Ķ���ʵ��
    	QueryRunner queryRunner = new QueryRunner();
    	String sql = "INSERT INTO customer(id,name,date) VALUES(?,?,?)";
    	try {
			con = JDBCTools.getConnection();
		//2��������update����
			queryRunner.update(con, sql, 2,"Jerry",new Date(new java.util.Date().getTime()));
		} catch (Exception e) {
            e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, con);
		}
    }
	/**
	 * �Լ�ʵ��handle����������query
	 */
@Test
	public void testResultSetHandle(){
		Connection con = null;
		QueryRunner queryRunner = new QueryRunner();
		String sql = "SELECT id,name,date FROM customer";
		try {
			con = JDBCTools.getConnection();
			Object object = queryRunner.query(con, sql, new ResultSetHandler<Object>(){

				@Override
				public Object handle(ResultSet rs) throws SQLException {//�Լ�ʵ��handle����
					List<Customer> customers = new ArrayList<>();
					while(rs.next()){
						int id = rs.getInt(1);
						String name = rs.getString(2);
						Date date = rs.getDate(3);
						
						Customer customer = new Customer(id,name,date);
						customers.add(customer);
					}
					
					return customers;
				}
			});
			System.out.println(object);//[Customer [id=1, name=Tom, date=2017-07-15], Customer [id=2, name=Jerry, date=2017-07-15]]
		} catch (Exception e) {
            e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, con);
		}
	}
/**
 * ����ResultSetHandle�ӿڵ�ʵ����BeanListHandler
 */
@Test
    public void testBeanListHandler(){
	    Connection con = null;
	    QueryRunner queryRunner = new QueryRunner();
		String sql = "SELECT I_D id,name,date FROM customer";
		
		try {
			con = JDBCTools.getConnection();
			Object object = queryRunner.query(con, sql, new BeanListHandler<Customer>(Customer.class));
			System.out.println(object);//[Customer [id=1, name=Tom, date=2017-07-15], Customer [id=2, name=Jerry, date=2017-07-15]]
		} catch (Exception e) {
            e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, con);
		}
  }
/**
 * ����ResultSetHandle�ӿڵ�ʵ����BeanHandler
 */
@Test
    public void testBeanHandler(){
	    Connection con = null;
	    QueryRunner queryRunner = new QueryRunner();
	    String sql = "SELECT I_D id,name,date FROM customer";
	    
	    try {
			con = JDBCTools.getConnection();
			Object object = queryRunner.query(con, sql, new BeanHandler<>(Customer.class));
			System.out.println(object);//Customer [id=1, name=Tom, date=2017-07-15]
		} catch (Exception e) {
            e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, con);
		}
  }
/**
 * ����ResultSetHandle�ӿڵ�ʵ����MapListHandler
 */
@Test
    public void testMapListHandler(){
	    Connection con = null;
	    QueryRunner queryRunner = new QueryRunner();
	    String sql = "SELECT I_D id,name,date FROM customer";
	    
	    try {
			con = JDBCTools.getConnection();
			List<Map<String,Object>> object = queryRunner.query(con, sql, new MapListHandler());
			System.out.println(object);//[{id=1, name=Tom, date=2017-07-15}, {id=2, name=Jerry, date=2017-07-15}]
		} catch (Exception e) {
	        e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, con);
		}
  }
/**
 * ����ResultSetHandle�ӿڵ�ʵ����ScalarHandler
 */
@Test 
    public void testScalarHandler(){
	    Connection con = null;
	    QueryRunner queryRunner = new QueryRunner();
	    String sql = "SELECT I_D id,name FROM customer";
	    
	    try {
	    	con = JDBCTools.getConnection();
			Object object = queryRunner.query(con, sql, new ScalarHandler<>(2));//���ز�ĵĽ�����е�2�е�����
			System.out.println(object);//Tom
		} catch (Exception e) {
            e.printStackTrace();
		} finally {
		    JDBCTools.releaseDB(null, null, con);	
		}
  }
}

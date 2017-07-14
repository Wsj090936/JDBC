package TestBatch;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import org.junit.Test;

/*
 * 使用JDBC批量处理语句
 */
public class TestBatch {
@Test
    public void TestBatch(){//使用addBatch()方法批量处理SQL语句
	    Connection con = null;
	    PreparedStatement ps = null;
	    String sql = "INSERT INTO customer(id,name,date) VALUES(?,?,?)";
	    try {
			con = JDBCTools.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
			Date date = new Date(new java.util.Date().getTime());
			long start = System.currentTimeMillis();
			for(int i = 0;i < 100000;i++){
				ps.setInt(1, i+1);
				ps.setString(2, "name"+(i + 1));
				ps.setDate(3, date);
				
				ps.addBatch();//累积SQL
				if((i + 1) % 1000 == 0){
					ps.executeBatch();
					ps.clearBatch();
				}
			}
			long end = System.currentTimeMillis();
			con.commit();
			System.out.println("Time:"+(end - start));//Time:1647
		} catch (Exception e) {
            e.printStackTrace();
		} finally{
			JDBCTools.release(null, con, ps);
		}
  }
@Test
    public void TestBathUsePreparedStatement(){//使用PreparedStatement插入100000条数据
    	Connection con = null;
    	PreparedStatement ps = null;
	    String sql = "INSERT INTO customer(id,name,date) VALUES(?,?,?)";
	    
    	try {
			con = JDBCTools.getConnection();
			con.setAutoCommit(false);//取消事务执行完后自动提交
			ps = con.prepareStatement(sql);
			Date date = new Date(new java.util.Date().getTime());
			long start = System.currentTimeMillis();
			
			for(int i = 0;i < 100000;i++){
				ps.setInt(1, i+1);
				ps.setString(2, "name"+(i + 1));
				ps.setDate(3, date);
				ps.executeUpdate();
			}
			long end = System.currentTimeMillis();
			con.commit();
			System.out.println("Time:"+(end - start));//Time:13972
		} catch (Exception e) {
            e.printStackTrace();
		} finally{
			JDBCTools.release(null, con, ps);
		}
    }
}

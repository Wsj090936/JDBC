package TestBatch;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import org.junit.Test;

/*
 * ʹ��JDBC�����������
 */
public class TestBatch {
@Test
    public void TestBatch(){//ʹ��addBatch()������������SQL���
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
				
				ps.addBatch();//�ۻ�SQL
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
    public void TestBathUsePreparedStatement(){//ʹ��PreparedStatement����100000������
    	Connection con = null;
    	PreparedStatement ps = null;
	    String sql = "INSERT INTO customer(id,name,date) VALUES(?,?,?)";
	    
    	try {
			con = JDBCTools.getConnection();
			con.setAutoCommit(false);//ȡ������ִ������Զ��ύ
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

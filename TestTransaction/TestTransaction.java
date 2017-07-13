package TestTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class TestTransaction {//事务的多个操作
	DAO dao = new DAO();
	@Test
	public  void transactionTest(){
		Connection con = null;
        try {
			con = JDBCTools.getConnection();
			con.setAutoCommit(false);
			String sql = "UPDATE customer SET money=money-1000 WHERE id=?";
			dao.update(con, sql,1);
			sql = "UPDATE customer SET money=money+1000 WHERE id=?";
			dao.update(con, sql,2);
			con.commit();//数据更新完后提交事务
		} catch (Exception e) {//如果遇到异常  就回滚事务
            e.printStackTrace();
            try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{//事务执行完毕或者出现了异常后，释放数据库连接
			JDBCTools.release(null, con, null);
		}
	}
	@Test
	public void testTransactionIsolation(){//事务一  执行更新操作
		Connection con = null;
		try {
			con = JDBCTools.getConnection();
			con.setAutoCommit(false);
			String sql = "UPDATE customer SET money=money-500 WHERE id=1";
			dao.update(con, sql);
			con.commit();
		} catch (Exception e) {
            e.printStackTrace();
		}finally{
			dao.release(null, con, null);
		}
	}
	@Test
	public void testTransactionIsolation1(){//事务二  查询操作
		Connection con = null;
		try {
			con = JDBCTools.getConnection();
			String sql = "SELECT money FROM customer WHERE id=1";
			Integer value = getvalues(con, sql);
			System.out.println(value);
		} catch (Exception e) {
            e.printStackTrace();
		}finally{
			dao.release(null, con, null);
		}
	}
    /**
     * 返回某条记录的某一字段的值
     * @param sql
     * @param args
     * @return
     */
    public <E> E getvalues(Connection con,String sql,Object ... args){
    	PreparedStatement ps = null;
    	ResultSet rs = null;//所得结果集只为一行一列
    	try {
			con = JDBCTools.getConnection();
			con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);//设置隔离级别为读已提交的
			ps = con.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1,args[i]);
			}
			rs = ps.executeQuery();
			if(rs.next()){
				return (E)rs.getObject(1);
			}
		} catch (Exception e) {
            e.printStackTrace();
		}finally{
			JDBCTools.release(rs, null, ps);
		}
    	return null;
    }
}

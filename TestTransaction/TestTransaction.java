package TestTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class TestTransaction {//����Ķ������
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
			con.commit();//���ݸ�������ύ����
		} catch (Exception e) {//��������쳣  �ͻع�����
            e.printStackTrace();
            try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{//����ִ����ϻ��߳������쳣���ͷ����ݿ�����
			JDBCTools.release(null, con, null);
		}
	}
	@Test
	public void testTransactionIsolation(){//����һ  ִ�и��²���
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
	public void testTransactionIsolation1(){//�����  ��ѯ����
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
     * ����ĳ����¼��ĳһ�ֶε�ֵ
     * @param sql
     * @param args
     * @return
     */
    public <E> E getvalues(Connection con,String sql,Object ... args){
    	PreparedStatement ps = null;
    	ResultSet rs = null;//���ý����ֻΪһ��һ��
    	try {
			con = JDBCTools.getConnection();
			con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);//���ø��뼶��Ϊ�����ύ��
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

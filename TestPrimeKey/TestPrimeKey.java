package TestPrimeKey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

public class TestPrimeKey {
	@Test
	public void test1(){
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO animal(name,age) VALUES (?,?)";
		try {
			con = JDBCTools.getConnection();
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, "aaa");
			ps.setInt(2, 67);
			ps.executeUpdate();
			ResultSet rs1 = ps.getGeneratedKeys();//获取包含了主码的结果集
			if(rs1.next()){
				System.out.println(rs1.getObject(1));
			}else{
				System.out.println("空");
			}
		} catch (Exception e) {
            e.printStackTrace();
		}finally{
			JDBCTools.release(con, ps);
		}
	}

}

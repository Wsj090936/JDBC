package TestBlob;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

public class TestBlob {
@Test
    public void getBlob(){//读取Blob数据
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		con = JDBCTools.getConnection();
		String sql = "SELECT id,name,age,picture FROM animal WHERE id=5";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		if(rs.next()){
			int id = rs.getInt(1);
			String name = rs.getString(2);
			int age = rs.getInt(3);
			
			Blob picture = rs.getBlob(4);//得到Blob对象
			//开始读入文件
			InputStream in = picture.getBinaryStream();
			OutputStream out = new FileOutputStream("cat.png");
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = in.read(buffer)) != -1){
				out.write(buffer, 0, len);
			}
		}
	} catch (Exception e) {
        e.printStackTrace();
	}
}
@Test
    public void insertBlob(){//插入Blob
	Connection con = null;
	PreparedStatement ps = null;
	try {
		con = JDBCTools.getConnection();
		String sql = "INSERT INTO animal(name,age,picture) VALUES(?,?,?)";
		ps = con.prepareStatement(sql);
		ps.setString(1, "TheCat");
		ps.setInt(2, 8);
		InputStream in = new FileInputStream("J:/test1/TomCat.png");//生成被插入文件的节点流
		//设置Blob
		ps.setBlob(3, in);
		
		ps.executeUpdate();
	} catch (Exception e) {
        e.printStackTrace();
	}finally{
		JDBCTools.release(con, ps);
	}
}
}

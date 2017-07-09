package TestPreparedStatement;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class GeneralQuery {
@Test
	public void test(){
		String sql = "SELECT Flow_ID FlowID,ID_card IDcard FROM examstudent WHERE Flow_ID=?";
		Student stu = get(Student.class,sql,1001);
		System.out.println(stu);
	}
	public <T> T get(Class<T> clazz,String sql,Object ... args){
		T entity = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = JDBCTools.getConnection();
			ps = con.prepareStatement(sql);
			for(int i = 0; i < args.length; i++){
				ps.setObject(i+1, args[i]);
			}
			//1、得到ResuktSet结果集
			rs = ps.executeQuery();//该结果集能够知道有几行，以及每一行的某一列对应的值
			//2、得到ResultSetMetaData对象
			ResultSetMetaData rsmd =  rs.getMetaData();//该对象可以知道结果集有几列，以及每一列所对应的别名
			//3、创建一个Map<String,Object>对象，用于存列的别名与对应的值
			Map<String,Object> valueMap = new HashMap<>();
			//4、处理结果集ResultSet与ResultSetMetaData来产生键值对entry，将其存入Map集合中
			while(rs.next()){
				for(int i = 0 ; i < rsmd.getColumnCount() ; i++){//遍历每一列，并得到其别名以及相应的值
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Object columnValue = rs.getObject(i + 1);
					
					valueMap.put(columnLabel, columnValue);
				}
				//5、利用反射创建Class类对应的运行时类的对象
				if(valueMap.size() > 0){
					 entity = clazz.newInstance();
				//6、遍历Map集合，利用反射对运行时类的对象对应的属性进行赋值
					for(Map.Entry<String, Object> entry : valueMap.entrySet()){
						String fieldname = entry.getKey();
						Object value = entry.getValue();
						Field f1 = clazz.getDeclaredField(fieldname);
						f1.setAccessible(true);
						f1.set(entity, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(rs, con, ps);
		}
		return entity;
		
	}
}

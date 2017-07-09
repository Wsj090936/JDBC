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
			//1���õ�ResuktSet�����
			rs = ps.executeQuery();//�ý�����ܹ�֪���м��У��Լ�ÿһ�е�ĳһ�ж�Ӧ��ֵ
			//2���õ�ResultSetMetaData����
			ResultSetMetaData rsmd =  rs.getMetaData();//�ö������֪��������м��У��Լ�ÿһ������Ӧ�ı���
			//3������һ��Map<String,Object>�������ڴ��еı������Ӧ��ֵ
			Map<String,Object> valueMap = new HashMap<>();
			//4����������ResultSet��ResultSetMetaData��������ֵ��entry���������Map������
			while(rs.next()){
				for(int i = 0 ; i < rsmd.getColumnCount() ; i++){//����ÿһ�У����õ�������Լ���Ӧ��ֵ
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Object columnValue = rs.getObject(i + 1);
					
					valueMap.put(columnLabel, columnValue);
				}
				//5�����÷��䴴��Class���Ӧ������ʱ��Ķ���
				if(valueMap.size() > 0){
					 entity = clazz.newInstance();
				//6������Map���ϣ����÷��������ʱ��Ķ����Ӧ�����Խ��и�ֵ
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

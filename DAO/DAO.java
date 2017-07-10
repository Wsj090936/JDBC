package DAO;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class DAO {
    public void updata(String sql,Object ... args){//���ݿ�ĸ��²�������ʵ������ɾ���ĵĲ���
    	Connection con = null;
    	PreparedStatement ps = null;
    	
    	try {
			con = JDBCTools.getConnection();//��ȡ���ݿ�����
			ps = con.prepareStatement(sql);
			for(int i = 0;i<args.length;i++){//���ռλ��
				ps.setObject(i + 1, args[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(con, ps);//�ͷ����ݿ�����
		}
    }
    public <T> T get(Class<T> clazz,String sql,Object ... args){
    	T entity = null;
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	try {
			con = JDBCTools.getConnection();
			ps = con.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){//���ռλ��
				ps.setObject(i + 1,args[i]);
			}
			rs = ps.executeQuery();//�õ������������������֪��ĳһ����ĳһ�еľ���ֵ
			if(rs.next()){
				ResultSetMetaData rsmd = rs.getMetaData();//�ö������֪��������м��У��Լ�ÿһ������Ӧ�ı�������Ϣ
				Map<String,Object> valueMap = new HashMap<>();//����Map���������������е������Լ���Ӧ������ֵ
				for(int i = 0;i < rsmd.getColumnCount();i++){
					String ColumnLabel = rsmd.getColumnLabel(i + 1);//�õ���ѯ����ÿһ�е�����
					Object ColumnValue = rs.getObject(i + 1);//�õ�ÿһ������Ӧ��ֵ
					valueMap.put(ColumnLabel, ColumnValue);
				}
                if(valueMap.size() > 0){
                	entity = clazz.newInstance();
    				for(Map.Entry<String, Object> entry : valueMap.entrySet()){
    				    String fieldName = entry.getKey();
    				    Object fieldvalue = entry.getValue();
    				    Field f1 = clazz.getDeclaredField(fieldName);
    				    f1.setAccessible(true);
    				    f1.set(entity, fieldvalue);
    				}
                }
			}
		} catch (Exception e) {
            e.printStackTrace();
		}
    	
    	return entity;
    }
}

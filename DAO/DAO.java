package DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;

public class DAO {
    public void updata(String sql,Object ... args){//���ݿ�ĸ��²�������ʵ������ɾ���ĵĲ���
    	Connection con = null;
    	PreparedStatement ps = null;
    	
    	try {
			con = getConnection();//��ȡ���ݿ�����
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
    public <T> T get(Class<T> clazz,String sql,Object ... args){//���ݿ�Ĳ�ѯ����
    	T entity = null;
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	try {
			con = getConnection();
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
    				for(Map.Entry<String, Object> entry : valueMap.entrySet()){//���÷���Ϊ��Ӧ�����Ը�ֵ
    				    String fieldName = entry.getKey();
    				    Object fieldvalue = entry.getValue();
//    				    Field f1 = clazz.getDeclaredField(fieldName);
//    				    f1.setAccessible(true);
//    				    f1.set(entity, fieldvalue);
    				    BeanUtils.setProperty(entity, fieldName, fieldvalue);//��Java�����Ը�ֵ
    				}
                }
			}
		} catch (Exception e) {
            e.printStackTrace();
		}
    	
    	return entity;
    }
	public Connection getConnection() throws Exception{//�������ݿ�
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;
		
		Properties properties = new Properties();
		
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		properties.load(in);
		
		driverClass = properties.getProperty("driver");
		url = properties.getProperty("jdbcurl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		Class.forName(driverClass);
		return DriverManager.getConnection(url, user, password);
	} 
	public void release(Connection con , Statement state){//�ر����ݿ�����
		if(state != null){
			try {
				state.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(con != null){
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	public void release(ResultSet rs , Connection con , Statement state){//�ر����ݿ�����
		if(rs != null)
		{
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(state != null){
			try {
				state.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(con != null){
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}


package TestTransaction;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;

public class DAO {
	/**
	 * ���ݿ�ĸ���
	 * @param sql
	 * @param args
	 */
    public void update(Connection con,String sql,Object ... args){//���ݿ�ĸ��²�������ʵ������ɾ���ĵĲ���
    	PreparedStatement ps = null;
    	
    	try {
			ps = con.prepareStatement(sql);
			for(int i = 0;i<args.length;i++){//���ռλ��
				ps.setObject(i + 1, args[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(null, null, ps);
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
    /**
     * ���ݿ�ĵ�����ѯ����
     * @param clazz
     * @param sql
     * @param args
     * @return
     */
    public <T> T getForOne(Connection con,Class<T> clazz,String sql,Object ... args){//���ݿ�Ĳ�ѯ����
    	T entity = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	try {
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
		}finally{
			JDBCTools.release(rs, null, ps);
		}
    	
    	return entity;
    }
    /**
     * ���ݿ�Ķ����ѯ���ؽ�����ϵĲ���
     * @param clazz
     * @param sql
     * @param args
     * @return
     */
    public <T> List<T> getForList(Connection con,Class<T> clazz,String sql,Object ... args){
    	List<T> list =  new ArrayList<>();
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
			ps = con.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();//�õ������
			List<Map<String,Object>> valueList = new ArrayList<>();//���ڴ�Ŷ�����¼��List����
			ResultSetMetaData rsmd = rs.getMetaData();
			Map<String,Object> map = null;//���һ����¼��Map����
			while(rs.next()){//��������
				map = new HashMap<String,Object>();
				for(int i = 0;i < rsmd.getColumnCount();i++){
					String columLabel = rsmd.getColumnLabel(i + 1);
					Object value = rs.getObject(i + 1);
					//��һ����¼����mao������
					map.put(columLabel, value);
				}
				valueList.add(map);
			}
			//�ж�valueList�Ƿ�Ϊ��  ����Ϊ�գ������valueList���ϣ��õ�һ����Map���󣬽���תΪClass������Ӧ�Ķ���
			T bean = null;
			if(valueList.size() > 0){
				for(Map<String,Object> each : valueList){
					for(Map.Entry<String, Object> e : each.entrySet()){
						String fieldname = e.getKey();
						Object fieldvalue = e.getValue();
						//Ϊ��Ӧ��Java�����Ը�ֵ
						bean =  clazz.newInstance();
						BeanUtils.setProperty(bean, fieldname, fieldvalue);
					}
					//��T�������list��
					list.add(bean);
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
		}finally{
			JDBCTools.release(rs, null, ps);
		}
    	return list;
    }
    /**
     * ���ݿ�����Ӳ���
     * @return
     * @throws Exception
     */
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
	/**
	 * ���ݿ�������ͷŲ���
	 * @param con
	 * @param state
	 */
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


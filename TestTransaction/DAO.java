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
	 * 数据库的更新
	 * @param sql
	 * @param args
	 */
    public void update(Connection con,String sql,Object ... args){//数据库的更新操作，可实现增、删、改的操作
    	PreparedStatement ps = null;
    	
    	try {
			ps = con.prepareStatement(sql);
			for(int i = 0;i<args.length;i++){//填充占位符
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
     * 返回某条记录的某一字段的值
     * @param sql
     * @param args
     * @return
     */
    public <E> E getvalues(Connection con,String sql,Object ... args){
    	PreparedStatement ps = null;
    	ResultSet rs = null;//所得结果集只为一行一列
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
     * 数据库的单个查询操作
     * @param clazz
     * @param sql
     * @param args
     * @return
     */
    public <T> T getForOne(Connection con,Class<T> clazz,String sql,Object ... args){//数据库的查询操作
    	T entity = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	try {
			ps = con.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){//填充占位符
				ps.setObject(i + 1,args[i]);
			}
			rs = ps.executeQuery();//得到结果集，利用它可以知道某一行中某一列的具体值
			if(rs.next()){
				ResultSetMetaData rsmd = rs.getMetaData();//该对象可以知道结果集有几列，以及每一列所对应的别名等信息
				Map<String,Object> valueMap = new HashMap<>();//建立Map集合用来存结果集中的列名以及对应的属性值
				for(int i = 0;i < rsmd.getColumnCount();i++){
					String ColumnLabel = rsmd.getColumnLabel(i + 1);//得到查询出的每一列的列名
					Object ColumnValue = rs.getObject(i + 1);//得到每一列所对应的值
					valueMap.put(ColumnLabel, ColumnValue);
				}
                if(valueMap.size() > 0){
                	entity = clazz.newInstance();
    				for(Map.Entry<String, Object> entry : valueMap.entrySet()){//利用反射为对应的属性赋值
    				    String fieldName = entry.getKey();
    				    Object fieldvalue = entry.getValue();
//    				    Field f1 = clazz.getDeclaredField(fieldName);
//    				    f1.setAccessible(true);
//    				    f1.set(entity, fieldvalue);
    				    BeanUtils.setProperty(entity, fieldName, fieldvalue);//对Java类属性赋值
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
     * 数据库的多个查询返回结果集合的操作
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
			rs = ps.executeQuery();//得到结果集
			List<Map<String,Object>> valueList = new ArrayList<>();//用于存放多条记录的List集合
			ResultSetMetaData rsmd = rs.getMetaData();
			Map<String,Object> map = null;//存放一条记录的Map集合
			while(rs.next()){//处理结果集
				map = new HashMap<String,Object>();
				for(int i = 0;i < rsmd.getColumnCount();i++){
					String columLabel = rsmd.getColumnLabel(i + 1);
					Object value = rs.getObject(i + 1);
					//将一条记录存入mao集合中
					map.put(columLabel, value);
				}
				valueList.add(map);
			}
			//判断valueList是否为空  若不为空，则遍历valueList集合，得到一个个Map对象，将其转为Class参数对应的对象
			T bean = null;
			if(valueList.size() > 0){
				for(Map<String,Object> each : valueList){
					for(Map.Entry<String, Object> e : each.entrySet()){
						String fieldname = e.getKey();
						Object fieldvalue = e.getValue();
						//为对应的Java类属性赋值
						bean =  clazz.newInstance();
						BeanUtils.setProperty(bean, fieldname, fieldvalue);
					}
					//将T对象放入list中
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
     * 数据库的连接操作
     * @return
     * @throws Exception
     */
	public Connection getConnection() throws Exception{//连接数据库
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
	 * 数据库的连接释放操作
	 * @param con
	 * @param state
	 */
	public void release(Connection con , Statement state){//关闭数据库连接
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
	public void release(ResultSet rs , Connection con , Statement state){//关闭数据库连接
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


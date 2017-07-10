package DAO;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class DAO {
    public void updata(String sql,Object ... args){//数据库的更新操作，可实现增、删、改的操作
    	Connection con = null;
    	PreparedStatement ps = null;
    	
    	try {
			con = JDBCTools.getConnection();//获取数据库连接
			ps = con.prepareStatement(sql);
			for(int i = 0;i<args.length;i++){//填充占位符
				ps.setObject(i + 1, args[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(con, ps);//释放数据库连接
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

package Test_Data_Source;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

public class DataSource_Test {
	/**
	 * 使用DBPC数据库连接池连接数据库
	 * 步骤:
	 * 1、导入commons pool2 jar包与commons dbcp2 jar包
	 * 2、创建数据库连接池（数据源实例）
	 * 3、将连接数据库必要的属性作为参数传入到数据源中指定的方法中
	 * 4、从数据源获取数据库连接
	 * @throws SQLException 
	 */
@Test
    public void TestDBCP() throws SQLException{
    	BasicDataSource dataSource = null;
    	//1、创建数据源实例
    	dataSource = new BasicDataSource();
    	//2、传入必要的连接数据库的参数
    	
    	dataSource.setUsername("root");
    	dataSource.setPassword("090936");
    	dataSource.setUrl("jdbc:mysql://localhost:3306/test");
    	dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    	//3、传入可选的参数
        dataSource.setMaxTotal(5);//指定同一时刻可以向数据库连接池中申请的最大连接数
        dataSource.setInitialSize(5);//指定数据库连接池中初始化连接数的个数
        dataSource.setMinIdle(2);//指定在数据库连接池中保存的最少的空闲连接的数量
        dataSource.setMaxWaitMillis(5000);//设定等待数据库连接池分配连接的最长时间(以毫秒为单位)，超出改时间则抛出异常
    	//3、从数据源获取数据库连接
    	Connection con = dataSource.getConnection();
    	System.out.println(con);
    }
/**
 * 使用DBCP数据库连接池编写通用的连接数据库的方法
 * 1、创建一个名为"dbcp.properties"的文件，里面存入连接数据库所需的信息，其中的键应该来自BasicDataSource中的属性
 * 2、加载"dbcp.properties"文件
 * 3、调用BasicDataSourceFactory中的createSource()方法创建DataSource
 * 4、从DataSource中获取数据库连接
 * @throws Exception 
 */
@Test
    public void TestDBCP2() throws Exception{
    	Properties properties = new Properties();
	    InputStream in = DataSource_Test.class.getClassLoader().getResourceAsStream("dbcp.properties");
	    properties.load(in);//加载dbcp.properties文件
	    DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);//创建数据源
	    Connection con = dataSource.getConnection();//获取数据库连接
	    System.out.println(con);
}
}

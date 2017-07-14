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
	 * ʹ��DBPC���ݿ����ӳ��������ݿ�
	 * ����:
	 * 1������commons pool2 jar����commons dbcp2 jar��
	 * 2���������ݿ����ӳأ�����Դʵ����
	 * 3�����������ݿ��Ҫ��������Ϊ�������뵽����Դ��ָ���ķ�����
	 * 4��������Դ��ȡ���ݿ�����
	 * @throws SQLException 
	 */
@Test
    public void TestDBCP() throws SQLException{
    	BasicDataSource dataSource = null;
    	//1����������Դʵ��
    	dataSource = new BasicDataSource();
    	//2�������Ҫ���������ݿ�Ĳ���
    	
    	dataSource.setUsername("root");
    	dataSource.setPassword("090936");
    	dataSource.setUrl("jdbc:mysql://localhost:3306/test");
    	dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    	//3�������ѡ�Ĳ���
        dataSource.setMaxTotal(5);//ָ��ͬһʱ�̿��������ݿ����ӳ�����������������
        dataSource.setInitialSize(5);//ָ�����ݿ����ӳ��г�ʼ���������ĸ���
        dataSource.setMinIdle(2);//ָ�������ݿ����ӳ��б�������ٵĿ������ӵ�����
        dataSource.setMaxWaitMillis(5000);//�趨�ȴ����ݿ����ӳط������ӵ��ʱ��(�Ժ���Ϊ��λ)��������ʱ�����׳��쳣
    	//3��������Դ��ȡ���ݿ�����
    	Connection con = dataSource.getConnection();
    	System.out.println(con);
    }
/**
 * ʹ��DBCP���ݿ����ӳر�дͨ�õ��������ݿ�ķ���
 * 1������һ����Ϊ"dbcp.properties"���ļ�����������������ݿ��������Ϣ�����еļ�Ӧ������BasicDataSource�е�����
 * 2������"dbcp.properties"�ļ�
 * 3������BasicDataSourceFactory�е�createSource()��������DataSource
 * 4����DataSource�л�ȡ���ݿ�����
 * @throws Exception 
 */
@Test
    public void TestDBCP2() throws Exception{
    	Properties properties = new Properties();
	    InputStream in = DataSource_Test.class.getClassLoader().getResourceAsStream("dbcp.properties");
	    properties.load(in);//����dbcp.properties�ļ�
	    DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);//��������Դ
	    Connection con = dataSource.getConnection();//��ȡ���ݿ�����
	    System.out.println(con);
}
}

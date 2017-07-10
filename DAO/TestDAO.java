package DAO;


import org.junit.Test;

public class TestDAO {

	DAO dao = new DAO();
	@Test
	public void testUpdata() {
		String sql = "INSERT INTO animal(id,name,age) VALUES (?,?,?)";
		dao.updata(sql, 3,"panda",8);
	}

	@Test
	public void testGet() {
        String sql = "SELECT Flow_ID flowID,type,ID_Card iDcard FROM examstudent WHERE Flow_ID=?";
        System.out.println(dao.get(Student.class, sql, 10002));
	}

}

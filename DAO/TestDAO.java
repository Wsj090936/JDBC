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
	public void testGetForOne() {
        String sql = "SELECT Flow_ID flowID,type,ID_Card iDcard FROM examstudent WHERE Flow_ID=?";
        System.out.println(dao.getForOne(Student.class, sql, 10002));
	}
    @Test
    public void testGetForList() {
    	String sql = "SELECT Flow_ID flowID,type,ID_Card iDcard FROM examstudent";
    	System.out.println(dao.getForList(Student.class, sql));
    }
}

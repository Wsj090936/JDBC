package DAO;
//ѧ����  ���ڴ洢ѧ����Ϣ
public class Student {
	public int flowID;//��ˮ��
	public String type;//��������
	public String iDcard;//���֤��
	public String examcard;//׼��֤����
	public String studentName;//ѧ������
	public String location;//����
	public int grade;//�ɼ�
	
	public int getFlowID() {
		return flowID;
	}

	public void setFlowID(int flowID) {
		this.flowID = flowID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getiDcard() {
		return iDcard;
	}

	public void setiDcard(String iDcard) {
		this.iDcard = iDcard;
	}

	public String getExamcard() {
		return examcard;
	}

	public void setExamcard(String examcard) {
		this.examcard = examcard;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Student() {
		super();
	}
	public Student(int flowID, String type, String iDcard, String examcard, String studentName, String location,
			int grade) {
		super();
		this.flowID = flowID;
		this.type = type;
		this.iDcard = iDcard;
		this.examcard = examcard;
		this.studentName = studentName;
		this.location = location;
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "Student [flowID=" + flowID + ", type=" + type + ", iDcard=" + iDcard + ", examcard=" + examcard
				+ ", studentName=" + studentName + ", location=" + location + ", Grade=" + grade + "]\n";
	}


}

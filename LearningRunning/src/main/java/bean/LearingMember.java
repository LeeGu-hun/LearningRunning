package bean;

public class LearingMember {
	int m_id;
	String m_email;
	String m_name;
	boolean m_app_u_no;
	
	
	
	public LearingMember(int m_id, String m_email, String m_name, String m_app_u_no) {
		super();
		this.m_id = m_id;
		this.m_email = m_email;
		this.m_name = m_name;
		this.m_app_u_no = (m_app_u_no==null || m_app_u_no.equals(""))?false:true;
	}
	
	public int getM_id() {
		return m_id;
	}
	public void setM_id(int m_id) {
		this.m_id = m_id;
	}
	public String getM_email() {
		return m_email;
	}
	public void setM_email(String m_email) {
		this.m_email = m_email;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public boolean isM_app_u_no() {
		return m_app_u_no;
	}
	public void setM_app_u_no(boolean m_app_u_no) {
		this.m_app_u_no = m_app_u_no;
	}
	
	
}

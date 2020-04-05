package com.spring.mvc.vo.chatting;

public class addChatIdListVO {

	private String m_email;
	private String m_name;
	private String checked;
	private String m_icon;
	private int m_idx;
	
	
	

	public int getM_idx() {
		return m_idx;
	}
	public void setM_idx(int m_idx) {
		this.m_idx = m_idx;
	}
	public String getM_icon() {
		return m_icon;
	}
	public void setM_icon(String m_icon) {
		this.m_icon = m_icon;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public String getM_email() {
		return m_email;
	}
	public void setM_email(String m_email) {
		this.m_email = m_email;
	}
	
}

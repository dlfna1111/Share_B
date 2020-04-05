package com.spring.mvc.vo.mypage;


import org.springframework.stereotype.Component;


@Component
public class PjGroupVO {
	int m_idx;
	String m_email, m_icon;
	
	public int getM_idx() {
		return m_idx;
	}
	public void setM_idx(int m_idx) {
		this.m_idx = m_idx;
	}
	public String getM_email() {
		return m_email;
	}
	public void setM_email(String m_email) {
		this.m_email = m_email;
	}
	public String getM_icon() {
		return m_icon;
	}
	public void setM_icon(String m_icon) {
		this.m_icon = m_icon;
	}
	
	
	
	
}
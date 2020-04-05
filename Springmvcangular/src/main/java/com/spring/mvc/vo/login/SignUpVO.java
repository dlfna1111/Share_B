package com.spring.mvc.vo.login;


import org.springframework.stereotype.Component;


@Component
public class SignUpVO {
	private int m_idx=0;
	private String m_email, m_pwd, m_name, m_icon, m_pjinvite, m_auth="";
	
	
	
	public String getM_auth() {
		return m_auth;
	}
	public void setM_auth(String m_auth) {
		this.m_auth = m_auth;
	}
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
	public String getM_pwd() {
		return m_pwd;
	}
	public void setM_pwd(String m_pwd) {
		this.m_pwd = m_pwd;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public String getM_icon() {
		return m_icon;
	}
	public void setM_icon(String m_icon) {
		this.m_icon = m_icon;
	}
	public String getM_pjinvite() {
		return m_pjinvite;
	}
	public void setM_pjinvite(String m_pjinvite) {
		this.m_pjinvite = m_pjinvite;
	}
	
	
}
package com.spring.mvc.vo.board;

import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class ProjectInfoVO {
	int pj_idx, m_idx;
	String g_leader, pj_bookmark, pj_backcolor, pj_name, m_icon, m_email;
	
	
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
	public String getPj_name() {
		return pj_name;
	}
	public void setPj_name(String pj_name) {
		this.pj_name = pj_name;
	}
	public String getPj_backcolor() {
		return pj_backcolor;
	}
	public void setPj_backcolor(String pj_backcolor) {
		this.pj_backcolor = pj_backcolor;
	}
	public int getPj_idx() {
		return pj_idx;
	}
	public void setPj_idx(int pj_idx) {
		this.pj_idx = pj_idx;
	}
	public int getM_idx() {
		return m_idx;
	}
	public void setM_idx(int m_idx) {
		this.m_idx = m_idx;
	}
	public String getG_leader() {
		return g_leader;
	}
	public void setG_leader(String g_leader) {
		this.g_leader = g_leader;
	}
	public String getPj_bookmark() {
		return pj_bookmark;
	}
	public void setPj_bookmark(String pj_bookmark) {
		this.pj_bookmark = pj_bookmark;
	}
	
	
	
}
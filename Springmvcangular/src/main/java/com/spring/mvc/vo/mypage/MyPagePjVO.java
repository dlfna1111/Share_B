package com.spring.mvc.vo.mypage;


import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class MyPagePjVO {
	int pj_idx;
	String g_leader, pj_bookmark, pj_backcolor, pj_name, pj_sdate, pj_ddate, pj_content;
	
	
	public int getPj_idx() {
		return pj_idx;
	}
	public void setPj_idx(int pj_idx) {
		this.pj_idx = pj_idx;
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
	public String getPj_backcolor() {
		return pj_backcolor;
	}
	public void setPj_backcolor(String pj_backcolor) {
		this.pj_backcolor = pj_backcolor;
	}
	public String getPj_name() {
		return pj_name;
	}
	public void setPj_name(String pj_name) {
		this.pj_name = pj_name;
	}
	public String getPj_sdate() {
		return pj_sdate;
	}
	public void setPj_sdate(String pj_sdate) {
		this.pj_sdate = pj_sdate;
	}
	public String getPj_ddate() {
		return pj_ddate;
	}
	public void setPj_ddate(String pj_ddate) {
		this.pj_ddate = pj_ddate;
	}
	public String getPj_content() {
		return pj_content;
	}
	public void setPj_content(String pj_content) {
		this.pj_content = pj_content;
	}
	
	
	
	
}
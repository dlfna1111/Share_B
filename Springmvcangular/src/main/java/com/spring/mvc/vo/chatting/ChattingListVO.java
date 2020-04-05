package com.spring.mvc.vo.chatting;

public class ChattingListVO {
	// m_idx 가 여기서 필요?? ㅇㅇ 내꺼만 가져 와야 해서.
	private int chat_idx, m_idx, noReadCnt;
	private String chat_name, color, new_date;
	private String e_mail;
	private String m_name;
	private String chat_outdate;
	
	
	public String getChat_outdate() {
		return chat_outdate;
	}
	public void setChat_outdate(String chat_outdate) {
		this.chat_outdate = chat_outdate;
	}
	public int getNoReadCnt() {
		return noReadCnt;
	}
	public void setNoReadCnt(int noReadCnt) {
		this.noReadCnt = noReadCnt;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public String getNew_date() {
		return new_date;
	}
	public void setNew_date(String new_date) {
		this.new_date = new_date;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getChat_idx() {
		return chat_idx;
	}
	public void setChat_idx(int chat_idx) {
		this.chat_idx = chat_idx;
	}
	public int getM_idx() {
		return m_idx;
	}
	public void setM_idx(int m_idx) {
		this.m_idx = m_idx;
	}
	public String getChat_name() {
		return chat_name;
	}
	public void setChat_name(String chat_name) {
		this.chat_name = chat_name;
	}

	
	
	
	
}

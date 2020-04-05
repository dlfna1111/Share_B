package com.spring.mvc.vo.chatting;

public class ChattingRoomVO {
	// m_idx는 로그인하면 session에 저장되어있을테니까.
	// 지금은 임시? 아니지 나중에 db로 넣을때 필요함.
	private int m_idx;
	private String chat_content, chat_date;
	private String m_name;
	private int chat_idx;

	
	public int getChat_idx() {
		return chat_idx;
	}
	public void setChat_idx(int chat_idx) {
		this.chat_idx = chat_idx;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public int getM_idx() {
		return m_idx;
	}
	public void setM_idx(int m_idx) {
		this.m_idx = m_idx;
	}
	public String getChat_content() {
		return chat_content;
	}
	public void setChat_content(String chat_content) {
		this.chat_content = chat_content;
	}
	public String getChat_date() {
		return chat_date;
	}
	public void setChat_date(String chat_date) {
		this.chat_date = chat_date;
	}
	
	
	
	
	
	
}

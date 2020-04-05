package com.spring.mvc.vo.board;

public class DetailMemoVO {
	private int check_idx, memo_idx;
	private String check_content, check_complete;
	private String m_name;
	
	private int comm_idx, m_idx;
	private String comm_content, comm_date;
	
	private String memo_bookmark;
	
	
	public String getMemo_bookmark() {
		return memo_bookmark;
	}
	public void setMemo_bookmark(String memo_bookmark) {
		this.memo_bookmark = memo_bookmark;
	}
	
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public int getCheck_idx() {
		return check_idx;
	}
	public void setCheck_idx(int check_idx) {
		this.check_idx = check_idx;
	}
	public int getMemo_idx() {
		return memo_idx;
	}
	public void setMemo_idx(int memo_idx) {
		this.memo_idx = memo_idx;
	}
	public String getCheck_content() {
		return check_content;
	}
	public void setCheck_content(String check_content) {
		this.check_content = check_content;
	}
	public String getCheck_complete() {
		return check_complete;
	}
	public void setCheck_complete(String check_complete) {
		this.check_complete = check_complete;
	}
	public int getComm_idx() {
		return comm_idx;
	}
	public void setComm_idx(int comm_idx) {
		this.comm_idx = comm_idx;
	}
	public int getM_idx() {
		return m_idx;
	}
	public void setM_idx(int m_idx) {
		this.m_idx = m_idx;
	}
	public String getComm_content() {
		return comm_content;
	}
	public void setComm_content(String comm_content) {
		this.comm_content = comm_content;
	}
	public String getComm_date() {
		return comm_date;
	}
	public void setComm_date(String comm_date) {
		this.comm_date = comm_date;
	}
	
	
	
}

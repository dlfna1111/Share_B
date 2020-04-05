package com.spring.mvc.vo.board;

public class MemoListVO {
	private int b_idx;
	private int memo_idx, memo_seq;
	private String memo_subject, memo_detail;
	private String memo_bookmark;
	
	
	public String getMemo_bookmark() {
		return memo_bookmark;
	}
	public void setMemo_bookmark(String memo_bookmark) {
		this.memo_bookmark = memo_bookmark;
	}
	public int getB_idx() {
		return b_idx;
	}
	public void setB_idx(int b_idx) {
		this.b_idx = b_idx;
	}
	public int getMemo_idx() {
		return memo_idx;
	}
	public void setMemo_idx(int memo_idx) {
		this.memo_idx = memo_idx;
	}
	public int getMemo_seq() {
		return memo_seq;
	}
	public void setMemo_seq(int memo_seq) {
		this.memo_seq = memo_seq;
	}
	public String getMemo_subject() {
		return memo_subject;
	}
	public void setMemo_subject(String memo_subject) {
		this.memo_subject = memo_subject;
	}
	public String getMemo_detail() {
		return memo_detail;
	}
	public void setMemo_detail(String memo_detail) {
		this.memo_detail = memo_detail;
	}
	
	
}

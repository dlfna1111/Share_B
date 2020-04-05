package com.spring.mvc.vo.board;

import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class BoardListVO {
	// 첫 프로젝트 시작하게 되면 들어오는 
	
	private int b_idx;
	private int memo_idx, memo_seq;
	private String memo_subject, memo_detail;
	private String b_name;
	private List<MemoListVO> memos;
	
	
	public List<MemoListVO> getMemos() {
		return memos;
	}
	public void setMemos(List<MemoListVO> memos) {
		this.memos = memos;
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
	public int getB_idx() {
		return b_idx;
	}
	public void setB_idx(int b_idx) {
		this.b_idx = b_idx;
	}
	public String getB_name() {
		return b_name;
	}
	public void setB_name(String b_name) {
		this.b_name = b_name;
	}
	
}
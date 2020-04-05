package com.spring.mvc.vo;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class DirectoryVO {
//	private int file_idx, pj_idx, b_idx, m_idx, pwd;
//	private long file_memory;
	private String userName;
	private String file_name; // 분리된 파일명
	private String file_title; // 실제 파일명
	private String dirDate; // 작성일
	private int pj_idx;
	
	// 이건 앵귤러에서 체크박스 전체선택 때문에 있는거.
	private String checked = "";
	
	
	
	public String getFile_title() {
		return file_title;
	}
	public void setFile_title(String file_title) {
		this.file_title = file_title;
	}
	public int getPj_idx() {
		return pj_idx;
	}
	public void setPj_idx(int pj_idx) {
		this.pj_idx = pj_idx;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getDirDate() {
		return dirDate;
	}
	public void setDirDate(String dirDate) {
		this.dirDate = dirDate;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	
}

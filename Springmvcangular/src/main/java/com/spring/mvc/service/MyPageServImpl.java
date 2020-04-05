package com.spring.mvc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.mvc.repository.BoardDaoImpl;
import com.spring.mvc.repository.MyPageDaoImpl;
import com.spring.mvc.vo.DirectoryVO;
import com.spring.mvc.vo.board.BoardListVO;
import com.spring.mvc.vo.board.DetailMemoVO;
import com.spring.mvc.vo.board.ProjectInfoVO;
import com.spring.mvc.vo.login.SignUpVO;
import com.spring.mvc.vo.mypage.MyPagePjVO;

@Service
public class MyPageServImpl {

	@Autowired
	MyPageDaoImpl mypagedao;

	// 모든 프로젝트 
	public List<MyPagePjVO> getAllPj(int m_idx) {
		return mypagedao.getAllPjFromDb(m_idx);
	}

	// 내 정보 
	public List<SignUpVO> getMyInfo(int m_idx) {
		return mypagedao.getMyInfoFromDb(m_idx);
	}
	
	// 프로젝트 생성 
	public List<MyPagePjVO> addPjServ(Map map) {
		return mypagedao.addPjFromDb(map);
	}
	
	// 프로젝트 나가기 
	public int exitPjServ(Map map) {
		return mypagedao.exitPjFromDb(map);
	}

	// 프로필 사진 수정
	public List<SignUpVO> photoEditServ(Map map) {
		return mypagedao.photoEditFromDb(map);
	}
	
	// 프로필 이름 수정
	public int nameEditServ(Map map) {
		return mypagedao.nameEditFromDb(map);
	}
	
	// 프로필 이름 수정
	public List<String> headerInfosServ(int m_idx) {
		return mypagedao.headerInfosFromDb(m_idx);
	}
	

}
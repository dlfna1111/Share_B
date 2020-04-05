package com.spring.mvc.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.javafaker.Faker;
import com.spring.mvc.vo.DirectoryVO;
import com.spring.mvc.vo.board.BoardListVO;
import com.spring.mvc.vo.board.DetailMemoVO;
import com.spring.mvc.vo.board.MemoListVO;
import com.spring.mvc.vo.board.ProjectInfoVO;
import com.spring.mvc.vo.login.SignUpVO;
import com.spring.mvc.vo.mypage.MyPagePjVO;

@Repository
public class MyPageDaoImpl  {
	
	SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		System.out.println("MyPageDAO 생성자 생성");
		this.sqlSession = sqlSession;
	}
	
	// 마이페이지 내가 속한 프로젝트 전체 가져오기
	public List<MyPagePjVO> getAllPjFromDb(int m_idx) {
		
		List<MyPagePjVO> pjInfos = sqlSession.selectList("mypage.getAllPj", m_idx);
		
		return pjInfos;
	}
	
	// 마이페이지 내 정보 가져오기.
	public List<SignUpVO> getMyInfoFromDb(int m_idx) {
		
		List<SignUpVO> pjInfos = sqlSession.selectList("mypage.getMyInfos", m_idx);
		
		return pjInfos;
	}
	
	// 프로젝트 생성
	public List<MyPagePjVO> addPjFromDb(Map map) {
		
		// 프로젝트 생성
		int addPj = sqlSession.insert("mypage.addPj", map);
		
		// pj_group 입력
		int addPjGroup = sqlSession.insert("mypage.addPjGroup", map);
		
		// 생성한 프로젝트 가져오기
		List<MyPagePjVO> pjInfos = sqlSession.selectList("mypage.getAllPj", Integer.parseInt((String)map.get("m_idx")));
		
		return pjInfos;
	}

	// 프로젝트 생성
	public int exitPjFromDb(Map map) {
		
		int exitRes = sqlSession.delete("mypage.exitPj", map);
		
		return exitRes;
	}
	
	// 프로필 사진 수정
	public List<SignUpVO> photoEditFromDb(Map map) {
		
		int exitRes = sqlSession.update("mypage.photoEdit", map);
		int m_idx = Integer.parseInt((String)map.get("m_idx"));
		List<SignUpVO> res = getMyInfoFromDb(m_idx);
		
		return res;
	}
	
	// 프로필 이름 수정
	public int nameEditFromDb(Map map) {
		
		int res = sqlSession.update("mypage.nameEdit", map);
		
		return res;
	}
	
	// HEADER 정보 가져오기
	public List<String> headerInfosFromDb(int m_idx) {
		
		List<String> headerInfo = sqlSession.selectList("mypage.getHeader", m_idx);
		
		return headerInfo;
	}
	
	
	
	
	
	
}



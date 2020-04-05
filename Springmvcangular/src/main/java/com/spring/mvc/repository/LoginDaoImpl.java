package com.spring.mvc.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.spring.mvc.vo.chatting.ChattingListVO;
import com.spring.mvc.vo.login.SignUpVO;

@Repository
public class LoginDaoImpl  {
	
	SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		System.out.println("LoginPageDAO 생성자 생성");
		this.sqlSession = sqlSession;
	}
	
	// 회원가입
	public int signUpFromDB(Map map) {
		System.out.println("회원가입 인증번호 : " + map.get("m_auth"));
		int signUpRes = sqlSession.insert("logIn.signUp",map);
		
		return signUpRes;
	}
	
	// 이메일 중복확인
	public int multiCheckFromDB(String m_email) {
		String multiChkRes = sqlSession.selectOne("logIn.multiCheck",m_email);
		
		int res = 0;
		if(multiChkRes != null) {
			res = 1;
		}
		return res;
	}

	// 이메일 인증
	public String signUpAuthFromDB(String m_email) {
		
		String multiChkRes = sqlSession.selectOne("logIn.signUpAuth",m_email);
		
		
		System.out.println("multiChkRes : " + multiChkRes);

		return multiChkRes;
	}
	
	// 이메일 인증 후 난수 업데이트
	public int signUpAuthUpdateFromDB(String m_email) {
		
		int multiChkRes = sqlSession.update("logIn.signUpAuthUpdate",m_email);
		
		return multiChkRes;
	}

	// 로그인
	public List<SignUpVO> logInFromDB(String m_email) {
		List<SignUpVO> multiChkRes = sqlSession.selectList("logIn.loginMailCheck",m_email);
		
		return multiChkRes;
	}
	
	// 구글 로그인
	public List<SignUpVO> googleLogInFromDB(Map map) {
		List<SignUpVO> listVO = new ArrayList<SignUpVO>();
		
		int m_idx =0; 
		listVO = sqlSession.selectList("logIn.googleLogin",map);
		System.out.println("listVO.get(0) : " + listVO.get(0).getM_email());
		
		// 구글 가입
		if(listVO.get(0).getM_email() == null) {
			map.put("m_icon", "google");
			int res  = sqlSession.insert("logIn.signUp",map);
			listVO = sqlSession.selectList("logIn.googleLogin", map);
		}
		return listVO;
	}
	
	// 비밀번호 찾기
	public String findPwdFromDB(String m_email) {
		int res = 0;
		String findPwd = sqlSession.selectOne("logIn.findPwd",m_email);
		
		return findPwd;
	}
	
	
	
	
}



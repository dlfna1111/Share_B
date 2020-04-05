package com.spring.mvc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.mvc.repository.BoardDaoImpl;
import com.spring.mvc.repository.LoginDaoImpl;
import com.spring.mvc.vo.login.SignUpVO;

@Service
public class LoginServImpl {

	@Autowired
	LoginDaoImpl logindao;
	
	// 회원가입
	public int signUp(Map map) {
		return logindao.signUpFromDB(map);
	}

	// 이메일 중복확인
	public int multiCheck(String m_email) {
		return logindao.multiCheckFromDB(m_email);
	}
	
	// 이메일 중복확인
	public String signUpAuth(String m_email) {
		return logindao.signUpAuthFromDB(m_email);
	}

	// 이메일 확인 후 난수 변경하기
	public int signUpAuthUpdate(String m_email) {
		return logindao.signUpAuthUpdateFromDB(m_email);
	}
	
	// 로그인
	public List<SignUpVO> logInServ(String m_email) {
		return logindao.logInFromDB(m_email);
	}

	// 구글 로그인
	public List<SignUpVO> googleLogInServ(Map map) {
		return logindao.googleLogInFromDB(map);
	}

	// 비밀번호 찾기
	public String findPwd(String m_email) {
		return logindao.findPwdFromDB(m_email);
	}

}
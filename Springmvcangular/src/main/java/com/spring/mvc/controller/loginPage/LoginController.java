package com.spring.mvc.controller.loginPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.service.LoginServImpl;
import com.spring.mvc.vo.login.SignUpVO;

@RestController
public class LoginController<AjaxResponseBody> {
	// 자동주입(Spring으로부터 application정보를 자동으로 받는다), 빈객체를 생성하지 않아도 됨.
	@Autowired
	ServletContext application;
	@Autowired
	HttpServletRequest httpReq;
	@Autowired
	LoginServImpl logInService;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	HttpSession session;
	

	// 회원가입
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/signUp", method = RequestMethod.GET)
	public int getChatList() throws ParseException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> map = getEDIT();

		return logInService.signUp(map);
	}

	// 이메일 중복체크
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/multiCheck", method = RequestMethod.GET)
	public int multiCheck() {

		String m_email = httpReq.getParameter("m_email");
		return logInService.multiCheck(m_email);
	}

	// 회원가입 이메일 보내기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/emailAuthSend", method = RequestMethod.GET)
	public int mailSending() {

		try {
			Map<String, Object> map = getEDIT();

			String tomail = (String) map.get("m_email");
			String ranNum = (String) map.get("m_auth");
			String content = "http://localhost:8080/Springmvcangular/signUpAuth?m_email="
								+ tomail + "&ranNum=" + ranNum;

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			messageHelper.setFrom("dlfna1111@gmail.com"); // 보내는사람 생략하면 정상작동을 안함
			messageHelper.setTo(tomail); // 받는사람 이메일
			messageHelper.setSubject("Share_B 회원가입 인증"); // 메일제목은 생략이 가능하다
			messageHelper.setText(content); // 메일 내용
			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}

		return 1;
	}

	// 회원가입 최종 이메일에서 들어오는 곳
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/signUpAuth", method = RequestMethod.GET)
	public ModelAndView signUpAuth(String m_email, String ranNum) {

		String getRanNum = logInService.signUpAuth(m_email);
		int res = 0;

		if (getRanNum.equals(ranNum)) {
			res = logInService.signUpAuthUpdate(m_email);
		} else {
			System.out.println("이미 인증된 경우.");
			res = 999; // 임시
		}
		String projectUrl = "http://localhost:4200";

		return new ModelAndView("redirect:" + projectUrl);
	}

	// 로그인
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/logIn", method = RequestMethod.GET)
	public List<Integer> logIn(HttpServletResponse response) throws ParseException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = getEDIT();

		String m_email = (String) map.get("m_email");
		String m_pwd = (String) map.get("pwd");

		List<SignUpVO> multiChkRes = logInService.logInServ(m_email);

		List<Integer> list = new ArrayList<Integer>();

		if (multiChkRes.size() == 0) {
			list.add(2);
			return list;
		}

		if ((multiChkRes.get(0).getM_auth()).equals("check")) {
			if (multiChkRes.get(0) != null) {
				if ((multiChkRes.get(0).getM_pwd()).equals(m_pwd)) {
					// 로그인 성공
					list.add(1);
					list.add(multiChkRes.get(0).getM_idx());
				} else {
					// 비번 틀림.
					list.add(2);
				}
			} else {
				// 아이디 틀림
				list.add(2);
			}
		} else {
			// 승인이 안된 이메일
			list.add(3);
		}
		
		return list;
	}

	// 비번 찾기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/findpwd", method = RequestMethod.GET)
	public List<String> findpwd() throws ParseException {
		String m_email = httpReq.getParameter("m_email");
		String ranNum = "" + (int) (Math.random() * 1000000);
		String content = "3분 이내로 인증 번호를 입력하세요.\n 인증번호 : " + ranNum;
		String m_pwd = logInService.findPwd(m_email);

		List<String> list = new ArrayList<String>();
		list.add(ranNum);
		list.add(m_pwd);
		System.out.println(list.get(0) + " / " + list.get(1));

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			messageHelper.setFrom("dlfna1111@gmail.com"); // 보내는사람 생략하면 정상작동을 안함
			messageHelper.setTo(m_email); // 받는사람 이메일
			messageHelper.setSubject("Share_B 비밀번호 찾기 인증번호"); // 메일제목은 생략이 가능하다
			messageHelper.setText(content); // 메일 내용
			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}

		return list;
	}

	// 구글 로그인
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/googleLogin", method = RequestMethod.GET)
	public List<SignUpVO> googleLogin() throws ParseException, JsonParseException, JsonMappingException, IOException {
		List<SignUpVO> list = new ArrayList<SignUpVO>();
		Map<String, Object> map = getEDIT();

		list = logInService.googleLogInServ(map);
			

		return list;
	}
	
	
	// edit JSON 가져오는 메소드
	public Map<String, Object> getEDIT() throws ParseException, JsonParseException, JsonMappingException, IOException{
		JSONParser jsonParse = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParse.parse(httpReq.getParameter("edit"));

		Map<String, Object> map;
		map = new ObjectMapper().readValue(jsonObject.toJSONString(), Map.class);
		
		return map;
	}


}

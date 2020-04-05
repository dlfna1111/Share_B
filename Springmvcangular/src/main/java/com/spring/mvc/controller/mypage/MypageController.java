package com.spring.mvc.controller.mypage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.service.MyPageServImpl;
import com.spring.mvc.vo.login.SignUpVO;
import com.spring.mvc.vo.mypage.MyPagePjVO;

@RestController
public class MypageController<AjaxResponseBody> {
	// 자동주입(Spring으로부터 application정보를 자동으로 받는다), 빈객체를 생성하지 않아도 됨.
	@Autowired
	ServletContext application;
	@Autowired
	HttpServletRequest httpReq;
	@Autowired
	MyPageServImpl myPageService;

	// 내가 가진 모든 프로젝트 정보 가져오기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/getAllPj", method = RequestMethod.GET)
	public List<MyPagePjVO> getAllPj() {
		int m_idx = Integer.parseInt((String)httpReq.getParameter("m_idx"));
		return myPageService.getAllPj(m_idx);
	}
	
	// 내 정보 가지고 오기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/getMyInfo", method = RequestMethod.GET)
	public List<SignUpVO> getMyInfo() {
		int m_idx = Integer.parseInt((String)httpReq.getParameter("m_idx"));
		return myPageService.getMyInfo(m_idx);
	}
	
	// 프로젝트 생성
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/addPj", method = RequestMethod.GET)
	public List<MyPagePjVO> addPj() throws ParseException, IOException {
		
		Map<String, Object> map = getEDIT();
		
		return myPageService.addPjServ(map);
	}
	
	
	// 프로젝트 나가기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/exitPj", method = RequestMethod.GET)
	public int exitPj() throws ParseException, IOException {
		
		Map<String, Object> map = getEDIT();
		
		return myPageService.exitPjServ(map);
	}
	
	// 프로필 사진 변경
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/profileEdit", method = RequestMethod.POST)
	public List<SignUpVO> profileEdit( MultipartFile file, MultipartHttpServletRequest request) throws IllegalStateException, IOException, ParseException{

		String[] m_idx = request.getParameterValues("m_idx");
		
		// 앵귤러 저장소에 저장
		String uploadPath = "C:/Users/EloomShin/spring-angular/src/assets/Profile/"+m_idx[0];
		File dir = new File(uploadPath);

		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		MultipartFile mFile = file;
		String originalFileName = mFile.getOriginalFilename();

		if (originalFileName != null && !originalFileName.equals("")) {
			File saveFile = new File(uploadPath, originalFileName);
			
			// 모든 파일명을 "작성자_파일명"
			saveFile = new File(uploadPath, originalFileName);
			mFile.transferTo(saveFile);
			System.out.println("saveFileNamed : " + originalFileName);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("m_idx", m_idx[0]);		
		map.put("m_icon", originalFileName);
		
		return myPageService.photoEditServ(map);
	}
	
	// 이름 수정
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/editName", method = RequestMethod.GET)
	public int editName() throws ParseException, IOException {
		
		Map<String, Object> map = getEDIT();
		
		return myPageService.nameEditServ(map);
	}
	
	// HEADER 정보
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/getHeaderInfos", method = RequestMethod.GET)
	public List<String> headerInfos(){
		
		int m_idx = Integer.parseInt((String)httpReq.getParameter("m_idx"));
		
		return myPageService.headerInfosServ(m_idx);
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













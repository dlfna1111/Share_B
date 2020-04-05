package com.spring.mvc.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.service.BoardServImpl;
import com.spring.mvc.vo.DirectoryVO;
import com.spring.mvc.vo.board.BoardListVO;
import com.spring.mvc.vo.board.DetailMemoVO;
import com.spring.mvc.vo.board.ProjectInfoVO;

@RestController
public class BoardsController<AjaxResponseBody> {

	// 자동주입(Spring으로부터 application정보를 자동으로 받는다), 빈객체를 생성하지 않아도 됨.
	@Autowired
	ServletContext application;
	@Autowired
	BoardServImpl boardService;
	@Autowired
	HttpServletRequest httpReq;

	// 플젝페이지 보드 정보
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/pjInfos", method = RequestMethod.GET)
	public List<BoardListVO> getAllpjInfos(HttpSession session) throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		int pj_idx = Integer.parseInt((String) httpReq.getParameter("pj_idx"));
		
		return boardService.getAllpjInfos(pj_idx);
	}

	// 플젝페이지 pj_group 정보
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/pjInfos2", method = RequestMethod.GET)
	public List<ProjectInfoVO> getAllpjInfos2(HttpSession session) throws ParseException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> map = getEDIT();
		
		return boardService.getAllpjInfos2(map);
	}
	
	// 보드 생성
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/addBoard", method = RequestMethod.GET)
	public List<BoardListVO> addBoard() throws ParseException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> map = getEDIT();

		return boardService.addBoard(map);
	}

	// 메모 추가
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/addMemo", method = RequestMethod.GET)
	public List<BoardListVO> addMemo() throws ParseException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> map = getEDIT();

		return boardService.addMemo(map);
	}

	// 메모 삭제
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/deleteMemo", method = RequestMethod.GET)
	public int deleteMemo(HttpServletRequest httpReq) throws ParseException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> map = getEDIT();
		
		int memo_idx = Integer.parseInt((String) map.get("memo_idx"));
		int b_idx = Integer.parseInt((String) map.get("b_idx"));


		return boardService.deleteMemo(memo_idx, b_idx);
	}

	// 보드 삭제
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/deleteBoard", method = RequestMethod.GET)
	public int deleteBoard(HttpServletRequest httpReq) throws ParseException {

		int b_idx = Integer.parseInt((String) httpReq.getParameter("b_idx"));

		return boardService.deleteBoard(b_idx);
	}

	// 메모 상세보기 관련1 : 체크리스트 가져오기.
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/getCheckList", method = RequestMethod.GET)
	public List<DetailMemoVO> getCheckList() throws ParseException {

		int memo_idx = Integer.parseInt(httpReq.getParameter("memo_idx"));

		return boardService.getCheckList(memo_idx);
	}

	// 메모 상세보기 관련1 : 댓글 가져오기.
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/getComment", method = RequestMethod.GET)
	public List<DetailMemoVO> getComment() throws ParseException {

		int memo_idx = Integer.parseInt(httpReq.getParameter("memo_idx"));

		return boardService.getComment(memo_idx);
	}

	// 체크 박스 클릭할때마다..
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/updateCheck", method = RequestMethod.GET)
	public int updateCheck() throws ParseException {

		int check_idx = Integer.parseInt((String) httpReq.getParameter("check_idx"));

		return boardService.updateCheck(check_idx);
	}

	// 메모 제목 수정
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/updateSubject", method = RequestMethod.GET)
	public int updateSubject() throws ParseException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = getEDIT();

		return boardService.updateSubject(map);
	}

	// 메모 상세내용 수정
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/updateDetail", method = RequestMethod.GET)
	public int updateDetail() throws ParseException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = getEDIT();

		return boardService.updateDetail(map);
	}

	// 체크리스트 항목 삭제
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/delCheck", method = RequestMethod.GET)
	public int delCheck() throws ParseException, JsonParseException, JsonMappingException, IOException {
		int check_idx = Integer.parseInt(httpReq.getParameter("check_idx"));

		return boardService.delCheck(check_idx);
	}

	// 체크리스트 항목 추가
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/addCheck", method = RequestMethod.GET)
	public List<DetailMemoVO> addCheck() throws ParseException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> map = getEDIT();

		return boardService.addCheck(map);
	}

	// 메모 댓글 등록
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/addComm", method = RequestMethod.GET)
	public List<DetailMemoVO> addComm() throws ParseException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> map = getEDIT();

		return boardService.addComm(map);
	}

	// 메모 댓글 등록
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/delComm", method = RequestMethod.GET)
	public int delComm() throws ParseException, JsonParseException, JsonMappingException, IOException {

		int comm_idx = Integer.parseInt(httpReq.getParameter("delComm"));

		return boardService.delComm(comm_idx);
	}

	// 메모 북마크 색상 수정
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/updateBookMark", method = RequestMethod.GET)
	public int updateBookMark() throws ParseException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = getEDIT();
		return boardService.updateBookMark(map);
	}

	// 메모 이동
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/moveMemo", method = RequestMethod.GET)
	public int moveMemo() throws ParseException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = getEDIT();
		return boardService.moveMemo(map);
	}
	
	// 저장된 파일 리스트
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/dirList", method = RequestMethod.GET)
	public List<DirectoryVO> dirList() throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		
		int pj_idx = Integer.parseInt((String)httpReq.getParameter("pj_idx"));
		List<DirectoryVO> voList = dirAllList(pj_idx);

		return voList;
	}
	
	// 파일보관함 삭제
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/deleteDir", method = RequestMethod.GET)
	public int dirDelete() throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		Map<String, Object> map = getEDIT();
		
		int pj_idx = Integer.parseInt((String)map.get("pj_idx"));
		String file_title = (String)map.get("file_title");
		
		String filePath = "C:/Users/EloomShin/spring-angular/src/assets/Project/"+pj_idx + "/" + file_title;
		
        boolean delYn = true;
        File file = new File(filePath);
 
        int res = 0;
        if(file.exists()) {
            delYn = file.delete();
            if(delYn){
                System.out.println("File Delete Success"); //성공
                res = 1;
			}else{
			    System.out.println("File Delete Fail"); //실패
			    res= 2;
			}
		}else{
		    System.out.println("File Not Found"); //미존재
		    res = 3;
		}
		return res;
	}
	
	// 파일 업로드
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public List<DirectoryVO> dir(MultipartFile file, String pj_idx, String m_idx) throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		int m_IDX = Integer.parseInt(m_idx);
		int pj_IDX = Integer.parseInt(pj_idx);
		String writer = boardService.getName(m_IDX);
		// 앵귤러 저장소에 저장
		String uploadPath = "C:/Users/EloomShin/spring-angular/src/assets/Project/"+pj_IDX;
		File dir = new File(uploadPath);

		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		MultipartFile mFile = file;
		String originalFileName = mFile.getOriginalFilename();
		String saveFileName = originalFileName;

		if (saveFileName != null && !saveFileName.equals("")) {
			File saveFile = new File(uploadPath, saveFileName);
			
			// 모든 파일명을 "작성자_파일명"
			String saveFileNamed = writer + "$" + saveFileName;
			saveFile = new File(uploadPath, saveFileNamed);
			mFile.transferTo(saveFile);
		}
		
		List<DirectoryVO> voList = dirAllList(pj_IDX);

		return voList;
	}

	
	// dir 리스트 가져오는 메소드
	public List<DirectoryVO> dirAllList(int pj_idx){
		
		String path = "C:/Users/EloomShin/spring-angular/src/assets/Project/"+pj_idx;
		
		File dir = new File(path);

		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		
		
		// 이름, 파일명 분리 전 파일명 리스트
        List<String> list = new ArrayList<String>();
        // 각 파일 작성일 담은 리스트
        List<String> dateList = new ArrayList<String>();
        
        // 하위의 모든 파일
        for (File info : FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)) {
        	if( info == null ) {
    			System.out.println("null");
    			List<DirectoryVO> empty = new ArrayList<DirectoryVO>();
    			return empty;
    		}
    		
            list.add(info.getName());
            String formatted = ""; 
    		
            BasicFileAttributes attrs = null; 
            try { 
            	attrs = Files.readAttributes(info.toPath(), BasicFileAttributes.class); 
    	        FileTime time = attrs.creationTime(); 
    	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd"); 
    	        formatted = simpleDateFormat.format(new Date(time.toMillis())); // 생성 날짜 가져오기. new Date(time.toMillis())
    	        dateList.add(formatted);
            } catch (IOException e) { e.printStackTrace(); }
        }

        // 리스트 하나의 배열 0은 작성자, 배열1은 파일명
        // 작성자, 파일명 분리해서 넣은 list
        List<String[]> dirInfo = new ArrayList<String[]>();
        for(int i = 0 ; i < list.size() ; i++) {
        	dirInfo.add(list.get(i).split("\\$"));
        }
		
        List<DirectoryVO> voList = new ArrayList<DirectoryVO>();
        
        for(int i = 0 ; i<dirInfo.size() ; i++) {
        	DirectoryVO vo = new DirectoryVO();
        	vo.setDirDate(dateList.get(i));
        	vo.setUserName(dirInfo.get(i)[0]);
        	vo.setFile_name(dirInfo.get(i)[1]);
        	vo.setPj_idx(pj_idx);
        	vo.setFile_title(list.get(i));
        	
        	voList.add(vo);
        }
        
        return voList;
	}
	
	
	// 프로젝트 북마크
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/pjBookMark", method = RequestMethod.GET)
	public List<ProjectInfoVO> pjBookMark(HttpSession session) throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		Map<String, Object> map = getEDIT();
		
		return boardService.pjBookMark(map);
	}
	

	// 프로젝트 초대 1 : 검색아이디 가져오기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/searchInvId", method = RequestMethod.GET)
	public List<String> searchInvId() {
		
		String m_email = httpReq.getParameter("text");

		return boardService.searchInvIdServ(m_email);
	}

	// 프로젝트 초대 2 : 초대하기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/inviteMember", method = RequestMethod.GET)
	public List<ProjectInfoVO> inviteMember() throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		JSONParser jsonParse = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParse.parse(httpReq.getParameter("edit"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("m_email");
		Map<String, Object> map;
		map = new ObjectMapper().readValue(jsonObject.toJSONString(), Map.class);
		
		Object pj_idx = jsonObject.get("pj_idx");
		
		List<Map> list = new ArrayList<Map>();
		for (int i = 0; i < jsonArray.size(); i++) {
			Map<String, Object> hm = new HashMap<String, Object>();
			hm.put("m_email", (String)jsonArray.get(i));
			hm.put("pj_idx", pj_idx);
			list.add(hm);
		}
		
		int res = boardService.inviteMemberServ(list);
		
		return boardService.getAllpjInfos2(map);
	}
	
	// 프로젝트 초대 2 : 초대하기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/kickMem", method = RequestMethod.GET)
	public int kickMem() throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		Map<String, Object> map = getEDIT();
		
		return boardService.kickMemServ(map);
	}
	
	// 프로젝트 배경색 바꾸기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/editBackColor", method = RequestMethod.GET)
	public int editBackColor() throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		Map<String, Object> map = getEDIT();
		
		return boardService.editBackColorServ(map);
	}
		
	
	// edit JSON 가져오는 메소드
	public Map<String, Object> getEDIT() throws ParseException, JsonMappingException, IOException{
		JSONParser jsonParse = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParse.parse(httpReq.getParameter("edit"));

		Map<String, Object> map;
		map = new ObjectMapper().readValue(jsonObject.toJSONString(), Map.class);
		
		return map;
	}

}

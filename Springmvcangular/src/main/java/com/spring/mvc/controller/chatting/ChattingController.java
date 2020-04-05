package com.spring.mvc.controller.chatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.service.ChatServImpl;
import com.spring.mvc.vo.chatting.ChattingListVO;
import com.spring.mvc.vo.chatting.ChattingRoomVO;
import com.spring.mvc.vo.chatting.addChatIdListVO;
import com.spring.mvc.vo.chatting.noReadVO;

@RestController
public class ChattingController<AjaxResponseBody> {
	// 자동주입(Spring으로부터 application정보를 자동으로 받는다), 빈객체를 생성하지 않아도 됨.
	@Autowired
	ServletContext application;
	@Autowired
	ChatServImpl chatService;
	@Autowired
	HttpServletRequest httpReq;

	// 채팅 정보
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/chatInfos", method = RequestMethod.GET)
	public List<ChattingListVO> getChatList() {
		// 여기서 session으로 누구의 리스트를 가져올지 검증해야한다.
		int m_idx = Integer.parseInt((String)httpReq.getParameter("m_idx"));
		return chatService.getChatList(m_idx);
	}
	public List<ChattingListVO> getChatList(int m_idx) {
		return chatService.getChatList(m_idx);
	}

	// 채팅방 만들때 id들불러오기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/getAddChatIds", method = RequestMethod.GET)
	public List<addChatIdListVO> getAddChatIds() {
		int m_idx =  Integer.parseInt((String)httpReq.getParameter("m_idx"));
		return chatService.getChatIdsList(m_idx);
	}

	// 채팅방 클릭시. 가져와야 할 정보
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/getRoomInfos", method = RequestMethod.GET)
	public List<Object> getRoomInfos(HttpServletRequest httpReq) throws ParseException {

		int chat_idx = Integer.parseInt(httpReq.getParameter("chat_idx"));
		System.out.println("채팅방 클릭 : " + chat_idx);
		return chatService.getChatRoomInfos(chat_idx);
	}

	// 메세지 저장.
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/saveMsg", method = RequestMethod.GET)
	public int saveMsg(HttpServletRequest httpReq) throws ParseException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = getEDIT();

		return chatService.saveMsg(map);
	}

	// 채팅방 생성
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/addChatRoom", method = RequestMethod.GET)
	public List<ChattingListVO>  addChatRoom() throws Exception {

		JSONParser jsonParse = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParse.parse(httpReq.getParameter("addRoomInfos"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("m_idxList");
		String chat_name = String.valueOf(jsonObject.get("chat_name"));
		int m_idx = Integer.parseInt((String)jsonObject.get("m_idx"));
		
		List<Map<String, Integer>> m_idxList = new ArrayList<Map<String, Integer>>();
		for (int i = 0; i < jsonArray.size(); i++) {
			Map<String, Integer> hm = new HashMap<String, Integer>();
			System.out.println(jsonArray.get(i).toString());
			System.out.println(jsonArray.get(i));
			System.out.println(String.valueOf(jsonArray.get(i)));
			int m1 =0;
			if(jsonArray.get(i) instanceof String) {
				m1 = Integer.parseInt((String)jsonArray.get(i));
				hm.put("m_idx", m1);
			}else {
				hm.put("m_idx", (int)((long)jsonArray.get(i)));
			}
			m_idxList.add(hm);
			System.out.println("addchatroom m_idx : " + m_idxList);
		}
		List<ChattingListVO> resList = new ArrayList<ChattingListVO>();
		int res = chatService.addChatRoom(chat_name, m_idxList, m_idx);
		if(res > 0) {
			resList = getChatList(m_idx);
			System.out.println("채팅방 그룹까지 모두 추가했다. res : " + res);
		}

		return resList;
	}

	// 채팅방 삭제
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/delChatRoom", method = RequestMethod.GET)
	public int delChatRoom(HttpServletRequest httpReq) throws ParseException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> map = getEDIT();
		
		return chatService.delChatRoom(map);
	}
	
	// 채팅방 나가면 나간시간 작성
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/exitChatRoom", method = RequestMethod.GET)
	public int exitChatRoom(HttpServletRequest httpReq) throws ParseException, JsonParseException, JsonMappingException, IOException {
		
		Map<String, Object> map = getEDIT();
		
		return chatService.exitChatRoom(map);
	}
	
	// 안읽은 메세지 개수 가져오기
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/noReadComm", method = RequestMethod.GET)
	public List<noReadVO> noReadComm(HttpServletRequest httpReq) throws ParseException, JsonParseException, JsonMappingException, IOException{
		int m_idx = Integer.parseInt((String) httpReq.getParameter("m_idx"));
		return chatService.noReadComm(m_idx);
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

package com.spring.mvc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.mvc.repository.ChatDaoImpl;
import com.spring.mvc.vo.chatting.ChattingListVO;
import com.spring.mvc.vo.chatting.ChattingRoomVO;
import com.spring.mvc.vo.chatting.addChatIdListVO;
import com.spring.mvc.vo.chatting.noReadVO;

@Service
public class ChatServImpl {
	
	@Autowired
	ChatDaoImpl chatdao;

	public List<ChattingListVO> getChatList(int m_idx) {
		return chatdao.getChatListFromDB(m_idx);
	}
	
//	public List<String> getChatIdSeaching() {
//		return chatdao.getChatIdSeachingFromDB();
//	}

	public List<addChatIdListVO> getChatIdsList(int m_idx) {
		return chatdao.getAddChatIdsListFromDB(m_idx);
	}
	
	public List<Object> getChatRoomInfos(int chat_idx) { 
		return chatdao.getChatRoomInfosFromDB(chat_idx);
	}
	
	public int saveMsg(Map map) { 
		return chatdao.saveMsgToDB(map);
	}

	public int addChatRoom(String chat_name, List<Map<String, Integer>> m_idxList, int m_idx) { 
		return chatdao.addChatRoomToDB(chat_name, m_idxList, m_idx);
	}
	
	public int delChatRoom(Map map) { 
		return chatdao.delChatRoomToDB(map);
	}
	
	public int exitChatRoom(Map map) { 
		return chatdao.exitChatRoomToDB(map);
	}
	
	public List<noReadVO> noReadComm(int m_idx) { 
		return chatdao.noReadCommToDB(m_idx);
	}
	

	
	
	
}

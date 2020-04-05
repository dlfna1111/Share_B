package com.spring.mvc.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.spring.mvc.vo.chatting.ChattingListVO;
import com.spring.mvc.vo.chatting.ChattingRoomVO;
import com.spring.mvc.vo.chatting.addChatIdListVO;
import com.spring.mvc.vo.chatting.noReadVO;

@Repository
public class ChatDaoImpl {
	
SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		
		System.out.println("CHatPageDAO 생성자 생성");
		this.sqlSession = sqlSession;
	}
	
	// 채팅 목록 가져오기
	public List<ChattingListVO> getChatListFromDB(int m_idx) {
		
		List<ChattingListVO> chatList = new ArrayList<ChattingListVO>();
		chatList = sqlSession.selectList("chat.chat_list", m_idx);
		
		
		for( int i = 0 ; i < chatList.size() ; i++ ) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("chat_idx", chatList.get(i).getChat_idx());
			map.put("chat_outdate", chatList.get(i).getChat_outdate());
			map.put("m_idx", m_idx);
			chatList.get(i).setNoReadCnt((int) sqlSession.selectOne("chat.noReadCntComm", map));
		}
		
		return chatList;
	}
	
	public List<addChatIdListVO> getAddChatIdsListFromDB(int m_idx) {

		List<addChatIdListVO> idList = new ArrayList<addChatIdListVO>();
		idList = sqlSession.selectList("chat.AddChatInfos", m_idx);
		
		return idList;
	}
	
	
	public List<Object> getChatRoomInfosFromDB(int chat_idx) {
		
		
		List<ChattingRoomVO> chatRoomInfos = new ArrayList<ChattingRoomVO>();
		chatRoomInfos = sqlSession.selectList("chat.chatRoomInfos", chat_idx);
		List<Integer> personList = new ArrayList<Integer>();
		personList = sqlSession.selectList("chat.getPersonList", chat_idx);
		
		
		List<Object> chatInfos = new ArrayList<Object>();
		chatInfos.add(chatRoomInfos);
		chatInfos.add(personList);
		
		return chatInfos;
	}
	
	// msg 저장이기 때문에 리턴값이 따로 없다.
	public int saveMsgToDB( Map map ){

		int res = sqlSession.insert("chat.saveMSG", map);
		
		return 1;
	}
	
	// 채팅방 생성
	public int addChatRoomToDB(String chat_name, List<Map<String, Integer>> m_idxList, int m_idx) {
		
		System.out.println("채팅방 생성");
		
		// chat_room 생성
		int addRoomRes = sqlSession.insert("chat.addChatRoom", chat_name);
		// chat_group 초대한 사람 목록 넣기.
		int invitePersonRes = sqlSession.update("chat.addChatGroup", m_idxList);
		System.out.println("m_idx : " + m_idx);
		// 처음 환영한다는 글을 넣는다.
//		int firstMSG = sqlSession.insert("chat.firstMSG", m_idx);
		
		return invitePersonRes;
	}
	
	// 채팅방 삭제
	public int delChatRoomToDB(Map map) {
		
		int delRoomRes = sqlSession.delete("chat.delRoom", map);
		
		return delRoomRes;
	}
	
	// 채팅방 나가기
	public int exitChatRoomToDB(Map map) {
		
		int res = sqlSession.update("chat.exitRoom", map);
		return res;
	}
	
	// 안읽은 메세지 가져오기
	public List<noReadVO> noReadCommToDB(int m_idx) {
		
		List<noReadVO> noReadList = new ArrayList<noReadVO>();
		
		noReadList = sqlSession.selectList("chat.noReadComm", m_idx);
		
		for( int i = 0 ; i < noReadList.size() ; i++ ) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("chat_idx", noReadList.get(i).getChat_idx());
			map.put("chat_outdate", noReadList.get(i).getChat_outdate());
			map.put("m_idx", m_idx);
			noReadList.get(i).setNoReadCnt((int) sqlSession.selectOne("chat.noReadCntComm", map));
		}

		
		return noReadList;
	}
	
	
	
	
}














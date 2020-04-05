package com.spring.mvc.controller.chatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.spring.mvc.vo.chatting.ChattingRoomVO;

public class EchoHandler extends TextWebSocketHandler {
	// m_idx, session : 세션에 들어온 모든 사람들.(m_idx 인데 String 으로 한건, jsonObject를 int로 바꾸는게
	// 귀찮은 일이다.
	private HashMap<String, WebSocketSession> OnPersonList = new HashMap<String, WebSocketSession>();
	//              chat_idx,        m_idx, session : 특정(chat_idx) 채팅룸에 들어온 사람.
	private HashMap<String, HashMap<String, WebSocketSession>> OnPersonListINRoom = new HashMap<String, HashMap<String, WebSocketSession>>();

	
	
	// 웹 소켓 세션을 저장할 리스트 생성
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
	// id session : 웹소켓 세션에 해당 m_idx를 넣는다.
	private HashMap<String, WebSocketSession> sessionINMain = new HashMap<String, WebSocketSession>();

	HashMap<String, WebSocketSession> idListWithSessionInRoom;
	// ※클라이언트 연결 된 후
	// WebSocketSession을 매개 변수로 받고 클라이언트가 연결된 후
	// 해당 클라이언트의 정보를 가져와 연결확인 작업을한다.
	// 클라이언트의 세션을 세션 저장 리스트에 add()로 추가 한다.

	// 새로운 세션이 연결되는 순간 이쪽의 메소드가 호출된다.
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	}

	// ※클라이언트와 연결이 끊어진 경우
	// add()와 반대로 remove()를 이용해서 세션리스트에서 제거한다.
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String m_idx = "";
		String chat_idx = "";
		// 전체 온라인 리스트에서 나가기.
		for (String i : OnPersonList.keySet()) {
			if (OnPersonList.get(i).equals(session)) {
				OnPersonList.remove(m_idx);
				m_idx = i;
				System.out.println("연결 끝 : " + m_idx);
				
				// 방에서 나가기. 아예 채팅을 안들어갔을 수도 있다.
				for (String k : OnPersonListINRoom.keySet()) {
					if (OnPersonListINRoom.get(k).get(i) != null) {
						chat_idx = k;
						OnPersonListINRoom.get(chat_idx).remove(m_idx);
						System.out.println("룸 연결 끝 : " + chat_idx);
						
						if (OnPersonListINRoom.get(chat_idx).size() == 0) {
							OnPersonListINRoom.remove(chat_idx);
						}
					}
				}
			}
		}

//		OnPersonList.remove(m_idx);
	}

	// 웹 소켓 서버로 데이터를 전송했을 경우
	// 연결된 모든 클라이언트에게 메시지 전송 : 리스트
	// 연결된 모든 사용자에게 보내야 하므로 for문으로 세션리스트에 있는 모든 세션들을 돌면서
	// sendMessage()를 이용해 데이터를 주고 받는다.
	// 메세지가 여기로 떨어진다.
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JSONParser jsonParse = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParse.parse(message.getPayload());

		// 메세지를 보냈을때
		if ("text".equals(jsonObject.get("type"))) {
			textMsg(jsonObject, session);

			// 특정 룸으로 들어왔을때
		} else if ("roomIn".equals(jsonObject.get("type"))) {
			roomIn(jsonObject, session);

			// 마이페이지에 들어왓을때 연결.
		} else if ("header".equals(jsonObject.get("type"))) {
			System.out.println("메인 소켓 열렸음.");
			String m_idx = String.valueOf(jsonObject.get("m_idx"));
			OnPersonList.put(m_idx, session);
		} else if ("exitChat".equals(jsonObject.get("type"))) {
			String m_idx = String.valueOf(jsonObject.get("m_idx"));
			String chat_idx = String.valueOf(jsonObject.get("chat_idx"));
			System.out.println(OnPersonListINRoom.get(chat_idx).keySet());
			OnPersonListINRoom.get(chat_idx).remove(m_idx);
			System.out.println("마지막 방 나간다. 채팅 자체를 끈다."+chat_idx + " / " + m_idx);
			System.out.println(OnPersonListINRoom.get(chat_idx).keySet());
			if (OnPersonListINRoom.get(chat_idx).size() == 0) {
				System.out.println("방이 삭제 되나?" + OnPersonListINRoom.get(chat_idx).size() );
				OnPersonListINRoom.remove(chat_idx);
			}
		}
	}

	private void roomIn(JSONObject jsonObject, WebSocketSession session) {

		String chat_idx = String.valueOf(jsonObject.get("chat_idx"));
		String beforeRoomNum = String.valueOf(jsonObject.get("beforeRoomNum"));
		String m_idx = String.valueOf(jsonObject.get("m_idx"));

		// 다른 방으로 들어가게 되면 OnPersonListINRoom에서 나와야 한다. 안나오면 모든 방에 계속 머무르게 된다.
		if (!beforeRoomNum.equals("0")) {
			OnPersonListINRoom.get(beforeRoomNum).remove(m_idx);

			if (OnPersonListINRoom.get(beforeRoomNum).isEmpty()) {
				OnPersonListINRoom.remove(beforeRoomNum);
				System.out.println("삭제");
			}
		}

		// 방에 아무도 없을때, 방을 생성해 주고, 그 방에 나를 넣어야 한다.
		if (OnPersonListINRoom.get(chat_idx) == null) {
			System.out.println("ROOM FIRST!!");
			HashMap<String, WebSocketSession> idListWithSessionInRoom = new HashMap<String, WebSocketSession>();
			idListWithSessionInRoom.put(m_idx, session);
			OnPersonListINRoom.put(chat_idx, idListWithSessionInRoom);
			System.out.println("chat_idx에 들어와있는 2사람들 몇명?" + OnPersonListINRoom.get(chat_idx).size());
		} else if (OnPersonListINRoom.get(chat_idx).get(m_idx) == null) {
			OnPersonListINRoom.get(chat_idx).put(m_idx, session);
			System.out.println("사람있는 방으로 들어옴. 몇명? : " + OnPersonListINRoom.get(chat_idx).size());

		}

	}

	// 채팅 보내는 곳
	private void textMsg(JSONObject jsonObject, WebSocketSession session) throws IOException {
		System.out.println("Text RECEIVE");
		String chat_idx = String.valueOf(jsonObject.get("chat_idx"));
		String text = (String) jsonObject.get("chat_content");
		String m_idx = (String) jsonObject.get("m_idx");
		String writer = (String) jsonObject.get("m_name");
		String pList = (String) jsonObject.get("pList");
		String[] roomPList = pList.split("/");
		String ONOFF = "";
		
		String onOffType = "";
		String offLine = "";
		
		
		idListWithSessionInRoom = OnPersonListINRoom.get(chat_idx); // 현재 특정 방에 들어와있는 사람.
		String msg="";
		
		for( int i = 0 ; i < roomPList.length ; i ++) {
			msg = writer + "/" + text + "/" + m_idx + "/" + chat_idx + "/";
			System.out.println("roomPlist : " + roomPList[i]);
			// 방에 들어와 있으면 : onLine
			if(idListWithSessionInRoom.get(roomPList[i]) != null) {
				onOffType = "onLine";
				msg += onOffType;
				idListWithSessionInRoom.get(roomPList[i]).sendMessage(new TextMessage(msg));
				System.out.println("방에 들어와 있다. onLine" + onOffType);
				
			// 방에는 없지만 칸반에는 들어와 있다. : onOff
			}else if(OnPersonList.get(roomPList[i]) != null) {
				onOffType = "onOff";
				msg += onOffType;
				OnPersonList.get(roomPList[i]).sendMessage(new TextMessage(msg));
				System.out.println("방에는 없지만 칸반에는 들어와 있다. : onOff " + OnPersonList.get(roomPList[i]));
				
			// 방에도 없고, 칸반에도 없다.	
			}else if(OnPersonList.get(roomPList[i]) == null) {
				onOffType = "offLine";
				offLine += roomPList[i] + "/";
				System.out.println("방에도 없고, 칸반에도 없다. : offLine " + onOffType );
				
			}
		}
		System.out.println("msg : " + msg);
		// 오프라인 리스트만 보낸사람한테 다시 보내기. 
		msg = "offLine/" + offLine;
		System.out.println("offLine : " + offLine);
		idListWithSessionInRoom.get(m_idx).sendMessage(new TextMessage(msg));
		
		
		
		
		// 온라인인 애들, 온라인이 아닌애들, 온라인인데 채팅방에 들어온 애들.. 
		// 등등 생각 하기.
//		for (String key : idListWithSessionInRoom.keySet()) {
//
//			for (int i = 0; i < roomPList.length; i++) { // 4명
//				if (idListWithSessionInRoom.get(roomPList[i]) != null) {
//					System.out.println("ON roomPList[i] : " + roomPList[i]);
//					ONOFF = "ON";
//				} else {
//					System.out.println("OFF roomPList[i] : " + roomPList[i]);
//					ONOFF = "OFF";
//				}
//			}
//			
//			msg = writer + "/" + text + "/" + m_idx + "/" + chat_idx + "/" + ONOFF;
//			System.out.println("key : " + key); //m_idx
//			System.out.println("msg : " + msg);
//			idListWithSessionInRoom.get(key).sendMessage(new TextMessage(msg));

			System.out.println("SEND END");
//		}
		
	}
}

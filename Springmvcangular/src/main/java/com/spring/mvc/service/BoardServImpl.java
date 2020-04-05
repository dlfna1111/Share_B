package com.spring.mvc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.mvc.repository.BoardDaoImpl;
import com.spring.mvc.vo.DirectoryVO;
import com.spring.mvc.vo.board.BoardListVO;
import com.spring.mvc.vo.board.DetailMemoVO;
import com.spring.mvc.vo.board.ProjectInfoVO;

@Service
public class BoardServImpl {

	@Autowired
	BoardDaoImpl boarddao;

	// 특정 프로젝트의 보드 내용 가져오기
	public List<BoardListVO> getAllpjInfos(int pj_idx) {
		return boarddao.getListFromDB(pj_idx);
	}
	// 특정 프로젝트 내용 가져오기
	public List<ProjectInfoVO> getAllpjInfos2(Map map) {
		return boarddao.getAllpjInfos2FromDb(map);
	}
	
	// 보드 생성
	public List<BoardListVO> addBoard(Map map) {
		
		boarddao.addBoardFromDbtest(map);
		int pj_idx = Integer.parseInt((String)map.get("pj_idx"));
		
		return boarddao.getListFromDB(pj_idx);
	}
	
	// 메모 추가
	public List<BoardListVO> addMemo(Map map) {
		
		boarddao.addMemoFromDb(map);
		
		int pj_idx = Integer.parseInt((String)map.get("pj_idx"));
		
		return boarddao.getListFromDB(pj_idx);
	}
	
	// 메모 삭제
	public int deleteMemo(int memo_idx, int b_idx) {
		return boarddao.deleteMemoFromDb(memo_idx, b_idx);
	}

	// 보드 삭제
	public int deleteBoard(int b_idx) {
		return boarddao.deleteBoardFromDb(b_idx);
	}
	
	// 메모 상세보기 관련 : 체크리스트 가져오기
	public List<DetailMemoVO> getCheckList(int memo_idx){
		return boarddao.getCheckListFromDB(memo_idx);
	}

	// 메모 상세보기 관련 : 댓글 가져오기
	public List<DetailMemoVO> getComment(int memo_idx){
		return boarddao.getCommentFromDB(memo_idx);
	}

	// 메모 상세보기 관련 : 댓글 가져오기
	public int updateCheck(int check_idx){
		return boarddao.updateCheckFromDB(check_idx);
	}
	
	// 메모 제목 수정
	public int updateSubject(Map map){
		return boarddao.updateSubjectFromDB(map);
	}

	// 메모 상세내용 수정
	public int updateDetail(Map map){
		return boarddao.updateDetailFromDB(map);
	}
	
	// 체크리스트 항목 삭제
	public int delCheck(int check_idx){
		return boarddao.delCheckFromDB(check_idx);
	}
	
	// 체크리스트 항목 추가
	public List<DetailMemoVO> addCheck(Map map){
		return boarddao.addCheckFromDB(map);
	}

	// 메모 댓글 등록
	public List<DetailMemoVO> addComm(Map map){
		System.out.println("메모댓글 등록 서비스 들어왔나");
		return boarddao.addCommFromDB(map);
	}
	
	// 메모 댓글 삭제
	public int delComm(int comm_idx){
		return boarddao.delCommFromDB(comm_idx);
	}
	
	// 메모 북마크 색상 수정
	public int updateBookMark(Map map){
		return boarddao.updateBookMarkFromDB(map);
	}
	
	// 메모 이동
	public int moveMemo(Map map){
		return boarddao.moveMemoFromDB(map);
	}

	// 프로젝트 북마크
	public List<ProjectInfoVO> pjBookMark(Map map){
		return boarddao.pjBookMarkFromDB(map);
	}
	
	// 프로젝트 팀원 초대
	public List<String> searchInvIdServ(String m_email){
		return boarddao.searchInvIdFromDB(m_email);
	}
	
	// 프로젝트 팀원 초대 2
	public int inviteMemberServ(List<Map> list){
		return boarddao.inviteMemberFromDB(list);
	}
	
	// 프로젝트 팀원 초대 2
	public int kickMemServ(Map map){
		return boarddao.kickMemFromDB(map);
	}
	
	// 프로젝트 배경 색상 변경
	public int editBackColorServ(Map map){
		return boarddao.editBackColorFromDB(map);
	}
	
	// 파일 업로드 작성자
	public String getName(int m_idx){
		return boarddao.getNameFromDB(m_idx);
	}
	
	

}
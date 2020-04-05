package com.spring.mvc.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.javafaker.Faker;
import com.spring.mvc.vo.DirectoryVO;
import com.spring.mvc.vo.board.BoardListVO;
import com.spring.mvc.vo.board.DetailMemoVO;
import com.spring.mvc.vo.board.MemoListVO;
import com.spring.mvc.vo.board.ProjectInfoVO;

@Repository
public class BoardDaoImpl  {
	
	SqlSession sqlSession;
	HttpSession session;
	
	public void setSqlSession(SqlSession sqlSession) {
		System.out.println("BoardPageDAO 생성자 생성");
		this.sqlSession = sqlSession;
	}
	
	// 특정 프로젝트의 프로젝트의 내용 가져오기
	public List<ProjectInfoVO> getAllpjInfos2FromDb(Map map) {

		List<ProjectInfoVO> pjInfos = new ArrayList<ProjectInfoVO>();
		pjInfos = sqlSession.selectList("board.getPjInfo", map);
		
		for(int i = 0 ; i < pjInfos.size() ; i++) {
			int m_idx = pjInfos.get(i).getM_idx();
			String m_icon = sqlSession.selectOne("board.getM_idx", m_idx);
 			pjInfos.get(i).setM_icon( m_icon );
		}
		
		return pjInfos;
	}
	
	
	// board생성
	public int addBoardFromDbtest(Map map) {
		
		int boardRes = sqlSession.insert("board.addBoard", map);
		int memoRes = sqlSession.insert("board.addMemo", map);
		
		return boardRes;
	}
	
	// 메모 추가
	public int addMemoFromDb(Map map) {
		
		int addMemoRes = sqlSession.insert("board.addOnlyMemo", map);
		
		return addMemoRes;
	}
	
	// 메모 삭제
	public int deleteMemoFromDb(int memo_idx, int b_idx) {
		// 삭제 전 그 삭제 대상의 memo_seq를 구한다.
		int memo_seq = sqlSession.selectOne("board.getMemoSeq", memo_idx);
		// 삭제 한다.
		int deleteMemoRes = sqlSession.delete("board.deleteMemo", memo_idx);
		
		Map remap = new HashMap<String, Integer>();
		remap.put("memo_seq", memo_seq);
		remap.put("b_idx", b_idx);
		// 나머지 메모들의 memo_seq의 순서를 재정리한다.
		int setMemoSeq = sqlSession.update("board.setMemoSeq", remap);
		
		return setMemoSeq;
	}
	
	// 보드 삭제
	public int deleteBoardFromDb(int b_idx) {
		
		// 보드 삭제
		int delBoardRes = sqlSession.delete("board.deleteBoard", b_idx);
		// 보드에 해당하는 메모 전체 삭제
		int delAllMemoRes = sqlSession.delete("board.deleteAllMemo", b_idx);
		
		return delAllMemoRes;
	}
	
	
	// 메모 상세보기 관련 : 체크리스트
	public List<DetailMemoVO> getCheckListFromDB(int memo_idx){
		List<DetailMemoVO> memoDetailInfos = new ArrayList(); 
		
		memoDetailInfos = sqlSession.selectList("board.checkList", memo_idx);
		return memoDetailInfos;
	}
	
	// 메모 상세보기 관련 : 댓글
	public List<DetailMemoVO> getCommentFromDB(int memo_idx){
		List<DetailMemoVO> memoDetailInfos = new ArrayList(); 
		
		memoDetailInfos = sqlSession.selectList("board.comment", memo_idx);
		return memoDetailInfos;
	}

	// 체크박스 클릭할때마다 
	public int updateCheckFromDB(int check_idx){
		
		int checkRes = sqlSession.update("board.checkBox", check_idx);
		return checkRes;
	}
	
	// 메모 제목 수정
	public int updateSubjectFromDB(Map map){
		
		int subjectRes = sqlSession.update("board.editSubject", map);
		return subjectRes;
	}
	
	// 메모 상세 수정
	public int updateDetailFromDB(Map map){
		
		int detailRes = sqlSession.update("board.editDetail", map);
		return detailRes;
	}
	
	// 체크리스트 항목 삭제
	public int delCheckFromDB(int check_idx){
		
		int detailRes = sqlSession.delete("board.delCheck", check_idx);
		return detailRes;
	}
	
	// 체크리스트 항목 추가
	public List<DetailMemoVO> addCheckFromDB(Map map){
		
		
		int addCheckRes = sqlSession.insert("board.addCheck", map);
		
		List<DetailMemoVO> memoDetailInfos = new ArrayList(); 
		memoDetailInfos = sqlSession.selectList("board.checkList", map);

		return memoDetailInfos;
	}

	// 메모 댓글 등록
	public List<DetailMemoVO> addCommFromDB(Map map){
		
		int addCommRes = sqlSession.insert("board.addComm", map);
		
		List<DetailMemoVO> memoDetailInfos = new ArrayList(); 
		memoDetailInfos = getCommentFromDB((int) map.get("memo_idx"));
		
		return memoDetailInfos;
	}
	
	// 메모 댓글 삭제
	public int delCommFromDB(int comm_idx){
		
		int delCommRes = sqlSession.delete("board.delComm", comm_idx);
		return delCommRes;
	}
	
	
	// 메모 북마크 수정
	public int updateBookMarkFromDB(Map map){
		int bookmarkRes=0;
		
		if(map.get("memo_bookmark") == null) {
			
			bookmarkRes = sqlSession.update("board.editNullBookMark", map);
		}else {
			
			bookmarkRes = sqlSession.update("board.editBookMark", map);
		}
		
		System.out.println("bookmarkRes : " + bookmarkRes);
		
		return bookmarkRes;
	}
	
	// 메모 이동
	public int moveMemoFromDB(Map map){
		int moveMemoRes=0;
		
		// 바꿀 메모 seq를 0.1로 바꾸기
		moveMemoRes = sqlSession.update("move.memo_zero", map);
		
		// 보드가 같고 숫자가 이동후가 더 클때 
		if(((map.get("b_idx_before")).equals(map.get("b_idx_after")))) {
			
			if((int)map.get("memo_seq_before") < (int)map.get("memo_seq_after")) {
				moveMemoRes = sqlSession.update("move.memo_minus", map); // 이동후 seq보다 작거나 같고 이동전 seq보다 큰 것에 -1
			}else {
				moveMemoRes = sqlSession.update("move.memo_plus", map); //  memo_seq가 이동전보다 작고 이동후보다 같거나 큰것에 +1 해주기
			}
			moveMemoRes = sqlSession.update("move.memo_move", map); // 마지막으로 다시 after_seq로 들어오기.
			
		}else{
		// 보드가 다를때 
			moveMemoRes = sqlSession.update("move.memo_minus_same", map);
			moveMemoRes = sqlSession.update("move.memo_plus_same", map);
			moveMemoRes = sqlSession.update("move.memo_move_same", map);
		}
		return moveMemoRes;
	}
	

	// 플젝 북마크
	public List<ProjectInfoVO> pjBookMarkFromDB(Map hm){
		
		int bookMarkRes = sqlSession.delete("board.pjBookMark", hm);
		List<ProjectInfoVO> pjInfo = getAllpjInfos2FromDb(hm);
		
		return pjInfo;
	}
	
	// 플젝 초대
	public List<String> searchInvIdFromDB(String m_email){
		
		List<String> searchInvIdRes = sqlSession.selectList("board.searchInvId", m_email);
		
		return searchInvIdRes;
	}

	
	// 플젝 초대
	public int inviteMemberFromDB(List<Map> list){
		
		int searchInvIdRes = sqlSession.insert("board.pjInvite", list);
		
		return searchInvIdRes;
	}
	
	// 플젝 강퇴
	public int kickMemFromDB(Map map){
		
		int res = sqlSession.delete("board.kickMem", map);
		
		return res;
	}
	
	// 플젝 배경 색상 변경
	public int editBackColorFromDB(Map map){
		
		int res = sqlSession.insert("board.editBackColor", map);
		
		return res;
	}
	
	// 파일 업로드 작성자
	public String getNameFromDB(int m_idx){
		
		String write = sqlSession.selectOne("board.getName", m_idx);
		
		return write;
	}
	
	
	// 특정 프로젝트 보드전체 정보 가져오는 메소드
	public List<BoardListVO> getListFromDB(int pj_idx){
		// memo_detail은 가지고 올 필요없다. 상세보기에서 가져오면 도니다.
		// 보드만 전체를 가지고 온다.
		List<BoardListVO> pjInfos = new ArrayList<BoardListVO>();
		pjInfos = sqlSession.selectList("board.board_list", pj_idx);
		
		// b_idx와 함께 memo의 정보들을 가져와야 한다.
		List<MemoListVO> memoTmp = new ArrayList<MemoListVO>();
		memoTmp = sqlSession.selectList("board.memo_list", pj_idx);
		
		for( int i = 0 ; i < pjInfos.size() ; i++ ) {
			List<MemoListVO> memoTmp2 = new ArrayList<MemoListVO>();
			
			for( int j = 0 ; j < memoTmp.size() ; j++ ) {
				if( pjInfos.get(i).getB_idx() == memoTmp.get(j).getB_idx() ) {
					memoTmp2.add(memoTmp.get(j));
				}
				
			}// inner for
			
			pjInfos.get(i).setMemos(memoTmp2);
		}// outer for
		
		return pjInfos;
	}
	
	
	
	
}



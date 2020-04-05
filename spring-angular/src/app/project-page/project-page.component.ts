import { Component, Renderer2, ViewChild, ElementRef, OnInit, Output, EventEmitter } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { __param } from 'tslib';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-project-page',
  templateUrl: './project-page.component.html',
  styleUrls: ['./project-page.component.css'],
  animations: [
    trigger('slideInOut', [
      state('in', style({
        transform: 'translate3d(0, 0, 0)'
      })),
      state('out', style({
        transform: 'translate3d(100%, 0, 0)'
      })),
      transition('in => out', animate('400ms ease-in-out')),
      transition('out => in', animate('400ms ease-in-out'))
    ])
  ],
})
export class ProjectPageComponent {
  readonly APP_URL = 'http://localhost:8080/Springmvcangular';

  constructor(private _http: HttpClient) {
    this.getAllPjInfos();
  }

  @Output() private pageShow = new EventEmitter<string>();
  emitData;

  ngOnInit() {

  }
 


  backcolorShow(data){
    console.log("프로젝트 페이지 : " + data);
    this.backcolor = data;
  }





  pageShowBox(data) {
    this.pageShow.emit(data);
  }

  doubleBtn = 1;
  menuShow: string = 'out';
  boardSubInput = '';
  BoardAreaInput = '';
  memoAreaInput = '';

  //상세보기
  detailMemo; // 상세보기 누르면 얘 정보가 나옴.
  viewMemo(b_idx, memo_idx) {

    if (this.doubleBtn === 1) {

      let boardIndex: number = this.pjInfos.findIndex(i => i.b_idx == b_idx);
      let memoIndex = this.pjInfos[boardIndex].memos.findIndex(i => i.memo_idx == memo_idx);
      this.detailMemo = this.pjInfos[boardIndex].memos[memoIndex]; // 지금은 memo_idx가 없어서 오류날것임
      this.memoDetailShow = true;
    }

  }

  // 상세보기 박스 열리는 관련
  memoDetailShow = false;
  detailBoxShow() {
    this.memoDetailShow = !this.memoDetailShow;
  }

  // 메뉴 버튼
  toggleMenu() {
    // 1-line if statement that toggles the value:
    this.menuShow = this.menuShow === 'out' ? 'in' : 'out';
  }






















  // 보드, 메모 생성 추가 관련 ----------------------------------------------------------------------------------------------














  addBoardInfos: object = {};
  //보드 생성
  onAddBoard(boardName, memo) {

    this.addBoardInfos = {
      "memo": memo,
      "b_name": boardName,
      "pj_idx": localStorage.getItem("pj_idx")
    }

    this._http.get(this.APP_URL + '/addBoard', {
      params: new HttpParams().set('edit', JSON.stringify(this.addBoardInfos))

    }).subscribe(
      data => {
        this.pjInfos = data;
      }
    );

    // 보드 생성하면 입력창 없애기
    this.displayBoardInput = "none";

    // 입력창, 토글 리셋
    this.boardSubInput = '';
    this.BoardAreaInput = '';
    this.boardShow = !this.boardShow;

  }

  memos: Array<string> = ['Apple', 'Orange', 'Banana'];
  addMemo: object = {};
  // 메모 추가
  onAddMemo(memo, b_idx) {
    console.log("b_idx : " + b_idx);
    this.addMemo = {
      "memo": memo,
      "b_idx": b_idx,
      "pj_idx": localStorage.getItem("pj_idx"),
    }

    console.log("pj_idx : " + localStorage.getItem("pj_idx"));

    this._http.get(this.APP_URL + '/addMemo', {
      params: new HttpParams().set('edit', JSON.stringify(this.addMemo))

    }).subscribe(
      data => {
        this.pjInfos = data;
      }
    );

    // 보드 생성하면 입력창 없애기
    this.memoBoxDisplay = "none";
    this.memoShow = !this.memoShow;
    // 입력창 리셋
    this.memoAreaInput = '';

  }

  boardShow: boolean;
  displayBoardInput: string;
  memoShow: boolean;
  memoBoxDisplay: string = "none";
  //보드 생성 입력창 버튼
  boardInputToggle() {
    this.boardShow = !this.boardShow;
    this.displayBoardInput = this.boardShow ? "inline" : "none";
  }

  b_idx: number = 0;
  // 메모추가 입력창 show
  memoInputToggle(b_idx) {
    console.log("b_idx : " + b_idx);
    this.b_idx = b_idx;
    this.memoShow = !this.memoShow;
    this.memoBoxDisplay = this.memoShow ? "inline" : "none";
  }

  // 이런식으로 되어있는 변수들은 하나로 합쳐도 될듯하다.
  deleteMemo: object;
  // 메모 삭제
  memoDelete(memo_idx, b_idx) {
    this.doubleBtn++;

    this.deleteMemo = {
      "memo_idx": memo_idx,
      "b_idx": b_idx
    }

    this._http.get(this.APP_URL + '/deleteMemo', {
      params: new HttpParams().set('edit', JSON.stringify(this.deleteMemo))
    }).subscribe(
      data => {

        let boardIndex: number = this.pjInfos.findIndex(i => i.b_idx == b_idx);
        let memoIndex: number = this.pjInfos[boardIndex].memos.findIndex(i => i.memo_idx == memo_idx);
        this.pjInfos[boardIndex].memos.splice(memoIndex, 1);

        this.doubleBtn = 1;
      }
    );
  }

  // 보드 삭제
  boardDel(b_idx) {
    
    console.log("b_idx : " + b_idx);
    this._http.get(this.APP_URL + '/deleteBoard', {
      params: new HttpParams().set('b_idx', b_idx)
    }).subscribe(
      data => {

        let boardIndex: number = this.pjInfos.findIndex(i => i.b_idx == b_idx);
        this.pjInfos.splice(boardIndex, 1);

      }
    );
  }


  // 북마크
  public color1: string;


















  // 프로젝트 관련 ====================================================================================================






















  // 프로젝트 별 북마크
  bookMarkShow: boolean;
  pjBookMark() {

    const edit={
      'pj_idx': this.PJ_IDX,
      'm_idx': this.M_IDX,
    }

    this._http.get(this.APP_URL + '/pjBookMark', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
        this.pjInfos2 = data;
      }
    );

    let Index: number = this.pjInfos2.findIndex(i => i.m_idx == this.M_IDX);
    this.bookMarkShow = this.pjInfos2[Index].pj_bookmark == 'o' ? true : false;
  }



  invIdList = [];
  getInvIdList: any;
  pjInvBox = false;
  // 프로젝트 팀원 초대 1
  pjInvBoxShow() {
    this.pjInvBox = !this.pjInvBox;
  }

  // 프로젝트 팀원 초대 2
  searchId(text) {
    this.invIdList = [];

    // 네자 입력했을때 해당하는 이메일 다 가지고 오고, 그 이후는 가져온 데이터에서 검색
    if (text.length >= 3) {
      this._http.get(this.APP_URL + '/searchInvId', {
        params: new HttpParams().set('text', text)
      }).subscribe(
        data => {
          this.getInvIdList = '';
          console.log(data);
          this.getInvIdList = data;
        }
      );
    }

    for (let index in this.getInvIdList) {
      if (this.getInvIdList[index].indexOf(text) != -1) {
        this.invIdList.push(this.getInvIdList[index]);

      }
    }
    
    if (text == '') {
      this.invIdList = [];
    }
  }

  invTmpList = [];
  // 프로젝트 팀원 초대 3
  inviteTmpMem(m_email) {
    // 여기서 이메일 체크..귀찮..



    if(this.invTmpList.indexOf(m_email) == -1){//m_email이 없다면 -1
      this.invTmpList.push(m_email);
    }

    this.invIdList = [];
  }

  // 프로젝트 팀원 초대 4: invTmpListdel
  delInvTmpList(m_email){
    console.log("m_email 삭제 : " + m_email);
    this.invTmpList.splice(m_email,1);
  }

  // 프로젝트 팀원 초대 5
  inviteMem(){

    const edit={
      "m_email":this.invTmpList,
      "pj_idx":this.PJ_IDX,
    }

    // invTmpList에 이미 다 넣어놨기 떄문에 여기서 목록을 가지고 오면 된다.
    // 배열 받는거 다시 봐야 할뜻?
     // 프로젝트 관련 정보
     this._http.get(this.APP_URL + '/inviteMember', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        this.pjInfos2 = data;
      }
    );
  }









  kickMemId:number;
  kickShow:boolean = false;
  kickCnt:number = 0;
  // 프로젝트 팀원 강퇴
  kickMemBox(m_idx){
    this.kickCnt ++;
    if(this.kickShow == true){
      m_idx = 0;
    }
    this.kickShow = !this.kickShow;
    this.kickMemId = m_idx;
  }

  kickMem(){

    const edit={
      'm_idx':this.kickMemId,
      'pj_idx':this.PJ_IDX,
    }
    this._http.get(this.APP_URL + '/kickMem', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        let Index: number = this.pjInfos2.findIndex(i => i.m_idx == this.kickMemId);
        this.pjInfos2.splice(Index, 1)
      }
    );
  }




























  // 모든 보드, 메모 정보를 넣는 변수
  pjInfos: any;
  pjInfos2: any;
  pjMINE:any;
  M_IDX: string = localStorage.getItem("m_idx");
  PJ_IDX: string = localStorage.getItem("pj_idx");
  // 배경 색상
  backcolor:string;
  // 플젝페이지로 들어오면 가장먼저 실행되는 메소드(모든 보드, 메모 내용 가져오는곳)
  getAllPjInfos() {
    console.log("M_IDX : " + this.M_IDX);
    const edit = {
      "m_idx": this.M_IDX,
      "pj_idx": this.PJ_IDX
    }

    // 프로젝트 관련 정보
    this._http.get(this.APP_URL + '/pjInfos2', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        this.pjInfos2 = data;
        let Index: number = this.pjInfos2.findIndex(i => i.m_idx == this.M_IDX);
        this.bookMarkShow = this.pjInfos2[Index].pj_bookmark == 'o' ? false : true;
        console.log(data);
        this.pjMINE = this.pjInfos2[Index];
        console.log("INdex : " + Index);
        this.backcolor = this.pjInfos2[Index].pj_backcolor;
        console.log("backColor : " + this.backcolor);
      }
    );

    // 보드 관련 정보
    this._http.get(this.APP_URL + '/pjInfos', {
      params: new HttpParams().set('pj_idx', this.PJ_IDX)
    }).subscribe(
      data => {
        this.pjInfos = data;
        console.log(data);
        console.log("this.pjInfos : " +  this.pjInfos);
      }
    );

    
  }



































  // 드래그 ------------------------------------------------------------------






























  onDrop(event: CdkDragDrop<string[]>) {

    // memo_idx
    console.log("drag : " + event.item.data);
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
      console.log(
        "\n event.previousContainer : " + event.previousContainer.id + " / " + event.container.id +
        "\n event.container.data : " + event.container.id +
        "\n event.previousIndex : " + event.previousIndex +
        "\n event.currentIndex : " + event.currentIndex
      );
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
      console.log(
        "event.previousContainer.data : " + event.previousContainer.id +
        "\n event.container.data : " + event.container.id +
        "\n event.previousIndex : " + event.previousIndex +
        "\n event.currentIndex : " + event.currentIndex
      );
    }

    const edit = {
      "pj_idx":this.PJ_IDX,
      "memo_idx": event.item.data,
      "b_idx_after": event.container.id,
      "b_idx_before": event.previousContainer.id,
      "memo_seq_after": event.currentIndex + 1,
      "memo_seq_before": event.previousIndex + 1
    }

    this._http.get(this.APP_URL + '/moveMemo', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
      }
    );



  }


}


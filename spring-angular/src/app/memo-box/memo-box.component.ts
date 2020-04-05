import { Component, Input, Output, EventEmitter, OnInit, ViewContainerRef } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ColorPickerService, Cmyk } from 'ngx-color-picker';

@Component({
  selector: 'app-memo-box',
  templateUrl: './memo-box.component.html',
  styleUrls: ['./memo-box.component.css']
})
export class MemoBoxComponent implements OnInit{
  
  readonly APP_URL = 'http://localhost:8080/Springmvcangular';

  constructor(private _http: HttpClient, public vcRef: ViewContainerRef, private cpService: ColorPickerService) {
    
   }
  
   ngOnInit(){
    this.getMemoInfos();
    this.color1 = this.detailMemo.memo_bookmark;

  }

  // 내가 누른 메모의 idx
  @Input() detailMemo;
  // 처음에 불러오는 체크리스트, 댓글 정보
  checkList:any;
  comment:any;


  // 업로드창 닫는곳
  @Output() private memoDetailShow = new EventEmitter<boolean>();

  public xBtn() {
    const memoDetailOff = false;
    this.memoDetailShow.emit(memoDetailOff);
  }



  editSubShow=true;
  editDetailShow=true;
  editBookMark=true;
  editBox(sort){
    if(sort == 1){
      this.editSubShow = !this.editSubShow;
      this.editDetailShow = true;
      this.addCheckShow = true;
    }else if(sort == 2){
      this.editDetailShow = !this.editDetailShow;
      this.editSubShow = true;
      this.addCheckShow = true;
    }else if(sort == 3){
      // 북마크
      this.editBookMark = !this.editBookMark;
      console.log("북마크 들어옴");
    }
  }
  addCheckShow=true;
  addBox(){
    this.addCheckShow = !this.addCheckShow;
    this.editDetailShow = true;
    this.editSubShow = true;
  }
  // 댓글창
  commInputBoxShow = true;
  commInput() {
    this.commInputBoxShow = !this.commInputBoxShow;
  }















  



// 컬러픽커ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ



















public color1: string = '';
public onChangeColor(color: string): Cmyk {
  
  const hsva = this.cpService.stringToHsva(color);
  const rgba = this.cpService.hsvaToRgba(hsva);
  
  this.detailMemo.memo_bookmark = color;
  this.color1 = color;
  console.log("color : "+color);
  //  console.log(rgba);

  const edit={
    "memo_idx": this.memo_idx,
    "memo_bookmark": color
  }

  this._http.get(this.APP_URL + '/updateBookMark', {
    params: new HttpParams().set('edit', JSON.stringify(edit))
  }).subscribe(
    data => {
      console.log(data);
    },
    error => {
      console.log('Error occured', error);
    }
  );
  return this.cpService.rgbaToCmyk(rgba);
}
nullCnt = 0;
// 널값 따로
nullColor(){
  console.log("널값따로");
  const edit={
    "memo_idx": this.memo_idx,
    "memo_bookmark": null
  }
  this._http.get(this.APP_URL + '/updateBookMark', {
    params: new HttpParams().set('edit', JSON.stringify(edit))
  }).subscribe(
    data => {
      console.log(data);
    },
    error => {
      console.log('Error occured', error);
    }
  );
  this.detailMemo.memo_bookmark = null;
}









//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ











  // 제목 수정
  editSubject(subject){

    const edit={
      "memo_idx": this.memo_idx,
      "subject": subject
    }

    this._http.get(this.APP_URL + '/updateSubject', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log('Error occured', error);
      }
    );

    this.editSubShow = !this.editSubShow;
    // 이거 온 곳이랑 변수가 연결돼있네??
    this.detailMemo.memo_subject = subject;
  }

  // 상세내용 수정
  editDetail(detail){
    console.log("detail : " + detail);
    detail = detail.replace(/(?:\r\n|\r|\n)/g, '<br/>');

    const edit={
      "memo_idx": this.memo_idx,
      "detail": detail
    }

    this._http.get(this.APP_URL + '/updateDetail', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log('Error occured', error);
      }
    );

    this.editDetailShow = !this.editDetailShow;
    this.detailMemo.memo_detail = detail;
  }

  








// 체크리스트 관련ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ












  
 
  // 체크 리스트
  checkListBarWidth = "0%";
  chListCheck(check_idx){
    console.log("check_idx : " + check_idx);

    let checkIdx:number = this.checkList.findIndex(i => i.check_idx == check_idx);

    if(this.checkList[checkIdx].check_complete == 'o'){
      this.checkList[checkIdx].check_complete = 'x';
    }else{
      this.checkList[checkIdx].check_complete = 'o';
    }
    
    this.progressBar();

    // 체크박스 클릭 할때마다 db를 가는게 맞는지 모르겠다.
    this._http.get(this.APP_URL + '/updateCheck', {
      params: new HttpParams().set('check_idx', check_idx)
    }).subscribe(
      data => {
        console.log("data : " + data );
      },
      error => {
        console.log('Error occured', error);
      }
    );
  }

  // 체크 항목 삭제
  delCheck(check_idx){
    console.log("check_idx : " + check_idx );
    this._http.get(this.APP_URL + '/delCheck', {
      params: new HttpParams().set('check_idx', check_idx)
    }).subscribe(
      data => {
        console.log(data);
        this.progressBar();
      },
      error => {
        console.log('Error occured', error);
      }
    );

    let checkIdx:number = this.checkList.findIndex(i => i.check_idx == check_idx);
    this.checkList.splice(checkIdx,1);

  }

  // 체크 항목 추가
  addCheck(content){
    console.log("content : " + content);
    const edit={
      "memo_idx": this.memo_idx,
      "check_content": content
    }

    this._http.get(this.APP_URL + '/addCheck', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        this.checkList = data;

        this.progressBar();

      },
      error => {
        console.log('Error occured', error);
      }
    );

    this.addCheckShow = !this.addCheckShow;

  }//addCheck













// 댓글 관련ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ













  commInputBox:string ='';
  M_IDX:string = localStorage.getItem("m_idx");
  addComm(content){
    console.log("Comm_content : " + content);

    const edit={
      "memo_idx": this.memo_idx,
      "comm_content": content,
      "m_idx":this.M_IDX,
    }

    this._http.get(this.APP_URL + '/addComm', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
        this.comment = data;
      },
      error => {
        console.log('Error occured', error);
      }
    );
    this.commInputBox='';
    this.commInput();
  }

  delComm(comm_idx){
    console.log("삭제 드러옴");

    this._http.get(this.APP_URL + '/delComm', {
      params: new HttpParams().set('delComm', comm_idx)
    }).subscribe(
      data => {
        console.log(data);
        let commFindIdx:number = this.comment.findIndex(i=>i.comm_idx == comm_idx);
        this.comment.splice(commFindIdx,1);
      },
      error => {
        console.log('Error occured', error);
      }
    );
  }



















  progressBar(){

    let totalCheck =  this.checkList.length;
    let checked = 0;
    let cnt=0;
    for(let i = 0 ; i < this.checkList.length ; i++){
      if(this.checkList[i].check_complete == 'o'){
        cnt++;
      }
    }
    checked = cnt;
    console.log("check compl: " + checked);
    
    this.checkListBarWidth = ((checked * 100)/totalCheck) + "%";
  }

  memo_idx;
  // memo_idx로 메모 정보들 가져오기.
  // 체크리스트랑 댓글 가져와야 하는데, 한번에 가지고 오거나. 두개 나눠서 가져오면 되는데,
  // 나중에 사용할때를 사용해서 두개 나눠서 가지고 오는게 나을 것 같다. 
  getMemoInfos(){
    this.memo_idx = this.detailMemo.memo_idx;
    console.log("memo_idx : " + this.memo_idx);

    // 체크리스트 가져오기
    this._http.get(this.APP_URL + '/getCheckList', {
      params: new HttpParams().set('memo_idx', JSON.stringify(this.memo_idx))
    }).subscribe(
      data => {
        this.checkList = data;
        console.log(data);
        this.progressBar();
      },
      error => {
        console.log('Error occured', error);
      }
    );

    // 댓글 가지고 오기
    this._http.get(this.APP_URL + '/getComment', {
      params: new HttpParams().set('memo_idx', JSON.stringify(this.memo_idx))
    }).subscribe(
      data => {
        this.comment = data;
        console.log(data);
      },
      error => {
        console.log('Error occured', error);
      }
    );


  }

    
}

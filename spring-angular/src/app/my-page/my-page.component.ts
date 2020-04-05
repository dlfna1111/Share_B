import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { FileUploader } from 'ng2-file-upload';
import { Observable } from 'rxjs';
import { AuthService, GoogleLoginProvider } from 'angular4-social-login';
@Component({
  selector: 'app-my-page',
  templateUrl: './my-page.component.html',
  styleUrls: ['./my-page.component.css']
})

export class MyPageComponent implements OnInit {

  readonly APP_URL = 'http://localhost:8080/Springmvcangular';

  @Output() private pageShow = new EventEmitter<string>();
  constructor(private fb: FormBuilder, private _http: HttpClient, private _socioAuthServ: AuthService) {
  }
  @Output() private GPhoto = new EventEmitter<string>();
  
  googleProfile(data) {
    this.GPhoto.emit(data);
  }


  Gphoto:string;
  ngOnInit() {
    this.getAllPj();
    this.getMyInfo();
    this.uploadForm = this.fb.group({
      document: [null, null],
      type: [null, Validators.compose([Validators.required])]
    });

    this.Gphoto = localStorage.getItem("GPhoto");
    this.googleProfile(this.Gphoto);
  }

  projectCnt = 0;
  // 프로젝트 클릭  
  pageShowBox(pj_idx) {
    if (this.projectCnt == 0) {

      localStorage.setItem("pj_idx", pj_idx);
      this.pageShow.emit('project');
    }

    this.projectCnt = 0;
  }






  // 프로젝트 별 북마크
  bookMarkShow: boolean;
  pjBookMark(pj_idx) {
    this.projectCnt++;

    this._http.get(this.APP_URL + '/pjBookMark', {
      params: new HttpParams().set('pj_idx', pj_idx)
    }).subscribe(
      data => {
        console.log(data);
      }
    );
    let Index: number = this.allPjInfo.findIndex(i => i.pj_idx == pj_idx);
    this.allPjInfo[Index].pj_bookmark = this.allPjInfo[Index].pj_bookmark === 'o' ? 'x' : 'o';
  }












  // 개인정보 설정 -----------------------------------------------------------------------


















  uploadForm: FormGroup;


  public uploader: FileUploader = new FileUploader({
    isHTML5: true
  });


  uploadSubmit() { 
    for (var i = 0; i < this.uploader.queue.length; i++) {
      let fileItem = this.uploader.queue[i]._file;
      if (fileItem.size > 10000000) {
        alert("Each File should be less than 10 MB of size.");
        return;
      }   
    } 
    for (var j = 0; j < this.uploader.queue.length; j++) {
      let data = new FormData();
      let fileItem = this.uploader.queue[j]._file;
      console.log("fileItem.name : " + fileItem.name);
      data.append('file', fileItem);
      data.append('fileSeq', 'seq' + j);
      data.append('dataType', this.uploadForm.controls.type.value);
      data.append('m_idx', this.M_IDX);
      this.uploadFile(data).subscribe(
        data => {
          console.log("asdfsdfasdfasdf",data);
          location.href="http://localhost:4200/";
          this.myInfo = data;
        },
        error => {
          console.log('Error occured', error);
        }

      );
    }
    this.uploader.clearQueue();
  }
  
  uploadFile(data: FormData): Observable<any> {
    return this._http.post<any>(this.APP_URL+"/profileEdit", data);
  }







// 프로필 수정 버튼 생성
editBtnShow = false;
editBtnBoxShow(){
  this.editBtnShow = !this.editBtnShow;
}

editName(text){

  const edit={
    "m_idx":this.M_IDX,
    "m_name":text
  }

  this._http.get(this.APP_URL + '/editName', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
        this.myInfo[0].m_name = text;
      }
    );

  this.editBtnBoxShow();
}








  //------------------------------------------------------------------------------------------------------------



















  // 프로젝트 색상 선택
  pjColor: any = {
    'color': ['#FFDDDD', '#FFF6DD', '#FFFFDD', '#E5FFDD', '#DDFFFF', '#DDE5FF', '#EEDDFF', '#FFDDFF', '#FFDDE5']
  };
  addBoxColor = '#FFDDDD';

  // 프로젝트 생성
  addPj(pj_name, pj_content) {

    const edit = {
      "pj_name": pj_name,
      "pj_content": pj_content,
      "pj_backcolor": this.addBoxColor,
      "m_idx": this.M_IDX
    }

    this._http.get(this.APP_URL + '/addPj', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
        this.allPjInfo = data;
      }
    );

    this.addPjShow = false;

  }

  addPjShow = false;
  addPjBoxShow() {
    this.addPjShow = !this.addPjShow;
  }

  // 프로젝트 삭제
  exitPj(pj_idx) {
    this.projectCnt++;

    const edit = {
      "pj_idx": pj_idx,
      "m_idx": this.M_IDX
    }

    this._http.get(this.APP_URL + '/exitPj', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
        let boardIndex: number = this.allPjInfo.findIndex(i => i.pj_idx == pj_idx);
        this.allPjInfo.splice(boardIndex, 1);
      }
    );

  }
















  M_IDX: string = localStorage.getItem("m_idx");
  allPjInfo: any;
  // 모든 프로젝트 정보
  getAllPj() {

    this._http.get(this.APP_URL + '/getAllPj', {
      params: new HttpParams().set('m_idx', this.M_IDX)
    }).subscribe(
      data => {
        this.allPjInfo = data;
        console.log(this.allPjInfo);

      }
    );

  }

  myInfo: any;
  // 내 정보
  getMyInfo() {
    this._http.get(this.APP_URL + '/getMyInfo', {
      params: new HttpParams().set('m_idx', this.M_IDX)
    }).subscribe(
      data => {
        this.myInfo = data;
        console.log("마이페이지 myInfo : "+data);

      }
    );
  }

}

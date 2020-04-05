import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

const URL = 'http://localhost:8080/Springmvcangular';

@Component({
  selector: 'app-directory-page',
  templateUrl: './directory-page.component.html',
  styleUrls: ['./directory-page.component.css'],
})
export class DirectoryPageComponent {
  constructor(private _http: HttpClient) {

  }
  ngOnInit() {
    this.getDirList();

  }

  dirList: any;


  // 전체선택
  allCheck = "";
  selectAll() {
    if (this.dirList.every(val => val.checked == "")) {
      this.dirList.forEach(val => { val.checked = "checked" });
      this.allCheck = "checked";
    }
    else {
      this.dirList.forEach(val => { val.checked = "" });
      this.allCheck = "";
    }
  }

  // 삭제
  deleteDir(file_title) {  

    let IDX = this.dirList.findIndex(i => i.file_title == file_title);
    this.dirList.splice(IDX, 1);

    const edit = {
      'file_title': file_title,
      'pj_idx': this.PJ_IDX,
    }

    this._http.get(URL + '/deleteDir', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        // let IDX = this.dirList.findIndex(i => i.file_title == file_title);
        // this.dirList.splice(IDX, 1);
      },
      error => {
        console.log('Error occured', error);
      }
      );
      
      

  }

  // 파일 다운로드
  selectList = "";
  downloadFile() {

    for (let i = 0; i < this.dirList.length; i++) {

      if (this.dirList[i].checked) {
        this.selectList += this.dirList[i].file_title + "/";
      }
    }

    let link = document.createElement("a");
    let file: string[] = this.selectList.split("/");

    console.log("file.length : " + file.length);

    for (let i = 0; i < file.length - 1; i++) {
      console.log("file[i] : " + file[i]);
      link.download = file[i];
      link.href = "assets/Project/" + this.PJ_IDX + "/" + file[i];
      link.click();
    }

    this.selectList = "";

    this.dirList.forEach(val => { val.checked = "" });
    this.allCheck = "";
  }


  uploadFlag = false;
  // 업로드창 On/Off
  uploadOnOff() {
    this.uploadFlag = !this.uploadFlag;
  }
  public uploadOff(uploadOff: boolean) {
    this.uploadFlag = uploadOff;
  }


  // 작성자 이름 기준으로 정렬
  sortNameFlag: boolean = true;
  nameSort() {
    if (this.sortNameFlag) {
      this.sortNameFlag = !this.sortNameFlag;
      this.dirList.sort(function (a, b) { // 오름차순
        return a.userName > b.userName ? -1 : a.userName < b.userName ? 1 : 0;
      });
    } else {
      this.sortNameFlag = !this.sortNameFlag;
      this.dirList.sort(function (a, b) { // 내림차순
        return a.userName < b.userName ? -1 : a.userName > b.userName ? 1 : 0;
      });
    }
  }

  // 날짜 기준으로 정렬
  sortDateFlag: boolean = true;
  dateSort() {
    if (this.sortDateFlag) {
      this.sortDateFlag = !this.sortDateFlag;
      this.dirList.sort(function (a, b) { // 오름차순
        return a.dirDate > b.dirDate ? -1 : a.dirDate < b.dirDate ? 1 : 0;
      });
    } else {
      this.sortDateFlag = !this.sortDateFlag;
      this.dirList.sort(function (a, b) { // 내림차순
        return a.dirDate < b.dirDate ? -1 : a.dirDate > b.dirDate ? 1 : 0;
      });
    }
  }

  searchText = '';

  dirListUpload(dirList: any) {
    console.log("드럴옴?");
    this.dirList = dirList;
    this.getDirList();
  }


  PJ_IDX: string = localStorage.getItem("pj_idx");
  // 디렉토리 파일 전부 가져오기.
  // 지금은 db에서 가져오지만 이제 그냥 디렉토리에서 가지고 올 수 잇도록 하기.
  getDirList() {

    console.log("this.PJ_IDx : " + this.PJ_IDX);
    this._http.get(URL + '/dirList', {
      params: new HttpParams().set('pj_idx', this.PJ_IDX)
    }).subscribe(
      data => {
        this.dirList = data;
        console.log("this.dirList :");
        console.log(this.dirList);
      },
      error => {
        console.log('Error occured', error);
      }
    );
  }

}

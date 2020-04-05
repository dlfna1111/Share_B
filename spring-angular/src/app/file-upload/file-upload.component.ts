import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {FileUploader, FileItem} from "ng2-file-upload";
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";

const URL = 'http://localhost:8080/Springmvcangular/upload';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  uploadForm: FormGroup;


  public uploader:FileUploader = new FileUploader({
    isHTML5: true
  });
  constructor(private fb: FormBuilder, private http: HttpClient ) { }

  ngOnInit() {
    this.uploadForm = this.fb.group({
      document: [null, null],
      type:  [null, Validators.compose([Validators.required])]
    });

  }

  PJ_IDX = localStorage.getItem("pj_idx");
  M_IDX = localStorage.getItem("m_idx");
  uploadSubmit(){
    
    
        for (var i = 0; i < this.uploader.queue.length; i++) {
          let fileItem = this.uploader.queue[i]._file;
          if(fileItem.size > 10000000){
            alert("Each File should be less than 10 MB of size.");
            return;
          }
        }
        for (var j = 0; j < this.uploader.queue.length; j++) {
          let data = new FormData();
          let fileItem = this.uploader.queue[j]._file;
          console.log("fileItem.name : " + fileItem.name);
          data.append('file', fileItem);
          data.append('fileSeq', 'seq'+j);
          data.append( 'dataType', this.uploadForm.controls.type.value);
          data.append('pj_idx', this.PJ_IDX);
          data.append('m_idx', this.M_IDX);
          this.uploadFile(data).subscribe(
            data => {
          },
          error => {
            console.log('Error occured', error);
          }
          
          );
        }
        this.uploader.clearQueue();


        

        this.xBtn();
  }

  uploadFile(data: FormData): Observable<any> {
    
        
        return this.http.post<any>(URL, data);

        
  }

 


  // 업로드창 닫는곳
  @Output() private uploadFlag = new EventEmitter<boolean>();
  @Output() private dirList = new EventEmitter<boolean>();
  aaa:any;
  public xBtn() {
    var self = this;
    this.http.get('http://localhost:8080/Springmvcangular/dirList', {
      params: new HttpParams().set('pj_idx', this.PJ_IDX)
    }).subscribe(
      data => {
        self.aaa = data;
      },
      error => {
        console.log('Error occured', error);
      }
    );

    this.uploadFlag.emit(false);
    console.log("this.dirList");
    console.log(this.dirList);

    self.dirList.emit(self.aaa);


  }
}



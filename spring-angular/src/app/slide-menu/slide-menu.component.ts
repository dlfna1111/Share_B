import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-slide-menu',
  templateUrl: './slide-menu.component.html',
  styleUrls: ['./slide-menu.component.css']
})
export class SlideMenuComponent implements OnInit {
  readonly APP_URL = 'http://localhost:8080/Springmvcangular';
  
  dirFlag = false;
  M_IDX: string = localStorage.getItem("m_idx");
  PJ_IDX: string = localStorage.getItem("pj_idx");

  constructor(private _http:HttpClient) { }

  ngOnInit() {
  }

  dirOnOFF(){
    this.dirFlag = !this.dirFlag;
  }









  @Output() private backcolor = new EventEmitter<string>();

  pjColor: any = {
    'color': ['#FFDDDD', '#FFF6DD', '#FFFFDD', '#E5FFDD', '#DDFFFF', '#DDE5FF', '#EEDDFF', '#FFDDFF', '#FFDDE5']
  };

  colorShow=false;
  colorBoxShow(){
    this.colorShow = !this.colorShow;
  }

  colorChange(color){
    this.backcolor.emit(color);
    const edit={
      "m_idx":this.M_IDX,
      "pj_backcolor":color,
      "pj_idx":this.PJ_IDX,
    }

    console.log("pj_IDX : " + this.PJ_IDX);

    this._http.get(this.APP_URL + '/editBackColor', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        console.log(data);
      }
    );
  }

}

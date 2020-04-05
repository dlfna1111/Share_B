import { Component, NgModule, Output, EventEmitter, Input } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { AuthService, GoogleLoginProvider } from 'angular4-social-login';
import { HttpClient, HttpParams } from '@angular/common/http';
import { timer } from 'rxjs';
import { APP_BASE_HREF } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})


export class AppComponent {

  readonly APP_URL = 'http://localhost:8080/Springmvcangular';
  M_IDX: string = localStorage.getItem("m_idx");
  ws: SockJS;
  myInfo;

  constructor(private _socioAuthServ: AuthService, private _http: HttpClient) {
    
    if (localStorage.getItem('page') != 'login') {
      this.startSWOpen();
    }
    this.getMyInfo();
    
  
  }

  ngOnInit() {


    if (localStorage.getItem('page')) {
      this.pageShow = localStorage.getItem('page');
    } else {
      this.M_IDX = localStorage.getItem("m_idx");
      this.pageShow = 'login';
    }

    if (this.wsValid === true) {
      this.wsConnect();
    }

    if(this.pageShow === 'mypage'){
      this.getNoReadComm();
    }
this.onMessagea();
  }





  GPhoto:string;
  googleProfile(data){
    
      console.log("구글프로필 : " + data);
      this.GPhoto = data;
  }


  
  pageShow;
  pageShowBox(data) {
    localStorage.setItem('page', data);
    this.pageShow = localStorage.getItem('page');
  }




















  // 웹소켓 관련 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


















  // 웹소켓 오픈
  startSWOpen() {
    if(localStorage.getItem("m_idx") != null){
    this.ws = SockJS(this.APP_URL + "/echo");
   }
  }

  wsValid = true;
  // 웹소켓 m_idx 연결.
  time: number = 5000;
  wsConnect() {
    if(localStorage.getItem("m_idx") != null){

      timer(0, 1000).subscribe(ellapsedCycles => {
        this.time -= 1000;
        if (this.time == 0) {
          var msg = {
            "type": "header",
            "m_idx": this.M_IDX,
          };
          this.ws.send(JSON.stringify(msg));
          this.wsValid = false;
        }
      });
    }
  }

  // 홈피 로고를 누르면 마이페이지로 간다.
  mypageShowBox() {
    this.pageShow = 'mypage';
    localStorage.setItem('page', 'mypage');
    localStorage.removeItem("pj_idx");
  }


  signOutShow:boolean = false;
  signOutShowBox(){
    this.signOutShow = !this.signOutShow;
  }


  user: any;
  // 로그아웃
  signOut(platform : string): void {
    this.ws.close(); 

    // 구글 로그인이 아닐 수 있다. 
    // 구글 로그인이 아닌 상태에서 로그아웃 하면 에러가 발생한다.
    platform = GoogleLoginProvider.PROVIDER_ID;
    this._socioAuthServ.signOut().then(
      (response) => {
      });
    
    this.user = null;
    localStorage.setItem('page', 'login');
    localStorage.removeItem("pj_idx");
    localStorage.removeItem('m_idx');
    localStorage.removeItem('GPhoto');
    this.pageShow = "login";
  }
















  // 채팅 관련 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ













  public toggleText: string = "Hide";
  private chatShow: boolean = false;
  // 채팅 outter 보여준다.
  public chatToggle(): void {
    this.chatShow = !this.chatShow;
    this.toggleText = this.chatShow ? "Show" : "Hide";

    if (this.toggleText == "Hide" && this.roomNumOutput > 0) {
      var msg = {
        "lastRoomNum": this.roomNumOutput,
        "type": "chatBoxOFF",
      };
      this.ws.send(JSON.stringify(msg));
    }

    // 헤더에 있는 채팅 버튼을 다시 눌러서 끌때,
    // 마지막에 들어간 방 나가기.
    if( this.chatShow === false && this.roomNumOutput != undefined && this.roomNumOutput == 0){
      console.log("마지막 방 나가기 : " + this.roomNumOutput);
      
      var exitChat = {
        "type": "exitChat",
        "m_idx": this.M_IDX,
        "chat_idx": this.roomNumOutput,
      };
      this.ws.send(JSON.stringify(exitChat));

      // beforRoomNum 의 chat_group에서 outDate 날짜 현재로 바꾸기.
      this._http.get(this.APP_URL + '/exitChatRoom', {
        params: new HttpParams().set('edit', JSON.stringify(exitChat))
      }).subscribe(
        data => {
          console.log("data : " + data);
        }
      );
      this.roomNumOutput = 0;
    }
  }


  public roomNumAPP(roomNum: number) {
    this.roomNumOutput = roomNum;
  }
  

  roomNumOutput: number;








  receiveTmp;
  receiveMSG = [];
  onMessagea() {
    var self = this;
    this.ws.onmessage = function onMessage(event) {

      this.receiveTmp = "" + event.data;
      this.receiveMSG = this.receiveTmp.split("/");
      console.log("app onMSG 에 들어오나" + this.receiveMSG[4]);
      // 채팅창에 들어와 있는 상태
     if(this.receiveMSG[4] === 'onOff'){
      self.noRead ++;

        console.log("noRead " + self.noRead);
      }
    }
  }








  // 내 정보 --------------------------------------------------------------------------------------------



















  getMyInfo() {
    if(localStorage.getItem("m_idx") != null){

      this._http.get(this.APP_URL + '/getMyInfo', {
        params: new HttpParams().set('m_idx', this.M_IDX)
      }).subscribe(
        data => {
          this.myInfo = data;
          console.log("myInfo:");
          console.log(data);
          localStorage.setItem('M_NAME',this.myInfo[0].m_name);
          console.log("M_NAME appComponent : " +localStorage.getItem('M_NAME'));
        }
        );
      }      

      
    }

    noReadBox(cnt){
      console.log("appComponent 여기 드러오아" + cnt);
      this.noRead = cnt;
    }

    
    noRead:number = 0;
    noReadComm : any;
    getNoReadComm(){
      var self = this;
      self.noRead = 0;
      self._http.get(this.APP_URL + '/noReadComm', {
        params: new HttpParams().set('m_idx', self.M_IDX)
      }).subscribe(
        data => {
          console.log("getnoreadcomm : ");
          console.log(data);
          self.noReadComm = data;
          
          for(var i = 0 ; i < this.noReadComm.length ; i ++){
            self.noRead += this.noReadComm[i].noReadCnt;
          }
          console.log("this.noRead : " + this.noRead);

        },
        error => {
          console.log('Error occured', error);
        }
      );

      


    }

    closePage(){
      console.log("closePage() = this.roomNumoutpu : " + this.roomNumOutput);
      if(this.roomNumOutput !== 0){

      
      const edit={
        'chat_idx':this.roomNumOutput,
        'm_idx':this.M_IDX
      }
      this._http.get(this.APP_URL + '/exitChatRoom', {
        params: new HttpParams().set('edit', JSON.stringify(edit))
      }).subscribe(
        data => {
          console.log("data : " + data);
        }
      );
    }
    }


}
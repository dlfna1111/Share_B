import { Component, Output, EventEmitter, Input } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { AuthService, GoogleLoginProvider } from 'angular4-social-login';
import { HttpClient, HttpParams } from '@angular/common/http';
import { timer } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {


  readonly APP_URL = 'http://localhost:8080/Springmvcangular';
  ws: SockJS;

  @Input() myInfo;

  public roomNum(roomNum: number) {
    this.roomNumOutput = roomNum;
  }

  roomNumOutput: number;
  constructor(private _socioAuthServ: AuthService, private _http: HttpClient) {
    this.startSWOpen();
  }








// 웹소켓 관련--------------------------------------------------------------------------









  wsValid = true;
  ngOnInit() {
    if(this.wsValid === true){

      this.isRunning = true;
      if(this.isRunCnt > 1){
        this.time = 60000*3;
        console.log("여기들어옴?");
      }
      this.findPwdAuth();
    }
  }

  M_IDX: string = localStorage.getItem("m_idx");
  startSWOpen() {
    this.ws = SockJS(this.APP_URL + "/echo");
    this.ws.onopen = function (event) {
      
    };
    
  }
  
  isRunning = false;
  time:number = 5000;
  authTime;
  isRunCnt = 0;
  // 3분 시간 초 세기
  findPwdAuth(){
    this.isRunCnt ++;
    console.log('this.isRunCnt : '+this.isRunCnt);
    console.log('this.isRunning'+this.isRunning);
    timer(0, 1000).subscribe(ellapsedCycles => {
      if(this.isRunning) {
        this.time -= 1000;
        if(this.time == 0){
          var msg = {
            "type": "header",
            "m_idx": this.M_IDX,
          };
          this.ws.send(JSON.stringify(msg));

          this.wsValid = false;
        }
      }
    });
  }


















  @Output() private pageShow = new EventEmitter<string>();
  emitData = '';
  projectShowBox(data) {
    this.pageShow.emit(data);
  }

  mypageShowBox() {
    this.emitData = "mypage";
    this.projectShowBox(this.emitData);
  }

  // 로그아웃
  logOut() {

    this.emitData = "login";
    localStorage.removeItem('m_idx');
    this.projectShowBox(this.emitData);
  }

  public toggleText: string = "Hide";
  aa: string = "";
  private show: boolean = false;

  public chatToggle(): void {
    this.show = !this.show;
    this.toggleText = this.show ? "Show" : "Hide";

    console.log("toggleText : " + this.toggleText);
    console.log("roomNumOutput : " + this.roomNumOutput);

    if (this.toggleText == "Hide" && this.roomNumOutput > 0) {
      var msg = {
        "lastRoomNum": this.roomNumOutput,
        "type": "chatBoxOFF",
      };
      console.log("갔나");
      this.ws.send(JSON.stringify(msg));
    }
  }


  user: any;
  // Method to log out.
  signOut(): void {
    this._socioAuthServ.signOut();
    this.user = null;
    this.emitData = "login";
    localStorage.removeItem('m_idx');
    this.projectShowBox('login');
  }










  

}









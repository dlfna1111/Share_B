import { Component, NgModule, OnInit, Input, Output, EventEmitter, ElementRef, ViewChild } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { timer } from "rxjs";
import { AuthService, GoogleLoginProvider } from 'angular4-social-login';
import { ResourceLoader } from '@angular/compiler';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  constructor(private _http: HttpClient, private _socioAuthServ: AuthService) {

  }
  ngOnInit() {
    // this.googleSDK();

  }

  readonly APP_URL = 'http://localhost:8080/Springmvcangular';
  @Output() private pageShow = new EventEmitter<string>();

  @ViewChild('loginRef', { static: true }) loginElement: ElementRef;
  auth2: any;








  bigPanel = '50%';
  bigpanelShow = true;
  aniBtn = '회원 가입'
  bigPanelBox() {
    this.bigpanelShow = !this.bigpanelShow;
    this.bigPanel = this.bigpanelShow ? '50%' : '0%';

    if (this.bigPanel == '0%') {
      this.aniBtn = '로그인';
    } else {
      this.aniBtn = '회원 가입';
    }
  }














  // 로그인 관련 ----------------------------------------------------------------------------------------------------
















  loginMail = '';
  loginPwd = '';
  // 로그인
  logIn() {

    if (!this.regExp.test(this.loginMail)) {
      return alert("이메일의 형식이 아닙니다.");
    }
    if (this.loginPwd.length < 8) {
      return alert("비밀번호는 8자 이상.");
    }

    const edit = {
      "m_email": this.loginMail,
      "pwd": this.loginPwd,
    }

    this._http.get(this.APP_URL + '/logIn', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        if (data[0] == 1) {

          // 여기서 이제 마이 페이지를 열어 주어야 한다.
          localStorage.setItem("m_idx", data[1]);
          this.pageShow.emit("mypage");
        } else if (data == 2) {
          return alert("아이디 또는 비밀번호가 틀립니다.");
        } else if (data == 3) {
          return alert("인증이 안된 이메일입니다. 인증을 끝내세요.");
        }
        location.href="http://localhost:4200/";
      }
    );
    this.loginMail = '';
    this.loginPwd = '';
    
    // 리로드 하는 이유는, 헤더를 app.component에다가 넣어 놨기 때문에 로그아웃을 해도
    // construct, ngonit 등 모두 재로드가 안된다. 다시 refresh 해줘야 한다. 
  }

  findEmail;
  m_pwdShow = 0;
  // 비밀번호 찾기
  findPwd(authInputNum) {

    if (authInputNum === this.authData[0]) {
      this.m_pwdShow = 1;
    } else {
      this.m_pwdShow = 2;
    }
  }

  findPwdShow = false;
  isRunning = false;
  time: number = 60000 * 3;
  authTime;
  isRunCnt = 0;
  // 3분 시간 초 세기
  findPwdAuth() {
    this.isRunCnt++;
    timer(0, 1000).subscribe(ellapsedCycles => {
      if (this.isRunning) {
        this.time -= 1000;
      }
    });
  }

  authData: any;
  // 인증번호 보내는 
  sendAuthNum(email) {
    this.findEmail = email;

    if (!this.regExp.test(email)) {
      return alert("이메일의 형식이 아닙니다.");
    }

    this._http.get(this.APP_URL + '/findpwd', {
      params: new HttpParams().set('m_email', email)
    }).subscribe(
      data => {
        // 인증번호를 받는다.
        console.log(data);
        this.authData = data;
      }
    );

    this.isRunning = true;
    if (this.isRunCnt > 1) {
      this.time = 60000 * 3;
    }
    this.findPwdAuth();
  }

  // x버튼
  findPwdBox() {
    this.findPwdShow = false;
  }

  // 비번찾기 창 뜨기
  findPwdBoxShow() {
    this.findPwdShow = !this.findPwdShow;
  }



















  // 구글 로그인 관련 ----------------------------------------------------------------------------------------------------













  user: any;
  M_IDX: string;
  info : any;
  // 구글 로그인
  singIn(platform: string): void {
    platform = GoogleLoginProvider.PROVIDER_ID;
    this._socioAuthServ.signIn(platform).then(
      (response) => {
        console.log(platform + " logged in user data is= ", response);
        this.user = response;
        localStorage.setItem('GPhoto', this.user.photoUrl);
        const edit = {
          "m_name": response.name,
          "m_email": response.email,
          "pwd": response.id,
          "m_auth": 'check',
        }


        
        // db가서 로그인 확인하고 m_idx 가져오기
        this._http.get(this.APP_URL + '/googleLogin', {
          params: new HttpParams().set('edit', JSON.stringify(edit))
        }).subscribe(
          data => {
            console.log(data);
            this.info = data;
            localStorage.setItem("m_idx", data[0].m_idx);
            if(this.M_IDX != null){
              
              localStorage.setItem('page', 'mypage');
              this.pageShow.emit("mypage");
            }else{
              localStorage.setItem("m_idx", data[0].m_idx);
              localStorage.setItem('page', 'mypage');
              this.pageShow.emit("mypage");
              // 리로드 하는 이유는, 헤더를 app.component에다가 넣어 놨기 때문에 로그아웃을 해도
              // construct, ngonit 등 모두 재로드가 안된다. 다시 refresh 해줘야 한다. 
              location.href="http://localhost:4200/";
            }
    

          }
        );

      }, (error) => {
        alert(JSON.stringify(error, undefined, 2));
      });


      

  }












  // 회원 가입 부분------------------------------------------------------------------------------------------------------------

















  // 이메일 형식 정규식
  regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

  // 중복 체크
  multiCtn = 0;
  multiCheck(email) {

    if (!this.regExp.test(email)) {
      return alert("이메일을 확인하세요.");
    } else {

      this._http.get(this.APP_URL + '/multiCheck', {
        params: new HttpParams().set('m_email', email)
      }).subscribe(
        data => {
          console.log(data);

          if (data === 0) {
            this.multiCtn = 1;
            return alert("사용 가능한 이메일입니다.");
          } else if (data === 1) {
            return alert("이미 사용중인 이메일입니다.");
          }
        }
      );
    }
  }


  // 회원가입
  pwdInput;
  pwdInput1;
  lastSignUpShow = false;
  signUp(name, e_mail, pwd1, pwd2) {
console.log(name, e_mail, pwd1);
    // 인증 숫자
    const ranNum: string = '' + Math.floor(Math.random() * 1000000);

    // 중복 확인 후에
    if (this.multiCtn != 0) {

      const edit = {
        "m_name": name,
        "m_email": e_mail,
        "pwd": pwd1,
        "m_auth": ranNum
      }

      if (pwd1 != pwd2) {

        this.pwdInput = '';
        this.pwdInput1 = '';
        return alert("비밀번호가 일치하지 않습니다.");
      } else if (!this.regExp.test(e_mail)) {
        return alert("이메일을 확인하세요.");
      } else {

        if (pwd1.length < 8) {
          return alert("비밀번호는 8자 이상.");
        }

        this.bigPanelBox();
        // 회원가입 먼저 하기.
        this._http.get(this.APP_URL + '/signUp', {
          params: new HttpParams().set('edit', JSON.stringify(edit))
        }).subscribe(
          data => {
            console.log(data);
            this.multiCtn = 0;
            this.lastSignUpShow = true;
            // 이메일 보내기
            this._http.get(this.APP_URL + '/emailAuthSend', {
              params: new HttpParams().set('edit', JSON.stringify(edit))
            }).subscribe(
              data => {
                console.log(data);
                this.multiCtn = 0;
              }
            );

          }
        );

      }

    } else {
      return alert("이메일 중복 확인 해주세요.");
    }

    this.bigPanel = '50%';
  }



}

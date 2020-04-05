import { Component, OnInit, Input, Output, EventEmitter, HostListener, Renderer2 } from '@angular/core';
import * as animations from '@angular/animations';
import { HttpClient, HttpParams } from '@angular/common/http';
import * as SockJS from 'sockjs-client';
import { anyChanged } from '@progress/kendo-angular-common';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';

interface Contact {
  id: number;
  name: string;
  email: string;
  avatarUrl: string;
}

const CONTACTS_MOCK: Contact[] = new Array(5)
  .fill({})
  .map(
    (_c: Contact, i: number) =>
      ({
        id: i,
        name: `Contact ${i}`,
        email: `email${i}@provider.com`,
        avatarUrl: `https://api.adorable.io/avatars/100/${~~(Math.random() * 100)}`,
      } as Contact),
  )
  .reverse(); // * to have them sorted in DESC order

@Component({
  selector: 'app-chat-box',
  templateUrl: './chat-box.component.html',
  styleUrls: ['./chat-box.component.css'],
  animations: [
    animations.trigger('noEnterAnimation', [animations.transition(':enter', [])]),
    animations.trigger('listItemAnimation', [
      animations.transition(':enter', [
        animations.style({ height: '0px', overflow: 'hidden' }),
        animations.group([animations.animate('250ms ease-out', animations.style({ height: '!' }))]),
      ]),
      animations.transition(':leave', [
        animations.style({ height: '!', overflow: 'hidden' }),
        animations.group([animations.animate('250ms ease-out', animations.style({ height: '0px' }))]),
      ]),
    ]),
    animations.trigger('sideContentAnimation', [
      animations.transition(':enter', [
        // we set the width of the outer container to 0, and hide the
        // overflow (so the inner container won't be visible)
        animations.style({ width: '0px', overflow: 'hidden' }),
        animations.group([
          // we animate the outer container width to it's original value
          animations.animate('250ms ease-out', animations.style({ width: '!' })),
          // in the same time we translate the inner element all the
          // way from left to right
          animations.query('.side-list-content-data-inner', [
            animations.style({ transform: 'translateX(-110%)' }),
            animations.group([animations.animate('250ms ease-out', animations.style({ transform: 'translateX(-0%)' }))]),
          ]),
        ]),
      ]),
      animations.transition(':leave', [
        animations.style({ overflow: 'hidden' }),
        animations.group([
          animations.animate('250ms ease-out', animations.style({ width: '0' })),
          animations.query('.side-list-content-data-inner', [
            animations.style({ transform: 'translateX(-0%)' }),
            animations.group([animations.animate('250ms ease-out', animations.style({ transform: 'translateX(-110%)' }))]),
          ]),
        ]),
      ]),
    ]),
    animations.trigger('emptyContentAnimation', [
      animations.transition(':leave', [
        animations.style({ opacity: '1' }),
        animations.group([animations.animate('250ms ease-out', animations.style({ opacity: '0', width: '0px' }))]),
      ]),
      animations.transition(':enter', [
        animations.style({ opacity: '0', width: '0px' }),
        animations.group([animations.animate('250ms ease-in', animations.style({ opacity: '1', width: '!' }))]),
      ]),
    ]),
  ],
})




export class ChatBoxComponent implements OnInit {
  @Input() ws: SockJS;
  noReadCnt: number;

  constructor(private _http: HttpClient, private renderer: Renderer2) {
    this.getAllChatInfos();
    let myItem = localStorage.getItem("m_idx");
    console.log("m_idx :"+ myItem);
  }

  messages: string[] = [];

  m_idx:number;

  ngOnInit() {
    this.onMessagea();
    this.m_idx = +localStorage.getItem("m_idx");

  }

  @Output() private noRead = new EventEmitter<number>();

    

  // 메세지 오면 여기로 온다.
  onMessagea() {
    const time = new Date();
    var nowHour = time.getHours();
    var nowMt = time.getMinutes();
    var getTime = '';
    var self = this;
    this.ws.onmessage = function onMessage(event) {
      if (nowHour <= 12 && nowHour >= 0) {
        getTime = '오전' + nowHour + ':' + nowMt;
      } else if (nowHour >= 13 && nowHour < 24) {
        getTime = '오후' + nowHour + ':' + nowMt;
      };

      self.receiveTmp = "" + event.data;
      self.receiveMSG = self.receiveTmp.split("/");
      
      // 채팅창에 들어와 있는 상태
      if(self.receiveMSG[4] === 'onLine'){
        const msgInfo =
        {
          "m_idx": self.receiveMSG[2],
          "chat_content": self.receiveMSG[1],
          "chat_date": getTime,
          "m_name": self.receiveMSG[0],
          "chat_idx": self.receiveMSG[3],
        };

        self.chatRoomInfos.push(msgInfo);
        
        // 칸반엔 들어와있고, 채팅은 off인 상태
      }else if(self.receiveMSG[4] === 'onOff'){
        this.noReadCnt ++;
        let IDX = self.chatInfos.findIndex(i => i.chat_idx == self.receiveMSG[3]);
        self.chatInfos[IDX].noReadCnt ++;

        // 여기서 app.ts의 noRead 변수를 올려줘야 한다.
        let readCnt = 0;
        for(var i = 0 ; i < self.chatInfos.length ; i ++){
          readCnt += self.chatInfos[i].noReadCnt;
        }
        self.noRead.emit(readCnt);

        console.log("noReadCnt " + readCnt); 
      }else if(self.receiveMSG[0] === 'offLine'){
        console.log("오프라인 들어옴?");
      }

    }
  }

  roomNum: 0;
  myType = "";
  beforeRoomNum = 0;

  readonly APP_URL = 'http://localhost:8080/Springmvcangular';


  receiveTmp = '';
  receiveMSG: string[];

  // 메시지 보내는 곳.
  sendMSG(angMsg) {

    const time = new Date();
    var nowHour = time.getHours();
    var nowMt = time.getMinutes();
    var getTime = '';
    // chat_date, m_name 은 java에서 session으로 가져와서 넣기.
    // 웹소켓 시간이랑 db 저장 될때의 시간이 다르게 저장될 것 같다?
    // 메시지 보낼때 웹소켓으로도 보내고, db로도 보내면 되니까. 
    // 시간을 여기서 정하고 같은 시간을 보내야 할듯.
    if (nowHour <= 12 && nowHour >= 0) {
      getTime = '오전' + nowHour + ':' + nowMt;
    } else if (nowHour >= 13 && nowHour < 24) {
      getTime = '오후' + nowHour + ':' + nowMt;
    }
    var msg = {
      "chat_idx": this.roomNum,
      "m_idx": this.M_IDX,
      "chat_content": "운영자 : 메시지가 정상적으로 전송이 안되었습니다. 사이트에 문의하세요.",
      "m_name": this.M_NAME,
      "type": 'text',
      "chat_date": time,
      "pList": this.pList,
    };


    console.log("메세지 보내는 곳 this.M_NAME : " + this.M_NAME);
    msg.chat_content = angMsg;
    this.ws.send(JSON.stringify(msg));

    // 메세지 DB 저장
    // 모든 변수를 다 보내야 하나?
    this._http.get(this.APP_URL + '/saveMsg', {
      params: new HttpParams().set('edit', JSON.stringify(msg))
    }).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log('Error occured', error);
      }
    );
    // 입력창 리셋
    this.chatInput = '';

  }// sendMSG()















// 채팅방 관련 ----------------------------------------------------------------------------------------------

















  @Output() private roomNumOutput = new EventEmitter<number>();

  public roomNumAPP(roomNum) {
    this.roomNumOutput.emit(roomNum);
  }

  chatInputBox='';
  // 채팅방 생성
  addRoom(subject) {

    var addRoomInfos = {
      "chat_name": subject,
      "m_idx":this.M_IDX,
      "m_idxList": this.addmIdxList,
    };

    this._http.get(this.APP_URL + '/addChatRoom', {
      params: new HttpParams().set('addRoomInfos', JSON.stringify(addRoomInfos))
    }).subscribe(
      data => {
        this.chatInfos = data;
      },
      error => {
        console.log('Error occured', error);
      }
    );
      this.chatInputBox = '';
    this.addmIdxList = null;
    this.xBtn(0);

  }

  addChatDisplay = "none";
  addChatDisplay1 = "block";
  idCheck: any = "y";
  personIcon;

  addPersonListdisp = "none";
  addIdList = []; // 방생성 초대id목록
  addmIdxList=[];
  // 채팅방에 추가할 체크한 사람 목록
  addPersonList(person) {
    this.addmIdxList[0]=this.M_IDX;
    let personIndex: number = this.addChatIdList.findIndex(i => i.m_email === person);

    this.addChatIdList[personIndex].checked = this.addChatIdList[personIndex].checked == "checked" ? "" : "checked";

    if (this.addChatIdList[personIndex].checked == "checked") {
      this.addIdList.push(this.addChatIdList[personIndex].m_email);
      this.addmIdxList.push(this.addChatIdList[personIndex].m_idx);
    } else {
      this.addIdList.splice(this.addIdList.indexOf(this.addChatIdList[personIndex].m_email), 1);
      this.addmIdxList.splice(this.addIdList.indexOf(this.addChatIdList[personIndex].m_idx), 1);
    }

    if (this.addChatIdList.findIndex(i => i.checked === "checked") == -1) {
      this.addPersonListdisp = "none";
    } else {
      this.addPersonListdisp = "block";
    }

    


  }

  private show: boolean = false;
  chatRoomdispShow: boolean = false;
  addChatIdList: any;

  // xBtn
  xBtn(num) {
    // 채팅방생성 화면
    if (num == 0) {
      this.show = !this.show;
      this.addChatDisplay = this.show ? "block" : "none";
      this.addChatDisplay1 = this.show ? "none" : "block";

      // 채팅로비 화면
    } else if (num >= 1) {
      this.chatRoomdispShow = !this.chatRoomdispShow;
      this.chatShowBox = false;
      this.lobbyAddBoxShow = true;
      this.addChatDisplay1 = "block";
      this.addChatDisplay = "none";
    }

  }

  delCheckBoxDisp = "none";
  delCheckBoxDispShow = false;
  delBoxShowIf ='';
  delCheckBoxShow(chat_idx){
    this.doubleBtnCnt++;

    if( chat_idx === 1.1 ){
      this.doubleBtnCnt = 1;
    }

    console.log("나가기 눌렀을대 doubleBtnCnt : " + this.doubleBtnCnt);
    this.delCheckBoxDispShow = !this.delCheckBoxDispShow;
    this.delCheckBoxDisp = this.delCheckBoxDispShow ? "block" : "none";
    this.delBoxShowIf = chat_idx;
    if(this.doubleBtnCnt != 2){
      this.doubleBtnCnt = 1;
    }
  }





  // 룸 정보 변경, 초대, 삭제 
  roomModifyBar(chat_idx){
    console.log("chat_idx : " + chat_idx);
  }


  


  // 나가기 버튼, 룸클릭 이중 클릭 방지 변수
  doubleBtnCnt = 1;
  delIndex:number;
  // 채팅방 삭제
  delRoom(chatIdx) {

    const edit={
      'chat_idx':chatIdx,
      'm_idx':this.M_IDX
    }

    this._http.get(this.APP_URL + '/delChatRoom', {
      params: new HttpParams().set('edit', JSON.stringify(edit))
    }).subscribe(
      data => {
        if(data === 1 ){
          let selectRoomIndex: number = this.chatInfos.findIndex(i => i.chat_idx === chatIdx);
          this.chatInfos.splice(selectRoomIndex,1);
        }
      }
    );
    this.delCheckBoxDispShow = false;
    this.delCheckBoxDisp='none';
    this.doubleBtnCnt = 1;
  }

  chatShowBox = false;
  // 채팅방 생성 입력 박스 
  addRoomInputBox(num) {
    this.chatShowBox = false;
    this.lobbyAddBoxShow = true;
    this.addChatDisplay1 = 'none';
    this.addChatDisplay = 'block';
    this.xBtn(num);
    // 누굴 초대할지 목록 불러온다.
    this._http.get(this.APP_URL + '/getAddChatIds', {
      params: new HttpParams().set('m_idx', this.M_IDX)
    }).subscribe(
      data => {
        this.addChatIdList = data;
        console.log(data);
      },
      error => {
        console.log('Error occured', error);
      }
    );
  }

  lobbyAddBoxShow = true;
  selectedChatRoomInfos: any;
  chatRoomInfos: any;
  PersonList: any;
  pList:string = '';
  chatRoomInfosALL: any;

  roomSelectDisp = "none";
  roomSelectIf;

  
  M_NAME:string;
  // 채팅방 클릭하면 어떤방으로 입장했는지 echoHandler로 정보를 보내 방에 참여한다.
  roomClick(roomNum) {

    
    this.roomSelectIf = roomNum;
    this.roomSelectDisp="block";

    if (this.doubleBtnCnt === 1) {

      this.chatShowBox = true;
      this.lobbyAddBoxShow = false;
      let selectRoomIndex: number = this.chatInfos.findIndex(i => i.chat_idx === roomNum);

      this.selectedChatRoomInfos = this.chatInfos[selectRoomIndex];


      // 헤더의 알림 개수도 바꿔줘야 한다.
      let IDX = this.chatInfos.findIndex(i => i.chat_idx == roomNum);
      this.chatInfos[IDX].noReadCnt = 0;
      let readCnt = 0;
      for(var i = 0 ; i < this.chatInfos.length ; i ++){
        readCnt += this.chatInfos[i].noReadCnt;
      }
      this.noRead.emit(readCnt);
      
      // 룸으로 들어가기
      this.roomNum = roomNum;
      this.myType = 'roomIn';
      // echo로 들어가는 건 모두 send인데
      // type으로 소켓오픈인지, 메세지인지 등을 정한다.
      var msg = {
        "chat_idx": this.roomNum,
        "m_idx": this.M_IDX,
        "type": "roomIn",
        "beforeRoomNum": this.beforeRoomNum,
      };
  
      this.ws.send(JSON.stringify(msg));
      console.log("chat_idx : " + this.roomNum);
      
      // 룸의 정보들을 가지고 온다. 과거 메시지들.
      this._http.get(this.APP_URL + '/getRoomInfos', {
        params: new HttpParams().set('chat_idx', roomNum)
      }).subscribe(
        data => {
          this.chatRoomInfosALL = data;
          this.chatRoomInfos = this.chatRoomInfosALL[0]; // 채팅룸 정보 -> 여기에 이름이 있다. M_IDX랑 같은 이름을 찾으면 될듯?
          this.PersonList = this.chatRoomInfosALL[1]; // 채팅에 참여한 사람들의 m_idx
          
          this.pList = '';
          for(var i = 0 ; i < this.PersonList.length ; i++ ){
            this.pList += this.PersonList[i]+'/';
          }
          console.log("this.M_NAME : " + this.M_NAME);
        },
        error => {
          console.log('Error occured', error);
        }
      );


      
      if(this.beforeRoomNum != 0){
        const edit={
          'chat_idx':this.beforeRoomNum,
          'm_idx':this.M_IDX
        }
        // beforRoomNum 의 chat_group에서 outDate 날짜 현재로 바꾸기.
        this._http.get(this.APP_URL + '/exitChatRoom', {
          params: new HttpParams().set('edit', JSON.stringify(edit))
        }).subscribe(
          data => {
            console.log("data : " + data);
          }
        );
      }//if() 전에 들어간 방에서 나온 시간 기록
      // 순서 바뀌면 안됨
      this.beforeRoomNum = this.roomNum;
      this.roomNumAPP(roomNum);
      const edit={
        'chat_idx':roomNum,
        'm_idx':this.M_IDX
      }
      // 지금 들어간 방 시간 기록
      this._http.get(this.APP_URL + '/exitChatRoom', {
        params: new HttpParams().set('edit', JSON.stringify(edit))
      }).subscribe(
        data => {
          console.log("data : " + data);
        }
      );

    }//if문
    this.doubleBtnCnt = 1;
  }// roomClick()


  chatContent = "";
  chatInput = "";
  // sendMsg(msg) {

  //   this.chatInput = '';
  //   this.sendMSG(msg);
  // }

  close() {
    this.ws.close();
  }

  chatInfos: any;
  M_IDX = localStorage.getItem("m_idx");
  getAllChatInfos() {

    this._http.get(this.APP_URL + '/chatInfos', {
      params: new HttpParams().set('m_idx', this.M_IDX)
    }).subscribe(
      data => {
        console.log("cahtInfos : ");
        console.log(data);
        this.chatInfos = data;
        this.M_NAME = localStorage.getItem('M_NAME');
      },
      error => {
        console.log('Error occured', error);
      }
    );

  }

  
  



  // contactList: Contact[] = CONTACTS_MOCK;

  // onAddItem() {
  //   const rndNum = Date.now();
  //   const newContact: Contact = {
  //     id: this.contactList.length * rndNum,
  //     name: `Contact ${this.contactList.length}`,
  //     email: `email${this.contactList.length}@provider.com`,
  //     avatarUrl: `https://api.adorable.io/avatars/285/${rndNum}`,
  //   };
  //   // * adding a new contact to the list
  //   this.contactList = [newContact, ...this.contactList];
  //   // * selecting the newly created contact
  //   this.onSelectItem(newContact);
  // }

  // onDeleteItem(contact: Contact) {
  //   // * removing a contact from the list
  //   this.contactList = this.contactList.filter(c => c.id != contact.id);
  //   if (this.selectedContact && this.selectedContact.id == contact.id) {
  //     // * if the removed contact is beaing focused on, then we remove the focus
  //     this.onSelectItem(null);
  //   }
  // }
}

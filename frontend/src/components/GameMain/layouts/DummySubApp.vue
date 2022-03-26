<template>
  <div id="app">
    <button @click="connect">
      연결
    </button>
    <button @click="startGame">
      게임시작
    </button>
    <button @click="dealMemberChoice">
      거래 멤버 선택
    </button>
    <button @click="dealMemberFinalChoice">
      거래 멤버 최종 결정
    </button>
    <button @click="dealMemberFinalChoiceFailed">
      거래 멤버 최종 결정 실패!
    </button>
    <button @click="voteTrue">
      투표 찬성
    </button>
    <button @click="voteFalse">
      투표 거절
    </button>
    <button @click="disconnect">
      해제
    </button>
    유저이름:
    <input
      v-model="writer"
      type="text" />
    내용: <input
      v-model="message"
      type="text"
      @keyup="sendMessage" />
    귓속말:
    <input
      v-model="reader"
      type="text" />
    <div
      v-for="(item, idx) in recvList"
      :key="idx">
      <h3>유저이름: {{ item.writer }}</h3>
      <h3>내용: {{ item.message }}</h3>
    </div>
  </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";

export default {
  name: "App",
  data() {
    return {
      writer: "",
      reader: "",
      message: "",
      recvList: [],
      roomId: "room1",
      connected: false,
    };
  },
  created() {
    // 여기서 connect()하면 페이지 접속 시 연결 - 사전에 사용자 id 저장 필요
    // this.connect();
  },
  methods: {
    // 엔터를 눌러 메세지 전송
    sendMessage(e) {
      if (e.keyCode === 13 && this.writer !== "" && this.message !== "") {
        this.send();
        this.message = "";
      }
    },
    // 전체 채팅 or 귓속말 전송
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          writer: this.writer,
          message: this.message,
          roomId: this.roomId,
          reader: this.reader,
        };
        // 전체 채팅 전송
        if (this.reader == "") this.stompClient.send("/pub/chat/message", JSON.stringify(msg), {});
        // 귓속말 전송
        else this.stompClient.send("/pub/chat/whisper", JSON.stringify(msg), {});
      }
    },
    startGame() {
      console.log("gameStart");
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          writer: this.writer,
          message: this.message,
          roomId: this.roomId,
          reader: this.reader,
        };
        // 게임 시작 메세지 전송
        this.stompClient.send("/game/start", JSON.stringify(msg), {});
      }
    },
    dealMemberChoice() {
      console.log("dealMemberChoice");
      this.message = "3";
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          writer: this.writer,
          message: this.message,
          roomId: this.roomId,
          reader: this.reader,
        };
        console.log(msg.message);
        // 게임 시작 메세지 전송
        this.stompClient.send("/game/choice", JSON.stringify(msg), {});
      }
    },
    dealMemberFinalChoice() {
      console.log("dealMemberFinalChoice");
      let map = [[this.writer, 500]];
      map.push(["2", 300]);
      console.log(map);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          writer: this.writer,
          message: map,
          roomId: this.roomId,
          reader: this.reader,
        };
        console.log(msg.message);
        // 게임 시작 메세지 전송
        this.stompClient.send("/game/finalchoice", JSON.stringify(msg), {});
      }
    },
    dealMemberFinalChoiceFailed() {
      console.log("dealMemberFinalChoiceFailed");
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          writer: this.writer,
          message: "",
          roomId: this.roomId,
          reader: this.reader,
        };
        console.log(msg.message);
        // 게임 시작 메세지 전송
        this.stompClient.send("/game/finalchoice", JSON.stringify(msg), {});
      }
    },
    voteTrue() {
      console.log("voteTrue");
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          writer: this.writer,
          message: true,
          roomId: this.roomId,
          reader: this.reader,
        };
        console.log(msg.message);
        // 게임 시작 메세지 전송
        this.stompClient.send("/game/vote", JSON.stringify(msg), {});
      }
    },
    voteFalse() {
      console.log("voteFalse");
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          writer: this.writer,
          message: false,
          roomId: this.roomId,
          reader: this.reader,
        };
        console.log(msg.message);
        // 게임 시작 메세지 전송
        this.stompClient.send("/game/vote", JSON.stringify(msg), {});
      }
    },
    connect() {
      if (this.connected) return;
      // 서버에 연결 요청 - StompWebSocketConfig 참조
      const serverURL = "http://localhost:8080/stomp/chat";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          console.log("소켓 연결 성공", frame);

          // 서버의 전체 채팅 endpoint를 구독
          this.stompClient.subscribe("/sub/chat/room/" + this.roomId, (res) => {
            console.log("전체 메시지 : ", res.body);
            //전체 메세지네 ㄱㄱㄱ
            // 받은 데이터를 json으로 파싱하고 리스트에 저장 - 채팅 내역
            this.recvList.push(JSON.parse(res.body));
          });

          // 서버의 귓속말 endpoint를 구독
          this.stompClient.subscribe("/sub/chat/room/" + this.roomId + "/" + this.writer, (res) => {
            console.log("DM : ", res.body);
            //어 귓속말이네 ㄱㄱ
            // 받은 데이터를 json으로 파싱하고 리스트에 저장 - 채팅 내역
            this.recvList.push(JSON.parse(res.body));
          });

          // 게임 시작 메세지 구독
          // 게임 기본 정보를 받아 화면에 출력
          // 다음 메세지로 sub/game/round/{players}를 받음
          this.stompClient.subscribe("/sub/game/start/" + this.roomId, (res) => {
            console.log("게임 시작 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/jobs/" + this.roomId, (res) => {
            console.log("직업 분배 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/order/" + this.roomId, (res) => {
            console.log("순서 결정 or 순위 발표 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/broker/" + this.roomId, (res) => {
            console.log("브로커 전달 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/deal/" + this.roomId, (res) => {
            console.log("거래 조건 생성 : ", res);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/choice/" + this.roomId, (res) => {
            console.log("플레이어 실시간 선택 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/finalchoice/" + this.roomId, (res) => {
            console.log("플레이어 최종 선택 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/vote/" + this.roomId, (res) => {
            console.log("한 플레이어 투표 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/finalvote/" + this.roomId, (res) => {
            console.log("플레이어 투표 최종 완료 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/rank/" + this.roomId, (res) => {
            console.log("라운드 순위 반환 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/finalrank/" + this.roomId, (res) => {
            console.log("최종 순위 반환 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });
          this.stompClient.subscribe("/sub/game/log/" + this.roomId, (res) => {
            console.log("최종 로그 반환 : ", res.body);
            //
            //
            this.recvList.push(JSON.parse(res.body));
          });

          // 처음 연결 시 접속 메세지 전송
          this.stompClient.send(
            "/pub/chat/enter",
            JSON.stringify({ roomId: this.roomId, writer: this.writer }, {})
          );
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },
    // 소켓 연결 해제
    disconnect() {
      if (this.connected) {
        // 연결 해제 메세지 전송
        this.stompClient.send(
          "/pub/chat/quit",
          JSON.stringify({ roomId: this.roomId, writer: this.writer }, {})
        );
        this.stompClient.disconnect();
        this.connected = false;
      }
    },
  },
};
</script>

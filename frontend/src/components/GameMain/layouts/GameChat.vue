<template>
  <div class="game-chat-write">
    <!-- 실제 채팅이 보여지는 창 -->
    <div
      class="game-chat-log"
      ref="recvList">
      <div
        v-for="(item, idx) in recvList"
        :key="idx">
        <!-- 귓속말 할 때 발신자와 수신자에게 보이는 메세지 -->
        <div v-if="item.reader == 'i208'">
          {{ item.writer }} 님이 모두에게 :
        </div>
        <!-- player가 입장/퇴장할 때 보이는 알림 -->
        <div
          v-else-if="item.reader == 'enter208'"
          style="color: #ffc107">
          📢 장사꾼 입/퇴장 알림
        </div>
        <!-- 전체채팅 할 때 모두에게 보이는 메세지 -->
        <div v-else>
          {{ item.writer }} 님이 {{ item.reader }} 님에게 :
        </div>
        <!-- 입력한 메세지 내용 -->
        <span> {{ item.message }} </span>
      </div>
    </div>

    <!-- 귓속말을 보낼 유저를 선택하는 select 창 -->
    <section class="not-game-chat-log">
      <span>귓속말을 보낼 장사꾼을 선택하세요.</span>
      <select
        class="form-select mb-3 mt-2"
        v-model="reader"
        aria-label="Default select example">
        <option
          selected
          disabled>
          귓속말을 보낼 장사꾼을 선택하세요.
        </option>
        <option :value="'i208'">
          모두에게
        </option>
        <option
          v-for="gameplayer in whisperPeople"
          :key="gameplayer"
          :value="gameplayer">
          {{ gameplayer }}
        </option>
      </select>

      <!-- 채팅 입력하는 input -->
      <div class="mb-3 input-content">
        <span>내용:</span>
        <input
          class="form-control"
          aria-label="default input example"
          v-model="message"
          type="text"
          @keyup="sendMessage" />
        <!-- 채팅 전송하는 버튼 -->
        <button
          type="button"
          class="btn btn-outline-warning chat-send-btn"
          @click="sendMessage">
          전송
        </button>
      </div>
    </section>
  </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mapActions } from "vuex";

export default {
  data() {
    return {
      writer: this.$store.getters.nickName,
      reader: 'i208',
      message: "",
      recvList: [],
      selected: null,
      // 여기에 플레이어 이름을 받아오기
      players: [],
      roomId: this.$route.params.code,
      player: this.player,
      host: '',  
      
      // 게임 체결여부 반환 
      playerResult: '',
    };
  },
  computed: {
    // 나를 제외한 다른 사람에게 귓속말이 가능하게 필터링
    whisperPeople: function () {
      return this.players.filter((player) => player != this.writer);
    },
  },

  //stomp Chat system
  created() {
    // 여기서 connect()하면 페이지 접속 시 연결 - 사전에 사용자 id 저장 필요
    this.connect();
    // this.emitter.on('connect', this.connect)
    // 채팅 나갔을 때 끊어짐
    this.emitter.on("chat_disconnect", this.disconnect);
  },
  mounted() {
    this.emitter.on("IamHost", this.gameStart);
    this.emitter.on('sendConclusion', isSuccess => this.sendConclusion(isSuccess));
    this.emitter.on('vote', vote => this.sendVote(vote));
    this.emitter.on('sendVoteSuccess', isVoted => this.sendVote(isVoted));
  },
  updated() {
    // 새로운 채팅이 입력되면 스크롤 하단으로 update
    let objDiv = this.$refs.recvList;
    objDiv.scrollTo({ top: objDiv.scrollHeight, behavior: "smooth" });
  },
  methods: {
    ...mapActions(["setPlayerJob", "setUserOrder","setBroker","setVoter","setConclusion","setMyMoney", "setDealConditions",]),
    setMyMoneys(mymoney){
      this.setMyMoney(mymoney);
    },
    sendVote(vote){
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          writer: this.writer,
          message: vote,
          roomId: this.roomId,
          reader: this.reader,
          player: this.player,
        };
        console.log("Send Vote:" + this.msg);
        this.stompClient.send("/game/vote", JSON.stringify(msg), {});
      }
      this.setVoters(false);
    },    
    sendConclusion(isSuccess) {
      console.log("Send Conclusion");
      //store의 플레이어별 닉네임과 결정금액을 이중 배열로 만들어 send
      if(isSuccess){
        var playerDealAmount = [];
        console.log(this.$store.getters.userNickName);
        var userNickNames = this.$store.getters.userNickName;
        var userPrices = this.$store.getters.userPrice;
        var n = userNickNames.length;
        console.log(userNickNames);
        //본인제외 닉네임과 거래금액 넣기
        for(var i=0; i < n; i++){
          if(userPrices[i] != 0){
            var tmp = [userNickNames[i],userPrices[i]];
            playerDealAmount.push(tmp);
          }
        }
        //본인 닉네임과 거래금액 넣기
        var myDeal = [this.$store.getters.nickName, this.$store.getters.dealPrice];
        playerDealAmount.push(myDeal);
        if (this.stompClient && this.stompClient.connected) {
          const msg = {
            writer: this.writer,
            message: playerDealAmount, // 이중배열 담아야함
            roomId: this.roomId,
            reader: this.reader,
            player: this.player,
          };
          // 전체 채팅 전송
          this.stompClient.send("/game/finalchoice", JSON.stringify(msg), {});
        }
      }else{
        if (this.stompClient && this.stompClient.connected) {
          const msg = {
            writer: this.writer,
            message: "",
            roomId: this.roomId,
            reader: this.reader,
            player: this.player,
          };
          // 전체 채팅 전송
          this.stompClient.send("/game/finalchoice", JSON.stringify(msg), {});
        }
      }
      
    },
    setConclusions(conclusion){
      this.setConclusion(conclusion);
    },
    setOrder(order) {
      this.setUserOrder(order);
    },
    // setBrokers(isBroker){
    //   console.log("setBrokers!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", isBroker)
    //   this.setBroker(isBroker);
    // },
    setVoters(isVoter){
      this.setVoter(isVoter);
    },
    // 엔터를 눌러 메세지 전송
    sendMessage(e) {
      if (e.keyCode === 13 && this.message !== "") {
        this.send();
        this.message = "";
        // 전송버튼 눌러서 메세지 전송
      } else if (e.type === "click" && this.writer !== null && this.message !== "") {
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
          player: this.player,
        };
        // 전체 채팅 전송
        if (this.reader == "i208") {
          this.stompClient.send("/pub/chat/message", JSON.stringify(msg), {});
        }
        // 귓속말 전송
        else {
          this.player = this.reader;
          this.stompClient.send("/pub/chat/whisper", JSON.stringify(msg), {});
          this.recvList.push(msg);
        }
      }
    },
    connect() {
      // 서버에 연결 요청 - StompWebSocketConfig 참조
      const serverURL = "https://i6b208.p.ssafy.io/stomp/chat";
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

            this.recvList.push(JSON.parse(res.body));
          });

          // 서버의 귓속말 endpoint를 구독
          this.stompClient.subscribe("/sub/chat/room/" + this.roomId + "/" + this.writer, (res) => {
            console.log("DM : ", res.body);

            this.recvList.push(JSON.parse(res.body));
          });

          // 누가 입장?
          // <<< {"roomId":"room1","writer":"sooyong","reader":null,"message":null}
          this.stompClient.subscribe("/sub/chat/enter/" + this.roomId, (res) => {
            var str = JSON.parse(res.body);
            console.log("enter : ", str);
            console.log("enter : ", str["writer"]);
            str["message"] = str["writer"] + "님이 입장하셨습니다.";
            this.recvList.push(str);
            this.reader = 'i208';
          });

          // 현재 플레이어들의 리스트(첫 번째 플레이어가 호스트)
          this.stompClient.subscribe("/sub/chat/players/" + this.roomId, (res) => {
            console.log("players : ", res.body);
            // player 받아서 push
            // res.body 초기화 해버리기!!!!!

            var str = JSON.parse(res.body);
            this.players = str;
            console.log(str);
            this.emitter.emit("hostNPlayers", str);
          });
          // 누가 퇴장?
          this.stompClient.subscribe("/sub/chat/quit/" + this.roomId, (res) => {
            console.log("quit : ", res.body);
            var str = JSON.parse(res.body);
            str["message"] = str["writer"] + "님이 퇴장하셨습니다.";
            this.recvList.push(str);
          });

          // 게임 관련 subscribe - "/sub/game/{endpoint}"

          // 게임 기본 정보를 받아 화면에 출력
          // 백에서 현재 인원수가 4보다 적다면 null을 반환
          // 다음 메세지로 sub/game/round/{players}를 받음
          this.stompClient.subscribe("/sub/game/start/" + this.roomId, (res) => {
            // console.log("게임 시작 : ", res.body);
            // 1. res.body 확인 후 GameDTO가 잘 왔다면 채팅창 or 게임 로그에 "게임을 시작합니다" 출력

            console.log("resbody : " + res.body);
            var str = JSON.parse(res.body)
            if (res.body != null) {
              // console.log("resbody가 GameDTO")
              this.emitter.emit("gameStarted");
              console.log("초기 자금 : " + str['playerList'][0]['money']);
              this.setMyMoneys(str['playerList'][0]['money']);
              // this.setMyMoneys(2000);
              
              //1. 게임로그에 메세지 띄우라고 emit
              this.emitter.emit('logGameStart','게임이 시작되었습니다 :) 1등을 향해')
              //2. 게임 화면 구성하는 메서드를 실행해달라고 emit
              this.emitter.emit('gameStarted');
            }
            // 안해도 돼
            // 2. null이 왔다면 인원이 부족하다는 알림 출력
            // this.recvList.push(JSON.parse(res.body));
          });

          // 현재 라운드의 플레이어별 능력을 반환
          this.stompClient.subscribe("/sub/game/jobs/" + this.roomId, (res) => {
            console.log("직업 분배 : ", res.body);

            // 1. res.body 확인 후 게임 로그에 "이번 라운드의 능력을 분배합니다" 등 출력
            this.emitter.emit("logAbilityShow", "플레이어들의 능력을 분배하겠습니다!")

            console.log("================== 각 res.body 출력 =================")
            var playerJob = JSON.parse(res.body);
            this.setPlayerJob(playerJob)
            // console.log("str: " + str);
            // console.log("str[0]: " + str[0]);  // object object
            // console.log("str[1].nickName: " + str[1].nickName);  // null
            // console.log("str[2].jobs: " + str[2].jobs);  // 선박, 로비
            // console.log("str[3].jobs[0]: " + str[3].jobs[0]);  // 인맥

            this.emitter.emit("initJobs", playerJob);

            // 2. 플레이어별 능력 컴포넌트??에 이를 반영
            // this.recvList.push(JSON.parse(res.body));
          });

          // 현재 라운드의 순서를 반환. 1라운드 : 랜덤, 2~3라운드 : 소지 금액 순
          this.stompClient.subscribe("/sub/game/order/" + this.roomId, (res) => {
            console.log("순서 결정 : ", res.body);

            var order = JSON.parse(res.body);
            var orderString = "";
            console.log("첫째 순번 : " + order[0]);
            orderString += "현재 라운드의 순서는 ";
            var imoji = ["1️⃣","2️⃣","3️⃣","4️⃣"];
            for (var i = 0; i < order.length; i++) {
              console.log(order[i]);
              // 1. res.body 확인 후 게임 로그에 "{round} 라운드의 순서입니다. ~~" 등 출력
              // 2. 백엔드에서 매 턴 Player형객체로 브로커를 보내줍니다 
              // 형식 : ["3","4","2","1"]
              this.emitter.emit('logRoundBrokerOrder', imoji[i] + '번째 브로커는 '+order[i]+'님입니다.')
              orderString += order[i];
              if(i!=order.length-1){
                orderString += " - "
              }
            }
            orderString += " 입니다."
            this.setOrder(order);
            
            this.emitter.emit('initOrder', orderString); //로그 쪽에서 emitter.on으로 받기
          });

          // 현재 턴의 브로커 닉네임을 반환
          this.stompClient.subscribe("/sub/game/broker/" + this.roomId, (res) => {
            // 1. 백엔드에서 매 턴 Player형 객체로 브로커를 보내줍니다
            // 2. res.body 확인 후 게임 로그에 "이번 턴의 브로커는 {nickname}입니다" 등 출력
            var broker = JSON.parse(res.body)['nickName']; //res.body['nickName']
            // console.log("브로커 전달 : ", broker);
            var brokerString = "💡이번 턴의 브로커는 " + broker + "님입니다.💡";
            this.emitter.emit('logRoundBroker', brokerString);
            if(this.$store.getters.nickName == broker){
              this.setBroker(true);
            }
          });

          // 현재 턴의 거래 조건을 반환
          // 거래조건이란? 브로커를 포함한 인원수, 필요 능력, 거래 금액으로 이루어진 구조체
          // 4명 기준 : 필요능력 2~3, 인원수 2~3, 거래 금액 1000~2000
          // 5명 기준 : 필요능력 2~3, 인원수 2~3, 거래 금액 1000~2000
          // 6명 기준 : 필요능력 2~4, 인원수 2~4, 거래 금액 1000~2000
          // ※ 거래에 필요한 인원수에 따른 거래 금액 보너스가 있음(3명 +200, 4명 +300)
          this.stompClient.subscribe("/sub/game/deal/" + this.roomId, (res) => {
            console.log("거래 조건 생성 : ", res);
            // 1. res.body 확인 후 게임 로그와 테이블에 현재 거래 조건을 출력
            // 2. 일정 시간 후 or 바로 타이머를 작동시킴
            // 3. 사전에 브로커로 지정된 플레이어에게 클릭 권한...같은걸 주고 입력 받음

            var deal = JSON.parse(res.body);
            // console.log("거래 금액 : " + deal.dealMoney);
            // console.log("필요 인원수 : " + deal.playerCount);
            // console.log("필요 능력 : " + deal.chosenJobs);
            
            this.emitter.emit('startTimer', 45);
            this.setDealConditions(deal)
            // this.recvList.push(JSON.parse(res.body));
            // 게임 조건입니다.
          });

          // 브로커의 선택을 실시간으로 반환
          // 이를 통해 모든 플레이어가 브로커의 선택과 동기화될 수 있음
          this.stompClient.subscribe("/sub/game/choice/" + this.roomId, (res) => {
            console.log("플레이어 실시간 선택 : ", res.body);
            // 1. res.body로 플레이어 객체가 오면, 해당 플레이어를 클릭했다는 뜻
            // 2. 따라서, 각 프론트에서는 사용자별 boolean 배열을 갖고 있다가 이를 반영하여 화면 결정

            console.log("여긴 글자 그대로 nickname 입니다 : " + res.body);

            

            // this.recvList.push(JSON.parse(res.body));
          });

          // 브로커의 최종 선택 --> 거래를 원하는 플레이어를 모두 선택후 결정하는 버튼에 연결
          // 거래를 원하는 플레이어를 이차원 리스트로 저장하여 전송해줘야함 List<List<Object>>
          this.stompClient.subscribe("/sub/game/finalchoice/" + this.roomId, (res) => {
            console.log("플레이어 최종 선택 : ", res.body);
            // 1. res.body로 투표에 참여한 플레이어 목록이 오면 해당 플레이어들에게 투표창을 띄워줌
            // {"dealMoney":1400,"playerCount":2,
            //"chosenJobs":["로비","정보","인맥"],
            //"vote":{"1":true,"2":false},
            //"dealAmount":{"1":0,"2":200},
            //"roundMoney":[200,300,400],
            //"voteOK":true,"dealOk":false,"dealSuccess":false}
            var deal = JSON.parse(res.body);
            console.log("deal.dealAmount", deal.dealAmount);
            
            console.log("플레이어 최종 선택 deal : " + deal["dealAmount"])
            if(deal == undefined || deal["dealAmount"] == undefined || deal["dealAmount"].length == 0) {
              console.log("제안 실패 : " + deal);
              this.setBroker(false);
              this.emitter.emit('initFinalChoice', deal.writer + "님이 거래 제안에 실패하셨습니다. 다음 거래로 넘어갑니다.");
            } else {
              console.log("제안한 금액 : " + deal.dealAmount); // 요건 map처럼 되어 있어서
              var dealLogString = "💸💸💸 이번 거래에 ";//{"1":300,"2":200}

              for(var nickName in deal.dealAmount){
                // console.log(deal.dealAmount[key]);
                if(nickName == this.$store.getters.nickName){
                  console.log(nickName + " " + this.$store.getters.nickName);
                  this.setVoter(true);
                }
                
                dealLogString += nickName + "(이)가 "              
                dealLogString += deal.dealAmount[nickName] + "만원 ";
              }  

              dealLogString += "으로 참여합니다.💸💸💸"
              console.log(dealLogString);
              this.emitter.emit('logDealState', dealLogString);
              this.emitter.emit('startTimer', 15);//여기까진 확인
            }
          });

          // 투표창이 뜬 플레이어는 거래 찬성 or 거래 반대를 투표함
          // 플레이어 별 투표현황은 서버에서 집계되고 모든 플레이어가 투표완료시 다음 턴으로 진행
          this.stompClient.subscribe("/sub/game/vote/" + this.roomId, (res) => {
            console.log("한 플레이어 투표 : ", res.body);

            var player = JSON.parse(res.body);
            console.log("제안한 금액 : " + player[0]); // 투표한 플레이어 닉네임

            // 1. res.body에 투표를 완료한 플레이어를 전송 --> 어떤 선택을 했는지는 비밀
            this.emitter.emit('logVoteState', player[0] + '님께서 거래 성사 여부를 결정했습니다.')

            // this.recvList.push(JSON.parse(res.body));
          });

          // 투표인원이 거래인원과 같아지면 백에서 자동으로 투표를 완료시킵니다.
          this.stompClient.subscribe("/sub/game/finalvote/" + this.roomId, (res) => {
            console.log("플레이어 투표 최종 완료 : ", res.body);
            // 투표가 전원완료되면 res.body에 플레이어 별 분배금액을 담아보냄
            // 분배금액은 프론트에서 (본인금액+res.body의 분배금액) 하여 계산된 본인의 가격을 보여주시면됩니다.
            // finalvote가 수행되면 다음턴으로 넘어갑니다

            var deal = JSON.parse(res.body);

            if (deal.dealSuccess) {
              console.log("거래 체결 : " + deal.dealSuccess);
            } else {
              console.log("거래 결렬 : " + deal.dealSuccess);
            }

            var dealLogString2 = "❗❗❗ 이번 거래로 ";//{"1":300,"2":200}

            for(var nickName in deal.dealAmount){
                // console.log(deal.dealAmount[key]);
                if(nickName == this.$store.getters.nickName){
                  console.log(nickName + " " + this.$store.getters.nickName);
                  this.setVoter(true);
                  this.setMyMoneys(deal.dealAmount[nickName]);
                }
                
                dealLogString2 += nickName + "(이)가 "              
                dealLogString2 += deal.dealAmount[nickName] + "만원 ";
              }  
              
              dealLogString2 += "챙겨갑니다 ❗❗❗"
            if(deal.dealSuccess == true) {
              this.emitter.emit('logFinalVote',dealLogString2);
            } else {
              this.emitter.emit('logFinalVote',"❌❌ 이번 거래는 체결되지 않았습니다. ❌❌");
            }
            //store의 보유금액 동기화

            //자신의 브로커 boolean을 false로 만듬
            this.setBroker(false);
            //투표가 종료되며 voter자격을 false로 만듬
            this.setVoter(false);
            this.setConclusions(true);
            // this.recvList.push(JSON.parse(res.body));
          });

          // 해당 라운드의 순위 반환
          this.stompClient.subscribe("/sub/game/rank/" + this.roomId, (res) => {  // 여기에서 타이머 일시정지
            console.log("라운드 순위 반환 : ", res.body);

            // 게임 로그에 현재 순위 반환
            var rank = JSON.parse(res.body);
            console.log("라운드 순위 반환 : ", rank);
            console.log("현재 1등! : " + rank[0]);
            this.emitter.emit('logRoundWin', '현재 가장 갑부는' + rank[0]+ '입니다.')
            
            
            // 위에서 1등 정의, 나머지 순서 게임 로그에 반환
            for (var i = 1; i < rank.length; i++) {
              console.log(rank[i]);
              this.emitter.emit('logRoundRank', '현재 '+ (i + 1) +'번째 갑부는' + rank[i] + '입니다.')
            }

            this.emitter.emit('stopTimer', 100);
            // this.recvList.push(rank);
          });


          // !!!!!! GameEndPage에 나타낼 것 !!!!!!!! //
          // 최종 순위 반환
          this.stompClient.subscribe("/sub/game/finalrank/" + this.roomId, (res) => {
            console.log("최종 순위 반환 : ", res.body);

            var finalrank = JSON.parse(res.body);
            console.log("최종 1등! : " + finalrank[0].nickName);
            for (var i = 0; i < finalrank.length; i++) {
              console.log(i + 1 + "등의 닉네임 : " + finalrank[i].nickName);
              console.log(i + 1 + "등의 최종 금액 : " + finalrank[i].money);
            }
            this.emitter.emit('logFinalRank', res.body)
            // this.recvList.push(JSON.parse(res.body));
          });

          // 게임 로그 반환
          // 부트스트랩 으로 테이블 보여주기
          this.stompClient.subscribe("/sub/game/log/" + this.roomId, (res) => {
            console.log("최종 로그 반환 : ", res.body);

            var log = JSON.parse(res.body);
            if (log.length == 3) {
              // 3차원 배열로 게임 히스토리 출력
              console.log("로그 3차원 배열 : ");
              for (var round = 0; round < log.length; round++) {
                for (var turn = 0; turn < log[0].length; turn++) {
                  for (var playerIndex = 0; playerIndex < log[0][0].length; playerIndex++) {
                    console.log(
                      round +
                        "라운드 " +
                        turn +
                        "턴 " +
                        playerIndex +
                        "번째 플레이어 : " +
                        log[round][turn][playerIndex]
                    );
                  }
                }
              }
              // 3차원 로그 리스트 담아줌
              this.emitter.emit('FinalGameLog', log);
            } else if (log.length > 3) { // 플레이어 리스트 출력
              console.log("플레이어 리스트를 순서대로 반환 : " + log);
              for (var i = 0; i < log.length; i++) {
                console.log(i + 1 + "번째 플레이어 닉네임 : " + log[i]);
              }
              // 플레이어 담아줌
              this.emitter.emit('FinalGamePlayers', log);
            }

            // this.recvList.push(JSON.parse(res.body));
          });

          // 처음 연결 시 접속 메세지 전송
          this.stompClient.send(
            "/pub/chat/enter",
            JSON.stringify({ roomId: this.roomId, writer: this.$store.getters.nickName,reader:'enter208'}, {})
          );
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },
    ////////// gameStart /////////////
    gameStart() {
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

    // 소켓 연결 해제
    disconnect() {
      if (this.connected) {
        // 연결 해제 메세지 전송
        // player 나가면 빼주기
        this.stompClient.send(
          "/pub/chat/quit",
          JSON.stringify({ roomId: this.roomId, writer: this.writer, reader: 'enter208'}, {})
        );
        // this.players = this.players.filter(this.player => player != this.writer)
        this.players.splice(this.players.indexOf(this.writer), 1);
        this.stompClient.disconnect();

        this.connected = false;

        window.addEventListener("beforeunload", this.disconnect);
        this.$router.push({ name: "Home" });
      }
    },
  },
};
</script>

<style scoped>
.game-chat-write {
  font-size: 16px;
}

.game-chat-write .game-chat-log {
  width: 100%;
  height: 25vh;
  overflow-y: scroll;
  -ms-overflow-style: none;
  margin-bottom: 10px;
  padding: 10px;
  border: solid rgb(67, 66, 47);
  border-radius: 10px;
}

.game-chat-write .game-chat-log div {
  margin-top: 10px;
}

.game-chat-write .game-chat-log::-webkit-scrollbar {
  display: none;
}

.game-chat-write .form-control {
  display: inline-block;
  width: 80%;
}

.game-chat-write .input-content .chat-send-btn {
  margin-left: 15px;
  margin-bottom: 4px;
}

.game-chat-write .input-content span {
  margin-bottom: 5px;
  display: block;
}

.not-game-chat-log .form-select {
  width: 52%;
}
</style>

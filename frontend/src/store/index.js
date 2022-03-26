import { createStore } from "vuex";

export default createStore({
  state: {
    userId: null, // DB에서 유저 키값
    nickName: null,
    rankPoint: null,
    naverId: null,
    winAmount: null,
    gangAmount: null,
    proGangAmount: null,
    lastGenRoom: null,

    myMoney: 0,

    gamePossible: false,
    // turn마다
    turnPrice: 0,  // 매 턴마다 주어지는 가격, 변동 없음
    userPrice: [0, 0, 0, 0, 0], // [300, 500, 0, 0, 200]
    userNickName: ["", "", "", "", ""],
    userOrder: [],

    // 결정여부
    conclusion: true,

    // 가져와야하는 값
    host: null,
    
    playerJobs: {},
    
    dealPrice: 0,  // 브로커가 가져갈 가격, 변동 심함
    dealLimitPeople: 0,
    dealCondition: [],
    dealStateCount: {},

    broker: false,
    voter: false,

    isVoteClicked: false,
    isDealClicked: false,
  },
  getters: {
    // state 상태 가져오기
    userId: (state) => state.userId,
    nickName: (state) => state.nickName,
    rankPoint: (state) => state.rankPoint,
    naverId: (state) => state.naverId,
    winAmount: (state) => state.winAmount,
    gangAmount: (state) => state.gangAmount,
    proGangAmount: (state) => state.proGangAmount,
    lastGenRoom: (state) => state.lastGenRoom,
    gamePossible: (state) => state.gamePossible,
    conclusion: (state) => state.conclusion,
    broker: (state) => state.broker,
    voter: (state) => state.voter,
    userOrder: (state) => state.userOrder,
    myMoney: (state) => state.myMoney,

    turnPrice: state => state.turnPrice,
    userPrice: state => state.userPrice,
    dealPrice: state => state.dealPrice,
    dealCondition: state => state.dealCondition,
    dealStateCount: state => state.dealStateCount,
    userNickName: state => state.userNickName,
    playerJobs: state => state.playerJobs,
    dealLimitPeople: state => state.dealLimitPeople,
    isVoteClicked: state => state.isVoteClicked,
    isDealClicked: state => state.isDealClicked,

    isLogined: function (state) {
      return state.userId && state.naverId;
    },

    isDealPossible(state) { // 여기에 현재 선택된 인원 제한도 걸어두기
      return (
        state.dealStateCount["선박"] &&
        state.dealStateCount["언변"] &&
        state.dealStateCount["창고"] &&
        state.dealStateCount["인맥"] &&
        state.dealStateCount["정보"] &&
        state.dealStateCount["로비"]
      );
    },

    findMyJob: state => nickName => {
      return state.playerJobs[nickName]
    },
  },
  mutations: {
    // state 상태 변경, 동기적이어야 함
    SET_USER: function (state, userdata) {
      state.userId = userdata["userId"];
      state.nickName = userdata["nickName"];
      state.rankPoint = userdata["rankPoint"];
      state.naverId = userdata["naverId"];
      state.winAmount = userdata["winAmount"];
      state.gangAmount = userdata["gangAmount"];
      state.proGangAmount = userdata["proGangAmount"];
      state.lastGenRoom = userdata["lastGenRoom"];
    },
    SET_NICKNAME(state, nickName) {
      console.log("닉네임", nickName);
      state.nickName = nickName;
    },
    DEAL_STATE_COUNT_PLUS(state, selectdata) {
      const first_ability = selectdata["first_ability"];
      const second_ability = selectdata["second_ability"];
      const isUserSelected = selectdata["isUserSelected"];

      if (isUserSelected) {
        state.dealStateCount[first_ability] += 1
        state.dealStateCount[second_ability] += 1
      }
      else {
        state.dealStateCount[first_ability] -= 1
        state.dealStateCount[second_ability] -= 1
      }
      // console.log("SELECTED_EVENT", isUserSelected, state.dealStateCount)
    },
    CHANGE_GAME_POSSIBLE(state, flag) {
      state.gamePossible = flag;
    },
    SET_USER_ORDER(state, userOrder) {
      console.log(userOrder);
      state.userOrder = userOrder;
      console.log(state.userOrder);
      // console.log(state.userNickName)
    },
    SET_BROKER(state, broker) {
      state.broker = broker;
      console.log("현재 자신이 브로커인지 출력 : " + state.broker);
      // console.log(state.userNickName)
    },
    SET_VOTER(state, voter) {
      state.voter = voter;
      console.log("현재 자신이 투표자인지 출력 : " + state.voter);
      // console.log(state.userNickName)
    },
    SET_CONCLUSION(state, conclusion) {
      state.conclusion = conclusion;
      console.log("거래 제안 여부를 출력: " + state.conclusion);
      // console.log(state.userNickName)
    },
    SET_MY_MONEY(state, myMoney) {
      state.myMoney += myMoney;
      console.log("현재 보유 금액을 출력: " + state.myMoney);
    },
    // SET_PLAYER_JOB(state, jsonJob) {
    //   state.userNickName[index] = NickName
    //   // console.log(state.userNickName)
    // }
    SET_PLAYER_JOB(state, jobObject) {
      state.playerJobs = jobObject
    },
    SET_DEAL_CONDITIONS(state, deal) {
      state.turnPrice = deal.turnPrice;  // 거래 금액
      state.dealPrice = deal.turnPrice;
      state.dealLimitPeople = deal.dealLimitPeople;  // 필요 인원수
      state.dealCondition = deal.dealCondition;  // 필요 능력
      state.dealStateCount = deal.dealStateCount;

      state.isDealClicked = false;
      state.isVoteClicked = false;

      // console.log("딜", state.dealPrice, state.dealLimitPeople, state.dealCondition, state.dealStateCount)
    },
    SET_IS_VOTE_CLICKED(state, isVoteClicked) {
      state.isVoteClicked = isVoteClicked
    },
    SET_IS_DEAL_CLICKED(state, isDealClicked) {
      state.isDealClicked = isDealClicked
    },
    UPDATE_PRICE(state, pricedata) {
      const value = pricedata["value"]
      const index = pricedata["index"]

      state.dealPrice += value
      state.userPrice[index] -= value
    },
    SET_USER_NICKNAME(state, userdata) {
      const NickName = userdata["NickName"]
      const index = userdata["index"]

      state.userNickName[index] = NickName
      // console.log(state.userNickName)
    },
    // SET_PLAYER_JOB(state, jsonJob) {
    //   state.userNickName[index] = NickName
    //   // console.log(state.userNickName)
    // }
  },
  actions: {
    // mutations 호출, 비동기 가능
    setUser: function ({ commit }, userdata) {
      commit("SET_USER", userdata);
    },
    setNickName({ commit }, nickName) {
      commit("SET_NICKNAME", nickName);
    },
    userSelectEvent({ commit }, selectdata) {
      commit("DEAL_STATE_COUNT_PLUS", selectdata);
    },
    changeGamePossible({ commit }, flag) {
      commit("CHANGE_GAME_POSSIBLE", flag);
    },
    updatePrice({ commit }, pricedata) {
      commit("UPDATE_PRICE", pricedata);
    },
    setUserNickName({ commit }, userdata) {
      commit("SET_USER_NICKNAME", userdata);
    },
    setUserOrder({ commit }, userdata) {
      commit("SET_USER_ORDER", userdata);
    },
    setBroker({ commit }, broker) {
      commit("SET_BROKER", broker);
    },
    setVoter({ commit }, voter) {
      commit("SET_VOTER", voter);
    },
    setConclusion({ commit }, conclusion) {
      commit("SET_CONCLUSION", conclusion);
    },
    setMyMoney({ commit }, mymoney) {
      commit("SET_MY_MONEY", mymoney);
    },
    setPlayerJob({commit}, jsonJob) {
      const jobObject = {}
      jsonJob.forEach(data => {
        const nickName = data.nickName
        const jobs = data.jobs

        jobObject[nickName] = jobs
      })
      commit("SET_PLAYER_JOB", jobObject)
    },
    setDealConditions({commit}, deal) {
      const turnPrice = deal.dealMoney;  // 거래 금액
      const dealLimitPeople = deal.playerCount;  // 필요 인원수
      const dealCondition = deal.chosenJobs;  // 필요 능력

      const dealStateCount = {
        "선박": 1,
        "언변": 1,
        "창고": 1,
        "인맥": 1,
        "정보": 1,
        "로비": 1,
      };
      
      dealCondition.forEach(element => {
        dealStateCount[element] = 0
      });
      // console.log(turnPrice, dealLimitPeople, dealCondition, dealStateCount)
      commit("SET_DEAL_CONDITIONS", {turnPrice, dealLimitPeople, dealCondition, dealStateCount})
    },
    setIsVoteClicked({commit}, flag) {
      commit("SET_IS_VOTE_CLICKED", flag)
    },
    setIsDealClicked({commit}, flag) {
      commit("SET_IS_DEAL_CLICKED", flag)
    },
  },
  modules: {},
});

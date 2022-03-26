<template>
  <div id="game-rule-info">
    <!-- Button trigger modal -->
    <button
      type="button"
      class="btn rule-info"
      data-bs-toggle="modal"
      data-bs-target="#exampleModal">
      게임방법💡
    </button>

    <!-- Modal -->
    <div
      class="modal fade"
      id="exampleModal"
      role="dialog"
      tabindex="-1"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true">
      <div
        class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
        <div
          class="modal-content">
          <div class="modal-header">
            <h5
              class="modal-title"
              id="exampleModalLabel">
              🎲 막나가는 장사꾼 게임 방법 🎲
            </h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close">
            </button>
          </div>
          <div class="modal-body">
            <span>

              👥 게임인원: 4인 ~ 6인 <br />

              📢 라운드: 3라운드<br />

              🌱 기본자금 : 5천만원<br />

              💰 거래금액 : 라운드가 진행될수록 제시되는 거래금액은 더 커집니다.<hr />
            </span>
            <span>
              📢 게임 시작<br />

              A. 능력 분배 💪<br />

              창고, 인맥, 언변, 정보, 로비, 선박 중 2가지 능력을 매 라운드마다 부여받게 됩니다.<br />

              B. 거래 단계 🤝<br />

              - 각 플레이어는 브로커가 됩니다. <br />
            
              - 브로커는 주어진 정보 조건을 만족하는 능력을 가진 플레이어들을 선택하여 정해진 금액을 적절하게 분배해야 합니다.<br />

              - 선택받은 플레이어는 거래 참가 여부를 선택할 수 있습니다.<br />
              
              - <span style="color:red;">한 명이라도 거래를 거부한다면 해당 거래는 실패하게 됩니다. </span> <br />
              이때, 한 명만 배신한다면 이득을 얻을 수 있지만 <span style="color:red;">2명 이상 거부했다면 패널티</span>가 부과됩니다.<br />

              <b>✨마치 거래에 참가할 것처럼 속이고 들어가서 해당 거래를 무효로 만들어버릴 수도 있겠죠?🤭✨ </b><br />

              C. 순위 발표 👑<br />

              - 매 라운드가 끝이 나면 순위를 발표합니다. 🏆<br />
              
              - 게임이 완전히 종료되기 전까지 각 플레이어의 보유 금액은 비공개입니다.<br />

              - 이전 라운드에서 순위가 높은 플레이어부터 다음 라운드의 브로커가 되어 게임을 진행하게 됩니다.<br />
            </span>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal">
              닫기
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  // data() {
  //   return {
  //     modalShow: false
  //   }
  // }
}

  

</script>

<style scoped>
#game-rule-info {
  font-size: 18px;
  line-height: 1.8;
  font-family: 'Nanum Gothic', sans-serif;
}

.modal-title {
  font-size: 30px;
}

.rule-info {
  float: right;
  color: white;
  padding-top: 12px;
  padding-bottom: 0;
  margin-right: 19%;
}


/* .rule-info .material-icons {
  font-size: 30px;
  color: white;
} */


.modal-backdrop {
  z-index: -1;
}

.btn:focus,.btn:active {
   outline: none !important;
   box-shadow: none;
}

/* @media (min-width: 576px) */
.modal-dialog {
    max-width: 50%;
}
</style>
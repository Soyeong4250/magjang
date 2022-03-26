package B208.mag_jang.domain;

import java.util.*;

public class DealDTO {
    private int dealMoney = 0; // 조건 : 금액
    private int playerCount = 0; // 조건 : 인원수
    private List<String> chosenJobs;// 조건 : 능력

    private Map<String,Boolean> vote; //투표리스트
    private Map<String,Integer> dealAmount; //분배금액 리스트
    private boolean isDealSuccess;
    private final int[] roundMoney = {200,300,400};

    //생성자
    public DealDTO() {
        super();
        this.dealMoney = 0;
        this.chosenJobs = new ArrayList<>();
        this.vote = new HashMap<>();
        this.dealAmount = new HashMap<>();
        this.isDealSuccess = false;
    }

    public DealDTO(int count, List<String> chosenJobs) {
        super();
        this.vote = new HashMap<>();
        this.dealAmount = new HashMap<>();
        this.isDealSuccess = false;
        this.dealMoney = 0;
        this.playerCount = count;
        this.chosenJobs = chosenJobs;
    }

    //투표리스트값넣기
    public void addVote(String nickname,boolean isVote) {
        this.vote.put(nickname, isVote);
    }
    //체결여부확인 - 투표 다했니?
    public boolean isVoteOK(){
        if(this.vote.size()==this.dealAmount.size()){
            return true;
        }    
        return false;
    }
    //거래가 체결됐니?
    public boolean isDealOk(){
        for (String nickname:vote.keySet()) {
            if(vote.get(nickname)==false){
                this.isDealSuccess=false;
                return isDealSuccess;//거래파토
            }
        }
        this.isDealSuccess=true;
        return isDealSuccess; //거래성사
    }
    //분배금액계산
    public void calcMoney(Integer round){
        //거래성사시
        if(isDealOk()){
            return;
        }else{
        //거래파토시
            List<String> patoList = new ArrayList<>();
            //파토가 한명이면 그사람만 라운드별 수당
            for (String nickname:vote.keySet()) {
                if(!vote.get(nickname)){
                    patoList.add(nickname);
                }
                dealAmount.put(nickname,0);
            }

            if(patoList.size()==1){
                dealAmount.put(patoList.get(0),roundMoney[round-1]);
            }else{
                for (String nickname:patoList) {
                    dealAmount.put(nickname,roundMoney[round-1]*-1);
                }
            }
            //파토가 2명이상이면 그 사람들다 라운드별 패널티
            return;
        }
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public List<String> getChosenJobs() {
        return chosenJobs;
    }

    public void setChosenJobs(List<String> chosenJobs) {
        this.chosenJobs = chosenJobs;
    }

    public Map<String, Boolean> getVote() {
        return vote;
    }

    public void setVote(Map<String, Boolean> vote) {
        this.vote = vote;
    }

    public Map<String, Integer> getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(Map<String, Integer> dealAmount) {
        this.dealAmount = dealAmount;
    }

    public boolean isDealSuccess() {
        return isDealSuccess;
    }

    public void setDealSuccess(boolean dealSuccess) {
        isDealSuccess = dealSuccess;
    }

    public int[] getRoundMoney() {
        return roundMoney;
    }

    public int getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(int dealMoney) {
        this.dealMoney = dealMoney;
    }

    @Override
    public String toString() {
        return "DealDTO{" +
                "playerCount=" + playerCount +
                ", chosenJobs=" + chosenJobs +
                ", vote=" + vote +
                ", dealAmount=" + dealAmount +
                ", isDealSuccess=" + isDealSuccess +
                ", roundMoney=" + Arrays.toString(roundMoney) +
                '}';
    }

}

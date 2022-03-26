package B208.mag_jang.domain;

import java.util.Arrays;

public class Player implements Comparable<Player>{
    private String nickName;
    private int money;
    private String[] jobs;
    private int gangAmount = 0;


    public Player(String player) {
        this.nickName = player;
        this.jobs = new String[2];
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String[] getJobs() {
        return jobs;
    }

    public void setJobs(String[] jobs) {
        this.jobs = jobs;
    }

    public int getGangAmount() {
        return gangAmount;
    }

    public void setGangAmount(int gangAmount) {
        this.gangAmount = gangAmount;
    }

    @Override
    public String toString() {
        return "Player : " +
                nickName +
                ", money=" + money +
                ", jobs=" + Arrays.toString(jobs);
    }

    @Override
    public int compareTo(Player p) {
        return -(this.getMoney()-p.getMoney());
    }
}

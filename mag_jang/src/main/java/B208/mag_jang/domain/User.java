package B208.mag_jang.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(columnDefinition = "varchar(255)")
    private String nickName;

    @Column(columnDefinition = "varchar(255)")
    private String naverId;

    @Column(columnDefinition = "bigint(20) default 0")
    private Long rankPoint;

    @Column(columnDefinition = "bigint(20) default 0")
    private Long winAmount;

    @Column(columnDefinition = "bigint(20) default 0")
    private Long gangAmount;

    @Column(columnDefinition = "bigint(20) default 0")
    private Long proGangAmount;

    @Column(columnDefinition = "bigint(20)")
    private Long lastGenRoom;

    public User(String naverId) {
        this.naverId = naverId;
    }

    public User() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNaverId() {
        return naverId;
    }

    public void setNaverId(String naverId) {
        this.naverId = naverId;
    }

    public Long getRankPoint() {
        return rankPoint;
    }

    public void setRankPoint(Long rankPoint) {
        this.rankPoint = rankPoint;
    }

    public Long getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(Long winAmount) {
        this.winAmount = winAmount;
    }

    public Long getGangAmount() {
        return gangAmount;
    }

    public void setGangAmount(Long gangAmount) {
        this.gangAmount = gangAmount;
    }

    public Long getProGangAmount() {
        return proGangAmount;
    }

    public void setProGangAmount(Long proGangAmount) {
        this.proGangAmount = proGangAmount;
    }

    public Long getLastGenRoom() {
        return lastGenRoom;
    }

    public void setLastGenRoom(Long lastGenRoom) {
        this.lastGenRoom = lastGenRoom;
    }

    @Override
    public String toString() {
        return "{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", rankPoint=" + rankPoint +
                ", winAmount=" + winAmount +
                ", proGangAmount=" + proGangAmount +
                '}';
    }
}

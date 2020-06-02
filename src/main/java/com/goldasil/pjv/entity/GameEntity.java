package com.goldasil.pjv.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Represents a current state of the game.
 */
@Entity
@Table(name = "games")
public class GameEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "currentPlayerToPlay", nullable = false)
    private int currentPlayerToPlay;

    @Column(name = "mainPlayerName", nullable = false)
    private String mainPlayerName;

    @Column(name = "dateTime")
    private Timestamp played_at;

    @Column(name = "playersInfo", columnDefinition = "text")
    private String playersInfo;

    @Column(name = "stock", columnDefinition = "text")
    private String stock;

    @Column(name = "waste", columnDefinition = "text")
    private String waste;

    @Column(name = "upcard")
    private String upcard;

    @Column(name = "lastMoveDTO", columnDefinition = "text")
    private String lastMoveDTO;

    @Column(name = "password", nullable = false)
    private String password;

    public GameEntity() {
    }

    /**
     * Create a new game.
     * @param mainPlayerName
     * @param playersInfo
     * @param stock
     * @param waste
     * @param upcard
     * @param lastMoveDTO
     * @param currentPlayerToPlay
     * @param password
     */
    public GameEntity(String mainPlayerName, String playersInfo, String stock, String waste, String upcard, String lastMoveDTO, int currentPlayerToPlay, String password) {
        this.currentPlayerToPlay = currentPlayerToPlay;
        this.mainPlayerName = mainPlayerName;
        this.played_at = getTime();
        this.playersInfo = playersInfo;
        this.stock = stock;
        this.waste = waste;
        this.upcard = upcard;
        this.lastMoveDTO = lastMoveDTO;
        this.password = password;
    }


    @Override
    public String toString() {
        return "GameEntity{" +
                "id=" + id +
                ", currentPlayerToPlay=" + currentPlayerToPlay +
                ", mainPlayerName='" + mainPlayerName + '\'' +
                ", played_at=" + played_at +
                ", playersInfo='" + playersInfo + '\'' +
                ", stock='" + stock + '\'' +
                ", waste='" + waste + '\'' +
                ", upcard='" + upcard + '\'' +
                ", lastMoveDTO='" + lastMoveDTO + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    private Timestamp getTime(){
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        return time;
    }


    public String getPlayersInfo() {
        return playersInfo;
    }

    public void setPlayersInfo(String playersInfo) {
        this.playersInfo = playersInfo;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUpcard() {
        return upcard;
    }

    public void setUpcard(String upcard) {
        this.upcard = upcard;
    }

    public String getLastMoveDTO() {
        return lastMoveDTO;
    }

    public void setLastMoveDTO(String lastMoveDTO) {
        this.lastMoveDTO = lastMoveDTO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMainPlayerName() {
        return mainPlayerName;
    }

    public void setMainPlayerName(String mainPlayerName) {
        this.mainPlayerName = mainPlayerName;
    }

    public Date getPlayed_at() {
        return played_at;
    }

    public void setPlayed_at(Timestamp played_at) {
        this.played_at = played_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCurrentPlayerToPlay() {
        return currentPlayerToPlay;
    }

    public void setCurrentPlayerToPlay(int currentPlayerToPlay) {
        this.currentPlayerToPlay = currentPlayerToPlay;
    }

    public String getWaste() {
        return waste;
    }

    public void setWaste(String waste) {
        this.waste = waste;
    }
}

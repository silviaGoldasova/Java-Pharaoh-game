package com.goldasil.pjv.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

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

    @Column(name = "data", columnDefinition = "text")
    private String data;

    @Column(name = "password", nullable = false)
    private String password;

    public GameEntity() {
    }

    public GameEntity(String mainPlayerName, int currentPlayerToPlay, String data, String password) {
        this.mainPlayerName = mainPlayerName;
        this.currentPlayerToPlay = currentPlayerToPlay;
        this.played_at = getTime();
        this.data = data;
        this.password = password;
    }

    private Timestamp getTime(){
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        return time;
    }

    @Override
    public String toString() {
        return "GameEntity{" +
                "id=" + id +
                ", currentPlayerToPlay=" + currentPlayerToPlay +
                ", mainPlayerName='" + mainPlayerName + '\'' +
                ", played_at=" + played_at +
                ", data='" + data + '\'' +
                ", password='" + password + '\'' +
                '}';
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
}

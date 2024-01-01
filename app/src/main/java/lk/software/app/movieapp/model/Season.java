package lk.software.app.movieapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Season implements Serializable {

    private String id;
    private String poster;
    private String season_number;
    private String number_of_episodes;

    private ArrayList<Episode> episodes;
    private String number;

    public String getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<Episode> episodes) {
        this.episodes = episodes;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSeason_number() {
        return season_number;
    }

    public void setSeason_number(String season_number) {
        this.season_number = season_number;
    }

    public String getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(String number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    @Override
    public String toString() {
        return "Season{" +
                "id='" + id + '\'' +
                ", poster='" + poster + '\'' +
                ", season_number='" + season_number + '\'' +
                ", number_of_episodes='" + number_of_episodes + '\'' +
                ", episodes=" + episodes +
                ", number='" + number + '\'' +
                '}';
    }
}

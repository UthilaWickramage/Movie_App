package lk.software.app.movieapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TVShow extends Item implements Serializable {
    private String title;
    private String poster_uri;
private int id;
    private String backdrop_url;
    private String rating;
    private String votes;
    private String overview;

    private ArrayList<String> genres;
    private String duration;
    private String number_of_episodes;
    private String lang;
    private String release;

private ArrayList<Season> seasons;

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    public TVShow() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_uri() {
        return poster_uri;
    }

    public void setPoster_uri(String poster_uri) {
        this.poster_uri = poster_uri;
    }

    public String getBackdrop_url() {
        return backdrop_url;
    }

    public void setBackdrop_url(String backdrop_url) {
        this.backdrop_url = backdrop_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(String country) {
        this.number_of_episodes = country;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRelease() {
        return release;
    }

    @Override
    public String getCountry() {
        return null;
    }

    public void setRelease(String release) {
        this.release = release;
    }
}

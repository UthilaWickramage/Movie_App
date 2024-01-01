package lk.software.app.movieapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie extends Item implements Serializable {
    private String title;
    private String poster_uri;
    private String backdrop_url;
    private String rating;
    private String votes;
    private int id;
    private String overview;

    private ArrayList<String> genres;
private String duration;
private String country;
private String lang;
private String release;

    public String getDuration() {
        return duration;
    }

    @Override
    public String getNumber_of_episodes() {
        return null;
    }

    @Override
    public ArrayList<Season> getSeasons() {
        return null;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public void setRelease(String release) {
        this.release = release;
    }


    public Movie() {
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public String getRating() {
        return rating;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop_url() {
        return backdrop_url;
    }

    public void setBackdrop_url(String backdrop_url) {
        this.backdrop_url = backdrop_url;
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
}

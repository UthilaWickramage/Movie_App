package lk.software.app.movieapp.model;

import java.util.ArrayList;

public class Slide {

    private String Image ;
    private String Title;

    private String desc;

    private ArrayList<String> genres;

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public Slide(String image, String title) {
        Image = image;
        Title = title;

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
package lk.software.app.movieapp.model;

import java.util.ArrayList;

public abstract class Item {
    public abstract int  getId();

    public abstract String getTitle();

    public abstract String getPoster_uri();

    public abstract String getBackdrop_url();

    public abstract String getOverview();

    public abstract String getRating();

    public abstract String getVotes();

    public abstract String getDuration();
    public abstract String getNumber_of_episodes();
public abstract ArrayList<Season> getSeasons();
    public abstract String getRelease();

    public abstract String getCountry();

    public abstract String getLang();

    public abstract ArrayList<String> getGenres();
}

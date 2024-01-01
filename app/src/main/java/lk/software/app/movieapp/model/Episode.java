package lk.software.app.movieapp.model;

public class Episode {
    private String number;
    private String name;
    private String overview;
    private String runtime;
    private String still_path;
    private String vote_average;
    private String vote_count;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getStill_path() {
        return still_path;
    }

    public void setStill_path(String still_path) {
        this.still_path = still_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", runtime='" + runtime + '\'' +
                ", still_path='" + still_path + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", vote_count='" + vote_count + '\'' +
                '}';
    }
}

package lk.software.app.movieapp;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MovieApi {
    @GET("popular")
    Call<JsonObject> getJson(@Query("api_key") String key);
    @GET("upcoming")
    Call<JsonObject> getUpcomingMovies(@Query("api_key") String key);
    @GET("top_rated")
    Call<JsonObject> getTopRatedMovies(@Query("api_key") String key);
    @GET("top_rated")
    Call<JsonObject> getTopRatedTvShows(@Query("api_key") String key);
    @GET("popular")
    Call<JsonObject> getPopularTvShows(@Query("api_key") String key);

    @GET("{movie_id}")
    Call<JsonObject> getMovieDetails(@Path("movie_id") int id,
                                    @Query("api_key") String key);

    @GET("credits")
    Call<JsonObject> getCredits(@Query("api_key") String key);

    @GET("{series_id}")
    Call<JsonObject> getTvDetails(@Path("series_id") int id,@Query("api_key") String key);

    @GET("{series_id}/season/{season_number}")
    Call<JsonObject> getSeasonDetails(@Path("series_id") String series_id, @Path("season_number") String season_number, @Query("api_key") String key);
}

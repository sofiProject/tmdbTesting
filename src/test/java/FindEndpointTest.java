package test.java;

import com.google.gson.JsonParser;
import main.java.TMDBHelper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FindEndpointTest {
    private TMDBHelper helper = new TMDBHelper();
    private String apiKey = "";
    private Find findRequest;

    private void sendFindRequest(String id, String externalSource) throws IOException {
        String stringURL = "https://api.themoviedb.org/3/find/" + id + "?api_key=" + apiKey + "&language=en-US&external_source=" + externalSource;
        String jsonResponseText = helper.getJsonAsString(stringURL, 10000);
        JsonParser parser = new JsonParser();
        findRequest = new Find(parser.parse(jsonResponseText));
    }

    @Test
    //This test should be in a separate test class that just tests the returned status because it
    //isn't unique to the Find endpoint
    public void notAuthenticated() throws IOException {
        String apiKey = "gggggg";
        String stringURL = "https://api.themoviedb.org/3/find/" + "nm0413168" + "?api_key=" + apiKey + "&language=en-US&external_source=" + "imdb_id";
        String jsonResponseText = helper.getJsonAsString(stringURL, 10000);

        assertEquals("An incorrect API key should return a 401 status", "401", jsonResponseText);
    }

    @Test
    //This test should be in a separate test class that just tests the returned status because it
    //isn't unique to the Find endpoint
    public void pageNotFound() throws IOException {
        String stringURL = "https://api.themoviedb.org/3/finds/" + "nm0413168" + "?api_key=" + apiKey + "&language=en-US&external_source=" + "imdb_id";
        String jsonResponseText = helper.getJsonAsString(stringURL, 10000);

        assertEquals("An incorrect Endpoint should return a 404 status", "404", jsonResponseText);
    }

    @Test
    public void findPeopleResultsByImdbIdReturnsCorrectName() throws IOException {
        String expectedName = "\"Hugh Jackman\"";

        sendFindRequest("nm0413168", "imdb_id");
        String actualFieldText = findRequest.getFieldValueAsString("person_results", 0, "name");
        assertEquals("The overview should equal this string: " + expectedName, actualFieldText, expectedName);
    }

    @Test
    //This test is valid for the behavior shown on https://developers.themoviedb.org, but I tried looking for an episode of the big bang theory
    //using a tvdb id and it did not return tv_results nor tv_season_results, just tv_episode results, which makes more sense to me.
    //If I were actually testing this API I would ask the developers if the Dr. Who behavior is correct or if the various ids
    //should be unique and only one result should be returned per ID.  If the behavior is in fact a bug then I would log the bug and
    //modify this test to fail until the bug is fixed.
    public void findEpisodeResultsByTheTvdbIdReturnsCorrectName() throws IOException {

        String tvResultsExpectedName = "\"아는 형님\""; //tv_results
        String expectedEpisodeName = "\"The Christmas Invasion\"";
        String expectedSeasonName = "\"Season 1\"";

        sendFindRequest("311603", "tvdb_id");

        String actualTvResultName = findRequest.getFieldValueAsString("tv_results", 0, "original_name");
        String actualTvEpisodeResultsName = findRequest.getFieldValueAsString("tv_episode_results", 0, "name");
        String actualSeasonName = findRequest.getFieldValueAsString("tv_season_results", 0, "name");

        assertEquals("searching for an episode of Doctor Who should not return a person", 0, findRequest.getResultsArray("person_results").size());
        assertEquals("Episode IDs on TVDB are not unique from show IDs therefore I expect to get back a result for some Korean game show :)",tvResultsExpectedName, actualTvResultName );
        assertEquals("The overview should equal this string: " + expectedEpisodeName, actualTvEpisodeResultsName, expectedEpisodeName);
        assertEquals("The Season name should be 'Season 1'", expectedSeasonName, actualSeasonName);
    }

    @Test
    public void findTvResultsByImdbIdReturnsCorrectOverview() throws IOException {
        String expectedOverviewText = "\"The Flash is a 1990 American television series that starred John Wesley Shipp as the superhero, the Flash, and co-starred Amanda Pays. The series was developed from the DC Comics characters by the writing team of Danny Bilson and Paul De Meo, and produced by their company, Pet Fly Productions, in association with Warner Bros. Television. Composer Danny Elfman wrote the show’s title theme, and Stan Winston Studios built the costume.\"";

        sendFindRequest("tt0098798", "imdb_id" );

        String actualFieldText = findRequest.getFieldValueAsString("tv_results", 0, "overview");

        assertEquals("The overview should equal this string: " + expectedOverviewText, actualFieldText, expectedOverviewText);
    }

    @Test
    public void findMovieResultsByImdbIdReturnsCorrectName(){
        //TODO: Use the IMDB id of a movie and query themoviedb then verify that the correct movie is return.
    }

    @Test
    public void findMovieResultsByTheTvdbIdReturnsCorrectName(){
        //TODO: Use the thetvdb id of a movie and query themoviedb then verify that the correct movie is return.
    }

    @Test
    public void findSeasonResultsByImdbIdReturnsCorrectName(){
        //TODO: Use the IMDB id of a tv series Season and query themoviedb then verify that the correct movie is return.
    }

    @Test
    public void findSeasonResultsByTheTvdbIdReturnsCorrectName(){
        //TODO: Use the thetvdb id of a tv series Season and query themoviedb then verify that the correct movie is return.
    }

    @Test
    public void findEpisodeResultsByTheTvdbIdReturnsValidTheMovieDbId(){
        //TODO: Use the thetvdb id of a tv episode and query themoviedb then verify that the
        // returned id can be used in a "tv/get-tv-details" query and the correct
        //information about the show is returned
    }

    @Test
    public void findEpisodeResultsByImdbIdReturnsValidTheMovieDbId(){
        //TODO: Use the imdb id of a tv episode and query themoviedb then verify that the
        // returned id can be used in a "tv/get-tv-details" query and the correct
        //information about the show is returned
    }

    @Test
    public void facebookMovieIdReturnsCorrectMovie(){
        //TODO: Get a facebook movie ID and verify that it returns the correct movie
    }

    @Test
    public void twitterPersonIdReturnsCorrectPerson(){
        //TODO: Get a Twitter person ID and verify that it returns the correct person
    }

    @Test
    public void instagramTvShowIdReturnsCorrectTvShow(){
        //TODO: Get an Instagram TV Show ID and verify that it returns the correct TV Show
    }
}

package test.java;

import com.google.gson.JsonParser;
import main.java.TMDBHelper;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class FindEndpointTestRunner {
//    String message = "Hello World";
    TMDBHelper helper = new TMDBHelper();
    String apiKey = "05808d165011b4774cd3dbef69ade85e";
    Find findRequest;

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
    public void findPeopleResultsByImdbIdReturnsCorrectName() throws IOException {
        String expectedName = "\"Hugh Jackman\"";

        sendFindRequest("nm0413168", "imdb_id");
        String actualFieldText = findRequest.getFieldValueAsString("person_results", 0, "name");
        assertEquals("The overview should equal this string: " + expectedName, actualFieldText, expectedName);
    }

    @Test
    //This test is valid for the behavior shown on https://developers.themoviedb.org, but I tried looking for an episode of the big bang theory
    //using a tvdb id and it did not return tv_results or tv_season_results, just tv_episode results, which makes more sense to me.
    //If I were actually testing this API I would ask the developers if the Dr. Who behavior is correct or if the various ids
    //should be unique and only one result should be returned per ID.  If the behavior is in fact a bug then I would log the bug and
    //modify this test to fail until the bug is fixed.
    public void findSeriesResultsByTheTvdbIdReturnsCorrectName() throws IOException {

        String tv_results_expected_string = "\"아는 형님\""; //tv_results
        String expectedEpisodeName = "\"The Christmas Invasion\"";
        String expectedSeasonName = "\"Season 1\"";

        sendFindRequest("311603", "tvdb_id");

        String actualTvResultName = findRequest.getFieldValueAsString("tv_results", 0, "original_name");
        String actualTvEpisodeResultsName = findRequest.getFieldValueAsString("tv_episode_results", 0, "name");
        String actualSeasonName = findRequest.getFieldValueAsString("tv_season_results", 0, "name");

        assertEquals("searching for an episode of Doctor Who should not return a person", 0, findRequest.getResultsArray("person_results").size());
        assertEquals("Episode IDs on TVDB are not unique from show IDs therefore I expect to get back a result for some Korean game show :)",tv_results_expected_string, actualTvResultName );
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

//    @Test
//    public void findByImdbIdNumber() throws IOException {
//        String imdbID = "nm0413168";// "tt0098798";
//        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //should do pretty print now.
//        URL url = new URL("https://api.themoviedb.org/3/find/" + imdbID + "?api_key=" + apiKey + "&language=en-US&external_source=imdb_id");
//        String stringURL = "https://api.themoviedb.org/3/find/" + imdbID + "?api_key=" + apiKey + "&language=en-US&external_source=imdb_id";
//
//        //I don't really need all of this because the tmdbhelper does it all for me a returns a string, which I can then parse.
////        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
////        httpURLConnection.setDoOutput(true);
////        httpURLConnection.setRequestMethod("GET");
////        httpURLConnection.setRequestProperty("Content-Type", "application/json");
////        httpURLConnection.connect();
////        httpURLConnection.getResponseCode();
//
//        String text = helper.getJsonAsString(stringURL, 10000);
//
//        JsonParser parser = new JsonParser();
//
////        String json = "{ \"f1\":\"Hello\",\"f2\":{\"f3:\":\"World\"}}";
//
//        JsonElement jsonTree = parser.parse(text);
//        JsonObject jobj = jsonTree.getAsJsonObject();
////        jobj.get("tv_results").getAsJsonArray().get(0).getAsJsonObject().get("original_name")
//
//        Map<String, Object> retMap = new Gson().fromJson(text, new TypeToken<HashMap<String, Object>>() {}.getType());
//
//        retMap.get("tv_results");
//
//
//
//
//        String json = text;
//
//        JsonReader jsonReader = new JsonReader(new StringReader(json));
//
//        try {
//            while(jsonReader.hasNext()){
//                JsonToken nextToken = jsonReader.peek();
//                System.out.println(nextToken);
//
//                if(JsonToken.BEGIN_OBJECT.equals(nextToken)){
//
//                    jsonReader.beginObject();
//
//                } else if(JsonToken.NAME.equals(nextToken)){
//
//                    String name  =  jsonReader.nextName();
//                    System.out.println(name);
//
//                } else if(JsonToken.STRING.equals(nextToken)){
//
//                    String value =  jsonReader.nextString();
//                    System.out.println(value);
//
//                } else if(JsonToken.NUMBER.equals(nextToken)){
//
//                    long value =  jsonReader.nextLong();
//                    System.out.println(value);
//
//                }
//                else if (JsonToken.BEGIN_ARRAY.equals(nextToken)){
//                    jsonReader.beginArray();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}

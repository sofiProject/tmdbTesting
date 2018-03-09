package test.java;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Find {
    JsonObject jsonObject;

    public Find(JsonElement jsonElement){
        this.jsonObject = jsonElement.getAsJsonObject();
    }

    public JsonArray getResultsArray(String resultSection){
        //I should verify that this is a jsonArray first, but for now I'll just expect the tester to know what they are doing :)
        return jsonObject.get(resultSection).getAsJsonArray();
    }

    public String getFieldValueAsString(String resultSection ,int index, String fieldName){
        //Again, type checking and throwing an error or returning null would be good here, but I'll expect the tester to know what they are doing.
        JsonArray array = getResultsArray(resultSection);

        if(array.size() == 0){
            //I could return null, but I decided on the empty string so if there is a fail
            // it happens at the assert instead of some random run time exception.
            // This also lets me expect the empty string if I search for a field I know shouldn't be there.
            return "";
        }
        else{
            return array.get(index).getAsJsonObject().get(fieldName).toString();
        }
//        return getResultsArray(resultSection).get(index).getAsJsonObject().get(fieldName).toString();
    }

    //jobj.get("tv_results").getAsJsonArray().get(0).isJsonObject()

//    {"movie_results":[],
//        "person_results":[],
//        "tv_results":[{"original_name":"The Flash","id":236,"name":"The Flash","vote_count":19,"vote_average":6.6,"first_air_date":"1990-09-20","poster_path":"/abxIKXvwtNByR5DuYs7M0cIXjWo.jpg","genre_ids":[80,18,10759,10765],"original_language":"en","backdrop_path":"/3JPFgvsnZfYsb6NPqlxdXK8RmgH.jpg","overview":"The Flash is a 1990 American television series that starred John Wesley Shipp as the superhero, the Flash, and co-starred Amanda Pays. The series was developed from the DC Comics characters by the writing team of Danny Bilson and Paul De Meo, and produced by their company, Pet Fly Productions, in association with Warner Bros. Television. Composer Danny Elfman wrote the show’s title theme, and Stan Winston Studios built the costume.","origin_country":["US"]}],
//        "tv_episode_results":[],
//        "tv_season_results":[]}
}

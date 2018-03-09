package test.java;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/*
With more time I would look at all the endpoints and see what they have in common and move that functionality into a parent class or a template for
all of them to extend or implement.

I could make the methods here protected, but I decided to go with public.
*/

public class Find {
    private JsonObject jsonObject;

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
    }
}

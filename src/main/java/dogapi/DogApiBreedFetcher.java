package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */


    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        //TODOTask 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.
         String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "/list" ;
         try {
             String response = run(url);
             JSONObject data = new JSONObject(response);
             if(data.getString("status").equals("error")){
                 throw new BreedNotFoundException(breed);
             }
             else{
                 List<String> subBreedList = new ArrayList<>();
                 JSONArray subBreedsArray =  data.getJSONArray("message");
                 for(int i=0; i<subBreedsArray.length(); i++){
                     subBreedList.add(subBreedsArray.getString(i));
                 }
                 return subBreedList;
             }
         }catch (IOException e) {
             throw new BreedNotFoundException(breed);
         } catch (Exception e) {
             throw new BreedNotFoundException(breed);
         }
    }


    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Empty response body");
            }
        }
    }

}
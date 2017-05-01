/**
 * Created by manu on 30/04/17.
 * Main class to get the Json REST
 */
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class JsonClientMain {

    public static void main(String[] args) {
        JsonClientMain jsonClientMain = new JsonClientMain();
        jsonClientMain.getRest();
    }
//getting JSON
    public void getRest(){
        try {
            URL url = new URL("https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String response="";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                response+= output;

            }
            conn.disconnect();

//            parse to jsonArray with use of GSON lib
            JsonArray jsonArray = (new JsonParser()).parse(response).getAsJsonArray();
            EntryCount<String> entryCount = new EntryCount<>();
            EntryCount<String> communities = new EntryCount<>();
            EntryCount<String> brokePoints = new EntryCount<>();
            List<String> list = new ArrayList<>();
            JsonArray jsonArrayBrokenPoints = new JsonArray();

//            Itarate to check the json values and the rankings
            jsonArray.forEach(item -> {
                if(item.getAsJsonObject().get("water_point_condition").getAsString().equals("broken")){
                    jsonArrayBrokenPoints.add(item);
                }
                communities.count(item.getAsJsonObject().get("communities_villages").getAsString());
                entryCount.count(item.getAsJsonObject().get("water_functioning").getAsString());
                brokePoints.count(item.getAsJsonObject().get("water_point_condition").getAsString());
                list.add(item.getAsJsonObject().get("communities_villages").getAsString());
            });

            EntryCount<String> communitiesBrokePoints = new EntryCount<>();
            jsonArrayBrokenPoints.forEach(item -> {

                communitiesBrokePoints.count(item.getAsJsonObject().get("communities_villages").getAsString());


            });
            Set<String> set = new HashSet<>(list);
            Map<String, Object> converted = new HashMap<>();
            Map<String, Object> percentage = new HashMap<>();
            List<Map<String, Object>> listMap = new ArrayList<>();
            List<Map<String, Object>> listMapPercentage = new ArrayList<>();

            int numberOfWaterPoints;
            for (String hold : set){
                numberOfWaterPoints =communities.get(hold);
                converted.put(hold, numberOfWaterPoints);
                percentage.put(hold, String.format("%.2f", ((double)communitiesBrokePoints.get(hold)/(double)numberOfWaterPoints)*(double)100));
            }

            listMap.add(converted);
            listMapPercentage.add(percentage);

//            building the expected output
            jsonObjectBuild(String.valueOf(entryCount.get("yes")),listMap,listMapPercentage);



        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

//    publishing the Json GSON lib

    public void jsonObjectBuild(String waterPoints, List<Map<String, Object>> community, List<Map<String, Object>> percentage){
        Map<String, Object> response = new HashMap<>();
        List<Object> objects = new ArrayList<>();
        List<Object> objectPercentage = new ArrayList<>();
        for(Map<String, Object> listMap :community) {
            objects.add(listMap);
        }for(Map<String, Object> listMapPercentage :percentage) {
            objectPercentage.add(listMapPercentage);
        }

        response.put("functional_water_points", waterPoints);
        response.put("number_of_water_points_per_community", objects);
        response.put("community_ranking", objectPercentage);

        System.out.println(new Gson().toJson(response));
    }






}

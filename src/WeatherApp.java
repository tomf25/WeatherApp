
//Retreive weather data from api

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class WeatherApp {
    public static JSONObject gerWeatherData(String locationName){

        //Get location coords
        JSONArray locationData = getLocationData(locationName);
        return null;
    }

    public static JSONArray getLocationData(String locationName){
        //replace whitespace in kocationname to + to adhere to apis fequest format
        locationName = locationName.replaceAll(" ", "+");
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try{
            //call api
            HttpURLConnection conn = fetchApiResponse(urlString);
            if(conn.getResponseCode() != 200){
                System.out.println("Error: couldnt not connect to api");
                return null;
            }
            else{
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }

                scanner.close();
                conn.disconnect();
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
                JSONArray locationData = (JSONArray) resultJsonObj.get("results");

                return locationData;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    


    }
    private static HttpURLConnection fetchApiResponse(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //connect to our API
            conn.connect();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }
        //couldnt make connection
        return null;
    }
}

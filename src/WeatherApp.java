
//Retreive weather data from api

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class WeatherApp {
    public static JSONObject gerWeatherData(String locationName){

        //Get location coords
        JSONArray locationData = getLocationData(locationName);

        //extract latitude and longitude data

        JSONOBject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latidude");
        double longitude = (double) location.get("longitude");

        //build api request url with location data
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
        "latitude="+ latitude + "&longitude=" + longitude + 
        "&hourly=temperature_2m,weather_code,wind_speed_10m,relative_humidity_2m";
        
        try{
            //call api and get reponse
            HttpURLConnection conn = fetchApiResponse(urlString);
            //check for response status

            if(conn.getResponseCode() != 200){
                System.out.println("Error: could not connect to api");
                return null;
            }
            //store resulting json data
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while(scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            //parse through our data

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            //retreive hourly data

            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            // we want the current hours data
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

        }catch(Exception e){
            e.printStackTrace();
        }



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
    

        return null;
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
    private static int findIndexOfCurrentTime(JSONArray timeList){
        String currentTime = getCurrentTime();

        return 0;
    }
    public static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        //format data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }
}

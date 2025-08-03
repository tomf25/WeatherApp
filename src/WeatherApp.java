
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
    public static JSONObject getWeatherData(String locationName){

        //Get location coords
        JSONArray locationData = getLocationData(locationName);

        //extract latitude and longitude data

        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
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

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            //get weather code (cloudy , rainy etc)
            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            //get humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            //get windspeed
            JSONArray windspeedData = (JSONArray) hourly.get("wingspeed_10m");
            double windspeed = (double) windspeedData.get(index);

            //build the weatger json data object that we are going to access in frontend

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);
            return weatherData;


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
        for(int i = 0; i  <timeList.size(); i++){
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }
        return 0;
    }

    private static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        //format data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }

    private static String convertWeatherCode(long weathercode){
        String weatherCondition  = "";
        if(weathercode == 0L){
            weatherCondition = "Clear";
        }
        else if (weathercode <= 3L && weathercode > 0L){
            weatherCondition = "Cloudy";

        }
        else if ((weathercode >= 51L && weathercode <= 67L)
            || (weathercode >= 80L && weathercode <= 99L)){
                weatherCondition = "Rain";
        }
        else if(weathercode >= 71L && weathercode <= 77L){
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }
}

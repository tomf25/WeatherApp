import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;

import org.json.simple.JSONObject;

public class WeatherAppGui extends JFrame{
    private JSONObject weatherData;

    public WeatherAppGui(){
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(450,650);

        setLocationRelativeTo(null); //Load our window to the center of the screen
        setLayout(null); //manually position layout components
        setResizable(false);
        
        addGuiComponents();
    }

    private void addGuiComponents(){
        JTextField searchTextField = new JTextField();    // search field
        searchTextField.setBounds(15, 15, 351, 45);

        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);

        // Weather icons
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        //temperature reading
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0,350,450,54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER); //Center text
        add(temperatureText);

        //Weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,405,450,36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //Humidity icon
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        //Humidity text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100% </html>");
        humidityText.setBounds(90,500,85,55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        //Windspeed icon
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220,500,74,66);
        add(windspeedImage);

        //Windspeed text
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h </html>");
        windspeedText.setBounds(310,500,85,55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedText);


        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        //Change to hand when hovering over button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        searchButton.setBounds(375,13,47,45);
        searchButton.addActionListener((ActionEvent e) -> {
            //get location from user
            String userInput = searchTextField.getText();
            
            //validate input
            if(userInput.replaceAll("\\s", "").length() <= 0){
                return;
            }
            //retreive weather data
            weatherData = WeatherApp.getWeatherData(userInput);

            //update weather image
            String weatherCondition = (String) weatherData.get("weather_condition");
            switch(weatherCondition){
                case "Clear":
                    weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                    break;

                case "Cloudy":
                    weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                    break;

                case "Rain":
                    weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                    break;

                case "Snow":
                    weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                    break;
        
            }

            //Update temperature

            double temperature = (double) weatherData.get("temperature");
            temperatureText.setText(temperature + " C");

            //Update weather condition text
            weatherConditionDesc.setText(weatherCondition);

            //update humidity text
            long humidity = (long) weatherData.get("humidity");
            humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

            //update windspeed text
            double windspeed = (double) weatherData.get("windspeed");
            windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");
        });
        add(searchButton);


    }

    //Create images in our gui
    private ImageIcon loadImage(String path){
        try{
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }


    
}


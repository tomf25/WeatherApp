import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class WeatherAppGui extends JFrame{
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

        // Search button
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        //Change to hand when hovering over button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        searchButton.setBounds(375,13,47,45);
        add(searchButton);

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


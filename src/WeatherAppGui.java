import java.awt.*;
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
    }
}


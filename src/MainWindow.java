import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by ola on 25.03.17.
 */
public class MainWindow extends JFrame{

    private JPanel MainWindowPanel;
    private JButton ReadFile;
    private JLabel RawPictureLabel;

    private Image myImage;

    MainWindow(){
        super("Tomograph Simulator");
        setContentPane(MainWindowPanel);
        pack();

        ReadFile.addActionListener((ActionEvent actionEvent) -> {
            try {
                this.myImage = new Image(RawPictureLabel);

                pack();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //MainWindow.super.dispose();
        });


        setVisible(true);
    }
}

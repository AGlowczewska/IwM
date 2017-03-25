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
    private JButton SimulateButton;
    private JTextField StepField;
    private JTextField ProbesField;
    private JTextField WidthField;
    private JLabel StepLabel;
    private JLabel ProbesLabel;
    private JLabel WidthLabel;
    private Image myImage;

    MainWindow(){
        super("Tomograph Simulator");
        setContentPane(MainWindowPanel);
        pack();

        ReadFile.addActionListener((ActionEvent actionEvent) -> {
            try {
                this.myImage = new Image(RawPictureLabel);
                ShowOptions();
                pack();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //MainWindow.super.dispose();
        });


        setVisible(true);
    }

    private void ShowOptions(){
        SimulateButton.setVisible(true);
        StepField.setVisible(true);
        ProbesField.setVisible(true);
        WidthField.setVisible(true);
        StepLabel.setVisible(true);
        ProbesLabel.setVisible(true);
        WidthLabel.setVisible(true);
    }
}

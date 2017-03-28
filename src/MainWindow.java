import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by ola on 25.03.17.
 */
public class MainWindow extends JFrame{

    private JPanel MainWindowPanel;
    private JButton ReadFileButton;
    private JLabel RawPictureLabel;
    private JButton SimulateButton;
    private JTextField StepField;
    private JTextField ProbesField;
    private JTextField WidthField;
    private JLabel StepLabel;
    private JLabel ProbesLabel;
    private JLabel WidthLabel;
    private JLabel SecondPictureLabel;
    private Image myImage;

    private DocumentListener updateSecondPicture;

    MainWindow(){
        super("Tomograph Simulator");
        setContentPane(MainWindowPanel);
        pack();

        updateSecondPicture = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (myImage.SetValues(StepField.getText(),ProbesField.getText(),WidthField.getText()) == true)
                    myImage.VisualizeDetectors(SecondPictureLabel);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (myImage.SetValues(StepField.getText(),ProbesField.getText(),WidthField.getText()) == true)
                    myImage.VisualizeDetectors(SecondPictureLabel);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                if (myImage.SetValues(StepField.getText(),ProbesField.getText(),WidthField.getText()) == true)
                    myImage.VisualizeDetectors(SecondPictureLabel);
            }

        };

        ReadFileButton.addActionListener((ActionEvent actionEvent) -> {
            try {
                this.myImage = new Image(RawPictureLabel);
                ShowOptions();
                pack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        SimulateButton.addActionListener((ActionEvent actionEvent) -> {
            if (myImage.SetValues(StepField.getText(),ProbesField.getText(),WidthField.getText()) == true) {
                myImage.CreateSingoram(RawPictureLabel);
            }
        });

        StepField.getDocument().addDocumentListener(updateSecondPicture);
        ProbesField.getDocument().addDocumentListener(updateSecondPicture);
        WidthField.getDocument().addDocumentListener(updateSecondPicture);

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

        /**** Fix for frame width below *****/
        StepLabel.setMinimumSize(new Dimension(100,-1));
        StepField.setPreferredSize(new Dimension(myImage.GetWidth() - 110, -1));
        /***********************************/
    }
}

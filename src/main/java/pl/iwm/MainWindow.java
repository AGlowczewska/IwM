package pl.iwm;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.ImageToDicom;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by ola on 25.03.17.
 */
public class MainWindow extends JFrame {

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
    private JLabel ThirdPictureLabel;
    private JLabel FilteredPictureLabel;
    private JButton ExportButton;
    private JTextField NameField;
    private JTextField PatientIdField;
    private JTextField StudyField;
    private JTextField SeriesField;
    private JTextField InstanceField;
    private Image myImage;

    private DocumentListener updateSecondPicture;

    MainWindow() {
        super("Tomograph Simulator");
        setContentPane(MainWindowPanel);
        pack();

        updateSecondPicture = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (myImage.SetValues(StepField.getText(), ProbesField.getText(), WidthField.getText()) == true)
                    myImage.VisualizeDetectors(SecondPictureLabel);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (myImage.SetValues(StepField.getText(), ProbesField.getText(), WidthField.getText()) == true)
                    myImage.VisualizeDetectors(SecondPictureLabel);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                if (myImage.SetValues(StepField.getText(), ProbesField.getText(), WidthField.getText()) == true)
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
            if (myImage.SetValues(StepField.getText(), ProbesField.getText(), WidthField.getText()) == true) {
                ThirdPictureLabel.setVisible(true);
                ThirdPictureLabel.setMinimumSize(new Dimension(myImage.GetWidth(), myImage.GetWidth()));
                pack();
                myImage.CreateSingoram(ThirdPictureLabel);

                ExportButton.setVisible(true);
                NameField.setVisible(true);
                PatientIdField.setVisible(true);
                StudyField.setVisible(true);
                SeriesField.setVisible(true);
                InstanceField.setVisible(true);


                /************TO DO******************************/
                myImage.FilterPicture(FilteredPictureLabel);
                /*************************************************/
            }
        });

        ExportButton.addActionListener(e -> {
            myImage.SaveToFile("temp.jpg");
            String scJpegFilePath = "temp.jpg";
            String newDicomFile = "result.dcm";
            try {
                //generate the DICOM file from the jpeg file and the other attributes supplied
                new ImageToDicom(scJpegFilePath, //path to existing JPEG image
                        newDicomFile, //output DICOM file with full path
                        NameField.getText(), //name of patient
                        PatientIdField.getText(), //patient id
                        StudyField.getText(), //study id
                        SeriesField.getText(), //series number
                        InstanceField.getText()); //instance number

                //now, dump the contents of the DICOM file to the console
                AttributeList list = new AttributeList();
                list.read(newDicomFile);
                System.out.println(list.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        StepField.getDocument().addDocumentListener(updateSecondPicture);
        ProbesField.getDocument().addDocumentListener(updateSecondPicture);
        WidthField.getDocument().addDocumentListener(updateSecondPicture);

        setVisible(true);
    }

    private void ShowOptions() {

        SimulateButton.setVisible(true);
        StepField.setVisible(true);
        ProbesField.setVisible(true);
        WidthField.setVisible(true);
        StepLabel.setVisible(true);
        ProbesLabel.setVisible(true);
        WidthLabel.setVisible(true);

        /**** Fix for frame width below *****/
        StepLabel.setMinimumSize(new Dimension(100, -1));
        StepField.setPreferredSize(new Dimension(myImage.GetWidth() - 110, -1));
        /***********************************/
    }
}

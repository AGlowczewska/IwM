import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class represents a chosen png file and performs actions on it.
 */
public class Image {

    private BufferedImage rawPicture;

    Image(JLabel RawPictureLabel) throws IOException {
        System.out.println("Hello World!");

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);        // allows to select only one file

        if (chooser.showOpenDialog(RawPictureLabel) == JFileChooser.APPROVE_OPTION) {
            final String rawFilePath = chooser.getSelectedFile().getPath(); //"pictures/alien.png";
            this.rawPicture = ImageIO.read(new File(rawFilePath));
            ImageIcon icon = new ImageIcon(this.rawPicture);
            RawPictureLabel.setIcon(icon);
            RawPictureLabel.revalidate();
            RawPictureLabel.repaint();
        }
    }
}

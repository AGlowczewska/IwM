import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class represents a chosen png file and performs actions on it.
 */
public class Image {

    private static final String IMG_PATH = "pictures/alien.png";
    private final BufferedImage RawPicture;

    Image(JLabel RawPictureLabel) throws IOException {
        System.out.println("Hello World!");
         this.RawPicture = ImageIO.read(new File(IMG_PATH));

        ImageIcon icon = new ImageIcon(this.RawPicture);
        RawPictureLabel.setIcon(icon);
        RawPictureLabel.revalidate();
        RawPictureLabel.repaint();
    }
}

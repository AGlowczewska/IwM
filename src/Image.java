import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import static java.lang.Math.*;

/**
 * Class represents a chosen png file and performs actions on it.
 */
public class Image {

    private BufferedImage rawPicture;
    private BufferedImage secondImage;
    private int pictureWH; // picture's width = picture's height

    /*****DATA FOR CREATING SINGRAM ****/
    private int width; // l - rozwartość stożka
    private int probes; // n - liczba detektorów
    private int step; //krok alfa układu emiter-detektor
    /**********************************/

    Image(JLabel RawPictureLabel) throws IOException {
        //System.out.println("Hello World!");

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);        // allows to select only one file

        if (chooser.showOpenDialog(RawPictureLabel) == JFileChooser.APPROVE_OPTION) {
            final String rawFilePath = chooser.getSelectedFile().getPath(); //"pictures/alien.png";
            this.rawPicture = ImageIO.read(new File(rawFilePath));

            if (rawPicture.getWidth() != rawPicture.getHeight()) {
                JOptionPane.showMessageDialog(null, "Obrazek musi być kwadratowy!", "Błędne wymiary", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            ImageIcon icon = new ImageIcon(this.rawPicture);
            RawPictureLabel.setIcon(icon);
            RawPictureLabel.revalidate();
            RawPictureLabel.repaint();
        }
        pictureWH = rawPicture.getWidth();
    }

    public boolean SetValues(String stp, String prbs, String wdth){
        try {
            this.step = Integer.parseInt(stp);
            this.probes = Integer.parseInt(prbs);
            this.width = Integer.parseInt(wdth);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Podane dane muszą być wartościami całkowitymi!", "Błędne wartości", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    public void CreateSingoram(){
        secondImage = new BufferedImage(pictureWH, pictureWH, rawPicture.getType());


        int radius = (pictureWH % 2 == 0) ? (pictureWH / 2) : ((pictureWH-1) / 2);
        System.out.println("r: " + radius);

        for (int j=0; j < pictureWH; j++)
            for (int i=0; i < pictureWH; i++){
                // równanie okregu: (x-a)^2+(y-b)^2=r^2
                double temp = pow(j - 147, 2) + pow((i - 147),2);
                if (temp == pow(radius,2)) {
                    System.out.println("i:" + i + " j:" + j);
                    SetRGBv(j,i,255,1,1);
                }
                    else SetRGBv(j,i,1,1,1);
            }
    }

    public void CreatePic(JLabel SecondPictureLabel){

        ImageIcon icon = new ImageIcon(this.secondImage);
        SecondPictureLabel.setIcon(icon);
        SecondPictureLabel.revalidate();
        SecondPictureLabel.repaint();
    }

    private int ReturnGrayColor(int x, int y){
        return rawPicture.getRGB(x,y) & 0x000000ff;
    }

    private void SetRGBv(int x, int y, int R, int G, int B){
        Color color = new Color(R, G, B);
        int temp = color.getRGB();
        this.secondImage.setRGB(x, y, temp);
    }

    public int GetWidth(){
        return pictureWH;
    }
}

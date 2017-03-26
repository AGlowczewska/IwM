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
        if (this.probes < 2) {
            JOptionPane.showMessageDialog(null, "Liczba próbek musi być większa niż 1!", "Błędne wartości", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    public void CreateSingoram(){
        secondImage = new BufferedImage(pictureWH, pictureWH, rawPicture.getType());

        /******** Writing a circle *******************/
        /******* According on number of steps *******/
        int radius = (pictureWH % 2 == 0) ? (pictureWH / 2) : ((pictureWH-1) / 2);
        int StepInDegrees = 360 / probes;
        for (float i = 0; i < 360; i += StepInDegrees){
            SetRGBv(PointOnCircle(i,radius).x,PointOnCircle(i,radius).y,255,1,1);
        }
        /************************************************/
        int degreesBetweenDetectors = width / (probes -1); // odleglosc miedzy detektorami w stopniach

        /***** Writing lines detector -emiter *****/
        Point CurrentPoint; Point CurrentDetector;
        for (float i = 0; i < 360; i += 360) {
            CurrentPoint = PointOnCircle(i,radius);
            for (int j = 0; j < probes; j ++) {
                CurrentDetector = PointOnCircle((i + (360 - width) / 2) + j*degreesBetweenDetectors, radius);
                BresenhamDraw(CurrentPoint.x, CurrentPoint.y, CurrentDetector.x, CurrentDetector.y);
            }
        }
        /************************************************/

    }

    private Point PointOnCircle(float degree, int radius){
        double angle = degree * Math.PI / 180;
        int myX = ((int) (Math.cos(angle) * radius) + radius);
        int myY = ((int) (Math.sin(angle) * radius) + radius);
        System.out.println("Degree:" + degree + " X: " + myX +" Y: " + myY);
        return new Point(myX, myY);
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

    private void BresenhamDraw(int x1, int y1, int x2, int y2) {

        int d, dx, dy, ai, bi, xi, yi;
        int x = x1, y = y1;

        // ustalenie kierunku rysowania
        xi = (x1 < x2) ? 1 : -1;
        dx = (x1 < x2) ? (x2-x1) : (x1-x2);
        yi = (y1 < y2) ? 1 : -1;
        dy = (y1 < y2) ? (y2-y1) : (y1 - y2);

        SetRGBv(x,y,255,0,0);

        // oś wiodąca OX
        if (dx > dy)
        {
            ai = (dy - dx) * 2;
            bi = dy * 2;
            d = bi - dx;
            // pętla po kolejnych x
            while (x != x2)
            {
                // test współczynnika
                x += xi;
                if (d >= 0)
                {
                    y += yi;
                    d += ai;
                }
                else
                {
                    d += bi;
                }
                SetRGBv(x,y,255,0,0);
            }
        }
        // oś wiodąca OY
        else
        {
            ai = ( dx - dy ) * 2;
            bi = dx * 2;
            d = bi - dy;
            // pętla po kolejnych y
            while (y != y2)
            {
                // test współczynnika
                y += yi;
                if (d >= 0)
                {
                    x += xi;
                    d += ai;
                }
                else
                {
                    d += bi;
                }
                SetRGBv(x,y,255,0,0);
            }
        }

    }
}

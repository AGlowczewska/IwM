import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class represents a chosen png file and performs actions on it.
 **/
public class Image {

    private BufferedImage rawPicture;
    private BufferedImage secondImage;
    private BufferedImage FinalImage; //image created from sinogram
    private int pictureWH; // picture's width = picture's height

    /*****DATA FOR CREATING SINGRAM ****/
    private int width; // l - rozwartość stożka
    private int probes; // n - liczba detektorów
    private int step; //krok alfa układu emiter-detektor
    private float topLight = 0;

    private ArrayList<Float> meanPixels = new ArrayList<>();
    private ArrayList<ArrayList<Float>> sinogramValues = new ArrayList<>();


    /**********************************/

    Image(JLabel RawPictureLabel) throws IOException {

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
            //CalculateTopLight(rawPicture);

        }
        pictureWH = rawPicture.getWidth();
    }

    public boolean SetValues(String steps, String probes, String width) {
        if (steps.length() == 0 || probes.length() ==0 || width.length() == 0) return false;

        try {
            this.step = Integer.parseInt(steps);
            this.probes = Integer.parseInt(probes);
            this.width = Integer.parseInt(width);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Podane dane muszą być wartościami całkowitymi!", "Błędne wartości", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (this.probes < 2) {
            JOptionPane.showMessageDialog(null, "Liczba próbek musi być większa niż 1!", "Błędne wartości", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (this.step <= 0) {
            JOptionPane.showMessageDialog(null, "Krok musi być dodatni", "Błędne wartości", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (this.width <= 0) {
            JOptionPane.showMessageDialog(null, "Rozwartość musi być dodatnia", "Błędne wartości", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    public void CreateSingoram() {
        FinalImage = new BufferedImage(pictureWH, pictureWH, rawPicture.getType());

        /*
        float rowHeight = pictureWH / sinogramValues.get(0).size();
        float elementWidth = pictureWH / sinogramValues.size();


        int currentX = 0;
        int currentY = 0;

        for (int i = 0; i < (360 / step) ; i ++) {
            for (int k = 0; k < sinogramValues.get(i).size(); k++) {
                float valueSum = sinogramValues.get(i).get(k);
                Color color = new Color(Math.round(valueSum), Math.round(valueSum), Math.round(valueSum));
                int rgb = color.getRGB() & 0xFF;

                for (int x = currentX; x < (i + 1) * elementWidth; x++) {
                    for (int y = currentY; y < (k + 1) * rowHeight; y++) {
                        sinogram.setRGB(x, y, rgb);
                    }
                }

                currentY = (int) ((k + 1) * rowHeight);
            }
            currentX = (int) ((i + 1) * elementWidth);
        } */

    }

  /*  public void CalculateTopLight(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if ((image.getRGB(i, j) & 0xFF) > topLight) {
                    topLight = (image.getRGB(i, j) & 0xFF);
                }
            }
        }
    } */

    private BufferedImage GetCopy(BufferedImage image){
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = image.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private Point PointOnCircle(float degree, int radius) {
        double angle = degree * Math.PI / 180;
        int pointX = ((int) (Math.cos(angle) * radius) + radius);
        int pointY = ((int) (Math.sin(angle) * radius) + radius);
        return new Point(pointX, pointY);
    }

    public void VisualizeDetectors(JLabel pictureLabel) {
        secondImage = GetCopy(rawPicture);

        /******** Writing a circle *******************/
        /******* According on number of steps *******/
        int radius = (pictureWH % 2 == 0) ? (pictureWH / 2) : ((pictureWH - 1) / 2);
        int StepInDegrees = 360 / probes;
        for (float i = 0; i < 360; i += StepInDegrees) {
            SetRGBv(PointOnCircle(i, radius).x, PointOnCircle(i, radius).y, 255, 1, 1);
        }
        /************************************************/
        int degreesBetweenDetectors = width / (probes - 1); // odleglosc miedzy detektorami w stopniach

        /***** Writing lines detector -emiter on second image*****/
        Point CurrentPoint;
        Point CurrentDetector;
        for (float i = 0; i < 360; i += step) {
          //  meanPixels.clear();
            CurrentPoint = PointOnCircle(i, radius);
            for (int j = 0; j < probes; j++) {
                CurrentDetector = PointOnCircle((i + (360 - width) / 2) + j * degreesBetweenDetectors, radius);
                BresenhamDraw(CurrentPoint.x, CurrentPoint.y, CurrentDetector.x, CurrentDetector.y);
            }
          //  sinogramValues.add(meanPixels);
        }
        /************************************************/


        ImageIcon icon = new ImageIcon(this.secondImage);
        pictureLabel.setIcon(icon);
        pictureLabel.revalidate();
        pictureLabel.repaint();
    }

    private int ReturnGrayColor(int x, int y) {
        return rawPicture.getRGB(x, y) & 0x000000ff;
    }

    private void SetRGBv(int x, int y, int R, int G, int B) {
        Color color = new Color(R, G, B);
        int temp = color.getRGB();
        this.secondImage.setRGB(x, y, temp);
    }

    public int GetWidth() {
        return pictureWH;
    }

    private void BresenhamDraw(int x1, int y1, int x2, int y2) {

        int d, dx, dy, ai, bi, xi, yi;
        int x = x1, y = y1;
        float summary = 0;
        int counter = 0;

        // ustalenie kierunku rysowania
        xi = (x1 < x2) ? 1 : -1;
        dx = (x1 < x2) ? (x2 - x1) : (x1 - x2);
        yi = (y1 < y2) ? 1 : -1;
        dy = (y1 < y2) ? (y2 - y1) : (y1 - y2);

        SetRGBv(x, y, 255, 0, 0);
        int rgb = rawPicture.getRGB(x, y) & 0xFF;
        summary += rgb;
        counter++;

        // oś wiodąca OX
        if (dx > dy) {
            ai = (dy - dx) * 2;
            bi = dy * 2;
            d = bi - dx;
            // pętla po kolejnych x
            while (x != x2) {
                // test współczynnika
                x += xi;
                if (d >= 0) {
                    y += yi;
                    d += ai;
                } else {
                    d += bi;
                }

                rgb = rawPicture.getRGB(x, y) & 0xFF;
                summary += rgb;
                SetRGBv(x, y, 255, 0, 0);
                counter++;

            }
        } else { // oś wiodąca OY
            ai = (dx - dy) * 2;
            bi = dx * 2;
            d = bi - dy;
            // pętla po kolejnych y
            while (y != y2) {
                // test współczynnika
                y += yi;
                if (d >= 0) {
                    x += xi;
                    d += ai;
                } else {
                    d += bi;
                }
                SetRGBv(x, y, 255, 0, 0);
                rgb = rawPicture.getRGB(x, y) & 0xFF;
                summary += rgb;
                counter++;
            }
        }

        summary = summary / (counter * topLight);
        meanPixels.add(summary);
    }
}

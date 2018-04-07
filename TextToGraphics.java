import java.io.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class TextToGraphics {

    private static String FILE_ENCODING = "utf-8";
    private static String OUTPUT_DIRECTORY = "output";

    public static void main(String[] args) {
        ArrayList<String> texts = loadTextsFromFile("arabic.txt");
        System.out.println("Processing " + texts.size() + " entries");
        for (int i = 0; i < texts.size(); i++) {
            createImage(texts.get(i), "file" + (i + 1) + ".png");
        }
    }

    private static ArrayList<String> loadTextsFromFile(String fileName) {
        ArrayList<String> toReturn = new ArrayList<String>();

        try {
            FileInputStream stream = new FileInputStream(fileName);
            InputStreamReader streamReader = new InputStreamReader(stream, FILE_ENCODING);
            BufferedReader br = new BufferedReader(streamReader);

            String line;

            while ((line = br.readLine()) != null) {
                toReturn.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    private static void createImage(String text, String outputFileName) {       
        /*
        Because font metrics is based on a graphics context, we need to create
        a small, temporary image so we can ascertain the width and height
        of the final image
        */
        try {            
            BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            Font font = new Font("Arial", Font.PLAIN, 48);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            int width = fm.stringWidth(text);
            int height = fm.getHeight();
            g2d.dispose();

            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2d.setFont(font);
            fm = g2d.getFontMetrics();
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, 0, fm.getAscent());
            g2d.dispose();

            String fullFileName = OUTPUT_DIRECTORY + "\\" + outputFileName;
            ImageIO.write(img, "png", new File(fullFileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
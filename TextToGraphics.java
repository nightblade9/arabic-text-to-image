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
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.gson.Gson;

public class TextToGraphics {

    private static final String FILE_ENCODING = "utf-8";
    private static final String OUTPUT_DIRECTORY = "output";
    private static final int FONT_SIZE = 64; // shrink to 32 in-game
    private static final Color TEXT_COLOUR = Color.WHITE;
    private static HashMap<String, String> TRANSLITERATION_TABLE;

    static {
        TRANSLITERATION_TABLE = new HashMap<String, String>();
        TRANSLITERATION_TABLE.put("ا", "a"); TRANSLITERATION_TABLE.put("أ", "a");
        TRANSLITERATION_TABLE.put("ب", "b"); TRANSLITERATION_TABLE.put("ت", "t");
        TRANSLITERATION_TABLE.put("ث", "th"); TRANSLITERATION_TABLE.put("ج", "j");
        TRANSLITERATION_TABLE.put("ح", "7"); TRANSLITERATION_TABLE.put("خ", "kh");
        TRANSLITERATION_TABLE.put("د", "d"); TRANSLITERATION_TABLE.put("ذ", "dh");
        TRANSLITERATION_TABLE.put("ر", "r"); TRANSLITERATION_TABLE.put("ز", "z");
        TRANSLITERATION_TABLE.put("س", "s"); TRANSLITERATION_TABLE.put("ش", "sh");
        TRANSLITERATION_TABLE.put("ص", "s"); TRANSLITERATION_TABLE.put("ض", "d"); 
        TRANSLITERATION_TABLE.put("ط", "t"); TRANSLITERATION_TABLE.put("ظ", "th");
        TRANSLITERATION_TABLE.put("ع", "3"); TRANSLITERATION_TABLE.put("غ", "gh");
        TRANSLITERATION_TABLE.put("ف", "f"); TRANSLITERATION_TABLE.put("ق", "q");
        TRANSLITERATION_TABLE.put("ك", "k"); TRANSLITERATION_TABLE.put("ل", "l");
        TRANSLITERATION_TABLE.put("م", "m"); TRANSLITERATION_TABLE.put("ن", "n");
        TRANSLITERATION_TABLE.put("ه", "h"); TRANSLITERATION_TABLE.put("و", "w");
        TRANSLITERATION_TABLE.put("ي", "y"); TRANSLITERATION_TABLE.put("ى", "y");
        TRANSLITERATION_TABLE.put("ء", "a");
        // tashkeel
        TRANSLITERATION_TABLE.put("ٌ", "u"); 
        TRANSLITERATION_TABLE.put("َ", "a"); TRANSLITERATION_TABLE.put("ِ", "i"); 
        TRANSLITERATION_TABLE.put("ٌ", "un"); TRANSLITERATION_TABLE.put("ُ", "u"); 
        TRANSLITERATION_TABLE.put("ٍ", "in");
        // ta-marbuwtah; is rarely in the middle of a word.
        TRANSLITERATION_TABLE.put("ة", "h");         
    }

    public static void main(String[] args) {
        ArrayList<Word> texts = loadTextsFromFile("words.json");
        System.out.println("Processing " + texts.size() + " entries");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output\\words.json"));
            writer.write("[\r\n");

            for (int i = 0; i < texts.size(); i++) {
                Word word = texts.get(i);
                word.transliteration = transliterate(word.arabic);
                String outputFilename = word.transliteration + ".png";
                createImage(word.arabic, outputFilename);
                writer.write(String.format(
                    "\t{ \"arabic\": \"%s\", \"english\": \"%s\", \"transliteration\": \"%s\" },\r\n",
                    word.arabic, word.english, word.transliteration));
            }

            writer.write("]\r\n");
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Word> loadTextsFromFile(String fileName) {
        ArrayList<Word> toReturn = new ArrayList<Word>();
        Gson g = new Gson();

        try {
            FileInputStream stream = new FileInputStream(fileName);
            InputStreamReader streamReader = new InputStreamReader(stream, FILE_ENCODING);
            BufferedReader br = new BufferedReader(streamReader);

            String line;

            while ((line = br.readLine()) != null) {
                if (line.length() == 1) {
                    // opening/closing brace
                    continue;
                }
                line = line.substring(0, line.length() - 1); // trim trailing comma
                Word w = g.fromJson(line, Word.class);
                toReturn.add(w);
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
            Font font = new Font("Arial", Font.PLAIN, FONT_SIZE);
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
            g2d.setColor(TEXT_COLOUR);
            g2d.drawString(text, 0, fm.getAscent());
            g2d.dispose();

            String fullFileName = OUTPUT_DIRECTORY + "\\" + outputFileName;
            ImageIO.write(img, "png", new File(fullFileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String transliterate(String s) {
        String toReturn = s.toString();
        
        for (Map.Entry<String, String> entry : TRANSLITERATION_TABLE.entrySet()) {
            String arabicLetter = entry.getKey();
            String transliterationText = entry.getValue();
            toReturn = toReturn.replace(arabicLetter, transliterationText);
        }

        // Shaddah is the letter before it
        for (int i = 0; i < toReturn.length(); i++) {
            char c = toReturn.charAt(i);
            if (c == 'ّ') {            
                toReturn = toReturn.substring(0, i) // up to, excluding shaddah
                + toReturn.charAt(i - 1) // repeat last letter
                + toReturn.substring(i + 1); // skip shaddah, everything else
            }
        }
        
        // If it starts with 3 or 7, replace with x3 or x7. This is because filenames that
        // start with images break OpenFL; see: https://github.com/openfl/openfl/issues/1876
        if (toReturn.startsWith("3") || toReturn.startsWith("7")) {
            toReturn = "x" + toReturn;
        }

        return toReturn;
    }
}
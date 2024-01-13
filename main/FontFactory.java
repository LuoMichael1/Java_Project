package main;

import java.io.File;
import java.awt.Font;

// Can use this to make fonts which may be better than having all the fonts in the main class
public class FontFactory {

    // new FontFactory.loadFont(filepath, fontsize)
    public static Font loadFont(String path, float size) {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
        } catch (Exception e) {
            System.out.println("Couldn't get font");
        }
        return font;
    }
}

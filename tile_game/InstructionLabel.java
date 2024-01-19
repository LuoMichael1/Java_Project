// This class handles the instruction label at the bottom of the screen during map gameplay.
// By Alec

package tile_game;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.awt.*;

import main.FontFactory;
import main.Main;

public class InstructionLabel {

    private static final String FILE_PATH = "dialogue/instruction-labels.txt";
    private static final int DRAW_Y = (int) (Main.HEIGHT * 0.87);
    private static final int BORDER_WIDTH = 2;
    private static final int LETTER_SPACING = 20;
    private static final String[] events = new String[] { "chest", "vent", "orb", "inVent", "walk",
            "openChest" };

    private static List<String> lines;
    private static int drawX;

    public static void loadInstructionLabels() {

        lines = new ArrayList<>();

        try {
            lines = Files.readAllLines(Paths.get(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Determine what text to display, then display the text
    public static void drawLine(Graphics2D graphic, String event) {

        int lineNumber = -1;

        for (int i = 0; i < events.length; i++) {

            if (events[i].equals(event))
                lineNumber = i;
        }

        if (lineNumber == -1 || event == null) {
            System.out.println("Invalid event");
            return;
        }

        displayText(graphic, lines.get(lineNumber));
    }

    // Display a message
    private static void displayText(Graphics2D graphic, String message) {

        // Calculate x position of text by subtracting half of the message width from
        // the window's halfway point
        drawX = Main.WIDTH / 2 - (LETTER_SPACING * (message.length() / 2));

        // Set anti-aliasing for a cleaner look
        graphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphic.setFont(FontFactory.loadFont("QuinqueFive_Font_1_1/QuinqueFive.ttf", 18));
        graphic.setColor(Color.BLACK);

        // Draw a black border around the text
        graphic.drawString(message, drawX - BORDER_WIDTH, DRAW_Y - BORDER_WIDTH);
        graphic.drawString(message, drawX + BORDER_WIDTH, DRAW_Y + BORDER_WIDTH);
        graphic.drawString(message, drawX + BORDER_WIDTH, DRAW_Y - BORDER_WIDTH);
        graphic.drawString(message, drawX - BORDER_WIDTH, DRAW_Y + BORDER_WIDTH);

        graphic.setColor(Color.WHITE);
        graphic.drawString(message, drawX, DRAW_Y);
    }
}

package tile_game;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.awt.*;

import main.FontFactory;
import main.Main;

public class InstructionLabel {

    private static List<String> lines;
    private static final String FILE_PATH = "dialogue/instruction-labels.txt";
    private static final int drawY = (int) (Main.HEIGHT * 0.87);
    private static final int BORDER_WIDTH = 2;
    private static int drawX;

    public static void loadInstructionLabels() {

        lines = new ArrayList<>();

        try {
            lines = Files.readAllLines(Paths.get(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawLine(Graphics2D graphic, String event) {

        if (event == null)
            return;

        int lineNumber;

        switch (event) {
            case "chest":
                lineNumber = 0;
                break;
            case "vent":
                lineNumber = 1;
                break;
            case "trigger":
                lineNumber = 2;
                break;
            case "orb":
                lineNumber = 3;
                break;
            case "inVent":
                lineNumber = 4;
                break;
            case "walk":
                lineNumber = 5;
                break;
            case "openChest":
                lineNumber = 6;
                break;
            default:
                System.out.println("Invalid input");
                return;
        }

        displayText(graphic, lines.get(lineNumber));
    }

    public static void displayText(Graphics2D graphic, String message) {

        drawX = Main.WIDTH / 2 - (20 * (message.length() / 2));

        graphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphic.setFont(FontFactory.loadFont("QuinqueFive_Font_1_1/QuinqueFive.ttf", 18));
        graphic.setColor(Color.BLACK);

        // Draw a black border around the text
        graphic.drawString(message, drawX - BORDER_WIDTH, drawY - BORDER_WIDTH);
        graphic.drawString(message, drawX + BORDER_WIDTH, drawY + BORDER_WIDTH);
        graphic.drawString(message, drawX + BORDER_WIDTH, drawY - BORDER_WIDTH);
        graphic.drawString(message, drawX - BORDER_WIDTH, drawY + BORDER_WIDTH);

        graphic.setColor(Color.WHITE);
        graphic.drawString(message, drawX, drawY);
    }
}

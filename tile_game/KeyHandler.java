package tile_game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class KeyHandler implements KeyListener {

    private HashSet<Integer> pressedKeys = new HashSet<>();
    private HashSet<Integer> handledKeys = new HashSet<>();

    public KeyHandler() {
        // System.out.println("Keyhandler made");
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("Keyhandler doing things");
        int code = e.getKeyCode();

        pressedKeys.add(code);
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        pressedKeys.remove(code);
        handledKeys.remove(code);
    }

    public boolean isKeyPressed(int keyCode) {
        if (pressedKeys.contains(keyCode) && !handledKeys.contains(keyCode)) {
            handledKeys.add(keyCode);
            return true;
        }
        return false;
    }

    public boolean isKeyHeld(int keyCode) {

        return pressedKeys.contains(keyCode);
    }
}

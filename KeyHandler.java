import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class KeyHandler implements KeyListener {

    boolean up, down, left, right;

    private HashSet<Integer> pressedKeys = new HashSet<>();
    private HashSet<Integer> handledKeys = new HashSet<>();

    public KeyHandler() {
        //System.out.println("Keyhandler made");
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Keyhandler doing things");
        int code = e.getKeyCode();

        switch (code) {

            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
        }

        pressedKeys.add(code);
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        switch (code) {

            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
        }

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
}

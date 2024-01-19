package main;
// Use to play music and sound effects

// used this website for help with the music
// https://www.geeksforgeeks.org/play-audio-file-using-java/

import java.io.File;
import javax.sound.sampled.*;

public class Music {

    private Clip clip;

    // play a sound and you can specifiy how many times it repeats. Setting how many
    // times it loops to -1 will make it loop forever
    public Music(String filePath, int loop) {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filePath)));

            if (loop == -1)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.loop(loop);
            stop();
        } catch (Exception e) {
            System.out.println("Something went wrong with starting the music" + e);
        }
    }

    public void stop() {
        clip.stop();
    }

    public void start() {
        clip.start();
    }

}

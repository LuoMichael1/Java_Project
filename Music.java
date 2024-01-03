// Use to play music
// MP3s will probably not work

// used this website for help with the music
// https://www.geeksforgeeks.org/play-audio-file-using-java/

import java.io.File; 
import javax.sound.sampled.*;

public class Music { 

	private Clip clip; 	

    // play a song on repeat until the method stop is called
	public Music(String filePath) { 
        try {
            clip = AudioSystem.getClip(); 
            clip.open(AudioSystem.getAudioInputStream(new File(filePath))); 
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            clip.start();
        }
        catch (Exception e) {
            System.out.println("Something went wrong with the music");
        } 
	} 
    // play a sound but you can specifiy how many times it repeats
    public Music(String filePath, int loop) { 
        try {
            clip = AudioSystem.getClip(); 
            clip.open(AudioSystem.getAudioInputStream(new File(filePath))); 
            clip.loop(loop); 
            clip.start();
        }
        catch (Exception e) {
            System.out.println("Something went wrong with the music");
        } 
	} 
	
	// stop the music 
	public void stop() { 
		clip.stop(); 
		clip.close(); 
	} 

} 

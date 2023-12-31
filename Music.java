// idk what this is
// halp, the docs are no use
//https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/contents.html


// used this website
// https://www.geeksforgeeks.org/play-audio-file-using-java/



import java.io.File; 
import javax.sound.sampled.*;

public class Music { 

	private Clip clip; 	

	public Music() { 
        try {
            clip = AudioSystem.getClip(); 
            clip.open(AudioSystem.getAudioInputStream(new File("music/bg.wav"))); 
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            clip.start();
        }
        catch (Exception e) {
            System.out.println("Something went wrong with the music");
        } 
	} 
	
	// Method to stop the audio 
	public void stop() { 
		clip.stop(); 
		clip.close(); 
	} 

} 

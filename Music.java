// idk what this is
// halp, the docs are no use
//https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/contents.html


// used this website
// https://www.geeksforgeeks.org/play-audio-file-using-java/



import java.io.File; 
import javax.sound.sampled.*;

public class Music { 

	Clip clip; 	
	AudioInputStream audioInputStream; 

	public Music() { 
        try {
		// create AudioInputStream object 
		audioInputStream = AudioSystem.getAudioInputStream(new File("music/bg.wav")); 
		
		// create clip reference 
		clip = AudioSystem.getClip(); 
		
		// open audioInputStream to the clip 
		clip.open(audioInputStream); 
		
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

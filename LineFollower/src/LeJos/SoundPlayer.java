package LeJos;

import java.io.File;
import lejos.hardware.Sound;

/**
 *The SoundPlayer class manages background sound playback. It also operates independently of the robot's core
 *functional threads
 * 
 *<p>Its {@code run} method, plays a designated sound file in a loop until stopped,
 * allowing continuous background music
 */
public class SoundPlayer extends Thread {
    private volatile boolean running = true;

    public void run() {
        File soundFile = new File("Mortal Kom.wav"); 
        while (running) {
            if (soundFile.exists()) {
                Sound.playSample(soundFile, Sound.VOL_MAX);
            } else {
                System.out.println("File not found");
                break;
            }
        }
    }

    // Method to stop the sound
    public void stopPlaying() {
        running = false;
    }
}

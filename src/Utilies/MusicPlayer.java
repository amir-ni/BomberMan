package Utilies;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicPlayer {
    public static void play(String soundFile,boolean loop){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            if(loop)
                clip.loop(0x7FFFFFFF);
            clip.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

package JGamePackage.JGame.GameObjects;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound extends GameObject{
    private Clip sound;
    private File file;
    public double Volume = 1.0;
    public boolean Playing;
    public final double Length;

    public Sound(File file){
        this.file = file;


        try {
            sound = AudioSystem.getClip();
            sound.open(AudioSystem.getAudioInputStream(this.file));
        } catch (Exception e) {
            throw new Error(e);
        }

        Length = (double) sound.getMicrosecondLength()*0.000001;
    }

    public Sound(String path){
        this(new File(path));
    }

    /**Reumes this sounds' playback from the beginning
     * 
     */
    public void Play(){
        Playing = true;
        this.sound.setFramePosition(0);
        this.sound.start();
        Playing = false;
    }

    /**Reumes this sounds' playback
     * 
     */
    public void UnPause(){
        Playing = true;
        this.sound.start();
    }

    /**Pauses playback of this sound
     * 
     */
    public void Pause(){
        Playing = false;
        this.sound.stop();
    }

    /**Pauses playback of this Sound and returns the frame position to 0
     * 
     */
    public void Stop(){
        this.Pause();
        this.sound.setFramePosition(0);
    }

    public void setInfiniteLoop(boolean shouldLoop){
        this.sound.loop(shouldLoop ? Clip.LOOP_CONTINUOUSLY : 0);
    }

    public void setLoop(int loops){
        this.sound.loop(loops);
    }

    public void SetVolume(double volume){
        FloatControl gainControl = (FloatControl) this.sound.getControl(FloatControl.Type.MASTER_GAIN);

        double range = gainControl.getMaximum() - gainControl.getMinimum();
        double gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue((float) gain);
        this.Volume = volume;
    }

    public void SetFramePosition(int pos){
        this.sound.setFramePosition(pos);
    }

    public boolean isPlaying(){
        return this.sound.isRunning();
    }
}

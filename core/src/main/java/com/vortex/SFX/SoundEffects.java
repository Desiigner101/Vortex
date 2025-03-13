package com.vortex.SFX;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public interface SoundEffects {
    public void playSoundEffect(String soundFile, double delay) throws UnsupportedAudioFileException, IOException, LineUnavailableException;
    public void playMusic(String musicFile);
    public void stopAudio();
    public void stopMusic();
    public void stopSoundEffect();
}

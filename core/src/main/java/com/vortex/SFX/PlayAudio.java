package com.vortex.SFX;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

public class PlayAudio implements SoundEffects{
    /*
     * Instructions for soundClass by Danidani
     * there's 5 codes
     * playMusic(String) - Enter the music name on the parameter(with the .wav), it will continously loop until the stopMusic method is called
     * playSoundEffect(String,double) - Enter the sfx name on the parameter(with the .wav), it will automatically stop once the sfx finishes.
     * stopMusic() - stops the music loop
     * stopSoundEffect() - stops the sound effect currently playing
     * stopAudio() - stops both the music loop and sound effect currently playing
     *
     * requirements:
     * you need to have 2 packages in your src file named MusicFolder and SoundEffectsFolder
     * src file, the MusicFolder should contain your music and SoundEffectsFolder should
     * contain your sfx files.
     *
     * LEZGAW MU GANA NA JUD KAPOYA
     * */
    private Clip soundEffectClip;
    private Clip musicClip;

    @Override
    public void playSoundEffect(String soundFile, double delay) {
        int delayMs = (int) (delay * 1000); // Convert seconds to milliseconds

        Timer timer = new Timer(delayMs, e -> {
            try {
                URL fileURL = PlayAudio.class.getResource("/SoundEffectsFolder/" + soundFile);
                if (fileURL == null) {
                    System.out.println("Sound effect file not found: " + soundFile);
                    return;
                }
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileURL);
                soundEffectClip = AudioSystem.getClip();
                soundEffectClip.open(audioStream);
                soundEffectClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        System.out.println("Sound effect finished playing.");
                        soundEffectClip.close();
                    }
                });
                soundEffectClip.start();
            } catch (Exception ex) {
                System.out.println("Error playing sound effect: " + ex.getMessage());
            }
        });

        timer.setRepeats(false); // Run only once
        timer.start(); // Start the delay
    }

    @Override
    public void playMusic(String musicFile) {//amp duagay nako na human ani bruhhhh basta mu gana na wtffff
        try {
            URL fileURL = PlayAudio.class.getResource("/MusicFolder/" + musicFile);

            if (fileURL == null) {
                System.out.println("Music file not found: " + musicFile);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileURL);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);

            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();

            System.out.println("Music is playing...");

        } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
        }
    }

    @Override
    public void stopAudio() {

        if (soundEffectClip != null && soundEffectClip.isRunning()) {
            soundEffectClip.stop();
            soundEffectClip.setMicrosecondPosition(0);
            System.out.println("Sound effect stopped.");
        }

        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.setMicrosecondPosition(0);
            System.out.println("Music stopped.");
        }
    }

    @Override
    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.setMicrosecondPosition(0);
            System.out.println("Music stopped.");
        }
    }

    @Override
    public void stopSoundEffect() {
        if (soundEffectClip != null && soundEffectClip.isRunning()) {
            soundEffectClip.stop();
            soundEffectClip.setMicrosecondPosition(0);
            System.out.println("Sound effect stopped.");
        }
    }
}

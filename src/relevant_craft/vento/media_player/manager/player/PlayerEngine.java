package relevant_craft.vento.media_player.manager.player;

import org.tritonus.share.sampled.file.TAudioFileFormat;
import relevant_craft.vento.media_player.gui.main.elements.control.Control;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class PlayerEngine implements Runnable {
    private final int BUFFER_SIZE = 1024 * 4;
    private final File file;
    private final byte[] mutedSound;
    private final Control control;

    private AudioInputStream in;
    private FloatControl volume;
    long frameSize;
    float sampleRate;
    long readFrames;
    private boolean isMuted;
    private boolean isPaused;
    private boolean isRunning;
    private Thread player;

    private long audioLength;
    private long secondsTotal;
    private long secondsCurrent;
    private long bytesPerSecond;
    private int bitrate;
    private String title;
    private String author;

    /**
     * Init player engine
     */
    public PlayerEngine(String path, Control control) {
        this.file = new File(path);
        this.mutedSound = this.getMutedSound();
        this.control = control;
        this.isMuted = false;
        this.isPaused = false;
        this.isRunning = false;

        this.loadAudioData();
    }

    /**
     * Prepare and play music
     */
    @Override
    public void run() {
        isRunning = true;
        this.renderCurrentPosition();

        try {
            in = AudioSystem.getAudioInputStream(file);
            AudioFormat format = this.getOutFormat(in.getFormat());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            //prepare line
            try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                if (line != null) {
                    line.open(format, BUFFER_SIZE);
                    line.start();

                    //volume
                    volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                    this.setVolume(control.getVolumeSlider().getProgress());

                    //stream audio
                    this.stream(AudioSystem.getAudioInputStream(format, in), line);

                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Play audio
     */
    public void play() throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        //run player
        try {
            player = new Thread(this);
            player.setPriority(Thread.MAX_PRIORITY);
            player.start();
        } catch (Exception ignored) {
        }
    }

    /**
     * Pause/resume audio
     */
    public void pause(boolean value) {
        isPaused = value;
    }

    /**
     * Mute audio
     */
    public void setMuted(boolean value) {
        isMuted = value;
    }

    /**
     * Set position
     */
    public void setPosition(double percentage) {
        try {
            //stop player
            isRunning = false;
            player.join();

            //set new position
            secondsCurrent = percentageToTime(percentage);

            //start player
            play();
        } catch (Exception ignored) {
        }
    }

    /**
     * Set volume
     */
    public void setVolume(double percentage) {
        volume.setValue(20.0f * (float) Math.log10(percentage));
    }

    /**
     * Stream audio
     */
    private void stream(AudioInputStream in, SourceDataLine line) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        frameSize = in.getFormat().getFrameSize();
        sampleRate = in.getFormat().getSampleRate();
        readFrames = 0;

        //go to position
        if (secondsCurrent > 0) {
            if (in.skip(secondsCurrent * bytesPerSecond) > 0) {
                readFrames = Math.round(secondsCurrent * sampleRate);
            }
        }

        //audio loop
        while (isRunning) {
            //check pause
            if (isPaused) {
                sleep(100);
                continue;
            }

            //read audio bytes
            int n = in.read(buffer, 0, buffer.length);
            if (n == -1) {
                return;
            }
            readFrames += n / frameSize;

            //write audio bytes
            line.write((isMuted ? mutedSound : buffer), 0, n);

            //update current time
            long secondsElapsed = Math.round(readFrames / sampleRate);
            if (secondsCurrent != secondsElapsed) {
                secondsCurrent = secondsElapsed;
                this.renderCurrentPosition();
            }
        }
    }

    /**
     * Create decoded audio format
     */
    private AudioFormat getOutFormat(AudioFormat format) {
        int channels = format.getChannels();
        float rate = format.getSampleRate();

        return new AudioFormat(PCM_SIGNED, rate, 16, channels, channels * 2, rate, false);
    }

    /**
     * Create muted sound
     */
    private byte[] getMutedSound() {
        byte[] mutedSound = new byte[BUFFER_SIZE];
        Arrays.fill(mutedSound, (byte) 0);

        return mutedSound;
    }

    /**
     * Get audio data
     */
    private void loadAudioData() {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioFormat format = this.getOutFormat(in.getFormat());

            //seconds current
            secondsCurrent = 0;

            //bytes length
            audioLength = in.available();

            //bitrate
            bitrate = (int) in.getFormat().properties().get("bitrate");

            AudioFileFormat baseFormat = AudioSystem.getAudioFileFormat(file);
            if (baseFormat instanceof TAudioFileFormat) {
                Map<?, ?> props = baseFormat.properties();

                //seconds length
                secondsTotal = ((Long) props.get("duration") / 1000000);

                //title
                title = (String) props.get("title");

                //author
                author = (String) props.get("author");

                //TODO image
            } else {
                //seconds length
                secondsTotal = (long) (baseFormat.getFrameLength() / baseFormat.getFormat().getFrameRate());

                //bitrate
                bitrate = (int) (format.getSampleSizeInBits() * format.getSampleRate() * format.getChannels() / 1000);
            }

            //bytes per second
            bytesPerSecond = audioLength / secondsTotal;

            in.close();
        } catch (Exception e) {
            //TODO throw
        }
    }

    /**
     * Render current position
     */
    private void renderCurrentPosition() {
        control.getSongSlider().setCurrentValue(secondsCurrent);
        control.getSongSlider().setProgress(timeToPercentage());
    }

    /**
     * Time to percentage
     */
    private double timeToPercentage() {
        return (double) secondsCurrent / secondsTotal;
    }

    /**
     * Percentage to time
     */
    private long percentageToTime(double percentage) {
        return (long) (percentage * secondsTotal);
    }

    /**
     * Sleep thread
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {
        }
    }

    /**
     * Return total seconds
     */
    public long getSecondsTotal() {
        return secondsTotal;
    }

    /**
     * Return current seconds
     */
    public long getSecondsCurrent() {
        return secondsCurrent;
    }

    /**
     * Return audio bitrate
     */
    public int getBitrate() {
        return bitrate;
    }

    /**
     * Return title of song
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return author of song
     */
    public String getAuthor() {
        return author;
    }
}

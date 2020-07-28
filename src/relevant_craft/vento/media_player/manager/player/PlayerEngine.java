package relevant_craft.vento.media_player.manager.player;

import relevant_craft.vento.media_player.gui.main.elements.control.Control;
import relevant_craft.vento.media_player.gui.main.elements.playlist.PlaylistItem;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class PlayerEngine implements Runnable {
    private final int BUFFER_SIZE = 1024 * 4;
    private final float NORMALIZATION_FACTOR = Short.MAX_VALUE + 1.0F;
    private final Control control;
    private final byte[] mutedSound;

    private FloatControl volume;
    private boolean isMuted;
    private boolean isPaused;
    private boolean isRunning;
    private Thread player;
    private TimeListener timeListener;
    private LoadListener loadListener;
    private SamplesListener samplesListener;
    private FinishListener finishListener;

    private PlaylistItem song;
    private File file;
    private AudioInputStream in;
    private long audioLength;
    private long secondsCurrent;
    private long bytesPerSecond;
    private long frameSize;
    private float sampleRate;
    private long readFrames;

    /**
     * Init player engine
     */
    public PlayerEngine(Control control) {
        this.control = control;
        this.mutedSound = getMutedSound();
        this.isMuted = false;
        this.isPaused = false;
        this.isRunning = false;
    }

    /**
     * Load audio file
     */
    public void loadAudio(PlaylistItem data) throws FileNotFoundException {
        this.song = data;

        if (isActive()) {
            stop();
        }

        if (isPaused) {
            isPaused = false;
        }

        file = new File(data.getPath());
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        loadAudioData();
    }

    /**
     * Prepare and play music
     */
    @Override
    public void run() {
        isRunning = true;
        isPaused = false;
        control.getPlayButton().setSelected(true);

        //notify time listener
        if (timeListener != null) {
            timeListener.onUpdate(secondsCurrent, timeToPercentage());
        }

        try {
            in = AudioSystem.getAudioInputStream(file);
            AudioFormat format = getOutFormat(in.getFormat());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            //prepare line
            try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                if (line != null) {
                    line.open(format, BUFFER_SIZE);
                    line.start();

                    //volume
                    volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                    setVolume(control.getVolumeSlider().getProgress());

                    //stream audio
                    stream(AudioSystem.getAudioInputStream(format, in), line);

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
            } catch (Exception ignored) {}

            //notify finish listener
            if (finishListener != null && isRunning) {
                player.interrupt();
                finishListener.onSongFinish();
            }
        }
    }

    /**
     * Play audio
     */
    public void play() {
        if (file == null) {
            return;
        }

        //run player
        try {
            player = new Thread(this);
            player.setPriority(Thread.MAX_PRIORITY);
            player.start();
        } catch (Exception ignored) {}
    }

    /**
     * Pause/resume audio
     */
    public void setPaused(boolean value) {
        isPaused = value;
    }

    /**
     * Mute audio
     */
    public void setMuted(boolean value) {
        isMuted = value;
    }

    /**
     * Stop audio
     */
    public void stop() {
        try {
            isRunning = false;
            player.join();
        } catch (Exception ignored) {}
    }

    /**
     * Set position
     */
    public void setPosition(double percentage) {
        //stop player
        stop();

        //set new position
        secondsCurrent = percentageToTime(percentage);

        //start player
        play();
    }

    /**
     * Set volume
     */
    public void setVolume(double percentage) {
        if (volume != null) {
            volume.setValue(20.0F * (float) Math.log10(percentage));
        }
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

            //get samples
            getSamples(buffer, n);

            //update current time
            long secondsElapsed = Math.round(readFrames / sampleRate);
            if (secondsCurrent != secondsElapsed) {
                secondsCurrent = secondsElapsed;
                //notify time listener
                if (timeListener != null) {
                    timeListener.onUpdate(secondsCurrent, timeToPercentage());
                }
            }
        }
    }

    /**
     * Create decoded audio format
     */
    public static AudioFormat getOutFormat(AudioFormat format) {
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

            //seconds current
            secondsCurrent = 0;

            //bytes length
            audioLength = in.available();

            //bytes per second
            bytesPerSecond = audioLength / song.getLength();

            //notify audio loaded listener
            if (loadListener != null) {
                loadListener.onAudioLoaded();
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get audio samples
     */
    private void getSamples(byte[] buffer, int n) {
        float[] samples = new float[BUFFER_SIZE / 2];
        float[] leftSamples = new float[BUFFER_SIZE / 4];
        float[] rightSamples = new float[BUFFER_SIZE / 4];

        for (int i = 0, sampleIndex = 0; i < n; ) {
            //left samples
            int leftSample = 0;
            leftSample |= buffer[i++] & 0xFF;
            leftSample |= buffer[i++] << 8;
            leftSamples[sampleIndex] = leftSample / NORMALIZATION_FACTOR;

            //right samples
            int rightSample = 0;
            rightSample |= buffer[i++] & 0xFF;
            rightSample |= buffer[i++] << 8;
            rightSamples[sampleIndex] = rightSample / NORMALIZATION_FACTOR;

            //left and right samples
            samples[sampleIndex * 2] = leftSamples[sampleIndex];
            samples[sampleIndex * 2 + 1] = rightSamples[sampleIndex];

            sampleIndex++;
        }

        //notify samples updater listener
        if (samplesListener != null) {
            samplesListener.onUpdate(samples, leftSamples, rightSamples);
        }
    }

    /**
     * Time updater listener
     */
    public interface TimeListener {
        void onUpdate(long currentSeconds, double progress);
    }

    /**
     * Add time updater listener
     */
    public void addTimeListener(TimeListener timeListener) {
        this.timeListener = timeListener;
    }

    /**
     * Audio loader listener
     */
    public interface LoadListener {
        void onAudioLoaded();
    }

    /**
     * Add audio loader listener
     */
    public void addLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    /**
     * Samples updater listener
     */
    public interface SamplesListener {
        void onUpdate(float[] samples, float[] leftSamples, float[] rightSamples);
    }

    /**
     * Add samples updater listener
     */
    public void addSamplesListener(SamplesListener samplesListener) {
        this.samplesListener = samplesListener;
    }

    /**
     * Finish listener
     */
    public interface FinishListener {
        void onSongFinish();
    }

    /**
     * Add song finish listener
     */
    public void addSongFinishListener(FinishListener finishListener) {
        this.finishListener = finishListener;
    }

    /**
     * Time to percentage
     */
    private double timeToPercentage() {
        return (double) secondsCurrent / song.getLength();
    }

    /**
     * Percentage to time
     */
    private long percentageToTime(double percentage) {
        return (long) (percentage * song.getLength());
    }

    /**
     * Sleep thread
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {}
    }

    /**
     * Return total seconds
     */
    public long getSecondsTotal() {
        return song.getLength();
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
        return song.getBitRate();
    }

    /**
     * Return title of song
     */
    public String getTitle() {
        return song.getTitle();
    }

    /**
     * Return author of song
     */
    public String getAuthor() {
        return song.getArtist();
    }

    /**
     * Return is player active
     */
    public boolean isActive() {
        if (player != null) {
            return player.isAlive();
        }

        return false;
    }
}

package relevant_craft.vento.media_player.manager.equalizer;

import com.tagtraum.jipes.math.FFTFactory;
import javafx.application.Platform;
import relevant_craft.vento.media_player.gui.main.elements.visualization.Equalizer;

import java.util.Arrays;

public class EqualizerManager {
    private final int BANDS = 32;
    private final Equalizer equalizer;
    private final double[] frequencyBand;
    private final double[] frequencyBandHighest = new double[] {1218.5385525623205, 1347.618679710946, 1387.9433147267268, 1386.3909693310948, 1385.206064084934, 1104.3986581181814, 1376.8629882061189, 1036.3824369083234, 2347.2423677986108, 2224.8655543042996, 2331.405838206991, 2016.644009462555, 2511.5173423950278, 1989.6700166755095, 1741.9179189695822, 2360.819175727214, 3548.5744235717384, 3041.2795205396656, 4085.235148074501, 2842.0596262403824, 5191.211396228532, 6052.530949140744, 4334.672706616708, 4731.409504664507, 12223.75891618153, 10509.177784838628, 7685.602544713116, 9999.921526427977, 20242.145872935736, 20476.21387093248, 14748.332580386888, 15423.612596145133};
    private final double[] bandBuffer;
    private final double[] decreaseBuffer;
    private final double[] audioBandBuffer;

    private FFTFactory.JavaFFT fft;
    private long lastUpdate;

    /**
     * Init equalizer manager
     */
    public EqualizerManager(Equalizer equalizer) {
        this.equalizer = equalizer;
        this.frequencyBand = new double[BANDS];
        this.bandBuffer = new double[BANDS];
        this.decreaseBuffer = new double[BANDS];
        this.audioBandBuffer = new double[BANDS];
        this.lastUpdate = System.currentTimeMillis();

        this.initEqualizerLevelChecker();
    }

    /**
     * Set equalizer bands' level
     */
    public void setLevels(float[] samples) {
        if (fft == null) {
            fft = new FFTFactory.JavaFFT(samples.length);
        }

        //apply FFT
        float[][] transformed = fft.transform(samples);
        float[] realPart = transformed[0];
        float[] imaginaryPart = transformed[1];
        double[] magnitudes = convertToMagnitudes(realPart, imaginaryPart);

        //convert to db
        for (int i = 0; i < magnitudes.length; i++) {
            magnitudes[i] = 20 * Math.log(magnitudes[i]);
        }

        //make frequency bands (for 32 bands)
        int counter = 0;
        int sampleCounter = 1;
        int power = 0;

        /*  0-7   = 1 sample	= 8
            8-15  = 2 samples	= 16
            16-19 = 4 samples	= 16
            20-23 = 6 samples	= 24
            24-27 = 16 samples	= 64
            28-31 = 32 samples	= 128
                                  ---
                                  256   */

        for (int i = 0; i < BANDS; i++) {
            double average = 0;

            if (i == 8 || i == 16 || i == 20 || i == 24 || i == 28) {
                power++;
                sampleCounter = (int) Math.pow(2, power);

                if (power == 3) {
                    sampleCounter -= 2;
                }
            }

            for (int k = 0; k < sampleCounter; k++) {
                double sample = magnitudes[counter];
                average += sample * (counter + 1);
                counter++;
            }

            average /= counter;
            frequencyBand[i] = average * 10;

            //compare with buffer
            if (frequencyBand[i] > bandBuffer[i]) {
                bandBuffer[i] = frequencyBand[i];
                decreaseBuffer[i] = frequencyBandHighest[i] * 0.02;      //decrease speed
            } else if (frequencyBand[i] < bandBuffer[i]) {
                bandBuffer[i] -= decreaseBuffer[i];
            }

            //update highest bands
            if (frequencyBand[i] > frequencyBandHighest[i]) {
                frequencyBandHighest[i] = frequencyBand[i];
            }

            //create audio bands
            audioBandBuffer[i] = bandBuffer[i] / frequencyBandHighest[i];

            //render
            equalizer.setBandLevel(i, audioBandBuffer[i]);
            lastUpdate = System.currentTimeMillis();
        }
    }

    /**
     * Convert to magnitudes
     */
    private double[] convertToMagnitudes(float[] realPart, float[] imaginaryPart) {
        double[] magnitudes = new double[realPart.length / 2];
        for (int i = 0; i < magnitudes.length; i++) {
            magnitudes[i] = Math.sqrt(Math.pow(realPart[i], 2) + Math.pow(imaginaryPart[i], 2));
        }

        return magnitudes;
    }

    /**
     * Init equalizer bands' level checker
     */
    private void initEqualizerLevelChecker() {
        Thread checker = new Thread(() -> {
            while (true) {
                if (System.currentTimeMillis() - lastUpdate > 50) {
                    //audio bands
                    if (Arrays.stream(audioBandBuffer).max().getAsDouble() > 0.0) {
                        for (int i = 0; i < audioBandBuffer.length; i++) {
                            if (audioBandBuffer[i] > 0.0) {
                                bandBuffer[i] -= decreaseBuffer[i] * 1.5;       //decrease speed
                                audioBandBuffer[i] = bandBuffer[i] / frequencyBandHighest[i];
                                final int band = i;
                                Platform.runLater(() -> equalizer.setBandLevel(band, audioBandBuffer[band]));
                            }
                        }
                    } else {
                        //reset to default
                        for (int i = 0; i < audioBandBuffer.length; i++) {
                            final int band = i;
                            Platform.runLater(() -> equalizer.setBandLevel(band, 0));
                        }
                    }
                }

                try {
                    Thread.sleep(30);
                } catch (Exception ignored) {}
            }
        });
        checker.start();
    }
}

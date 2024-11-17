package com.github.coderodde.file.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a program for counting byte histograms in files.
 * 
 * @version 1.0.2 (Nov 17, 2024)
 * @since 1.0.0 (Nov 13, 2024)
 */
public final class ByteHistogramApp {
    
    private static final Logger LOGGER = 
            Logger.getLogger(ByteHistogram.class.getSimpleName());

    public static void main(String[] args) {
        final ByteHistogram byteHistogram = new ByteHistogram();
        
        if (args.length == 0) {
            try {
                loadByteHistogramFromStdin(byteHistogram);
            } catch (IOException ex) {
                LOGGER.log(
                        Level.SEVERE, 
                        "Could not load the histogram from " + 
                        "standard input stream.");
            }
        } else {
            for (final String fileName : args) {
                try {
                    loadByteHistogramFromFile(fileName, byteHistogram);
                } catch (IOException ex) {
                    LOGGER.log(
                            Level.SEVERE,
                            "Could not load the histogram from file \"{0}\".", 
                            fileName);
                }
            }
        }
        
        System.out.println(byteHistogram);
    }
    
    /**
     * Loads the byte histogram data only from standard input.
     * 
     * @param byteHistogram the byte histogram for holding the statistics.
     * 
     * @throws IOException if I/O fails.
     */
    private static void loadByteHistogramFromStdin(
            final ByteHistogram byteHistogram) throws IOException {
        
        loadImpl(new BufferedInputStream(System.in),
                 byteHistogram);
    }
    
    /**
     * Loads the byte histogram data only from the file named {@code fileName}.
     * 
     * @param fileName      the name of the file to load from.
     * @param byteHistogram the byte histogram for holding the statistics.
     * 
     * @throws FileNotFoundException if file named {@code fileName} is not 
     *                               found.
     * @throws IOException if I/O fails.
     */
    private static void loadByteHistogramFromFile(
            final String fileName,
            final ByteHistogram byteHistogram)
            throws FileNotFoundException, IOException {
        
        final InputStream is =
                new BufferedInputStream(
                        new FileInputStream(fileName));
        
        loadImpl(is, byteHistogram);
    }
    
    /**
     * Performs the actual loading of the data to the input byte histogram.
     * 
     * @param is            the input stream from which to load the data.
     * @param byteHistogram the target byte histogram.
     * 
     * @throws IOException if I/O fails.
     */
    private static void loadImpl(final InputStream is, 
                                 final ByteHistogram byteHistogram) 
            throws IOException {
        int ch;
        
        while ((ch = is.read()) != -1) {
            byteHistogram.insert(ch);
        }
    }
}

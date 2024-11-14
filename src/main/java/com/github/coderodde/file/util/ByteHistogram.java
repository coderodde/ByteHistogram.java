package com.github.coderodde.file.util;

/**
 * This class implements the byte histogram.
 * 
 * @version 1.0.0 (Nov 13, 2024)
 * @since 1.0.0 (Nov 13, 2024)
 */
public final class ByteHistogram {
    
    private static final int HISTOGRAM_CAPACITY = 256;
    private static final int SCREEN_WIDTH = 80;
    private static final int LINE_PREAMBLE_WIDTH = 11;
    
    private final long[] data = new long[HISTOGRAM_CAPACITY];
    
    /**
     * Account the byte {@code b}.
     * 
     * @param b the byte to account. 
     */
    public void insert(final int b) {
        data[b]++;
    }
    
    /**
     * Converts this byte histogram to an ASCII art.
     * 
     * @return ASCII art version of this byte histogram.  
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final long maximumCount = computeMaximumCount();
        final int countStringLength = 
                computeCountStringLength(maximumCount);
        
        final String lineFormat = getLineFormat(countStringLength);
        
        for (int i = 0; i < data.length; i++) {
            loadLine(sb,
                     lineFormat,
                     countStringLength,
                     i,
                     maximumCount);
        }
        
        return sb.toString();
    }
    
    /**
     * Builds the format for printing the lines in the output.
     * 
     * @param countStringLength the count string length in characters.
     * 
     * @return the format for printing the lines in the output.
     */
    private static String getLineFormat(final int countStringLength) {
        return String.format("0x%%02x [%%c]: %% %dd %%s\n", countStringLength);
    }
    
    /**
     * Loads a single line to the total output of this byte histogram.
     * 
     * @param sb                the string builder.
     * @param lineFormat        the format of the line.
     * @param countStringLength the length of the count string.
     * @param index             the byte index.
     * @param maximumCount      the maximum count in the histogram.
     */
    private void loadLine(final StringBuilder sb, 
                          final String lineFormat,
                          final int countStringLength,
                          final int index, 
                          final long maximumCount) {
        
        sb.append(
                String.format(
                        lineFormat,
                        index, 
                        !Character.isISOControl((char) index) ?
                                (char) index : 
                                '?',
                        data[index],
                        computeBarAscii(data[index],
                                        maximumCount,
                                        countStringLength)));
    }
    
    /**
     * Computes and returns the bar ASCII art.
     * 
     * @param count             the count of the line we are processing.
     * @param maximumCount      the maximum count in the byte histogram.
     * @param countStringLength the count string length.
     * 
     * @return the bar ASCII art. 
     */
    private static String computeBarAscii(final long count,
                                          final long maximumCount,
                                          final int countStringLength) {
                                   
        final float ratio = ((float) count) / ((float) maximumCount);
        
        final int maximumBarLength = SCREEN_WIDTH
                                   - LINE_PREAMBLE_WIDTH 
                                   - countStringLength;
        
        final int barLength = (int)(ratio * maximumBarLength);
        
        final StringBuilder sb = new StringBuilder(barLength);
        
        for (int i = 0; i < barLength; i++) {
            sb.append("*");
        }
        
        return sb.toString();
    }
    
    /**
     * Computes the maximum count in this byte histogram.
     * 
     * @return the maximum count.
     */
    private long computeMaximumCount() {
        long m = 0L;
        
        for (final long count : data) {
            m = Math.max(m, count);
        }
        
        return m;
    }
    
    /**
     * Computes and returns the length of the widest length string.
     * 
     * @param maximumLength the maximum length of the byte histogram.
     * 
     * @return the widest length of the count string in characters.
     */
    private static int computeCountStringLength(final long maximumLength) {
        return Long.toString(maximumLength).length();
    }
}

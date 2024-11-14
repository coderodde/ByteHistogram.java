package com.github.coderodde.file.util;

/**
 * This exception class wraps two exceptions: 
 * <ul>
 *  <li><code>MultipleFileNotFoundException</code></li>
 *  <li><code>MultipleIOException</code></li>
 * </ul>
 * @version 1.0.0 (Nov 14, 2024)
 * @since 1.0.0 (Nov 14, 2024)
 */
public final class PairException extends Exception {
    
    private final MultipleFileNotFoundException fileNotFoundException;
    private final MultipleIOException ioException;
    
    public PairException(
            final MultipleFileNotFoundException fileNotFoundException,
            final MultipleIOException ioException) {
        this.fileNotFoundException = fileNotFoundException;
        this.ioException = ioException;
    }
    
    public MultipleFileNotFoundException getFileNotFoundException() {
        return fileNotFoundException;
    }
    
    public MultipleIOException getIOException() {
        return ioException;
    }
}

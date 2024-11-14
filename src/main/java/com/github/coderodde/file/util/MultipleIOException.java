package com.github.coderodde.file.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class holds a list of actual exception object of type 
 * {@link java.io.IOException}.
 * 
 * @version 1.0.0 (Nov 13, 2024)
 * @since 1.0.0 (Nov 13, 2024)
 */
public final class MultipleIOException extends Exception {
    
    private final List<IOException> exceptionList = new ArrayList<>();
    
    public void add(final IOException ex) {
        exceptionList.add(
                Objects.requireNonNull(
                        ex,
                        "The input exception is null."));
    }
    
    public boolean isEmpty() {
        return exceptionList.isEmpty();
    }
    
    public Collection<IOException> getExceptionList() {
        return Collections.unmodifiableCollection(exceptionList);
    }
}

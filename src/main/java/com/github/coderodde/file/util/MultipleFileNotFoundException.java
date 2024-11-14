package com.github.coderodde.file.util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class holds a list of actual exception object of type 
 * {@link java.io.FileNotFoundException}.
 * 
 * @version 1.0.0 (Nov 13, 2024)
 * @since 1.0.0 (Nov 13, 2024)
 */
public final class MultipleFileNotFoundException extends Exception {
    
    private final List<FileNotFoundException> exceptionList = new ArrayList<>();
    
    public void add(final FileNotFoundException ex) {
        exceptionList.add(
                Objects.requireNonNull(
                        ex,
                        "The input exception is null."));
    }
    
    public boolean isEmpty() {
        return exceptionList.isEmpty();
    }
    
    public Collection<FileNotFoundException> getExceptionList() {
        return Collections.unmodifiableCollection(exceptionList);
    }
}

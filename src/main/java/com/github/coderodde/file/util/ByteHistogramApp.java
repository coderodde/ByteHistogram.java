package com.github.coderodde.file.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a program for counting byte histograms in files.
 * 
 * @version 1.0.0 (Nov 13, 2024)
 * @since 1.0.0 (Nov 13, 2024)
 */
public final class ByteHistogramApp {

    public static void main(String[] args) {
        List<InputStream> inputStreamList = null;
        
        // Prepare the input streams from which to build the (shared) byte 
        // histogram:
        try {
            inputStreamList = getInputStreams(args);
        } catch (final PairException ex) {
            
            ex.getFileNotFoundException()
              .getExceptionList()
              .forEach((e) -> System.err.println(e.getMessage()));
            
            ex.getIOException()
              .getExceptionList()
              .forEach((e) -> System.err.println(e.getMessage()));
            
            System.exit(-1);
        }
        
        // Once here, we have valid input streams. Request the histogram and 
        // print it in the console:
        try {
            System.out.println(processInputStreamList(inputStreamList));
        } catch (MultipleIOException ex) {
            
            ex.getExceptionList()
              .forEach((e) -> System.err.println(e.getMessage()));
            
            System.exit(-2);
        }
    }
    
    /**
     * Converts the input argument list to the list of input streams.
     * 
     * @param args the names of the files to process.
     * 
     * @return the input stream list.
     * 
     * @throws MultipleFileNotFoundException if any file failed.
     */
    private static List<InputStream> getInputStreams(final String[] args)
            throws PairException {
        
        if (args.length == 0) {
            return List.of(System.in);
        }
        
        final List<InputStream> inputStreamList = new ArrayList<>(args.length);
        
        final MultipleFileNotFoundException exceptionListFileNotFound = 
                new MultipleFileNotFoundException();
        
        final MultipleIOException exceptionListIO =
                new MultipleIOException();
        
        for (final String fileName : args) {
            try {
                final InputStream is = new FileInputStream(new File(fileName));
                inputStreamList.add(is);
            } catch (final FileNotFoundException ex) {
                // Add the exception ex to the exceptionList:
                exceptionListFileNotFound.add(ex);
                
                for (final InputStream is : inputStreamList) {
                    try {
                        is.close();
                    } catch (final IOException ioException) {
                        exceptionListIO.add(ioException);
                    }
                }
            }
        }
        
        if (!exceptionListIO.isEmpty() || 
            !exceptionListFileNotFound.isEmpty()) {
            // Once here, something went wrong. Throw:
            
            throw new PairException(exceptionListFileNotFound,
                                    exceptionListIO);
        }
        
        return inputStreamList;
    }
    
    /**
     * Builds the shared histogram from the input streams in the argument.
     * 
     * @param inputStreamList the list of input stream supplying the bytes.
     * 
     * @return the shared byte histogram.
     * 
     * @throws MultipleIOException if any stream threw.
     */
    private static ByteHistogram
         processInputStreamList(final List<InputStream> inputStreamList) 
         throws MultipleIOException {
             
        final ByteHistogram histogram = new ByteHistogram();
        final MultipleIOException ex = new MultipleIOException();
        
        for (final InputStream is : inputStreamList) {
            try {
                processInputStream(
                        new BufferedInputStream(is), 
                        histogram);
                
            } catch (final IOException e) {
                // Add the new I/O exception e to ex::
                ex.add(e);
            }
        }
        
        if (!ex.isEmpty()) {
            // Once here, something went wrong. Throw:
            throw ex;
        }
        
        return histogram;
    }
         
    /**
     * Processes the input stream reading bytes from it until end of file is 
     * reached.
     * 
     * @param is        the input stream.
     * @param histogram the target histogram.
     * 
     * @throws IOException if I/O fails.
     */
    private static void processInputStream(final InputStream is,
                                           final ByteHistogram histogram) 
            throws IOException {
        
        int i;
        
        while ((i = is.read()) != -1) {
            histogram.insert(i);
        }
        
        is.close();
    }
}


package com.exercise.mergesort.csvsplitter;

import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@FunctionalInterface
public interface CSVSplitter {
    File[] generateSortedFilesWithBufferNumberOfRows(File inputFile, int bufferLength)
            throws IOException, CsvValidationException, InterruptedException, ExecutionException, TimeoutException;
}

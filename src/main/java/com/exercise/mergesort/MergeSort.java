package com.exercise.mergesort;

import com.exercise.mergesort.csvsplitter.CSVSplitter;
import com.exercise.mergesort.merger.CSVMerge;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface MergeSort extends CSVSplitter, CSVMerge {
    default File sort(File csv, int bufferRows) throws CsvValidationException, ExecutionException, InterruptedException, IOException, TimeoutException {
        File[] partitions = generateSortedFilesWithBufferNumberOfRows(csv, bufferRows);

        while ( partitions.length != 1) {
            partitions = mergePairOfFiles(partitions);
        }

        return partitions[0];
    }
}

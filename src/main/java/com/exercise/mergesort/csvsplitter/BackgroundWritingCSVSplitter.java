package com.exercise.mergesort.csvsplitter;

import com.exercise.SortBufferAndWriteAsCSV;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@FunctionalInterface
public interface BackgroundWritingCSVSplitter extends CSVSplitter, SortBufferAndWriteAsCSV {
    @Override
    default File[] generateSortedFilesWithBufferNumberOfRows(File inputFile, final int bufferLength)
            throws IOException, CsvValidationException, InterruptedException, ExecutionException, TimeoutException {
        CSVReader inputCSV = new CSVReader(new FileReader(inputFile));
        String[] row = inputCSV.readNext();

        List<File> output = new LinkedList<File>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<File> asyncWrite = null;

        while(row != null) {
            final List<String[]> buffer = new LinkedList();
            for(int i=0; i< bufferLength; i++) {
                buffer.add(row);
                row = inputCSV.readNext();
                if(row == null) break;
            }

            if(asyncWrite == null) {
                asyncWrite = executorService.submit(() -> sortAndWriteToFile(buffer));
            } else {
                output.add(asyncWrite.get(10, TimeUnit.SECONDS));
                asyncWrite = executorService.submit(() -> sortAndWriteToFile(buffer));
            }
        }
        output.add(asyncWrite.get(100, TimeUnit.SECONDS));

        return output.toArray(new File[0]);
    }
}

package com.exercise.mergesort.csvsplitter;

import com.exercise.SortBufferAndWriteAsCSV;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@FunctionalInterface
public interface SingleThreadedCSVSplitter extends CSVSplitter, SortBufferAndWriteAsCSV {
    @Override
    default File[] generateSortedFilesWithBufferNumberOfRows(File inputFile, final int bufferLength)
            throws IOException, CsvValidationException {
        CSVReader inputCSV = new CSVReader(new FileReader(inputFile));
        String[] row = inputCSV.readNext();

        List<File> output = new LinkedList<File>();

        while(row != null) {
            final List<String[]> buffer = new LinkedList();
            for(int i=0; i< bufferLength; i++) {
                buffer.add(row);
                row = inputCSV.readNext();
                if(row == null) break;
            }

            output.add(sortAndWriteToFile(buffer));
        }

        return output.toArray(new File[0]);
    }
}

package com.exercise.mergesort.merger;

import com.exercise.mergesort.ZeroIndexedColumnToSortBy;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface CSVMerge extends ZeroIndexedColumnToSortBy {

    /*
     * Merge two sorted files
     * @Return the file contained the merged output
     */
    default File merge(File sorted1, File sorted2) throws IOException, CsvValidationException {
        int indexToSortBy = zeroIndexedColumnToSortBy();
        CSVReader sortedFile1CsvReader = new CSVReader(new FileReader(sorted1));
        CSVReader sortedFile2CsvReader = new CSVReader(new FileReader(sorted2));

        File outputFile = File.createTempFile("sorting", ".csv");
        CSVWriter writer = new CSVWriter(new FileWriter(outputFile));

        String[] sorted1Row = sortedFile1CsvReader.readNext();
        String[] sorted2Row = sortedFile2CsvReader.readNext();

        while (sorted1Row != null) {
            if(sorted2Row == null || sorted1Row[indexToSortBy].compareTo(sorted2Row[indexToSortBy]) <= 0) {
                writer.writeNext(sorted1Row,false);
                sorted1Row = sortedFile1CsvReader.readNext();
            } else {
                writer.writeNext(sorted2Row,false);
                sorted2Row = sortedFile2CsvReader.readNext();
            }
        }

        while (sorted2Row != null) {
            writer.writeNext(sorted2Row,false);
            sorted2Row = sortedFile2CsvReader.readNext();
        }

        writer.close();

        return outputFile;
    }

    File[] mergePairOfFiles(File[] files) throws IOException, CsvValidationException, ExecutionException, InterruptedException;
}

package com.exercise;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Sorter {

    Sorter(int bufferLength, int indexToSortBy) {
        this.bufferLength = bufferLength;
        this.indexToSortBy = indexToSortBy;
    }

    /*
     * Number of rows that can be sorted in memory
     */
    private int bufferLength;
    private int indexToSortBy;

    /*
     * Merge two sorted files
     * @Return the file contained the merged output
     */
    File merge(File sorted1, File sorted2) throws IOException, CsvValidationException {
        CSVReader sortedFile1CsvReader = new CSVReader(new FileReader(sorted1));
        CSVReader sortedFile2CsvReader = new CSVReader(new FileReader(sorted2));

        File outputFile = File.createTempFile("sorting", ".csv");
        CSVWriter writer = new CSVWriter(new FileWriter(outputFile));

        String[] sorted1Row = sortedFile1CsvReader.readNext();
        String[] sorted2Row = sortedFile2CsvReader.readNext();

        while (sorted1Row != null) {
            if(sorted2Row == null || sorted1Row[indexToSortBy].compareTo(sorted2Row[indexToSortBy]) <= 0) {
                writer.writeNext(sorted1Row);
                sorted1Row = sortedFile1CsvReader.readNext();
            } else {
                writer.writeNext(sorted2Row);
                sorted2Row = sortedFile2CsvReader.readNext();
            }
        }

        while (sorted2Row != null) {
            writer.writeNext(sorted2Row);
            sorted2Row = sortedFile2CsvReader.readNext();
        }

        writer.close();

        return outputFile;
    }

    File[] generateSortedFilesWithBufferNumberOfRows(File inputFile) throws IOException, CsvValidationException {
        CSVReader inputCSV = new CSVReader(new FileReader(inputFile));
        String[] row = inputCSV.readNext();

        List<File> output = new LinkedList<File>();

        while(row != null) {
            List<String[]> buffer = new LinkedList();
            for(int i=0; i< this.bufferLength; i++) {
                buffer.add(row);
                row = inputCSV.readNext();
                if(row == null) break;
            }

            output.add(sortAndWriteToFile(buffer));
        }

        return output.toArray(new File[0]);
    }

    private File sortAndWriteToFile(List<String[]> buffer) throws IOException {
        buffer.sort(Comparator.comparing(array -> array[indexToSortBy]));

        File output = File.createTempFile("baseSortedPartition", ".csv");

        CSVWriter writer = new CSVWriter(new FileWriter(output));
        writer.writeAll(buffer);
        writer.close();

        return output;
    }

}

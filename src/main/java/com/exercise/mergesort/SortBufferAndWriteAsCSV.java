package com.exercise;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public interface SortBufferAndWriteAsCSV extends ZeroIndexedColumnToSortBy {
    /*
      Sort input buffer and persist to a temporary file
      @return java.io.File containing sorted output written to the machines temp storage location
     */
    default File sortAndWriteToFile(List<String[]> buffer) throws IOException {
        buffer.sort(Comparator.comparing(array -> array[zeroIndexedColumnToSortBy()]));

        File output = File.createTempFile("baseSortedPartition", ".csv");

        CSVWriter writer = new CSVWriter(new FileWriter(output));
        writer.writeAll(buffer);
        writer.close();

        return output;
    }
}

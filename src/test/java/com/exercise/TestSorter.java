package com.exercise;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TestSorter {

    private File createTempCSV(Iterable<String[]> content) throws IOException {
       File output =  File.createTempFile("basicMergeTestInput", ".csv");
       CSVWriter writer =  new CSVWriter( new FileWriter(output));

       writer.writeAll(content);
       writer.close();

       return output;
    }

    private List<String[]> inMemoryMerge(int column, List<String[]> ... lists) {
        final List<String[]> flattenList = new LinkedList<String[]>();
        Arrays.stream(lists).forEach(flattenList::addAll);

        flattenList.sort(Comparator.comparing(a -> a[column]));

        return flattenList;
    }

    @Test
    public void basicMergeTest() throws IOException, CsvException {

        List<String[]> csv1Content = Arrays.asList(
                new String[] {"apple", "1"},
                new String[] {"banana", "3"},
                new String[] {"carrot", "5"},
                new String[] {"donut", "7"}
        );

        List<String[]> csv2Content = Arrays.asList(
                new String[] {"apple1", "2"},
                new String[] {"banana1", "4"},
                new String[] {"carrot1", "6"},
                new String[] {"donut1", "8"}
        );

        File file1 = createTempCSV(csv1Content);
        File file2 = createTempCSV(csv2Content);

        File sortedFile = new Sorter(1, 0).merge(file1, file2);

        CSVReader mergedCSV = new CSVReader(new FileReader(sortedFile));

        List<String[]> expected = inMemoryMerge(0, csv1Content, csv2Content);
        List<String[]> actual = mergedCSV.readAll();

        Assert.assertEquals("Merge CSV has correct length", expected.size(), actual.size());
        for(int i=0; i < expected.size(); ++i) {
            Assert.assertTrue(
                    String.format("Miss match on row %d \n\tcomparing %s \n\tto %s", i,
                            String.join(",", expected.get(i)),
                            String.join(",", actual.get(i))
                    ),
                    stringArrayAreEqual(expected.get(i), actual.get(i)));
        }
    }

    private boolean stringArrayAreEqual(String[] expected, String[] actual) {
        for(int i=0; i<expected.length; ++i) {
            if(!expected[i].equals(actual[i])) return false;
        }
        return true;
    }

    @Test
    public void testGenerationOfSortedPartitions() throws IOException, CsvValidationException {

        List<String[]> csv1Content = Arrays.asList(
                new String[] {"apple", "1"},
                new String[] {"banana", "3"},
                new String[] {"carrot", "5"},
                new String[] {"donut", "7"}
        );

        int bufferSize = 1;

        File file1 = createTempCSV(csv1Content);

        File[] sortedFiles = new Sorter(bufferSize, 0).generateSortedFilesWithBufferNumberOfRows(file1);

        Assert.assertEquals("Correct number of files generated", csv1Content.size(), sortedFiles.length);
    }
}

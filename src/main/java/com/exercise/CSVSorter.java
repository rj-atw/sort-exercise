package com.exercise;

import com.exercise.mergesort.MergeSort;
import com.exercise.mergesort.ParallelMergeSort;
import com.exercise.mergesort.SingleThreadedMergeSort;
import com.opencsv.exceptions.CsvValidationException;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.*;

public class CSVSorter implements Callable<Integer> {

    @CommandLine.Option(names = {"-f"}, required = true, description="Path to the csv file to parse")
    private File csv;

    @CommandLine.Option(names={"-k"}, required = true, description="1-based index of the column to sort on")
    private int columnIndex1Based = 1;

    @CommandLine.Option(names={"-s"}, description="Number of rows to keep in the buffer")
    private int bufferRows = 2048;

    @CommandLine.Option(names={"-p"}, description = "Flag to run the algorithm in parallel")
    private boolean isParallel = false;

    private final ExecutorService executorService = Executors.newWorkStealingPool();

    public File sort() throws IOException, CsvValidationException, ExecutionException, InterruptedException, TimeoutException {
        MergeSort mergeSort = isParallel ?
                new ParallelMergeSort(zeroIndexedColumnToSortBy(), executorService) :
                new SingleThreadedMergeSort(zeroIndexedColumnToSortBy());

        return mergeSort.sort(csv, bufferRows);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println(String.format("Args: %s %d %d " + isParallel, csv.toPath(), columnIndex1Based, bufferRows));

        Files.copy(sort().toPath(), System.out);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CSVSorter()).execute(args);

        System.exit(exitCode);
    }

    public int zeroIndexedColumnToSortBy() {
        return columnIndex1Based - 1;
    }
}

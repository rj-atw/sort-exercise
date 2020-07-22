package com.exercise;

import com.opencsv.exceptions.CsvValidationException;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
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
        CSVMergeSortHelper mergeSort = new CSVMergeSortHelper(bufferRows, columnIndex1Based-1);

        File[] partition = mergeSort.generateSortedFilesWithBufferNumberOfRows(csv);

        while ( partition.length != 1) {
            partition = isParallel ? concurrentMergePairsOfFiles(mergeSort, partition) : mergePairsOfFiles(mergeSort, partition);
        }

        return partition[0];
    }

    private File[] concurrentMergePairsOfFiles(final CSVMergeSortHelper mergeSort, File[] partition) throws InterruptedException, ExecutionException {
        List<Callable<File>> plannedMerges = new LinkedList<>();

        for(int i = 0; i < partition.length; i=i+2) {
            final int fi = i;
            if(i+1 < partition.length) {
                Callable<File> mergedFile = ()-> mergeSort.merge(partition[fi], partition[fi+1]);
                plannedMerges.add(mergedFile);
            } else {
                plannedMerges.add(() -> partition[fi]);
            }
        }

        List<Future<File>> outputFutures = executorService.invokeAll(plannedMerges, 10, TimeUnit.MINUTES);
        List<File> output = new LinkedList<File>();

        for(Future<File> future: outputFutures) {
            output.add(future.get());
        }

        return output.toArray(new File[0]);
    }

    private File[] mergePairsOfFiles(CSVMergeSortHelper mergeSortHelper, File[] partition) throws IOException, CsvValidationException {
        List<File> output = new LinkedList<File>();

        for(int i =0 ; i < partition.length; i=i+2) {
            if(i+1 < partition.length) {
                output.add(mergeSortHelper.merge(partition[i], partition[i + 1]));
            } else {
                output.add(partition[i]);
            }
        }
        return output.toArray(new File[0]);
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
}

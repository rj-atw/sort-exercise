package com.exercise.mergesort.merger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public abstract class MultiThreadedCSVMerge implements CSVMerge {

    @Override
    public File[] mergePairOfFiles(File[] partitions) throws ExecutionException, InterruptedException {

        List<Callable<File>> plannedMerges = new LinkedList<>();

        for(int i = 0; i < partitions.length; i=i+2) {
            final int fi = i;
            if(i+1 < partitions.length) {
                Callable<File> mergedFile = ()-> merge(partitions[fi], partitions[fi+1]);
                plannedMerges.add(mergedFile);
            } else {
                plannedMerges.add(() -> partitions[fi]);
            }
        }

        List<Future<File>> outputFutures = getExecutorService().invokeAll(plannedMerges, 10, TimeUnit.MINUTES);
        List<File> output = new LinkedList<>();

        for(Future<File> future: outputFutures) {
            output.add(future.get());
        }

        return output.toArray(new File[0]);
    }

    abstract public ExecutorService getExecutorService();
}

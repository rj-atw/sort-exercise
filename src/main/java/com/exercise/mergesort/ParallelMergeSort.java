package com.exercise.mergesort;

import com.exercise.mergesort.csvsplitter.BackgroundWritingCSVSplitter;
import com.exercise.mergesort.merger.MultiThreadedCSVMerge;

import java.util.concurrent.ExecutorService;

public class ParallelMergeSort extends MultiThreadedCSVMerge implements MergeSort, BackgroundWritingCSVSplitter {
    private final int zeroIndexedColumnToSortBy;
    private final ExecutorService executorService;

    public ParallelMergeSort(int zeroIndexedColumnToSortBy, ExecutorService executorService) {
        this.zeroIndexedColumnToSortBy = zeroIndexedColumnToSortBy;
        this.executorService = executorService;
    }

    @Override
    public int zeroIndexedColumnToSortBy() {
        return zeroIndexedColumnToSortBy;
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }
}

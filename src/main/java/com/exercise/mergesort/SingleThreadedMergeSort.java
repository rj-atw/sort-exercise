package com.exercise.mergesort;

import com.exercise.CSVSorter;
import com.exercise.mergesort.csvsplitter.SingleThreadedCSVSplitter;
import com.exercise.mergesort.merger.SingleThreadedCSVMerge;

public class SingleThreadedMergeSort extends SingleThreadedCSVMerge implements MergeSort, SingleThreadedCSVSplitter {
    private final int zeroIndexedColumnToSortBy;

    public SingleThreadedMergeSort(int zeroIndexedColumnToSortBy) {
        this.zeroIndexedColumnToSortBy = zeroIndexedColumnToSortBy;
    }

    @Override
    public int zeroIndexedColumnToSortBy() {
        return zeroIndexedColumnToSortBy;
    }
}

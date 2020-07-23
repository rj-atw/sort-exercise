package com.exercise.mergesort.merger;

import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class SingleThreadedCSVMerge implements CSVMerge {


    @Override
    public File[] mergePairOfFiles(File[] partitions) throws IOException, CsvValidationException {
        List<File> output = new LinkedList<>();

        for(int i =0 ; i < partitions.length; i=i+2) {
            if(i+1 < partitions.length) {
                output.add(merge(partitions[i], partitions[i + 1]));
            } else {
                output.add(partitions[i]);
            }
        }
        return output.toArray(new File[0]);
    }
}

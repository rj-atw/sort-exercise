For a coding interview implemented a memory-constrained merge sort. 

Code is built and test via docker just run the following to build an image named sorter
```
docker build -t sorter .
```

Can be run via docker as (assumes image name is sorter)
```
docker run -v "${PATH_TO_CSV_DIR}:/mnt/tests" -it sorter "-f /mnt/tests/${CSV_NAME} -k=${COLUMN_TO_SORT_ON} -s ${MAX_NUMBER_OF_ROWS_STORED_IN_MEMORY}"
```

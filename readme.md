For a coding interview implemented a memory-constrained merge sort. 

Can be run via docker as
```
docker run -v "${PATH_TO_CSV_DIR}:/mnt/tests" -it sorter "-f /mnt/tests/${CSV_NAME} -k=${COLUMN_TO_SORT_ON} -s ${MAX_NUMBER_OF_ROWS_STORED_IN_MEMORY}"
```

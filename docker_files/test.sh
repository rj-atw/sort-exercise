run_test() {
  file=$1
  sortColumn=$2
  split=$3
  
  
  chdir /tmp
  
  rm -f sort_output.csv
  rm -f test_output.csv
  
  sort -k $sortColumn,$sortColumn -f $file -t, -s  > /tmp/sort_output.csv
  java -jar /srv/sort-exercise.jar -f $file -k $sortColumn > test_output.csv
  
  if [ ! "`diff test_output.csv sort_output.csv | wc -l`" -eq "0" ]; then
    echo test failed: -f $file -k $sortColumn -s $split
    echo output:
    diff test_output.csv sort_output.csv
    echo 
    echo
    exit 1
  fi
}

test_dir=$1
for f in `ls $test_dir`; do
  run_test "$test_dir/$f" 1 1
  run_test "$test_dir/$f" 1 2
  run_test "$test_dir/$f" 2 2
  run_test "$test_dir/$f" 2 2
  run_test "$test_dir/$f" 4 10
  run_test "$test_dir/$f" 4 10
done

#! /bin/bash

file='../results/results-Extremecolluding-large/results-avg-Extreme-Colluding.txt'
cat $file | grep -A3 "positive" | tail -n 3 | awk -f linegraph.awk -v HOWMANY=3 DIVIDE=1 > lixo.txt
gnuplot < large-fpp.gnuplot

cat $file | grep -A3 "negative" | tail -n 3 | awk -f linegraph.awk -v HOWMANY=3 DIVIDE=1 > lixo.txt
gnuplot < large-fnp.gnuplot


cat $file | grep -A3 "execution" | tail -n 3 | awk -f linegraph.awk -v HOWMANY=3 > lixo.txt
gnuplot < large-exec.gnuplot

epstopdf fpp.eps
epstopdf fnp.eps
epstopdf exec.eps

mv fpp.pdf ../results/results-Extremecolluding-large
mv fnp.pdf ../results/results-Extremecolluding-large
mv exec.pdf ../results/results-Extremecolluding-large

rm fpp.eps fnp.eps exec.eps lixo.txt



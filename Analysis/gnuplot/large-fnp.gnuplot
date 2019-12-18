set term postscript #eps enhanced color
set output 'fnp.eps'
set ylabel "False negative pools (%)"
set xlabel "Number of nodes"
set xtics 10000
file='lixo.txt'
plot file using 1:2 w lp title "HO", file using 1:3 w lp title "AV1", file using 1:4 w lp title "AV2"

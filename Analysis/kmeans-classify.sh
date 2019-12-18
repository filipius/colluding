#! /bin/bash

#echo $0
pwd=`pwd`

#dir=`getAbsPath.tcl $pwd $0`
#echo "dir = $dir"
dir=`dirname $0`

cd $dir
awk -f prepare-kmeans-input.awk | octave | awk -f filter-kmeans-result.awk 
cd $pwd

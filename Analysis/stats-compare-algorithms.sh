#! /bin/bash

if [ $# -ne 2 ]; then
	echo "Wrong arguments. It should be"
	echo "$0 xml_configfile config_id"
	exit 1
fi

scriptsdir="helperscripts"
xmltool="$scriptsdir/xml_extract_configuration.sh"
xmltool2="$scriptsdir/xml_get_run.sh"
basefile=`$xmltool2 $1 $2 nodeconfigfile`
averageruns=`$xmltool2 $1 $2 averageruns`

finalpart="$(basename $basefile)"
tmpdir="tmp/$finalpart"
mkdir -p "$tmpdir"
hostname=`hostname`
tmpfile="$tmpdir/$hostname-$$-tmp.txt"
outfilebase="results/results-$finalpart"
outfiletarget="results/results-avg-$finalpart"
outfiletarget2="results/results-stdev-$finalpart"
rm -f $tmpfile $outfilebase

echo $tmpfile $finalpart $outfilebase

average=$averageruns
let i=1
while test $i -le $average
do
	$scriptsdir/compare-algorithms.sh $@
	$scriptsdir/compileData.sh $outfilebase $tmpdir $@
	cat $outfilebase >> $tmpfile
	echo "" >> $tmpfile
	let i=$i+1
done

awk -f $scriptsdir/compute-average.awk $tmpfile > $outfiletarget
awk -f $scriptsdir/compute-stdev.awk $outfiletarget $tmpfile > $outfiletarget2
mv $tmpfile $outfilebase

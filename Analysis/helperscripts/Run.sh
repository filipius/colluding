#! /bin/bash


if [ $# -lt 1 ]; then
	echo "Wrong arguments. It should be"
	echo "$0 config_file [other_args]"
	exit 1
fi


TAB=$'\t';
configfile=$1
#configfile="../ConfigFiles/config1E3.txt"
#analysistype=$2

hostname=`hostname`
output1=tmp/$hostname-output$$.txt
#matrixfile=matrix.txt
classification=tmp/$hostname-classification$$.txt
votingpoolsfile=tmp/$hostname-votingpools$$.txt

case "$configfile" in 
    -D*)
	java -Xms1024m -Xmx2048m -cp ../BIC\ -\ malicious\ nodes/bin/:../BIC\ -\ malicious\ nodes/lib/jfreechart-1.0.10/lib/jcommon-1.0.13.jar:../BIC\ -\ malicious\ nodes/lib/jfreechart-1.0.10/lib/jfreechart-1.0.10.jar:../BIC\ -\ malicious\ nodes/lib/colt/lib/colt.jar $configfile pools.Simulation > $output1
	cat $output1 | grep -w "Pool" | cut -f3- |sed s/"${TAB}MT"/""/g | sed s/"${TAB}-${TAB}"//g > $votingpoolsfile
	;;
    *)
	java -Xms1024m -Xmx2048m -cp ../BIC\ -\ malicious\ nodes/bin/:../BIC\ -\ malicious\ nodes/lib/jfreechart-1.0.10/lib/jcommon-1.0.13.jar:../BIC\ -\ malicious\ nodes/lib/jfreechart-1.0.10/lib/jfreechart-1.0.10.jar:../BIC\ -\ malicious\ nodes/lib/colt/lib/colt.jar pools.Simulation $configfile > $output1
	#grep ";" $output1 | tr ',' ' ' | tr ';' '\n' > $matrixfile

	#whatever is after the invocation of octave is stupid and useless...
	#this serves to clean all needless wording
	#octave < cluster.m | grep -A1 finished | grep -v finished > $classification

	cat $output1 | grep -w "Pool" | cut -f 3,5,7,8,9,10,13 > $votingpoolsfile
	;;
esac


echo $output1 $classification $votingpoolsfile


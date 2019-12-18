#! /bin/sh

mkdir -p tmp


if [ $# -lt 2 ]; then
	echo "Wrong arguments. It should be"
	echo "$0 config_file anlysis_type [other_args]"
	exit 1
fi

scriptsdir="$(dirname $0)"
configfile=$1
#configfile="../ConfigFiles/config1E3.txt"
analysistype=$2

result=( `$scriptsdir/Run.sh $@` )

outputfile=${result[0]}
classification=${result[1]}
votingpoolsfile=${result[2]}

#echo $outputfile $classification $votingpoolsfile

start=`python -c 'import time;print(time.time())'`

if [ $analysistype -eq 4 -o "$analysistype" = "PERFECT_THRESHOLD"  ]; then
	python ./src/evaluation/callAnalyzers.py $analysistype $votingpoolsfile < $outputfile > $classification
else
	shift 1
	echo "python ./src/evaluation/callAnalyzers.py $analysistype $@ < $outputfile > $classification"
	python ./src/evaluation/callAnalyzers.py $analysistype $@ < $outputfile > $classification
fi

end=`python -c 'import time;print(time.time())'`

runtime=`python -c "print($end-$start)"`

#echo "--------------- $classification"
#cat $classification
#echo "--------------- $classification"
#echo "--------------- $outputfile"
#cat $outputfile
#echo "--------------- $outputfile"
python ./src/evaluation/precision.py $configfile $votingpoolsfile $classification

echo "runtime: $runtime"

#rm $tmptime

#rm $votingpoolsfile $outputfile $classification

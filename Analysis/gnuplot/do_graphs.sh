#! /bin/sh


#if [ $# -ne 1 ]; then
#	echo "Wrong arguments. It should be"
#	echo "$0 resultsfile"
#	exit 1
#fi


dirname="$(dirname $0)"

$dirname/bargraphs.sh $dirname/../AnalysisConfiguration.xml 1
$dirname/bargraphs.sh $dirname/../AnalysisConfiguration.xml 2
$dirname/bargraphs.sh $dirname/../AnalysisConfiguration.xml 3
$dirname/bargraphs.sh $dirname/../AnalysisConfiguration.xml 4
$dirname/bargraphs.sh $dirname/../AnalysisConfiguration.xml 5
$dirname/bargraphs.sh $dirname/../AnalysisConfiguration.xml 6
#$dirname/bargraphs.sh $dirname/../AnalysisConfiguration.xml 12

#$dirname/linegraphs.sh
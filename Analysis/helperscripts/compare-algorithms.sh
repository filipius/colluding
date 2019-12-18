#! /bin/bash


if [ $# -ne 2 ]; then
	echo "Wrong arguments. It should be"
	echo "$0 xml_configfile config_nbr"
	exit 1
fi

#example: basefile="$basedir/Base-SingleAttackColluding.txt"
#basefile=$1
scriptsdir="$(dirname $0)"
xmltool="$scriptsdir/xml_get_configuration.sh"
xmltool2="$scriptsdir/xml_get_run.sh"
basefile=`$xmltool2 $1 $2 nodeconfigfile`
baseconfignbr=`$xmltool2 $1 $2 configuration`


finalpart="$(basename $basefile)"
echo "$basefile $finalpart"
basedir="ConfigFiles"
#basedirtmp="ConfigFiles/tmpConfig"
outbase="tmp/$finalpart"
tmpfile="$outbase/tmp$$"

list_clients=`$xmltool $1 $baseconfignbr nodes/number`
list_pools=`$xmltool $1 $baseconfignbr pools/factor`
list_algorithms=`$xmltool $1 $baseconfignbr algorithms/algorithm @algid`

#echo $list_pools
#echo $list_algorithms
#echo $list_clients
#exit 0

shift 2

#for num_clients in 100 1000 #10000
#for num_clients in 10000
#for num_clients in 100
for num_clients in $list_clients
do
	#pools1=$num_clients
	#pools2=`expr $num_clients \* 3`
	#pools3=`expr $num_clients \* 10`
	#pools4=`expr $num_clients \* 25`
	#pools5=`expr $num_clients \* 50`
	#pools6=`expr $num_clients \* 100`
	#pools7=`expr $num_clients \* 250`
	#for num_pools in $pools1 $pools2 $pools3 $pools4 $pools5 $pools6
	#for num_pools in $pools3 $pools4
	#for num_pools in $pools3
	for fpools in $list_pools
	do
		num_pools=`expr $num_clients \* $fpools`
		#echo "clients: $num_clients -- pools = $num_pools"
		config_filename="$outbase/c${num_clients}p${num_pools}.txt"
		cat $basefile | sed s/"NUM_POOLS = replace-me"/"NUM_POOLS = $num_pools"/ | sed s/"NUM_CLIENTS = replace-me"/"NUM_CLIENTS = $num_clients"/ > $config_filename
		cmpfilename="$outbase/comparisonc${num_clients}p${num_pools}t${threshold}.txt"
		rm -f $cmpfilename
		#for alg in 2 3 4 5 7 8 9
		#for alg in 5
		#for alg in 5 8 9
		for alg in $list_algorithms
		do
			echo "Algorithm = $alg"
			echo "Algorithm = $alg" >> $cmpfilename
			$scriptsdir/Analysis.sh $config_filename $alg $@ >> $cmpfilename
			echo "" >> $cmpfilename
		done
	done
done

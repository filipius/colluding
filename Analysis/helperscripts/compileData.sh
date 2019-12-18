#! /bin/bash


if [ $# -ne 4 ]; then
	echo "Wrong arguments. It should be"
	echo "$0 resultsfile dir xml_configfile config_nbr"
	exit 1
fi

outfile="$1"
inbase="$2"
rm -f $outfile

scriptsdir="$(dirname $0)"
xmltool="$scriptsdir/xml_get_configuration.sh"
xmltool2="$scriptsdir/xml_get_run.sh"
basefile=`$xmltool2 $3 $4 nodeconfigfile`
configid=`$xmltool2 $3 $4 configuration`
list_clients=`$xmltool $3 $configid nodes`
list_pools=`$xmltool $3 $configid pools`
list_algorithms=`$xmltool $3 $configid algorithms`


array=("nodes classification errors" "miscalculated pools" "discarded pools" \
		"false positive pools" "false negative pools" "gain of malicious" "execution time")

for param in 2 3 4 5 6 7
#for param in 2
do
	echo ${array[$param - 1]} >> $outfile
	#for num_clients in 100 1000 #10000
	#for num_clients in 10000
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
		for fpools in $list_pools
		do
			num_pools=`expr $num_clients \* $fpools`
			cmpfilename="$inbase/comparisonc${num_clients}p${num_pools}t${threshold}.txt"
			echo -n "$num_clients	$num_pools	" >> $outfile
			#echo "cat ${cmpfilename} | awk -f compileData.awk -v OPTION=$param -v NODES=${num_clients} >> $outfile"
			cat ${cmpfilename} | awk -f $scriptsdir/compileData.awk -v OPTION=$param -v NODES=${num_clients} >> $outfile
		done
	done
	echo "" >> $outfile
done

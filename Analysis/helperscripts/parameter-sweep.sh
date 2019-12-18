#! /bin/bash

basedir="ConfigFiles"
basedirtmp="ConfigFiles/tmpConfig"
outbase="tmp"
tmpfile="$outbase/tmp$$"

for num_clients in 100 1000 10000
do
	pools1=$num_clients
	pools2=`expr $num_clients \* 10`
	pools3=`expr $num_clients \* 25`
	pools4=`expr $num_clients \* 50`
	pools5=`expr $num_clients \* 100`
	pools6=`expr $num_clients \* 250`
	for num_pools in $pools1 $pools2 $pools3 $pools4 $pools5 $pools6
	do
		#echo "clients: $num_clients -- pools = $num_pools"
		filename="$basedirtmp/c${num_clients}p${num_pools}.txt"
		basefile="$basedir/Base-Colluding5pc.txt"
		cat $basefile | sed s/"NUM_POOLS = replace-me"/"NUM_POOLS = $num_pools"/ | sed s/"NUM_CLIENTS = replace-me"/"NUM_CLIENTS = $num_clients"/ > $filename
		
		thisoutfilename="$outbase/resultc${num_clients}p${num_pools}t${threshold}.txt"
		statsoutfilename="$outbase/statsc${num_clients}p${num_pools}t${threshold}.txt"
		result=(`./Run.sh $filename 1`)
		outputfile=${result[0]}
		cat ${outputfile} | grep score | cut -d" " -f 5 > $tmpfile
		cat ${tmpfile} | awk -f min-avg-max.awk > $statsoutfilename
		maxarray=(`cat $statsoutfilename`)
		max=${maxarray[2]}
		cat ${tmpfile} | awk -f normalize.awk -v MAX=$max > $thisoutfilename
	done
done

rm $tmpfile